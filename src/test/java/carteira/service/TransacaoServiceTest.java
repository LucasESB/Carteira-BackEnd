package carteira.service;

import carteira.domain.enums.TipoCategoriaEnum;
import carteira.domain.exception.NegocioException;
import carteira.domain.model.*;
import carteira.domain.repository.CategoriaRepository;
import carteira.domain.repository.ContaRepository;
import carteira.domain.repository.TransacaoRepository;
import carteira.domain.repository.UsuarioRepository;
import carteira.domain.service.TransacaoService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransacaoServiceTest {

    @MockBean
    private TransacaoRepository transacaoRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private CategoriaRepository categoriaRepository;

    @MockBean
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoService transacaoService;

    @Test
    public void retornarSucesso_QuandoSalvar() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setNome("Lucas");
        usuario.setUsuario("lucas");

        Mockito.when(usuarioRepository.findById(usuario.getId()))
                .thenReturn(Optional.of(usuario));

        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setTipo(TipoCategoriaEnum.RECEITA);
        categoria.setNome("Renda extra");

        Mockito.when(categoriaRepository.findById(categoria.getId()))
                .thenReturn(Optional.of(categoria));

        Conta conta = new Conta();
        conta.setId("1");
        conta.setNome("Nubank");
        conta.setSaldo(0.00);

        Mockito.when(contaRepository.findById(conta.getId()))
                .thenReturn(Optional.of(conta));

        Transacao transacao = new Transacao();
        transacao.setValor(10);
        transacao.setDataTransacao(new Date());
        transacao.setDataCadastro(new Date());
        transacao.setDescricao("Apenas para teste");
        transacao.setCategoria(categoria);
        transacao.setConta(conta);
        transacao.setUsuario(usuario);

        Mockito.when(transacaoRepository.save(transacao))
                .thenReturn(transacao);

        Transacao transacaoResponse = transacaoService.salvar(transacao);

        Assertions.assertThat(transacaoResponse).isNotNull();
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoAtualizarComSaldoInvalido() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setNome("Lucas");
        usuario.setUsuario("lucas");

        Mockito.when(usuarioRepository.findById(usuario.getId()))
                .thenReturn(Optional.of(usuario));

        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setTipo(TipoCategoriaEnum.DESPESA);
        categoria.setNome("Internet");

        Mockito.when(categoriaRepository.findById(categoria.getId()))
                .thenReturn(Optional.of(categoria));

        Conta conta = new Conta();
        conta.setId("1");
        conta.setNome("Carteira");
        conta.setSaldo(0.00);

        Mockito.when(contaRepository.findById(conta.getId()))
                .thenReturn(Optional.of(conta));

        Transacao transacao = new Transacao();
        transacao.setId("1");
        transacao.setValor(99.90);
        transacao.setDataTransacao(new Date());
        transacao.setDataCadastro(new Date());
        transacao.setDescricao("Apenas para teste");
        transacao.setCategoria(categoria);
        transacao.setConta(conta);
        transacao.setUsuario(usuario);

        Categoria categoriaAnterior = new Categoria();
        categoriaAnterior.setId("2");
        categoriaAnterior.setTipo(TipoCategoriaEnum.RECEITA);
        categoriaAnterior.setNome("Renda extra");

        Conta contaAnterior = new Conta();
        contaAnterior.setId("2");
        contaAnterior.setNome("Nubank");
        contaAnterior.setSaldo(0.00);

        Transacao transacaoAnterior = new Transacao();
        transacaoAnterior.setId("1");
        transacaoAnterior.setValor(55.50);
        transacaoAnterior.setDataTransacao(new Date());
        transacaoAnterior.setDataCadastro(new Date());
        transacaoAnterior.setDescricao("Apenas para teste");
        transacaoAnterior.setCategoria(categoriaAnterior);
        transacaoAnterior.setConta(contaAnterior);
        transacaoAnterior.setUsuario(usuario);

        Mockito.when(transacaoRepository.findById(transacao.getId()))
                .thenReturn(Optional.of(transacaoAnterior));

        transacaoService.salvar(transacao);
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoBuscarComDatasInvalidas() {
        transacaoService.buscar("10/02/2023", "10/02/2023");
    }

    @Test
    public void retornarSucesso_QuandoBuscarTransacaoPorId() {
        Transacao transacao = new Transacao();
        transacao.setId("1");

        Mockito.when(transacaoRepository.findById(transacao.getId()))
                .thenReturn(Optional.of(transacao));

        Transacao transacaoResponse = transacaoService.buscar(transacao.getId());

        Assertions.assertThat(transacaoResponse).isNotNull();
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoBuscarTransacaoPorId() {
        Transacao transacao = new Transacao();
        transacao.setId("1");

        Mockito.when(transacaoRepository.findById(transacao.getId()))
                .thenReturn(Optional.empty());

        transacaoService.buscar(transacao.getId());
    }

    @Test(expected = NegocioException.class)
    public void retornarErro_QuandoVerificarSeTransacaoExiste() {
        Mockito.when(transacaoRepository.existsById("1"))
                .thenReturn(false);
        transacaoService.verificarSeTransacaoExiste("1");
    }
}
