async function loadUsers() {
    try {
        console.log("loadUsers start");
        let loading = await fetch("/api/user");
        console.log("loadUsers after fetch", loading);
        if (!loading.ok) {
            throw new Error(`HTTP error! status: ${loading.status}`);
        }
        let data = await loading.json();
        console.log("loadUsers after json", data);
        await displayNav(data);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

async function displayNav(user) {
    const nameNav = document.getElementById("name-nav");
    const rolesNav = document.getElementById("roles-nav");
    if (!nameNav || !rolesNav) return;
    nameNav.innerHTML = user.username;
    rolesNav.innerHTML = ' with roles: ' + user.roles;
    let table = document.getElementById("for-one");
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