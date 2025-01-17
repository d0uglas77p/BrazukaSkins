package brasuzaskins.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavegacaoController {

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
}
