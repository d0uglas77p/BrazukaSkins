package brasuzaskins.controller;

import brasuzaskins.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NavegacaoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/vender")
    public String vender() {
        return "vender";
    }

    @GetMapping("/calcular")
    public String calcular() {
        return "calcular";
    }

    @GetMapping("/sobre")
    public String sobre() {
        return "sobre";
    }

    @GetMapping("/ajuda")
    public String ajuda() {
        return "ajuda";
    }

    // MAPEAMENTO DA PAGINA DE RECUPERAÇÃO QUE É RECEBIDO VIA EMAIL DO LINK DE RECUPERAÇÃO
    @GetMapping("/recuperarSenha")
    public String mostrarFormularioRecuperacao(@RequestParam("token") String token, Model model) {
        // VERIFICA SE O TOKEN É VALIDO
        if (usuarioService.isTokenValido(token)) {
            model.addAttribute("token", token);  // PASSA O TOKEN PARA O THYMELEAF
            return "recuperarSenha";

        } else {
            model.addAttribute("errorMessage", "Token de recuperação inválido ou expirado.");
            return "redirect:/index";
        }
    }
}
