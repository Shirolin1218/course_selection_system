const courseBtn = document.querySelector(".course-btn");
const enrollmentBtn = document.querySelector(".enrollment-btn");

enrollmentBtn.addEventListener("click", function () {
    localStorage.setItem("userId", "admin");
})