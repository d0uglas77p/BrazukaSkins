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

    // METODO PARA CADASTRAR O USUARIO
    public Usuario cadastrarUsuario(Usuario usuario, String confirmarSenha) {

        // RECEBE A EXCEÇÃO SE AS SENHAS NÃO FOREM IGUAIS
        if (usuario.getPassword() == null || !usuario.getPassword().equals(confirmarSenha)) {
            throw new ConfirmarSenhasException("As senhas não coincidem");
        }

        // RECEBE A EXCEÇÃO SE O EMAIL JÁ EXISTE NO CADASTRO
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailJaCadastradoException("E-mail já cadastrado");
        }

        // RECEBE A EXCEÇÃO SE O TELEFONE JÁ EXISTE NO CADASTRO
        if (usuarioRepository.existsByTelefone(usuario.getTelefone())) {
            throw new TelefoneJaCadastradoException("Número de telefone já cadastrado");
        }

        // CRIPTOGRAFA A SENHA
        String senhaCriptografada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(senhaCriptografada);

        // SALVA O USUARIO
        return usuarioRepository.save(usuario);
    }

    // METODO PARA VERIFICAR SE O EMAIL JÁ ESTÁ CADASTRADO
    public boolean isEmailCadastrado(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // METODO PARA ENVIAR E-MAIL DE RECUPERAÇÃO
    public void enviarEmailRecuperacao(String email, String mensagem) {

        // SE O EMAIL O EMAIL NÃO ESTIVER CADASTRADO
        if (!isEmailCadastrado(email)) {
            throw new EmailNaoCadastradoException("E-mail não cadastrado");
        }

        // ENVIA O EMAIL
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Recuperação de Senha");
        mailMessage.setText(mensagem);
        javaMailSender.send(mailMessage);
    }

    // EXCEPTION SE O EMAIL JÁ ESTA CADASTRADO PARA CADASTRO
    public static class EmailJaCadastradoException extends RuntimeException {
        public EmailJaCadastradoException(String message) {
            super(message);
        }
    }

    // EXCEPTION SE O TELEFONE JÁ ESTA CADASTRADO
    public static class TelefoneJaCadastradoException extends RuntimeException {
        public TelefoneJaCadastradoException(String message) {
            super(message);
        }
    }

    // EXCEPTION SE AS SENHAS NÃO SÃO IGUAIS
    public static class ConfirmarSenhasException extends RuntimeException {
        public ConfirmarSenhasException(String message) {
            super(message);
        }
    }

    // EXCEPTION SE O EMAIL NÃO ESTA CADASTRADO NA RECUPERAÇÃO DE SENHA
    public static class EmailNaoCadastradoException extends RuntimeException {
        public EmailNaoCadastradoException(String message) {
            super(message);
        }
    }

    // METODO PARA GERAR O TOKEN DE RECUPERAÇÃO
    @Transactional
    public String gerarTokenRecuperacao(String email) {
        String token = UUID.randomUUID().toString(); // TOKEN

        LocalDateTime dataExpiracao = LocalDateTime.now().plusHours(1); // EXPERIÇÃO DO TOKEN

        usuarioRepository.salvarTokenRecuperacao(email, token, dataExpiracao); // SALVA O TOKEN DO USUARIO

        return token;
    }

    // METODO PARA VERIFICAR SE O TOKEN É VÁLIDO
    public boolean isTokenValido(String token) {
        LocalDateTime agora = LocalDateTime.now();
        Usuario usuario = usuarioRepository.findByTokenRecuperacao(token, agora);
        return usuario != null;
    }

    // METODO PARA ATUALIZAR A SENHA DO USUÁRIO APÓS A RECUPERAÇÃO
    @Transactional
    public void atualizarSenha(String token, String novaSenha) {

        // CONDIÇÃO QUE VERIFICA SE A NOVA SENHA É MENOR QUE 8
        if (novaSenha.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
        }

        Usuario usuario = usuarioRepository.findByTokenRecuperacao(token, LocalDateTime.now());

        // SE A SENHA O TOKEN DO USUARIO FOR DIFERENTE DE NULL
        if (usuario != null) {
            // CRIPTOGRAFA A NOVA SENHA
            usuario.setPassword(BCrypt.hashpw(novaSenha, BCrypt.gensalt()));
            usuario.setTokenRecuperacao(null);
            usuario.setTokenExpiracao(null);

            // SALVA
            usuarioRepository.save(usuario);

        // LANÇA A EXCEPTION DO TOKEN INVALIDO
        } else {
            throw new TokenExpiradoException("O token expirou ou é inválido.");
        }
    }

    // EXCEÇÃO LANÇADA QUANDO O TOKEN EXPIRA OU É INVÁLIDO
    public static class TokenExpiradoException extends RuntimeException {
        public TokenExpiradoException(String message) {
            super(message);
        }
    }
}
