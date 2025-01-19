package brasuzaskins.controller;

import brasuzaskins.model.Usuario;
import brasuzaskins.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Usuario usuario,
                            @RequestParam("confirmPassword") String confirmPassword,
                            RedirectAttributes redirectAttributes) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso!");
            return "redirect:/index";

        } catch (UsuarioService.EmailJaCadastradoException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/index";

        } catch (UsuarioService.ConfirmarSenhasException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/index";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar usuário: " + e.getMessage());
            return "redirect:/index";
        }
    }
}
