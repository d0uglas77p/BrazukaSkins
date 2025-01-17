/* MENU ESQUERDO */
document.getElementById("menu-btn").onclick = function() {
    var sidebar = document.getElementById("sidebar");
    
    if (sidebar.style.width === "250px") {
        sidebar.style.width = "0";
    } else {
        sidebar.style.width = "250px"; 
    }
};

// Função para fechar o menu lateral quando o mouse sair
document.getElementById("sidebar").onmouseleave = function() {
    var sidebar = document.getElementById("sidebar");
    sidebar.style.width = "0";  
};
/* MENU ESQUERDO */


/* MENU DIREITO */
document.getElementById("login-menu-btn").onclick = function () {
    var sidebarLogin = document.getElementById("sidebar-login");
    if (sidebarLogin.style.width === "180px") {
        sidebarLogin.style.width = "0"; 
    } else {
        sidebarLogin.style.width = "180px"; 
    }
};

document.getElementById("sidebar-login").onmouseleave = function () {
    var sidebarLogin = document.getElementById("sidebar-login");
    sidebarLogin.style.width = "0"; 
};
/* MENU DIREITO */


/* MODAL LOGIN */
function openModal() {
    document.getElementById("loginModal").style.display = "block";
}

function closeModal() {
    document.getElementById("loginModal").style.display = "none";
}

/* MODAL LOGIN */


/* MODAL CADASTRO */
function openModalCadastro() {
    document.getElementById("cadastroModal").style.display = "block";
}

function closeModalCadastro() {
    document.getElementById("cadastroModal").style.display = "none";
}

/* MODAL CADASTRO */

/* FECHAR OS MODAIS */
window.onclick = function (event) {
    const loginModal = document.getElementById("loginModal");
    const cadastroModal = document.getElementById("cadastroModal");

    // Fecha o modal de login se o clique for fora dele
    if (event.target === loginModal) {
        closeModal();
    }

    // Fecha o modal de cadastro se o clique for fora dele
    if (event.target === cadastroModal) {
        closeModalCadastro();
    }
};
/* FECHAR OS MODAIS */