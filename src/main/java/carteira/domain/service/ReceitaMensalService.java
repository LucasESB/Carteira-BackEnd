package carteira.domain.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.ReceitaMensal;
import carteira.domain.repository.ReceitaMensalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ReceitaMensalService {

    private ReceitaMensalRepository repository;
    private UsuarioService usuarioService;
    private CategoriaService categoriaService;

    @Transactional
    public List<ReceitaMensal> buscar() {
        return repository.findByExcluido(false);
    }

    @Transactional
    public ReceitaMensal buscar(String receitaMensalId) {
        return repository.findById(receitaMensalId)
                .orElseThrow(() -> new NegocioException("Receita mensal não encontrada"));
    }

    @Transactional
    public void verificarSeReceitaMensalExiste(String receitaMensalId) {
        buscar(receitaMensalId);
    }

    @Transactional
    public ReceitaMensal salvar(ReceitaMensal receitaMensal) {
        receitaMensal.setCategoria(categoriaService.buscar(receitaMensal.getCategoria().getId()));
        receitaMensal.setUsuario(usuarioService.buscar(receitaMensal.getUsuario().getId()));

        return repository.save(receitaMensal);
    }

    @Transactional
    public void excluir(ReceitaMensal receitaMensal) {
        receitaMensal.setExcluido(true);
        repository.save(receitaMensal);
    }
}
