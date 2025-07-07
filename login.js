document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("paylink-login-form");
  const emailInput = document.getElementById("paylink-login-email");
  const passwordInput = document.getElementById("paylink-login-password");
  const message = document.getElementById("paylink-login-message");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = emailInput.value.trim();
    const password = passwordInput.value.trim();

    try {
      const response = await fetch("http://localhost:8083/payment/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (response.ok && data.token) {
        message.textContent = data.message;
        message.style.color = "green";
        localStorage.setItem("jwtToken", data.token);
        window.location.href = "payment-add.html"; // Redirect only if login successful
      } else {
        message.textContent = data.message || "❌ Login failed";
        message.style.color = "red";
      }

    } catch (error) {
      console.error("Login error:", error);
      message.textContent = "❌ Something went wrong. Try again.";
      message.style.color = "red";
    }
  });
});
