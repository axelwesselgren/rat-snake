document.addEventListener("DOMContentLoaded", function() {
    var disconnectBtn = document.getElementById("disconnectBtn");
    var logoutBtn = document.getElementById("logoutBtn");
    var shutdownBtn = document.getElementById("shutdownBtn");

    disconnectBtn.addEventListener("click", function() {
        checkAuthorization(function(isAuthenticated) {
            if (isAuthenticated) {
                startBtn.disabled = false;
                disconnectBtn.disabled = true
                socket.emit("disconnect");
            } else {
                logoutBtn.click();
            }
        });
    });

    shutdownBtn.addEventListener("click", function() {
        checkAuthorization(function(isAuthenticated) {
            if (isAuthenticated) {
                socket.emit("shutdown");
            } else {
                logoutBtn.click();
            }
        });
    });

    function checkAuthorization(callback) {
        fetch('/logged_in', { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            callback(data.is_authenticated);
        });
    }
});