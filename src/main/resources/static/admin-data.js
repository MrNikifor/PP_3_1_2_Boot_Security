async function dataAboutAllUsers() {
    const response = await fetch("/api/admin");
    return await response.json();
}

async function getUserDataById(userId) {
    const response = await fetch(`/api/admin/${userId}`);
    return await response.json();
}

async function dataAboutAllRoles() {
    const response = await fetch("/api/admin/roles");
    return await response.json();
}

async function createNewUser(user) {
    return await fetch("/api/users", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(user)
    });
}

async function deleteUserData(userId) {
    return await fetch(`/api/admin/${userId}`, {method: 'DELETE'});
}

async function editUserData(user) {
    return await fetch("/api/admin", {
        method: "PUT",
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(user)
    });
}

async function addNewUserForm() {
    const newUserForm = document.getElementById("newUser");
    newUserForm.addEventListener('submit', async function (event) {
        event.preventDefault();
        document.getElementById("newUserError").innerHTML = "";
        const rolesSelected = newUserForm.querySelector("#roles");
        let roles = [];
        for (let option of rolesSelected.selectedOptions) {
            roles.push({id: Number(option.dataset.roleId), name: option.value});
        }
        const newUserData = {
            username: newUserForm.querySelector("#username").value.trim(),
            age: newUserForm.querySelector("#age").value.trim(),
            email: newUserForm.querySelector("#email").value.trim(),
            rawPassword: newUserForm.querySelector("#password").value.trim(),
            roles: roles
        };
        let response = await createNewUser(newUserData);
        if (response.ok) {
            newUserForm.reset();
            allUsersData = await dataAboutAllUsers();
            await fillTableOfAllUsers();
        } else {
            let list = await response.json();
            for (let error of list) {
                document.getElementById("newUserError").innerHTML += error + "<br>";
            }
        }
    });
}

async function fillAllRoles() {
    const newUserForm = document.getElementById("newUser");
    const rolesSelect = newUserForm.querySelector("#roles");
    const roles = rolesData;
    rolesSelect.options.length = 0;
    for (let role of roles) {
        const option = document.createElement("option");
        option.value = role.name;
        option.textContent = role.name.replace('ROLE_', '');
        option.dataset.roleId = role.id;
        rolesSelect.appendChild(option);
    }
}

async function fillTableOfAllUsers() {
    const usersTable = document.getElementById("usersTableId");
    const users = allUsersData;
    let usersTableHTML = "";
    for (let user of users) {
        usersTableHTML +=
            `<tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.roles.map(role => role.name.replace('ROLE_', '')).join(' ')}</td>
                <td>
                    <button class="btn btn-info btn-sm text-white"
                            data-bs-toggle="modal"
                            data-bs-target="#editModal"
                            data-user-id="${user.id}">
                        Edit</button>
                </td>
                <td>
                    <button class="btn btn-danger btn-sm btn-delete"
                            data-bs-toggle="modal"
                            data-bs-target="#deleteModal"
                            data-user-id="${user.id}">                     
                        Delete</button>
                </td>
            </tr>`;
    }
    usersTable.innerHTML = usersTableHTML;
}

async function fillModal(modal) {
    modal.addEventListener("show.bs.modal", async function (event) {
        const userId = event.relatedTarget.dataset.userId;
        const user = await getUserDataById(userId);
        const modalBody = modal.querySelector(".modal-content");
        const idInput = modalBody.querySelector("input[data-user-id='id']");
        const usernameInput = modalBody.querySelector("input[data-user-id='username']");
        const ageInput = modalBody.querySelector("input[data-user-id='age']");
        const emailInput = modalBody.querySelector("input[data-user-id='email']");
        idInput.value = user.id;
        usernameInput.value = user.username;
        ageInput.value = user.age;
        emailInput.value = user.email;
        const rolesSelect = modalBody.querySelector("select[data-user-id='roles']");
        const roles = rolesData;
        rolesSelect.options.length = 0;
        for (let role of roles) {
            const option = document.createElement("option");
            option.value = role.name;
            option.textContent = role.name.replace('ROLE_', '');
            option.dataset.roleId = role.id;
            if (user.roles.map(role => role.id).includes(role.id)) {
                option.selected = true;
            }
            rolesSelect.appendChild(option);
        }
    });
}

async function deleteUserSubmit() {
    const modalDelete = document.getElementById("modalBodyDelete");
    modalDelete.addEventListener("submit", async function (event) {
        event.preventDefault();
        const userId = event.target.querySelector("#idDelete").value;
        let response = await deleteUserData(userId);
        if (response.ok) {
            if (userId == currentUserData.id) {
                window.location.href = "http://localhost:8081/logout";
            } else {
                allUsersData = await dataAboutAllUsers();
                await fillTableOfAllUsers();
            }
        } else {
            let list = await response.json();
            for (let error of list) {
                document.getElementById("editUserError").innerHTML += error + "<br>";
            }
        }
    });
}

async function editUserSubmit() {
    const modalEdit = document.getElementById("modalBodyEdit");
    modalEdit.addEventListener('submit', async function (event) {
        event.preventDefault();
        document.getElementById("editUserError").innerHTML = "";
        const rolesSelected = modalEdit.querySelector("#rolesEdit");
        let roles = [];
        for (let option of rolesSelected.selectedOptions) {
            roles.push({id: Number(option.dataset.roleId), name: option.value});
        }
        let user = {
            id: document.getElementById("idEdit").value,
            username: document.getElementById("usernameEdit").value.trim(),
            age: document.getElementById("ageEdit").value.trim(),
            email: document.getElementById("emailEdit").value.trim(),
            rawPassword: document.getElementById("passwordEdit").value.trim(),
            roles: roles
        };
        let response = await editUserData(user);
        if (response.ok) {
            if (user.id == currentUserData.id) {
                if (user.username != currentUserData.username) {
                    window.location.href = "http://localhost:8081/logout";
                }
                currentUserData = await dataAboutCurrentUser();
                await showUserOnNavbar();
                await fillTableAboutCurrentUser();
            }
            allUsersData = await dataAboutAllUsers();
            await fillTableOfAllUsers();
            document.getElementById("modalEditClose").click();
        } else {
            let list = await response.json();
            for (let error of list) {
                document.getElementById("editUserError").innerHTML += error + "<br>";
            }
        }
    });
}
