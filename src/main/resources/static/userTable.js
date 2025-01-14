
let allRoles;
async function loadUsers() {
    try {
        console.log("loadUsers start");
        let loading = await fetch("/api/admin/users");
        console.log("loadUsers after fetch", loading);
        if (!loading.ok) {
            throw new Error(`HTTP error! status: ${loading.status}`);
        }
        let data = await loading.json();
        console.log("loadUsers after json", data);
        if (!data || !data.usernav || !data.users || !data.allRoles) {
            console.error("Invalid response format:", data);
            return;
        }
        allRoles = data.allRoles;
        await displayNav(data.usernav);
        await displayUsers(data.users, data.allRoles);
        console.log("loadUsers end");
        initEditButtons();
        initDeleteButtons();
        initCreateButton(allRoles);
        await displayUsersAuth(data.usernav);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

async function displayNav(username) {
    const nameNav = document.getElementById("name-nav");
    const rolesNav = document.getElementById("roles-nav");
    if (!nameNav || !rolesNav) return;
    nameNav.innerHTML = username.username;
    rolesNav.innerHTML = ' with roles: ' + username.roles;
}

async function displayUsers(users, roles) {
    let table = document.getElementById("users-table");
    if(!table) return;

    table.innerHTML = '';

    for (let user of users) {
        const modalId = `modalEdit-${user.id}`;
        const row = table.insertRow(-1);
        let cell0 = row.insertCell(0);
        let cell1 = row.insertCell(1);
        let cell2 = row.insertCell(2);
        let cell3 = row.insertCell(3);
        let cell4 = row.insertCell(4);
        let cell5 = row.insertCell(5);
        let cell6 = row.insertCell(6);
        let cell7 = row.insertCell(7);

        cell0.innerHTML = user.id;
        cell1.innerHTML = user.firstName;
        cell2.innerHTML = user.lastName;
        cell3.innerHTML = user.age;
        cell4.innerHTML = user.username;
        cell5.innerHTML = user.roles;
        cell6.innerHTML = `<button type="button" class="btn btn-primary btn-sm edit-button" data-user-id="${user.id}">Edit</button>`;
        cell7.innerHTML = `<button type="button" class="btn btn-danger btn-sm delete-button" data-user-id="${user.id}">Delete</button>`;
    }
}

async function displayUsersAuth(user) {
    let table = document.getElementById("for-one-user");
    if(!table) return;

    table.innerHTML = '';

        const row = table.insertRow(-1);
        let cell0 = row.insertCell(0);
        let cell1 = row.insertCell(1);
        let cell2 = row.insertCell(2);
        let cell3 = row.insertCell(3);
        let cell4 = row.insertCell(4);
        let cell5 = row.insertCell(5);


        cell0.innerHTML = user.id;
        cell1.innerHTML = user.firstName;
        cell2.innerHTML = user.lastName;
        cell3.innerHTML = user.age;
        cell4.innerHTML = user.username;
        cell5.innerHTML = user.roles;
}

document.addEventListener('DOMContentLoaded', loadUsers);