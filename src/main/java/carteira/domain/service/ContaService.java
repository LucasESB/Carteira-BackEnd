package carteira.domain.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.Conta;
import carteira.domain.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ContaService {

    private ContaRepository repository;
    private UsuarioService usuarioService;

    @Transactional
    public List<Conta> buscar() {
        return repository.findByExcluido(false);
    }

    @Transactional
    public Conta buscar(String contaId) {
        return repository.findById(contaId)
                .orElseThrow(() -> new NegocioException("Conta não encontrada"));
    }

    @Transactional
    public void verificarSeContaExiste(String contaId) {
        buscar(contaId);
    }

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
    public void excluir(Conta conta){
        conta.setExcluido(true);
        repository.save(conta);
    }
}
