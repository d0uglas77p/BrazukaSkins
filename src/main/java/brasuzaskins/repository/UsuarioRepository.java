package brasuzaskins.repository;

import brasuzaskins.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // METODO PARA VERIFICAR SE UM EMAIL JÁ ESTÁ CADASTRADO
    boolean existsByEmail(String email);

    // METODO PARA VERIFICAR SE UM TELEFONE JÁ ESTÁ CADASTRADO
    boolean existsByTelefone(String telefone);

    // METODO PARA SALVAR O TOKEN DE RECUPERAÇÃO DE SENHA NO BANCO DE DADOS
    @Modifying
    @Query("UPDATE Usuario u SET u.tokenRecuperacao = :token, u.tokenExpiracao = :tokenExpiracao WHERE u.email = :email")
    void salvarTokenRecuperacao(
            @Param("email") String email,
            @Param("token") String token,
            @Param("tokenExpiracao") LocalDateTime tokenExpiracao
    );

    // METODO PARA BUSCAR UM USUARIO PELO TOKEN DE RECUPERAÇÃO, VERIFICANDO SE O TOKEN É VÁLIDO
    @Query("SELECT u FROM Usuario u WHERE u.tokenRecuperacao = :token AND u.tokenExpiracao > :dataAtual")
    Usuario findByTokenRecuperacao(
            @Param("token") String token,
            @Param("dataAtual") LocalDateTime dataAtual
    );
}
