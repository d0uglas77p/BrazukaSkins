function openModalPerfil() {
    document.getElementById("perfilModal").style.display = "block";
}

function closeModalPerfil() {
    document.getElementById("perfilModal").style.display = "none";
}

function openModalLinkSteam() {
    closeModalPerfil();
    document.getElementById("linkSteamModal").style.display = "block";
}

function closeModalLinkSteam() {
    document.getElementById("linkSteamModal").style.display = "none";
}

function openModalEditarPerfil() {
    closeModalPerfil();
    document.getElementById("editarPerfilModal").style.display = "block";
}

function closeModalEditarPerfil() {
    document.getElementById("editarPerfilModal").style.display = "none";
}

function openModalExcluirConta() {
    closeModalEditarPerfil();
    document.getElementById("excluirContaModal").style.display = "block";
}

function closeModalExcluirConta() {
    document.getElementById("excluirContaModal").style.display = "none";
}

window.onclick = function (event) {
    const perfilModal = document.getElementById("perfilModal");
    const linkSteamModal = document.getElementById("linkSteamModal");
    const editarPerfilModal = document.getElementById("editarPerfilModal");
    const excluirContaModal = document.getElementById("excluirContaModal");

    if (event.target === perfilModal) {
        closeModalPerfil();
    }

    if (event.target === linkSteamModal) {
            closeModalLinkSteam();
        }

    if (event.target === editarPerfilModal) {
        closeModalEditarPerfil();
    }

    if (event.target === excluirContaModal) {
        closeModalExcluirConta();
    }
};