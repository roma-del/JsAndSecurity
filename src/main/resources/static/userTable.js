console.log("JavaScript file is loaded");

async function loadUsers() {
    console.log("loadUsers function is called");
    try {
        const response = await fetch("http://localhost:8080/admin");
        console.log("Response:", response);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log("JSON Data:", data);

        if (!data || !data.users) {
            throw new Error("No 'users' array found in the response.");
        }
        displayUsers(data.users);
    } catch (error) {
        console.error('Error fetching users:', error);
        document.getElementById('error-message').textContent = error.message;
    }
}

function displayUsers(users) {
    console.log("displayUsers function is called");
    const userTable = document.getElementById("users-table").getElementsByTagName('tbody')[0];
    userTable.innerHTML = ""; // Очищаем таблицу

    users.forEach(user => {
        const row = userTable.insertRow();
        const cells = ['id', 'firstName', 'lastName', 'age', 'username', 'roles'];

        cells.forEach(cellName => {
            const cell = row.insertCell();

            if (user[cellName] === undefined || user[cellName] === null) {
                cell.textContent = 'N/A';
            } else if (Array.isArray(user[cellName]) && cellName === 'roles') {
                cell.textContent = user[cellName].map(role => role.name).join(', ');
            } else {
                cell.textContent = user[cellName];
            }
        });
    });
}

document.addEventListener('DOMContentLoaded', loadUsers);