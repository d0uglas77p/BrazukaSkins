                                                       ALPHA*
-----------------------------------------------------------------------------------------------------------------------------
                      ESSE É UM DOCUMENTO DE ANOTAÇÃO QUE IRÁ SERVIR COMO GUIA PARA CRIAR PROJETOS
                                   SPRING + THYMELEAF + HTML + CSS + JAVASCRIPT + MYSQL
-----------------------------------------------------------------------------------------------------------------------------
                                                    BRAZUKA SKINS
-----------------------------------------------------------------------------------------------------------------------------
                                                    CONFIGURAÇÕES 
-------------------------------------------------------------------------------------------------------------------------------------------------------

1. Criar o projeto no spring initializr:

   Maven/Java
   Spring Boot 3.4.1

   Project Metadata:
   Group Id: com.brasuzaskins
   Artifact: BrazukaSkins
   Name: BrazukaSkins
   Description: Comunidade Brasileira de Skins do Counter Strike

   Package name: brasuzaskins
   Packaging: war (empacotador do maven para subir na hospedagem)
   Java: 17 version

   Dependencies:
   Spring web
   Spring Data JPA
   Lombok    
   MySQL
   Thymeleaf    
   Devtools

   Gerar o arquivo, extrair e abrir no Intellij

-------------------------------------------------------------------------------------------------------------------------------------------------------

2. No arquivo POM, implementar:

        <!-- Para implantar no Tomcat EXTERNO-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>
    
    COMENTAR O TOMCAT (funcionamento do Devtools):
   
        <!--<dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
                <scope>provided</scope>
        </dependency>-->

   No plugin Maven: clean/install

-------------------------------------------------------------------------------------------------------------------------------------------------------

3. Na classe Main extends SpringBootServletInitializer
   Empacotar a aplicação como um WAR e implantá-la em um servidor de aplicação externo

-------------------------------------------------------------------------------------------------------------------------------------------------------

4. Criar um pacote -> config -> criar classe -> ResourceConfig -> implements WebMvcConfigurer
   Configura o Spring para acessar todas os arquivos da Static

   Para que os arquivos nos html tenham o /static no src: <br>
  
       img src="/static/images/logo.png"
-------------------------------------------------------------------------------------------------------------------------------------------------------

5. ESTRUTURA DO PROJETO:
   
         java
           brazukaskins
             config
               Classe: ResourceConfig
             controller
               Classe: NavegacaoController
               Classe: UsuarioController
             model
               Classe: Usuario
             repository
               Classe: UsuarioRepository
             service
               Classe: UsuarioService
             Classe Main: BrazukaSkinsApplication
    
          resources
             static
               css
                 index.css
               fontawesome
                 Arquivos do Font Awesome para icones
             images
               imagens.png
             js
               index.js
             templates
               index.html
               Outras paginas
             Arquivo de Configuração: application.properties

OBS: PARA CRIAR AS INTERFACES/FRONT-END
Abrir a pasta resources no VSCODE

-------------------------------------------------------------------------------------------------------------------------------------------------------

6. CONFIGURAÇÕES NO application.properties*

       #Nome da aplicação
       spring.application.name=BrazukaSkins
    
       #Desabilita o cache do Thymeleaf para desenvolvimento (ótimo para mudanças rápidas)
       spring.thymeleaf.cache=false
    
       #Configurações do Spring DevTools para facilitar o desenvolvimento
       spring.devtools.restart.enabled=true
       spring.devtools.livereload.enabled=true
    
       #Configuração de conexão com o banco de dados MySQL
       spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco?useSSL=false&serverTimezone=UTC
       spring.datasource.username=seu_usuario
       spring.datasource.password=sua_senha
       spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    
       #Configuração JPA (Hibernate)
       spring.jpa.hibernate.ddl-auto=update
       spring.jpa.show-sql=true

-------------------------------------------------------------------------------------------------------------------------------------------------------
                                                      HTML 

### Abaixo será listado linhas de códigos necessárias para o funcionamen da estrutura HTML

<!-- O Spring Boot usa o Thymeleaf para renderizar as views. -->
<html xmlns:th="http://www.thymeleaf.org">

-------------------------------------------------------------------------------------------------------------------------------------------------------

<head>
    <!-- Font Awesome para o ícone de menu -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <!-- Ant Design Componentes UI -->
    <link href="https://cdn.jsdelivr.net/npm/antd@4.18.6/dist/antd.min.css" rel="stylesheet">

    <!-- CSS do projeto -->
    <link rel="stylesheet" href="/static/css/index.css">

    <title>Brazuka Skins</title>
</head>

NO FINAL DO body
<script src="/static/js/index.js"></script>

-------------------------------------------------------------------------------------------------------------------------------------------------------

OBS: ACIMA NAS CONFIGURAÇÕES, FOI INCLUIDO UMA CLASSE PARA CONFIGURAR OS ARQUIVOS NAS PASTAS STATIC.
O SPRING BOOT AUTOMATICAMENTE JÁ RECONHECE OS ARQUIVOS NAS PASTAS STATIC, O QUE PARA ACESSAR NO INTELLIJ OS ARQUIVOS DO FRONT SERIA ASSIM:

    <img src="/images/logo.png">

PORÉM PARA PODER CRIAR O FRONT-END NO VSCODE, ESSA CLASSE FOI NECESSARIA, ASSIM COLOCANDO /static NA FRENTE, TANTO O INTELLIJ, QUANTO O VSCODE CONSEGUE ACESSAR:

    <img src="/static/images/logo.png">

SE FOR FAZER O FRONT PELO INTELLIJ NÃO SERIA NECESSARIO ESSA CLASSE

--------------------------------------------------------------------------------------------------------------------------------------------------------------

EXEMPLO DE USO DO JAVASCRIPT DO PROJETO

    <ul>
        <li><a href="javascript:void(0);" onclick="openModal()">Entrar</a></li>
    </ul>

NO index.js

    /* MODAL CADASTRO */
    function openModalCadastro() {
        document.getElementById("cadastroModal").style.display = "block";
    }

    function closeModalCadastro() {
        document.getElementById("cadastroModal").style.display = "none";
    }

    /* MODAL CADASTRO */

    /* FECHAR OS MODAIS */
    window.onclick = function (event) {
        const loginModal = document.getElementById("loginModal");
        const cadastroModal = document.getElementById("cadastroModal");

        // Fecha o modal de login se o clique for fora dele
        if (event.target === loginModal) {
            closeModal();
        }

        // Fecha o modal de cadastro se o clique for fora dele
        if (event.target === cadastroModal) {
            closeModalCadastro();
        }
    };
    /* FECHAR OS MODAIS */

NO index.css

    /* O Modal (inicialmente escondido) */
    .modal-login {
        display: none;
        position: fixed;
        z-index: 10; 
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        overflow: auto;
    }

    .modal-login-content {
        background-color: #232323;
        margin: 190px auto;
        width: 30%;
        border-radius: 20px;
        padding: 20px;
    }

--------------------------------------------------------------------------------------------------------------------------------------------------------------

=================================== HTML ====================================

================================ THYMELEAF ==================================


ABAIXO SEŔA LISTADO INFORMAÇÕES DO THYMELEAF PARA ACESSAR OS ENDPOINTS DAS CLASSES CONTROLLERS

EXEMPLO DE UM FORMULÁRIO, COM INPUTS, AONDE AO CLICAR EM CADASTRAR SERÁ FEITO UMA REQUISIÇÃO DE CADASTO NO SERVIDOR

                                ------------------------------------------------

    <!-- INICIO MODAL CADASTRO -->
    <div id="cadastroModal" class="modal-cadastro">
        <div class="modal-cadastro-content">
            <span class="close-cadastro" onclick="closeModalCadastro()">&times;</span>

            <form th:action="@{/usuarios/cadastrar}" th:method="post">
                <div class="div-cadastro">
                    <h2>Cadastro</h2>

                    <div class="div-input">
                        <input type="text" class="ant-input" id="nome-cadastro" name="nome" placeholder="Nome" required>
                        <input type="text" class="ant-input" id="sobrenome-cadastro" name="sobrenome" placeholder="Sobrenome" required>
                    </div>

                    <div class="div-input">
                        <input type="tel" class="ant-input" id="telefone-cadastro" name="telefone" placeholder="Telefone" required>
                        <input type="email" class="ant-input" id="email-cadastro" name="email" placeholder="E-mail" required>
                    </div>

                    <div class="div-input">
                        <input type="password" class="ant-input" id="senha" name="password" placeholder="Senha" required>
                        <input type="password" class="ant-input" id="repetir-senha" name="confirmPassword" placeholder="Repetir Senha" required>
                    </div>

                    <div class="div-btn">
                        <button class="btn-cadastro" type="submit">Cadastrar</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <!-- FIM MODAL CADASTRO -->

                                ------------------------------------------------

ATRIBUTOS IMPORTANTES A SEREM DESTACADOS:

    <form>
    </form>
        O elemento <form> no HTML é usado para criar um formulário que permite aos usuários enviar dados para o servidor. 
        Ele serve como um contêiner para diferentes campos de entrada, como <input>, <textarea>, <select> e botões, permitindo coletar informações do usuário.

    th: 
        O th: é um prefixo utilizado pelo Thymeleaf, um mecanismo de template para Java (template engine), para permitir a integração dinâmica entre o HTML e os dados do servidor. 
        Ele substitui ou complementa atributos padrão do HTML para permitir a manipulação e renderização de dados no lado do servidor.

    <form th:action="@{/usuarios/cadastrar}" th:method="post">
        ao submeter o formulário, os dados serão enviados para o endpoint /usuarios/cadastrar

        para submeter o formulário, é utilizado o esse botão com o type="submit"
                                <button class="btn-cadastro" type="submit">Cadastrar</button>
        
        th:method="post"
            Define que, ao submeter o formulário, será feita uma requisição HTTP com o método POST.          

        Por que usar POST?
            Segurança: Ao usar POST, os dados enviados não ficam expostos na URL, ao contrário de uma requisição GET.
            Envio de Dados: POST é ideal para enviar informações, como quando um usuário preenche um formulário de cadastro ou login.
    
    RESUMINDO:
        - Enviar uma requisição dentro de um formulario <form th:action="@{/usuarios/cadastrar}" th:method="post">
        - th: = prefixo do Thymeleaf (template para java), permite integração dinânimica entre o HTML e os dados do servidor.
        - action="@{/usuarios/cadastrar}" = endpoint da classe CONTROLLER
        - th:method="post" = envio de dados.
        - <button class="btn-cadastro" type="submit"></button> = botão que submete uma ação dentro do seu <form> 

    <input type="tel" class="ant-input" id="telefone-cadastro" name="telefone" placeholder="Telefone" required>

    ID = Serve para identificar unicamente o campo de entrada no documento. 
    O id é único dentro de uma página e pode ser usado para referenciar o campo em scripts JavaScript ou para associar etiquetas <label> ao campo.

    NAME = Define o nome do campo de entrada que será enviado com os dados do formulário quando o formulário for submetido. 
    Esse nome é utilizado no servidor para acessar o valor enviado, seja em um controller (como no Spring) ou para processar o dado em outra parte da aplicação.

================================ THYMELEAF ==================================


================================ BACK-END SPRING BOOT ===================================

AQUI SERÁ LISTADO TODAS AS CLASSES PARA FAZER O CADASTRO DO USUARIO DE ACORDO COM O FORMULARIO ACIMA

model -> repository -> service -> controller

model = modelo do Usuario, com os atributos IGUAIS ao do formularios name="telefone" e etc

repository = em Spring Data JPA tem a função de facilitar a interação entre a aplicação e o banco de dados,
permitindo operações de persistência (como salvar, buscar, atualizar e excluir dados) sem a necessidade de escrever implementações SQL manualmente.

service = tem a responsabilidade de conter a lógica de negócios.
Ele serve como intermediário entre o controlador e o repositório, orquestrando operações e regras de negócio antes de interagir com o banco de dados.

controller = em uma aplicação Spring é a camada responsável por receber as requisições HTTP, processá-las e retornar a resposta adequada.
Ele atua como o ponto de entrada para a aplicação, onde você pode mapear as URLs e associá-las aos métodos que irão processar essas requisições.

        A principal função do Controller é lidar com as interações do usuário (como formulários ou solicitações REST) e invocar a lógica de negócios apropriada, 
        normalmente delegando a execução dessa lógica para a camada de serviço. Ele também lida com a formatação da resposta, 
        seja retornando dados diretamente (em APIs REST) ou direcionando o usuário para uma página específica (em aplicações web tradicionais).

----------------- MODEL -----------------

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String sobrenome;
    private String telefone;
    private String email;
    private String password;

    // Métodos geters e seters
}

@Data: Gera automaticamente métodos getters, setters, toString(), equals(), e hashCode(). // Lombok
@Entity: Marca a classe como uma entidade JPA, mapeando-a para uma tabela no banco de dados. // Spring Data JPA

@GeneratedValue(strategy = GenerationType.IDENTITY): Define que o valor da chave primária será gerado automaticamente pelo banco de dados, usando uma estratégia de auto incremento.

----------------- REPOSITORY -----------------

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
boolean existsByEmail(String email);
}

@Repository: e a interface UsuarioRepository têm a finalidade de integrar o Spring com o mecanismo de persistência, permitindo a comunicação com o banco de dados.

extends JpaRepository<Usuario, Long> A interface UsuarioRepository estende JpaRepository, o que significa que ela herda uma série de métodos prontos para trabalhar com a entidade Usuario no banco de dados.

JpaRepository é uma interface genérica que fornece as principais operações de persistência de dados, como:

    save(): Para salvar ou atualizar registros no banco de dados.
    findById(): Para buscar registros por ID.
    findAll(): Para recuperar todos os registros.
    deleteById(): Para deletar registros com base no ID.

----------------- SERVICE -----------------

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }
}

É responsável pela lógica de negócios para cadastrar um novo usuário, utilizando o repositório UsuarioRepository para verificar a existência do email e salvar o usuário no banco de dados. Vamos analisar cada parte do código

@Service: marca a classe como um serviço no Spring, indicando que ela contém a lógica de negócios. Isso permite que o Spring a registre como um bean no contexto da aplicação e a injete em outras classes (como no UsuarioController).

@Autowired: é usada para injeção de dependência. Ela permite que o Spring injete automaticamente uma instância de UsuarioRepository na classe UsuarioService para que você possa usá-la para interagir com o banco de dados.

O método existsByEmail() verifica se já existe um usuário com o mesmo email no banco de dados.

Se o email já estiver registrado, o método lança uma exceção ResponseStatusException, com o código de status BAD_REQUEST (400) e a mensagem "E-mail já cadastrado". Isso interrompe o fluxo de execução e retorna a resposta com o erro.

Se o email não estiver em uso, o método save() do repositório usuarioRepository é chamado para salvar o usuário no banco de dados. O save() persiste o usuário e retorna o objeto salvo, o que inclui o ID gerado automaticamente (caso esteja usando @GeneratedValue).

----------------- CONTROLLER -----------------

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

É um exemplo de controlador no Spring para manipular requisições HTTP relacionadas aos usuários.

@RestController: indica que essa classe é um controlador para uma API REST. Ela já combina @Controller e @ResponseBody, ou seja, os métodos dessa classe retornam dados diretamente (geralmente em formato JSON ou texto), sem a necessidade de usar templates de visualização.

@RequestMapping: define a URL base para todos os métodos dessa classe. Nesse caso, todos os endpoints começam com /usuarios.

    Por exemplo, o endpoint para cadastrar um novo usuário seria /usuarios/cadastrar.

@Autowired: é usada para injetar a dependência da classe UsuarioService no controlador. O Spring gerencia automaticamente a criação e a injeção da instância do serviço.

@PostMapping: mapeia requisições HTTP POST para o método cadastrar(). Esse endpoint será acionado quando o cliente enviar uma requisição POST para a URL /usuarios/cadastrar. O método cadastrar() é responsável por cadastrar um novo usuário.

================================ BACK-END SPRING BOOT ===================================


 
