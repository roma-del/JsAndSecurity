// Функция для отправки формы
async function submitEditForm(form, modal) {
    try {
        console.log('Form submit event triggered. Form:', form);
        const formData = new FormData(form);
        console.log('Form data:', formData);
        const user = {
            id: parseInt(formData.get('id'), 10),
            username: formData.get('username'),
            lastName: formData.get('lastName'),
            age: parseInt(formData.get('age'), 10),
            firstName: formData.get('firstName'),
            password: formData.get('password'),
            roles: Array.from(formData.getAll('roles')),
        };
        console.log('User data before fetch:', user);
        console.log('fetch: start');
        const response = await fetch(`/api/admin/userEditForm/${user.id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user),
        });
        console.log('fetch: end');
        console.log('Response from /api/admin/userEditForm:', response);
        if (!response.ok) {
            const errorMessage = `HTTP error! status: ${response.status}`;
            console.error(errorMessage);
            throw new Error(errorMessage);
        }
        console.log('User updated successfully!');
        modal.hide();
        console.log('Modal hidden.');
        console.log("loadUsers before");
        await loadUsers();
        console.log('loadUsers finished.');
    } catch (error) {
        console.error('Error during form submission:', error);
    }
}

// Функция для открытия модального окна
async function openEditModal(button) {
    console.log('Click event triggered on:', button);
    try {
        const userId = button.dataset.userId;
        console.log("userId: ", userId);
        const response = await fetch(`/api/admin/users/${userId}`);
        console.log('Response from /api/users:', response);
        if (!response.ok) {
            const message = `HTTP error! status: ${response.status}`;
            console.error(message);
            return;
        }

        const user = await response.json();
        console.log('User data:', user);
        const modal = new bootstrap.Modal(document.getElementById('exampleModal'));
        console.log('Modal object:', modal);
        document.getElementById('userId').value = user.id;
        document.getElementById('id').value = user.id;
        document.getElementById('firstName').value = user.firstName;
        document.getElementById('lastName').value = user.lastName;
        document.getElementById('age').value = user.age;
        document.getElementById('username').value = user.username;
        document.getElementById('password').value = "";
        const rolesSelect = document.getElementById('choose_role');
        console.log('Roles select:', rolesSelect);
        rolesSelect.options.length = 0;
        console.log('allRoles', allRoles);
        allRoles.forEach((role) => {
            console.log('Current role:', role);
            const option = document.createElement('option');
            option.value = role.authority;
            if (user.roles && user.roles.includes(role.authority)) {
                option.selected = true;
            }
            option.textContent = role.authority;
            rolesSelect.appendChild(option);
            console.log('Role option created and appended:', option);
        });
        const form = document.getElementById('editForm');
        console.log("Before form.onsubmit");
        form.onsubmit = (e) => {
            console.log("Form onsubmit triggered");
            e.preventDefault();
            submitEditForm(form, modal);
        };
        console.log("After form.onsubmit");
        console.log('Modal show.');
        modal.show();
    } catch (error) {
        console.error('Error opening modal:', error);
    }
}

async function deleteUser(button) {
    console.log('Click event triggered on:', button);
    try {
        const userId = button.dataset.userId;
        console.log("userId: ", userId);
        const response = await fetch(`/api/admin/users/${userId}`);
        console.log('Response from /api/users:', response);
        if (!response.ok) {
            const message = `HTTP error! status: ${response.status}`;
            console.error(message);
            return;
        }
        const user = await response.json();
        console.log('User data:', user);
        const modal = new bootstrap.Modal(document.getElementById('deleteConfirmationModal'));
        console.log('Modal object:', modal);
        document.getElementById('deleteUserId').value = user.id;
        document.getElementById('deleteId').value = user.id;
        document.getElementById('deleteFirstName').value = user.firstName;
        document.getElementById('deleteLastName').value = user.lastName;
        document.getElementById('deleteAge').value = user.age;
        document.getElementById('deleteUsername').value = user.username;
        const rolesSelect = document.getElementById('deleteChoose_role');
        console.log('Roles select:', rolesSelect);
        rolesSelect.options.length = 0;
        console.log('allRoles', allRoles);
        allRoles.forEach((role) => {
            console.log('Current role:', role);
            const option = document.createElement('option');
            option.value = role.authority;
            if (user.roles && user.roles.includes(role.authority)) {
                option.selected = true;
            }
            option.textContent = role.authority;
            rolesSelect.appendChild(option);
            console.log('Role option created and appended:', option);
        });

        modal.show();
        document.getElementById('confirmDeleteButton').onclick = async () => {
            const response = await fetch(`/api/admin/userDelete/${userId}`);
            console.log('Response from /api/userDelete/ delete:', response);
            if (!response.ok) {
                const message = `HTTP error! status: ${response.status}`;
                console.error(message);
                return;
            }

            console.log('User deleted successfully!');
            modal.hide();
            await loadUsers();
        }

    } catch (error) {
        console.error('Error during user deletion:', error);
    }
}

function initEditButtons() {
    const editButtons = document.querySelectorAll('.edit-button');
    console.log('Edit buttons:', editButtons);
    editButtons.forEach(button => {
        console.log('Adding event listener to button:', button);
        button.addEventListener('click', (event) => {
            event.preventDefault();
            openEditModal(button);
        });
    });
}

function initDeleteButtons() {
    const deleteButtons = document.querySelectorAll('.delete-button');
    console.log('Delete buttons:', deleteButtons);
    deleteButtons.forEach(button => {
        console.log('Adding event listener to button:', button);
        button.addEventListener('click', (event) => {
            event.preventDefault();
            deleteUser(button);
        });
    });
}