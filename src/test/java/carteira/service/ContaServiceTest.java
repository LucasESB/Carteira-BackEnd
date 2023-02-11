package carteira.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.Conta;
import carteira.domain.model.Usuario;
import carteira.domain.repository.ContaRepository;
import carteira.domain.repository.UsuarioRepository;
import carteira.domain.service.ContaService;
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
public class ContaServiceTest {

    @MockBean
    private ContaRepository contaRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaService contaService;

    @Test
    public void retornarSucesso_QuandoSalvar() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setNome("Lucas");
        usuario.setUsuario("lucas");
        usuario.setSenha("lucas1234578");

        Mockito.when(usuarioRepository.findById(usuario.getId()))
                .thenReturn(Optional.of(usuario));

        Conta conta = new Conta();
        conta.setId("1");
        conta.setNome("Nubank");
        conta.setSaldo(0.00);
        conta.setUsuario(usuario);

        Mockito.when(contaRepository.findByNome(conta.getNome()))
                .thenReturn(Optional.empty());

        Mockito.when(contaRepository.save(conta))
                .thenReturn(conta);

        Conta contaResponse = contaService.salvar(conta);

        Assertions.assertThat(contaResponse).isNotNull();
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoSalvar() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setNome("Lucas");
        usuario.setUsuario("lucas");
        usuario.setSenha("lucas1234578");

        Conta conta = new Conta();
        conta.setId("1");
        conta.setNome("Nubank");
        conta.setSaldo(0.00);
        conta.setUsuario(usuario);

        Conta contaRetorno = new Conta();
        contaRetorno.setId("2");
        contaRetorno.setNome("Carteira");
        contaRetorno.setSaldo(0.00);
        contaRetorno.setUsuario(usuario);

        Mockito.when(contaRepository.findByNome(conta.getNome()))
                .thenReturn(Optional.of(contaRetorno));

        contaService.salvar(conta);
    }

    @Test
    public void retornarSucesso_QuandoBuscarContaPorId() {
        Conta conta = new Conta();
        conta.setId("1");

        Mockito.when(contaRepository.findById(conta.getId()))
                .thenReturn(Optional.of(conta));

        Conta contaResponse = contaService.buscar(conta.getId());

        Assertions.assertThat(contaResponse).isNotNull();
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoBuscarContaPorId() {
        Conta conta = new Conta();
        conta.setId("1");

        Mockito.when(contaRepository.findById(conta.getId()))
                .thenReturn(Optional.empty());

        contaService.buscar(conta.getId());
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoVerificarSeContaExiste() {
        Mockito.when(contaRepository.existsById("1"))
                .thenReturn(false);
        contaService.verificarSeContaExiste("1");
    }
}
