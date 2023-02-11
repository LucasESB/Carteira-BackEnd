package carteira.domain.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.Categoria;
import carteira.domain.repository.CategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaService {

    private CategoriaRepository repository;
    private UsuarioService usuarioService;

    @Transactional
    public Categoria salvar(Categoria categoria) {
        Categoria cat = repository.findByNome(categoria.getNome()).orElse(null);

        if (cat != null && !cat.equals(categoria)) {
            throw new NegocioException("Já existe um registro com o nome informado");
        }

        categoria.setUsuario(usuarioService.buscar(categoria.getUsuario().getId()));

        return repository.save(categoria);
    }

    @Transactional
    public List<Categoria> buscar() {
        return repository.findAll();
    }

    @Transactional
    public Categoria buscar(String categoriaId) {
        if (categoriaId == null) {
            throw new NegocioException("Id não informado");
        }

        return repository.findById(categoriaId)
                .orElseThrow(() -> new NegocioException("Categoria não encontrada"));
    }

    @Transactional
    public void verificarSeCategoriaExiste(String categoriaId) {
        if (categoriaId == null) {
            throw new NegocioException("Id não informado");
        }

        if (!repository.existsById(categoriaId)) {
            throw new NegocioException("Categoria não encontrada");
        }
    }

    @Transactional
    public void excluir(String categoriaId){
        if (categoriaId == null) {
            throw new NegocioException("Id não informado");
        }
        repository.deleteById(categoriaId);
    }
}
