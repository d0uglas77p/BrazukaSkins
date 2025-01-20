package brasuzaskins.service;

import brasuzaskins.model.Usuario;
import brasuzaskins.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
}
