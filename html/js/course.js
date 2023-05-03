const idBox = document.querySelector(".id-box")
const idInput = document.querySelector(".id-input");
const nameInput = document.querySelector(".name-input");
const weekInput = document.querySelector(".week-input");
const startInput = document.querySelector(".start-input");
const endInput = document.querySelector(".end-input");
const creditInput = document.querySelector(".credit-input");
const createBtn = document.querySelector(".create-btn");
const delBtn = document.querySelector(".del-btn");

let courseList;
fetch("http://localhost:8080/get_course")
    .then(response => response.json())
    .then(data => {
        console.log(data);
        courseList = data;
    })
    .catch(error => {
        console.log(error);
        alert("與伺服器連接異常。" + error);
    });

idInput.addEventListener("input", input => {
    const inputValue = input.target.value;
    console.log(inputValue);
    let findId = courseList.filter(course => {
        return course.course_id === inputValue;
    });
    //若資料庫有對應值則做動作
    if (findId.length > 0) {
        console.log(findId);
        nameInput.value = findId[0].name;
        nameInput.disabled = true;
        weekInput.value = findId[0].week;
        startInput.value = findId[0].start_time;
        endInput.value = findId[0].end_time;
        creditInput.value = findId[0].credit;
    } else {
        nameInput.disabled = false;
        inputCourse = null;
        weekInput.value = null;
        creditInput.value = null
    }
})

nameInput.addEventListener("change", input => {
    const inputValue = input.target.value;
    console.log(inputValue);
    let findCourse = courseList.filter(course => {
        return course.name === inputValue;
    });
    //若資料庫有對應值則做動作
    if (findCourse.length > 0) {
        console.log(findCourse);
        //刪除已有的選單
        idBox.innerHTML = "";
        idBox.appendChild(idInput);
        //透過資料生成選單
        const selectIdByCourse = document.createElement("select");
        const defaultOption = document.createElement("option")
        defaultOption.textContent = "請選擇id";
        defaultOption.selected = true;
        defaultOption.disabled = true;
        selectIdByCourse.appendChild(defaultOption);
        //透過資料生成選項
        findCourse.forEach(item => {
            const newOption = document.createElement("option");
            newOption.textContent = item.course_id;
            selectIdByCourse.appendChild(newOption);
        });
        const endOption = document.createElement("option")
        endOption.textContent = "新增時段";
        selectIdByCourse.appendChild(endOption);
        idBox.appendChild(selectIdByCourse);
        //監聽課程ID下拉式選單
        selectIdByCourse.addEventListener("change", function () {
            nameInput.disabled = true;
            idInput.disabled = true;
            console.log(this.value); // 打印選中的 value(課程id)
            //取出該課程id的資料
            let findCourseById = courseList.filter(item => {
                return item.course_id === this.value;
            });
            if (findCourseById.length > 0) {
                idInput.value = findCourseById[0].course_id;
                weekInput.value = findCourseById[0].week;
                startInput.value = findCourseById[0].start_time;
                endInput.value = findCourseById[0].end_time;
                creditInput.value = findCourseById[0].credit;
            } else if (selectIdByCourse.value === "新增時段") {
                idInput.disabled = false;
                nameInput.disabled = true;
                weekInput.value = 0;
                creditInput.value = 0;
            } else {
                idInput.disabled = false;
                nameInput.disabled = false;
                weekInput.value = null;
                creditInput.value = null
            }
        });
    } else {
        inputCourse = null;
        weekInput.value = null;
        creditInput.value = null
    }
})


createBtn.addEventListener("click", function () {
    let body;
    //調整時間字串格式
    if (startInput.value.length !== 8) {
        startInput.value += ":00";
    }
    if (endInput.value.length !== 8) {
        endInput.value += ":00";
    }

    body = {
        "course_list": [
            {
                "course_id": `${idInput.value}`,
                "name": `${nameInput.value}`,
                "week": `${weekInput.value}`,
                "start_time": `${startInput.value}`,
                "end_time": `${endInput.value}`,
                "credit": `${creditInput.value}`
            }
        ]
    }
    console.log(body);
    fetch("http://localhost:8080/new_course", { //宣告API?格式
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
            alert(error);
            alert("新增失敗。");
        })

});

delBtn.addEventListener("click", function () {

    let body;
    //確認課程id欄位
    body = {
        "course_list": [
            {
                "course_id": `${idInput.value}`,
                "name": `${nameInput.value}`,
                "week": `${weekInput.value}`,
                "start_time": `${startInput.value}`,
                "end_time": `${endInput.value}`,
                "credit": `${creditInput.value}`
            }
        ]
    }

    let delItem;
    //判斷有無course_id
    if (body.course_list[0].course_id) {
        delItem = body.course_list[0].course_id;
    } else {
        delItem = idInput.value;
    }
    if (window.confirm("確定要刪除?" + `${delItem}`)) {
        fetch("http://localhost:8080/delete_course", { //宣告API?格式
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
                alert("新增失敗。" + error);
            })

    } else {
        console.log("cancel");
        console.log(body.course_list[0]);
        return;
    }
});