
document.getElementById("forgotPasswordForm").addEventListener("submit", function (e) {
    e.preventDefault(); // prevent default form submit

    const email = document.getElementById("email").value;

    if (!email) {
        alert("Please enter your email.");
        return;
    }

    fetch("http://localhost:8083/payment/api/auth/forgot-password?email=" + encodeURIComponent(email), {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            return response.text().then(error => { throw new Error(error); });
        }
    })
    .then(data => {
        alert("Password reset link sent to your email.");
        console.log(data);
    })
    .catch(error => {
        alert("Error: " + error.message);
        console.error("Error:", error);
    });
});

