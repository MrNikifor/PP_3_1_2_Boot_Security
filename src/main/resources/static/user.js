let currentUserData;

document.addEventListener('DOMContentLoaded', async function () {
    currentUserData = await dataAboutCurrentUser();
    await showUserOnNavbar();
    await fillTableAboutCurrentUser();
});
