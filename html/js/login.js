const signUpBtn = document.querySelector(".sign-up-btn");
const loginBtn = document.querySelector(".login-btn");
const idInput = document.querySelector(".id-input");
const loginCheck = document.querySelector(".login-check");


signUpBtn.addEventListener("click", function () {
    window.location.href = "./sign_up.html";
});


loginBtn.addEventListener("click", function () {
    console.log("loginBtn clicked");
    //使用API進行get_student取的所有學生資料
    fetch("http://localhost:8080/get_student")
        .then(response => {
            return response.json();
        })
        .then(data => {
            //透過學生資料判斷id是否存在
            let find = data.filter(student => {
                return student.id === idInput.value;
            });
            if (find.length === 0) {
                alert("此id尚未註冊");
                idInput.value = "";
                return;
            }
            loginCheck.textContent = `${idInput.value}，歡迎回來!`;
            localStorage.setItem("userId", idInput.value);
            window.location.href = "./enrollment.html";
        })
        .catch(error => {
            console.error(error);
            alert(error);
            alert("與伺服器連線中斷。");
        })
});

