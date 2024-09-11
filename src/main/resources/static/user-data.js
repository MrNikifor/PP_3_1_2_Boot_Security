async function dataAboutCurrentUser() {
    const response = await fetch("/api/user")
    return await response.json();
}

async function fillTableAboutCurrentUser(){
    const currentUserTable = document.getElementById("currentUserTable");
    const currentUser = currentUserData;

    let currentUserTableHTML = "";
    currentUserTableHTML +=
        `<tr>
            <td>${currentUser.id}</td>
            <td>${currentUser.username}</td>
            <td>${currentUser.age}</td>
            <td>${currentUser.email}</td>
            <td>${currentUser.roles.map(role => role.name.replace('ROLE_', '')).join(' ')}</td>
        </tr>`
    currentUserTable.innerHTML = currentUserTableHTML;
}