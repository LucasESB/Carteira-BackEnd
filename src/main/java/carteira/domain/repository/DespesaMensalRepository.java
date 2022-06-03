package carteira.domain.repository;

import carteira.domain.model.DespesaMensal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DespesaMensalRepository extends MongoRepository<DespesaMensal, String> {
    List<DespesaMensal> findByExcluido(boolean excluido);
}
