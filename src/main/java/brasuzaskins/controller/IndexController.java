package brasuzaskins.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/*  identificar a classe como um controlador de requisições HTTP. */
@Controller
public class IndexController {

    /* No Spring Boot, o controlador mapeia uma URL para uma view
    (que é o arquivo HTML) por meio do retorno do metodo.
     */

    /* A anotação @GetMapping("/") indica que o metodo home()
    será chamado quando o navegador fizer uma requisição GET para a raiz da aplicação (/).
     */
    @GetMapping("/")
    public String index(Model model) {
        /* Dentro do metodo home(), o Spring adiciona o atributo message ao Model.
        O Model é um objeto que carrega os dados que você deseja passar para a view (neste caso, o arquivo home.html).
         */
        model.addAttribute("message", "Bem-vindo à Comunidade Brazuka Skins!");
        return "index"; // Vai procurar por "home.html" no diretório templates
    }

    /* O metodo home() dentro de HomeController é mapeado para a URL raiz / da aplicação.
    Isso significa que sempre que alguém acessar http://localhost:8080/, esse metodo será invocado.
     */
}
