package brasuzaskins.controller;

import brasuzaskins.model.Usuario;
import brasuzaskins.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

        } catch (UsuarioService.TelefoneJaCadastradoException e) {
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

    @PostMapping("/recuperar")
    public String recuperar(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            if (!usuarioService.isEmailCadastrado(email)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Esse e-mail não está cadastrado!");
                return "redirect:/index";
            }

            // Envia o e-mail de recuperação
            usuarioService.enviarEmailRecuperacao(email, "EMAIL ESTÁ CADASTRADO");

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("successMessage", "Um e-mail foi enviado com as instruções.");
            return "redirect:/index";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao processar a solicitação: " + e.getMessage());
            return "redirect:/index";
        }
    }

}
