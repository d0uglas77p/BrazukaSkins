package brasuzaskins.controller;

import brasuzaskins.model.Usuario;
import brasuzaskins.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // METODO PARA CADASTRAR USUARIO
    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Usuario usuario,
                            @RequestParam("confirmPassword") String confirmPassword,
                            RedirectAttributes redirectAttributes) {

            // USUARIO CADASTRADO
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso!");
            return "redirect:/index";

            // EMAIL JÁ CADASTRADO
        } catch (UsuarioService.EmailJaCadastradoException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/index";

            // TELEFONE JÁ CADASTRADO
        } catch (UsuarioService.TelefoneJaCadastradoException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/index";

            // SENHAS NÃO SÃO IGUAIS
        } catch (UsuarioService.ConfirmarSenhasException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/index";

            // ERRO
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar usuário: " + e.getMessage());
            return "redirect:/index";
        }
    }

    // METODO PARA ENVIAR LINK DE RECUPERAÇÃO DE CONTA
    @PostMapping("/recuperar")
    public String recuperar(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
                // VERIFICA SE EXISTE UM EMAIL CADASTRADO
            if (!usuarioService.isEmailCadastrado(email)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Esse e-mail não está cadastrado!");
                return "redirect:/index";
            }

            // GERA UM TOKEN UNICO PARA A RECUPERAÇÃO DE SENHA
            String token = usuarioService.gerarTokenRecuperacao(email);

            // GERA O LINK PARA RECUPERAR A SENHA + O TOKEN GERADO
            String urlRecuperacao = "http://localhost:8080/recuperarSenha?token=" + token;

            // ENVIAR O EMAIL COM O LINK
            usuarioService.enviarEmailRecuperacao(email, "Clique no link para recuperar sua senha: " + urlRecuperacao);

            redirectAttributes.addFlashAttribute("successMessage", "Um e-mail foi enviado com as instruções.");
            return "redirect:/index";

            // ERRO
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao processar a solicitação: " + e.getMessage());
            return "redirect:/index";
        }
    }

    // METODO QUE ATUALIZA A NOVA SENHA DO USUARIO
    @PostMapping("/atualizarSenha")
    public String atualizarSenha(@RequestParam("token") String token,
                                 @RequestParam("password") String novaSenha,
                                 RedirectAttributes redirectAttributes) {
        try {
            // SE O TOKEN FOR VALIDO
            if (usuarioService.isTokenValido(token)) {
                // ATUALIZA A SENHA DO USUARIO
                usuarioService.atualizarSenha(token, novaSenha);

                redirectAttributes.addFlashAttribute("successMessage", "Senha atualizada com sucesso.");
                return "redirect:/index";

                // SE O TOKEN NÃO FOR VALIDO, NÃO ATUALIZA
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Token inválido ou expirado.");
                return "redirect:/index";
            }

            // ERRO
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar a senha: " + e.getMessage());
            return "redirect:/index";
        }
    }

    // METODO QUE REDIRECIONA QUANDO O FORMULARARIO DE LOGIN FOR ENVIADO
    @PostMapping("/entrar")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            // AUTENTICA O USUÁRIO COM O SERVIÇO DE AUTENTICAÇÃO
            Usuario usuario = usuarioService.autenticarUsuario(email, password);

            // ARMAZENA O USUÁRIO AUTENTICADO NA SESSÃO
            session.setAttribute("usuarioLogado", usuario);

            redirectAttributes.addFlashAttribute("successMessage", "Login efetuado com sucesso.");

            return "redirect:/logado";
        } catch (UsuarioService.AutenticacaoException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/index";
        }
    }
}
