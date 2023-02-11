package carteira.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.Categoria;
import carteira.domain.model.TipoCategoria;
import carteira.domain.model.Usuario;
import carteira.domain.repository.CategoriaRepository;
import carteira.domain.repository.UsuarioRepository;
import carteira.domain.service.CategoriaService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoriaServiceTest {

    @MockBean
    private CategoriaRepository categoriaRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Test
    public void retornarSucesso_QuandoSalvar() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setNome("Lucas");
        usuario.setUsuario("lucas");
        usuario.setSenha("lucas1234578");

        Mockito.when(usuarioRepository.findById(usuario.getId()))
                .thenReturn(Optional.of(usuario));

        Categoria categoria = new Categoria();
        categoria.setTipo(TipoCategoria.RECEITA);
        categoria.setNome("Salário");
        categoria.setUsuario(usuario);

        Mockito.when(categoriaRepository.findByNome(categoria.getNome()))
                .thenReturn(Optional.empty());

        Mockito.when(categoriaRepository.save(categoria))
                .thenReturn(categoria);

        Categoria categoriaResponse = categoriaService.salvar(categoria);

        Assertions.assertThat(categoriaResponse).isNotNull();
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoSalvar() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setNome("Lucas");
        usuario.setUsuario("lucas");
        usuario.setSenha("lucas1234578");

        Categoria categoria = new Categoria();
        categoria.setTipo(TipoCategoria.RECEITA);
        categoria.setNome("Salário");
        categoria.setUsuario(usuario);

        Categoria categoriaRetorno = new Categoria();
        categoriaRetorno.setId("2");
        categoriaRetorno.setTipo(TipoCategoria.RECEITA);
        categoriaRetorno.setNome("Renda extra");
        categoriaRetorno.setUsuario(usuario);

        Mockito.when(categoriaRepository.findByNome(categoria.getNome()))
                .thenReturn(Optional.of(categoriaRetorno));

        categoriaService.salvar(categoria);
    }

    @Test
    public void retornarSucesso_QuandoBuscarCategoriaPorId() {
        Categoria categoria = new Categoria();
        categoria.setId("1");

        Mockito.when(categoriaRepository.findById(categoria.getId()))
                .thenReturn(Optional.of(categoria));

        Categoria categoriaResponse = categoriaService.buscar(categoria.getId());

        Assertions.assertThat(categoriaResponse).isNotNull();
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoBuscarCategoriaPorId() {
        Categoria categoria = new Categoria();
        categoria.setId("1");

        Mockito.when(categoriaRepository.findById(categoria.getId()))
                .thenReturn(Optional.empty());

        categoriaService.buscar(categoria.getId());
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoVerificarSeCategoriaExiste() {
        Mockito.when(categoriaRepository.existsById("1"))
                .thenReturn(false);
        categoriaService.verificarSeCategoriaExiste("1");
    }
}
