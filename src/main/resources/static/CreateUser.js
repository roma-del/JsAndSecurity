async function createNewUser(user) {
    try {
        const response =  await fetch("/api/userCreateForm", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(user)
        });
        if (!response.ok) {
            const message = `HTTP error! status: ${response.status}`;
            console.error(message);
            return;
        }
        const data = await response.json();
        console.log('User created successfully!:', data);
        return true;
    } catch (error) {
        console.error('Error creating user:', error);
        return false;
    }
}

async function addNewUserForm() {
    const newUserForm = document.getElementById("createFormUser");
    newUserForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const firstname = newUserForm.querySelector("#createFirstName").value.trim();
        const lastname = newUserForm.querySelector("#createLastName").value.trim();
        const age = newUserForm.querySelector("#createAge").value.trim();
        const username = newUserForm.querySelector("#createUsername").value.trim();
        const password = newUserForm.querySelector("#createPassword").value.trim();
        const rolesSelect = document.getElementById('createChoose_role');
        const selectedRoles = Array.from(rolesSelect.selectedOptions, option => option.value);

        const newUserData = {
            firstName: firstname,
            username: username,
            lastName: lastname,
            age: age,
            password: password,
            roles: selectedRoles
        };
        console.log("newUserData перед отправкой:", newUserData);
        const isCreated =  await createNewUser(newUserData);
        if (isCreated) {
            newUserForm.reset();
            await loadUsers();
            const backButton = document.getElementById('nav-home-tab');
            if(backButton){
                const tab = new bootstrap.Tab(backButton);
                tab.show();
            } else {
                console.log('nav-home-tab is null');
            }
        }

    });
}

function initCreateButton(allRoles) {
    console.log('initCreateButton is called');
    const createButton = document.getElementById('nav-profile-tab');
    console.log('Create button:', createButton);
    if(createButton){
        console.log("before addEventListener");
        try {
            createButton.addEventListener('shown.bs.tab', (event) => {
                console.log("addEventListener shown.bs.tab");
                addNewUserForm();
                const rolesSelect = document.getElementById('createChoose_role');
                console.log('Roles select:', rolesSelect);
                rolesSelect.options.length = 0;
                console.log('allRoles', allRoles);
                allRoles.forEach((role) => {
                    console.log('Current role:', role);
                    const option = document.createElement('option');
                    option.value = role.authority;
                    option.textContent = role.authority;
                    rolesSelect.appendChild(option);
                    console.log('Role option created and appended:', option);
                });
            });
            console.log("after addEventListener");
        } catch (error) {
            console.log("Error addEventListener", error);
        }
    } else {
        console.log("createButton is null")
    }
}