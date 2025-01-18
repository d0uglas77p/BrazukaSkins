package brasuzaskins.controller;

import brasuzaskins.model.Usuario;
import brasuzaskins.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            return "Cadastro realizado com sucesso!";
        } catch (ResponseStatusException e) {
            return e.getReason();
        }
    }
}
