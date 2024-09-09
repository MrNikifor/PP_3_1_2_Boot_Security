async function deleteUserData(userId){
    try {
        const response = await fetch(`/api/users/${userId}`, {method: 'DELETE'});
        if (!response.ok) {
            throw new Error("Network response was not ok " + response.statusText);
        }
    } catch (error) {
        console.error("Ошибка при удалении пользователя:", error);
        alert("Не удалось удалить пользователя. Попробуйте позже.");
    }
}

const modalDelete = document.getElementById("deleteModal");

async function DeleteModalHandler() {
    await fillModal(modalDelete);
}

const formDelete = document.getElementById("modalBodyDelete");
formDelete.addEventListener("submit", async function(event) {
        event.preventDefault();

        try {
            const userId = event.target.querySelector("#idDelete").value;
            await deleteUserData(userId);
            await fillTableOfAllUsers();

            const modalBootstrap = bootstrap.Modal.getInstance(modalDelete);
            modalBootstrap.hide();
        } catch (error) {
            console.error("Ошибка при удалении пользователя:", error);
            alert("Не удалось удалить пользователя. Попробуйте позже.");
        }
    }
)
