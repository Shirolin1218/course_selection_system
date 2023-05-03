const userId = localStorage.getItem("userId");
const loginCheck = document.querySelector(".login-check");
const logoutBtn = document.querySelector(".logout-btn");
const idInput = document.querySelector(".id-input");
const courseBox = document.querySelector(".course-box");
const createBtn = document.querySelector(".create-btn");
const delBtn = document.querySelector(".del-btn");
const selectedCourseBox = document.querySelector(".selected-course-box");
const selectedCourseName = document.querySelector(".selected-course-name");
const selectedCourseTime = document.querySelector(".selected-course-time");
const selectedCourseCredit = document.querySelector(".selected-course-credit");


const selectCourseList = document.createElement("select");
const selectedCourseList = document.createElement("select");


useCourseListCreateSelect();

//確認是否有收到userId
if (userId) {
    console.log(userId);
    loginCheck.textContent = `${userId}，歡迎回來`;
    logoutBtn.style.display = "block";
    idInput.value = userId;

    if (userId === "admin") {
        idInput.disabled = false;
    } else {//不是admin便取得userId的選課紀錄
        getDataByStudentId();
        idInput.disabled = true;
    }

} else {
    console.log("尚未登入");
    loginCheck.textContent = `尚未登入`;
    logoutBtn.style.display = "none";
    idInput.value = "";
    idInput.disabled = true;
}
//監聽idInput 只有admin可以進行輸入
idInput.addEventListener("change", function () {
    getDataByStudentId()
});

selectCourseList.addEventListener("change", function () {
    console.log(selectCourseList.value);
    selectedCourseTime.textContent = ""
    selectedCourseName.textContent = selectCourseList.value;

    //取得課程資料庫
    getCourseList().then(courseList => {
        //篩選此選項的課程
        let findCourse = courseList.filter(course => {
            return course.name === selectCourseList.value;
        });
        findCourse.forEach(selectedCourse => {
            selectedCourseTime.textContent += "星期" + selectedCourse.week + " " + selectedCourse.start_time + "~" + selectedCourse.end_time + "\n";
            selectedCourseCredit.textContent = "學分:" + selectedCourse.credit;
        })


        console.log(findCourse);
    });
})

logoutBtn.addEventListener("click", function () {
    if (window.confirm("確定要登出?")) {
        localStorage.removeItem("userId");
        window.location.href = "../index.html";
    } else {
        return;
    }
});



createBtn.addEventListener("click", function () {
    if (!idInput.value) {
        alert("請輸入ID");
        return;
    }

    let body = {
        "student_id": `${idInput.value}`,
        "course_list": [
            `${selectCourseList.value}`
        ]
    }
    console.log(body);
    fetch("http://localhost:8080/new_enrollment", { //宣告API?格式
        method: "POST",
        headers: {//固定寫法?
            "Content-Type": "application/json"
        },
        //將上面的body轉成JSON格式送出
        body: JSON.stringify(body)
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            alert(data.message);
        })
        .catch(error => {
            console.log(error)
            alert("加選失敗。" + error);
        })
});

delBtn.addEventListener("click", function () {
    if (!idInput.value) {
        alert("請輸入ID");
        return;
    }
    let body = {
        "student_id": `${idInput.value}`,
        "course_list": [
            `${selectCourseList.value}`
        ]
    }
    if (window.confirm("確定要退選?" + `${selectCourseList.value}`)) {
        fetch("http://localhost:8080/delete_enrollment", { //宣告API?格式
            method: "POST",
            headers: {//固定寫法?
                "Content-Type": "application/json"
            },
            //將上面的body轉成JSON格式送出
            body: JSON.stringify(body)
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                alert(data.message);
            })
            .catch(error => {
                console.log(error);
                alert("退選失敗。" + error);
            })
    } else {
        console.log("cancel");
        console.log(body.course_list[0]);
        return;
    }
});


function getDataByStudentId() {
    fetch("http://localhost:8080/get_enrollments_and_courses_by_student_id", {
        method: "POST",
        headers: {//固定寫法?
            "Content-Type": "application/json"
        },
        //將上面的body轉成JSON格式送出
        body: idInput.value
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            return data;
        })
        .catch(error => {
            console.log(error);
            alert("取得選課資料失敗。" + error);
        });
}

function getCourseList() {
    return new Promise((resolve, reject) => {
        fetch("http://localhost:8080/get_course")
            .then(response => response.json())
            .then(data => {
                resolve(data);
            })
            .catch(error => {
                reject(error)
                alert("取得選課資料失敗" + error);
            });
    });
}


function useCourseListCreateSelect() {
    getCourseList().then(courseList => {
        //生成選項欄位
        const defaultOption = document.createElement("option")
        defaultOption.textContent = "請選擇課程";
        defaultOption.selected = true;
        defaultOption.disabled = true;
        selectCourseList.appendChild(defaultOption);
        //取出所有課程名並排除重複的名稱
        const courseName = new Set(courseList.map(course => course.name));
        //透過資料生成選項
        courseName.forEach(name => {
            const newOption = document.createElement("option");
            newOption.textContent = name;
            selectCourseList.appendChild(newOption);
        });
        courseBox.appendChild(selectCourseList);
    })
}
