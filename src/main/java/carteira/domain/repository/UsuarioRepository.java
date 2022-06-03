package carteira.domain.repository;

import carteira.domain.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByUsuario(String usuario);

    List<Usuario> findByExcluido(boolean excluido);
}