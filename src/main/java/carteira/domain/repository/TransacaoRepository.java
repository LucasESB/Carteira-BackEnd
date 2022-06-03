package carteira.domain.repository;

import carteira.domain.model.Transacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface TransacaoRepository extends MongoRepository<Transacao, String> {

    @Query("{dataTransacao: {$gte: ?0, $lte: ?1}}")
    List<Transacao> findByDataTransacao(Date ini, Date fin);
    List<Transacao> findByExcluido(boolean excluido);
}
