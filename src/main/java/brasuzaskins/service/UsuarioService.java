package brasuzaskins.service;

import brasuzaskins.model.Usuario;
import brasuzaskins.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario usuario, String confirmarSenha) {
        // Verifica se as senhas são iguais
        if (usuario.getPassword() == null || !usuario.getPassword().equals(confirmarSenha)) {
            throw new ConfirmarSenhasException("As senhas não coincidem");
        }

        // Verifica se o e-mail já foi cadastrado
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailJaCadastradoException("E-mail já cadastrado");
        }

        // Verifica se o telefone já foi cadastrado
        if (usuarioRepository.existsByTelefone(usuario.getTelefone())) {
            throw new TelefoneJaCadastradoException("Número de telefone já cadastrado");
        }

        // Criptografa a senha antes de salvar com o JBCrypt
        String senhaCriptografada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(senhaCriptografada);

        // Salva o usuário
        return usuarioRepository.save(usuario);
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
}
