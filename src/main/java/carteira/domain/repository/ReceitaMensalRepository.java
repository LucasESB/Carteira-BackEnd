package carteira.domain.repository;

import carteira.domain.model.ReceitaMensal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReceitaMensalRepository extends MongoRepository<ReceitaMensal, String> {
    List<ReceitaMensal> findByExcluido(boolean excluido);
}
