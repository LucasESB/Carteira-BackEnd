package carteira.domain.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.DespesaMensal;
import carteira.domain.model.TipoCategoria;
import carteira.domain.repository.DespesaMensalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DespesaMensalService {

    private DespesaMensalRepository repository;
    private UsuarioService usuarioService;
    private CategoriaService categoriaService;

    @Transactional
    public List<DespesaMensal> buscar() {
        return repository.findByExcluido(false);
    }

    @Transactional
    public DespesaMensal buscar(String despesaMensalId) {
        return repository.findById(despesaMensalId)
                .orElseThrow(() -> new NegocioException("Despesa mensal não encontrada"));
    }

    @Transactional
    public void verificarSeDespesaMensalExiste(String despesaMensalId) {
        buscar(despesaMensalId);
    }

    @Transactional
    public DespesaMensal salvar(DespesaMensal despesaMensal) {
        despesaMensal.setCategoria(categoriaService.buscar(despesaMensal.getCategoria().getId()));

        if (!despesaMensal.getCategoria().getTipo().equals(TipoCategoria.DESPESA)) {
            throw new NegocioException("A categoria deve ser do tipo: DESPESA");
        }

        despesaMensal.setUsuario(usuarioService.buscar(despesaMensal.getUsuario().getId()));

        return repository.save(despesaMensal);
    }

    @Transactional
    public void excluir(DespesaMensal despesaMensal) {
        despesaMensal.setExcluido(true);
        repository.save(despesaMensal);
    }
}
