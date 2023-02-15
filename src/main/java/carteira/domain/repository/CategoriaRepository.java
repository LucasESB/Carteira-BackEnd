package carteira.domain.repository;

import carteira.domain.model.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoriaRepository extends MongoRepository<Categoria, String> {

    Optional<Categoria> findByNome(String nome);
}
