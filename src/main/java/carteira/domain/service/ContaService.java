package carteira.domain.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.Conta;
import carteira.domain.repository.ContaRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ContaService {

    private ContaRepository repository;
    private UsuarioService usuarioService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional
    public Conta salvar(Conta conta) {
        Conta con = repository.findByNome(conta.getNome()).orElse(null);

        if (con != null && !con.equals(conta)) {
            throw new NegocioException("Já existe um registro com o nome informado");
        }

        conta.setUsuario(usuarioService.buscar(conta.getUsuario().getId()));

        return repository.save(conta);
    }

    @Transactional
    public List<Conta> buscar() {
        return repository.findAll();
    }

    @Transactional
    public Conta buscar(String contaId) {
        if (contaId == null) {
            throw new NegocioException("Id não informado");
        }

        return repository.findById(contaId)
                .orElseThrow(() -> new NegocioException("Conta não encontrada"));
    }

    @Transactional
    public void verificarSeContaExiste(String contaId) {
        if (contaId == null) {
            throw new NegocioException("Id não informado");
        }

        if (!repository.existsById(contaId)) {
            throw new NegocioException("Conta não encontrada");
        }
    }

    @Transactional
    public void excluir(String contaId) {
        if (contaId == null) {
            throw new NegocioException("Id não informado");
        }
        repository.deleteById(contaId);
    }

    @Transactional
    public boolean adicionarValor(String contaId, double valor) {
        Query query = new Query(Criteria.where("id").is(contaId));

        Update update = new Update();
        update.inc("saldo", valor);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Conta.class);

        return result.getModifiedCount() > 0;
    }

    @Transactional
    public boolean removerValor(String contaId, double valor) {
        Query query = new Query(Criteria.where("id").is(contaId));

        Update update = new Update();
        update.inc("saldo", valor * (-1));

        UpdateResult result = mongoTemplate.updateFirst(query, update, Conta.class);

        return result.getModifiedCount() > 0;
    }
}
