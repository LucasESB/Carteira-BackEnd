package carteira.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.Usuario;
import carteira.domain.repository.UsuarioRepository;
import carteira.domain.service.UsuarioService;
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
public class UsuarioServiceTest {

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void retornarSucesso_QuandoSalvar() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setNome("Lucas");
        usuario.setUsuario("lucas");
        usuario.setSenha("lucas1234578");

        Mockito.when(usuarioRepository.findByUsuario(usuario.getUsuario()))
                .thenReturn(Optional.empty());

        Mockito.when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Assertions.assertThat(usuarioResponse).isNotNull();
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoSalvar() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setNome("Lucas");
        usuario.setUsuario("lucas");
        usuario.setSenha("lucas1234578");

        Usuario usuarioRetorno = new Usuario();
        usuarioRetorno.setId("2");
        usuarioRetorno.setNome("Eduardo");
        usuarioRetorno.setUsuario("eduardo");
        usuarioRetorno.setSenha("eduardo1234578");


        Mockito.when(usuarioRepository.findByUsuario(usuario.getUsuario()))
                .thenReturn(Optional.of(usuarioRetorno));

        Mockito.when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        usuarioService.salvar(usuario);
    }

    @Test
    public void retornarSucesso_QuandoBuscarUsuarioPorId() {
        Usuario usuario = new Usuario();
        usuario.setId("1");

        Mockito.when(usuarioRepository.findById(usuario.getId()))
                .thenReturn(Optional.of(usuario));

        Usuario usuarioResponse = usuarioService.buscar(usuario.getId());

        Assertions.assertThat(usuarioResponse).isNotNull();
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoBuscarUsuarioPorId() {
        Usuario usuario = new Usuario();
        usuario.setId("1");

        Mockito.when(usuarioRepository.findById(usuario.getId()))
                .thenReturn(Optional.empty());

        usuarioService.buscar(usuario.getId());
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoVerificarSeUsuarioExiste() {
        Mockito.when(usuarioRepository.existsById("1"))
                .thenReturn(false);
        usuarioService.verificarSeUsuarioExiste("1");
    }
}
