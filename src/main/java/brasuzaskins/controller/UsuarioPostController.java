package brasuzaskins.controller;

import brasuzaskins.model.Usuario;
import brasuzaskins.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioPostController {

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

            String token = usuarioService.gerarTokenRecuperacao(email);

            String urlRecuperacao = "http://localhost:8080/recuperarSenha?token=" + token;

            usuarioService.enviarEmailRecuperacao(email, "Clique no link para recuperar sua senha: " + urlRecuperacao);

            redirectAttributes.addFlashAttribute("successMessage", "Um e-mail foi enviado com as instruções.");
            return "redirect:/index";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao processar a solicitação: " + e.getMessage());
            return "redirect:/index";
        }
    }

    @PostMapping("/atualizarSenha")
    public String atualizarSenha(@RequestParam("token") String token,
                                 @RequestParam("password") String novaSenha,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (usuarioService.isTokenValido(token)) {

                usuarioService.atualizarSenha(token, novaSenha);
                redirectAttributes.addFlashAttribute("successMessage", "Senha atualizada com sucesso.");
                return "redirect:/index";

            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Token inválido ou expirado.");
                return "redirect:/index";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar a senha: " + e.getMessage());
            return "redirect:/index";
        }
    }

    @PostMapping("/entrar")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.autenticarUsuario(email, password);

            session.setAttribute("usuarioLogado", usuario);
            redirectAttributes.addFlashAttribute("successMessage", "Login efetuado com sucesso.");

            return "redirect:/logado";
        } catch (UsuarioService.AutenticacaoException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/index";
        }
    }

    @PostMapping("/atualizarPerfil")
    public String atualizarPerfil(@ModelAttribute Usuario usuarioAtualizado,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

            if (usuarioLogado == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Usuário não está logado.");
                return "redirect:/index";
            }

            usuarioAtualizado.setId(usuarioLogado.getId());
            Usuario usuarioAtualizadoFinal = usuarioService.atualizarPerfil(usuarioAtualizado, usuarioLogado);
            session.setAttribute("usuarioLogado", usuarioAtualizadoFinal);

            redirectAttributes.addFlashAttribute("successMessage", "Perfil atualizado com sucesso!");
            return "redirect:/logado";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar o perfil: " + e.getMessage());
            return "redirect:/logado";
        }
    }

    @PostMapping("/excluirConta")
    public String excluirConta(HttpSession session, @RequestParam("password") String senhaInformada, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuário não autenticado.");
            return "redirect:/logado";
        }

        if (!BCrypt.checkpw(senhaInformada, usuario.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Senha incorreta.");
            return "redirect:/logado";
        }

        try {
            usuarioService.excluirUsuario(usuario);
            session.removeAttribute("usuarioLogado");

            redirectAttributes.addFlashAttribute("successMessage", "Conta excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir a conta: " + e.getMessage());
        }

        return "redirect:/index";
    }

    @PostMapping("/vincularSteam")
    public String vincularSteam(@RequestParam("linkSteam") String linkSteam,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

            if (usuarioLogado == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Usuário não está logado.");
                return "redirect:/index";
            }

            usuarioLogado.setLinkSteam(linkSteam);
            usuarioService.atualizarPerfil(usuarioLogado, usuarioLogado);
            session.setAttribute("usuarioLogado", usuarioLogado);

            redirectAttributes.addFlashAttribute("successMessage", "Conta Steam vinculada com sucesso!");
            return "redirect:/logado";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao vincular a conta Steam: " + e.getMessage());
            return "redirect:/logado";
        }
    }
}