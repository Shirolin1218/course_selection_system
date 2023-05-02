
const signUpBtn = document.querySelector(".sign-up-btn");
const idInput = document.querySelector(".id-input");
const nameInput = document.querySelector(".name-input");
const pwdInput = document.querySelector(".pwd-input");
const rPwdInput = document.querySelector(".r-pwd-input");



signUpBtn.addEventListener("click", function () {
    console.log("signUpBtn clicked");

    if (pwdInput.value !== rPwdInput.value || pwdInput.value === "") {
        alert("請確認密碼");
        pwdInput.value = "";
        rPwdInput.value = "";
        return;
    }
    console.log(idInput.value);
    console.log(nameInput.value);

    let body = {
        //postman發出的內容
        "student_list": [{
            "id": `${idInput.value}`,
            "name": `${nameInput.value}`
        }]
    }

    fetch("http://localhost:8080/new_student", {
        //宣告API?格式
        method: "POST",
        headers: {//固定寫法?
            "Content-Type": "application/json"
        },
        //將上面的body轉成JSON格式送出
        body: JSON.stringify(body)
    })
        .then(response => {
            return response.json();
        })
        .then(data => {
            console.log(data);
            if (data.message.includes("發生錯誤")) {
                alert(data.message);
                return;
            }
            alert("id：" + idInput.value + "註冊成功");
            window.location.href = "./login.html";
        })
        .catch(error => {
            console.log(error);
        })
})