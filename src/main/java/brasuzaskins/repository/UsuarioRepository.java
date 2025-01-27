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

    boolean existsByEmail(String email);

    boolean existsByTelefone(String telefone);

    boolean existsByLinkSteam(String linkSteam);

    Usuario findByEmail(String email);

    @Modifying
    @Query("UPDATE Usuario u SET u.tokenRecuperacao = :token, u.tokenExpiracao = :tokenExpiracao WHERE u.email = :email")
    void salvarTokenRecuperacao(
            @Param("email") String email,
            @Param("token") String token,
            @Param("tokenExpiracao") LocalDateTime tokenExpiracao
    );

    @Query("SELECT u FROM Usuario u WHERE u.tokenRecuperacao = :token AND u.tokenExpiracao > :dataAtual")
    Usuario findByTokenRecuperacao(
            @Param("token") String token,
            @Param("dataAtual") LocalDateTime dataAtual
    );
}