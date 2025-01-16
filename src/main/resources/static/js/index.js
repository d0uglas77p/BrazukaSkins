// Função para abrir e fechar o menu lateral ao clicar no menu-btn
document.getElementById("menu-btn").onclick = function() {
    var sidebar = document.getElementById("sidebar");
    
    // Verifica se o menu está aberto (largura 250px) e alterna entre abrir e fechar
    if (sidebar.style.width === "250px") {
        sidebar.style.width = "0";  // Fecha o menu
    } else {
        sidebar.style.width = "250px";  // Abre o menu
    }
};

// Função para fechar o menu lateral quando o mouse sair
document.getElementById("sidebar").onmouseleave = function() {
    var sidebar = document.getElementById("sidebar");
    sidebar.style.width = "0";  // Fecha o menu
};



// Função para abrir e fechar o menu lateral direito ao clicar no login-menu-btn
document.getElementById("login-menu-btn").onclick = function () {
    var sidebarLogin = document.getElementById("sidebar-login");
    if (sidebarLogin.style.width === "180px") {
        sidebarLogin.style.width = "0"; // Fecha o menu
    } else {
        sidebarLogin.style.width = "180px"; // Abre o menu
    }
};

// Função para fechar o menu lateral direito quando o mouse sair
document.getElementById("sidebar-login").onmouseleave = function () {
    var sidebarLogin = document.getElementById("sidebar-login");
    sidebarLogin.style.width = "0"; // Fecha o menu
};

