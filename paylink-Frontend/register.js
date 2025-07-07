document.getElementById('register-form').addEventListener('submit', async function (e) {
  e.preventDefault();

  const userName = document.getElementById('register-username').value;
  const email = document.getElementById('register-email').value;
  const password = document.getElementById('register-password').value;

  const payload = { userName, email, password };

  try {
    const response = await fetch("http://localhost:8083/payment/user/store", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    const data = await response.text();
    document.getElementById('register-message').innerText = data;

    if (response.ok) {
      setTimeout(() => window.location.href = "payment-add.html", 1500);
    }
  } catch (error) {
    document.getElementById('paylink-register-message').innerText = "❌ Something went wrong. Try again.";
  }
});
