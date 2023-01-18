package carteira.domain.repository;

import carteira.domain.model.Conta;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ContaRepository extends MongoRepository<Conta, String> {
    Optional<Conta> findByNome(String nome);
}
