package brasuzaskins.controller;

import brasuzaskins.model.Usuario;
import brasuzaskins.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NavegacaoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    // MAPEAMENTO DA PAGINA DE RECUPERAÇÃO QUE É RECEBIDO VIA EMAIL DO LINK DE RECUPERAÇÃO
    @GetMapping("/recuperarSenha")
    public String mostrarFormularioRecuperacao(@RequestParam("token") String token, Model model) {
        // VERIFICA SE O TOKEN É VALIDO
        if (usuarioService.isTokenValido(token)) {
            model.addAttribute("token", token);  // PASSA O TOKEN PARA O THYMELEAF
            return "recuperarSenha";

        } else {
            model.addAttribute("errorMessage", "Token de recuperação inválido ou expirado.");
            return "redirect:/index";
        }
    }

    @GetMapping("/logado")
    public String logado(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        if (session.getAttribute("usuarioLogado") == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você precisa fazer login para acessar esta página.");
            return "redirect:/index";
        }

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("usuario", usuario);

        return "logado";
    }

    // MAPEAMENTO PARA REALIZAR O LOGOUT DO USUÁRIO
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // INVALIDA A SESSÃO DO USUÁRIO (FAZENDO O LOGOUT)
        session.invalidate();

        redirectAttributes.addFlashAttribute("successMessage", "Você saiu da sua conta.");
        return "redirect:/index";
    }

}
