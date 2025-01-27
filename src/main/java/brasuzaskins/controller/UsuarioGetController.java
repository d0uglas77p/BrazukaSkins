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

import java.time.format.DateTimeFormatter;

@Controller
public class UsuarioGetController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/recuperarSenha")
    public String mostrarFormularioRecuperacao(@RequestParam("token") String token, Model model) {
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

        if (usuario != null && usuario.getDataRegistro() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataFormatada = usuario.getDataRegistro().format(formatter);
            model.addAttribute("dataRegistroFormatada", dataFormatada);
        }

        model.addAttribute("usuario", usuario);
        return "logado";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();

        redirectAttributes.addFlashAttribute("successMessage", "Você saiu da sua conta.");
        return "redirect:/index";
    }
}