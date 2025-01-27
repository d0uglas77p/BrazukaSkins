package brasuzaskins.service;

import brasuzaskins.model.Usuario;
import brasuzaskins.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public Usuario cadastrarUsuario(Usuario usuario, String confirmarSenha) {

        if (usuario.getPassword() == null || !usuario.getPassword().equals(confirmarSenha)) {
            throw new ConfirmarSenhasException("As senhas não coincidem");
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailJaCadastradoException("E-mail já cadastrado");
        }

        if (usuarioRepository.existsByTelefone(usuario.getTelefone())) {
            throw new TelefoneJaCadastradoException("Número de telefone já cadastrado");
        }

        String senhaCriptografada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(senhaCriptografada);

        return usuarioRepository.save(usuario);
    }

    public boolean isEmailCadastrado(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void enviarEmailRecuperacao(String email, String mensagem) {
        if (!isEmailCadastrado(email)) {
            throw new EmailNaoCadastradoException("E-mail não cadastrado");
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Recuperação de Senha");
        mailMessage.setText(mensagem);
        javaMailSender.send(mailMessage);
    }

    public static class EmailJaCadastradoException extends RuntimeException {
        public EmailJaCadastradoException(String message) {
            super(message);
        }
    }

    public static class TelefoneJaCadastradoException extends RuntimeException {
        public TelefoneJaCadastradoException(String message) {
            super(message);
        }
    }

    public static class ConfirmarSenhasException extends RuntimeException {
        public ConfirmarSenhasException(String message) {
            super(message);
        }
    }

    public static class EmailNaoCadastradoException extends RuntimeException {
        public EmailNaoCadastradoException(String message) {
            super(message);
        }
    }

    @Transactional
    public String gerarTokenRecuperacao(String email) {
        String token = UUID.randomUUID().toString();

        LocalDateTime dataExpiracao = LocalDateTime.now().plusHours(1);

        usuarioRepository.salvarTokenRecuperacao(email, token, dataExpiracao);

        return token;
    }

    public boolean isTokenValido(String token) {
        LocalDateTime agora = LocalDateTime.now();
        Usuario usuario = usuarioRepository.findByTokenRecuperacao(token, agora);
        return usuario != null;
    }

    @Transactional
    public void atualizarSenha(String token, String novaSenha) {
        if (novaSenha.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
        }

        Usuario usuario = usuarioRepository.findByTokenRecuperacao(token, LocalDateTime.now());

        if (usuario != null) {
            usuario.setPassword(BCrypt.hashpw(novaSenha, BCrypt.gensalt()));
            usuario.setTokenRecuperacao(null);
            usuario.setTokenExpiracao(null);

            usuarioRepository.save(usuario);

        } else {
            throw new TokenExpiradoException("O token expirou ou é inválido.");
        }
    }

    public static class TokenExpiradoException extends RuntimeException {
        public TokenExpiradoException(String message) {
            super(message);
        }
    }

    public Usuario autenticarUsuario(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new AutenticacaoException("E-mail ou senha incorretos");
        }

        if (!BCrypt.checkpw(senha, usuario.getPassword())) {
            throw new AutenticacaoException("E-mail ou senha incorretos");
        }

        return usuario;
    }

    public static class AutenticacaoException extends RuntimeException {
        public AutenticacaoException(String message) {
            super(message);
        }
    }

    public Usuario atualizarPerfil(Usuario usuarioAtualizado, Usuario usuarioLogado) {
        if (!usuarioLogado.getId().equals(usuarioAtualizado.getId())) {
            throw new IllegalArgumentException("Usuário não autorizado a alterar esses dados.");
        }

        usuarioLogado.setNome(usuarioAtualizado.getNome());
        usuarioLogado.setSobrenome(usuarioAtualizado.getSobrenome());
        usuarioLogado.setTelefone(usuarioAtualizado.getTelefone());
        usuarioLogado.setEmail(usuarioAtualizado.getEmail());

        if (usuarioAtualizado.getPassword() != null && !usuarioAtualizado.getPassword().isEmpty()) {
            usuarioLogado.setPassword(BCrypt.hashpw(usuarioAtualizado.getPassword(), BCrypt.gensalt()));
        }

        if (usuarioAtualizado.getLinkSteam() != null && !usuarioAtualizado.getLinkSteam().isEmpty()) {
            if (!usuarioAtualizado.getLinkSteam().startsWith("https://steamcommunity.com/")) {
                throw new IllegalArgumentException("O link da Steam deve ser um perfil público válido.");
            }
            usuarioLogado.setLinkSteam(usuarioAtualizado.getLinkSteam());
        }

        return usuarioRepository.save(usuarioLogado);
    }

    @Transactional
    public void excluirUsuario(Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getId()).orElse(null);

        if (usuarioExistente == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        usuarioRepository.delete(usuarioExistente);
    }
}