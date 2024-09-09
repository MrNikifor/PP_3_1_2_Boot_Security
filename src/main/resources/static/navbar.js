async function showUserOnNavbar() {
    const navbarCurrentUsername = document.getElementById("navbarCurrentUsername")
    const navbarCurrentUserRoles = document.getElementById("navbarCurrentUserRoles")
    const currentUser = currentUserData;
    navbarCurrentUsername.innerHTML = `${currentUser.username}`;
    navbarCurrentUserRoles.innerHTML = `${currentUser.roles.map(role => role.name.replace('ROLE_', '')).join(' ')}`;
}