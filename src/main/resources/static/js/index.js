document.getElementById("menu-btn").onclick = function() {
    var sidebar = document.getElementById("sidebar");
    
    if (sidebar.style.width === "250px") {
        sidebar.style.width = "0";
    } else {
        sidebar.style.width = "250px"; 
    }
};

document.getElementById("sidebar").onmouseleave = function() {
    var sidebar = document.getElementById("sidebar");
    sidebar.style.width = "0";  
};

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

function openModal() {
    document.getElementById("loginModal").style.display = "block";
}

function closeModal() {
    document.getElementById("loginModal").style.display = "none";
}

function openModalRecuperar() {
    closeModal();
    document.getElementById("recuperarModal").style.display = "block";
}

function closeModalRecuperar() {
    document.getElementById("recuperarModal").style.display = "none";
}

function openModalCadastro() {
    document.getElementById("cadastroModal").style.display = "block";
}

function closeModalCadastro() {
    document.getElementById("cadastroModal").style.display = "none";
}

window.onclick = function (event) {
    const loginModal = document.getElementById("loginModal");
    const recuperarModal = document.getElementById("recuperarModal");
    const cadastroModal = document.getElementById("cadastroModal");

    if (event.target === loginModal) {
        closeModal();
    }

    if (event.target === recuperarModal) {
        closeModalRecuperar();
    }

    if (event.target === cadastroModal) {
        closeModalCadastro();
    }
};

function formatarTelefone(input) {
    var telefone = input.value.replace(/\D/g, '');
    telefone = telefone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    input.value = telefone;
}

document.getElementById("telefone-cadastro").addEventListener("input", function () {
    var telefone = this.value.replace(/\D/g, '');

    if (telefone.length !== 11) {
        this.setCustomValidity("Número inválido.");
    } else {
        this.setCustomValidity("");
    }
});