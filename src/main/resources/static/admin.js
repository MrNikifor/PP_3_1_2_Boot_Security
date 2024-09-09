let currentUserData;
let allUsersData;
let rolesData;

document.addEventListener('DOMContentLoaded', async function () {
    currentUserData = await dataAboutCurrentUser();
    allUsersData = await dataAboutAllUsers();
    rolesData = await dataAboutAllRoles();
    await showUserOnNavbar();
    await fillTableOfAllUsers();
    await fillTableAboutCurrentUser();
    await fillAllRoles();
    await addNewUserForm();
    await DeleteModalHandler();
    await EditModalHandler();
    await deleteUserSubmit();
    await editUserSubmit();
});

async function EditModalHandler() {
    const modalEdit = document.getElementById("editModal");
    await fillModal(modalEdit);
}

async function DeleteModalHandler() {
    const modalDelete = document.getElementById("deleteModal");
    await fillModal(modalDelete);
}
