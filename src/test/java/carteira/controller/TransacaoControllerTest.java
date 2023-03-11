package carteira.controller;

import carteira.domain.enums.TipoCategoriaEnum;
import carteira.domain.model.*;
import carteira.domain.service.CategoriaService;
import carteira.domain.service.ContaService;
import carteira.domain.service.TransacaoService;
import carteira.domain.service.UsuarioService;
import carteira.utilitarios.DataHora;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ContaService contaService;

    @Autowired
    private TransacaoService transacaoService;

    @Test
    public void retornarSucesso_QuandoInserir() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Sophia");
        usuario.setUsuario("sophia");
        usuario.setSenha("sophia12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Categoria categoria = new Categoria();
        categoria.setNome("Categoria teste 1");
        categoria.setTipo(TipoCategoriaEnum.RECEITA);
        categoria.setUsuario(usuarioResponse);

        Categoria categoriaResponse = categoriaService.salvar(categoria);

        Conta conta = new Conta();
        conta.setNome("Conta teste 1");
        conta.setSaldo(100.00);
        conta.setUsuario(usuarioResponse);

        Conta contaResponse = contaService.salvar(conta);

        Transacao transacao = new Transacao();
        transacao.setValor(0.01);
        transacao.setDataTransacao(new Date());
        transacao.setDescricao("Transacao de teste");
        transacao.setUsuario(usuarioResponse);
        transacao.setCategoria(categoriaResponse);
        transacao.setConta(contaResponse);

        MockHttpServletResponse response = mockMvc.perform(post("/transacoes")
                        .header("Authorization", "Basic c29waGlhOnNvcGhpYTEyMzQ1Njc4")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Transacao transacaoResponse = objectMapper.readValue(response.getContentAsString(), Transacao.class);

        assertThat(transacaoResponse.getId()).isNotBlank();
        assertThat(transacaoResponse.getValor()).isEqualTo(transacao.getValor());
        assertThat(transacaoResponse.getDataTransacao()).isEqualTo(transacao.getDataTransacao());
        assertThat(transacaoResponse.getDescricao()).isEqualTo(transacao.getDescricao());
        assertThat(transacaoResponse.getDataCadastro()).isNotNull();

        assertThat(transacaoResponse.getUsuario().getId()).isEqualTo(transacao.getUsuario().getId());
        assertThat(transacaoResponse.getUsuario().getNome()).isEqualTo(transacao.getUsuario().getNome());
        assertThat(transacaoResponse.getUsuario().getUsuario()).isEqualTo(transacao.getUsuario().getUsuario());

        assertThat(transacaoResponse.getCategoria().getId()).isEqualTo(transacao.getCategoria().getId());
        assertThat(transacaoResponse.getCategoria().getNome()).isEqualTo(transacao.getCategoria().getNome());
        assertThat(transacaoResponse.getCategoria().getTipo()).isEqualTo(transacao.getCategoria().getTipo());

        assertThat(transacaoResponse.getConta().getId()).isEqualTo(transacao.getConta().getId());
        assertThat(transacaoResponse.getConta().getNome()).isEqualTo(transacao.getConta().getNome());

        Conta contaAtualiza = contaService.buscar(conta.getId());

        assertThat(contaAtualiza.getSaldo()).isEqualTo(conta.getSaldo() + transacao.getValor());
    }

    @Test
    public void retornarSucesso_QuandoAtualizar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Daniel");
        usuario.setUsuario("daniel");
        usuario.setSenha("daniel12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Categoria categoria = new Categoria();
        categoria.setNome("Categoria teste 2");
        categoria.setTipo(TipoCategoriaEnum.RECEITA);
        categoria.setUsuario(usuarioResponse);

        Categoria categoriaResponse = categoriaService.salvar(categoria);

        Conta conta = new Conta();
        conta.setNome("Conta teste 2");
        conta.setSaldo(100.00);
        conta.setUsuario(usuarioResponse);

        Conta contaResponse = contaService.salvar(conta);

        Transacao transacao = new Transacao();
        transacao.setValor(0.01);
        transacao.setDataTransacao(new Date());
        transacao.setDescricao("Transacao de teste");
        transacao.setUsuario(usuarioResponse);
        transacao.setCategoria(categoriaResponse);
        transacao.setConta(contaResponse);

        Transacao transacaoResponse = transacaoService.salvar(transacao);

        usuario = new Usuario();
        usuario.setNome("Lorena");
        usuario.setUsuario("lorena");
        usuario.setSenha("lorena12345678");

        usuarioResponse = usuarioService.salvar(usuario);

        categoria = new Categoria();
        categoria.setNome("Categoria teste 3");
        categoria.setTipo(TipoCategoriaEnum.DESPESA);
        categoria.setUsuario(usuarioResponse);

        categoriaResponse = categoriaService.salvar(categoria);

        Conta contaAtualizar = new Conta();
        contaAtualizar.setNome("Conta teste 3");
        contaAtualizar.setSaldo(100.00);
        contaAtualizar.setUsuario(usuarioResponse);

        contaResponse = contaService.salvar(contaAtualizar);

        transacao.setId(transacaoResponse.getId());
        transacao.setValor(0.02);
        transacao.setDataTransacao(new Date());
        transacao.setDescricao("Transacao de teste atualizar");
        transacao.setDataCadastro(transacaoResponse.getDataCadastro());
        transacao.setUsuario(usuarioResponse);
        transacao.setCategoria(categoriaResponse);
        transacao.setConta(contaResponse);

        MockHttpServletResponse response = mockMvc.perform(put("/transacoes/{transacoeId}", transacaoResponse.getId())
                        .header("Authorization", "Basic bG9yZW5hOmxvcmVuYTEyMzQ1Njc4")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        transacaoResponse = objectMapper.readValue(response.getContentAsString(), Transacao.class);

        assertThat(transacaoResponse.getId()).isEqualTo(transacao.getId());
        assertThat(transacaoResponse.getValor()).isEqualTo(transacao.getValor());
        assertThat(transacaoResponse.getDataTransacao()).isEqualTo(transacao.getDataTransacao());
        assertThat(transacaoResponse.getDescricao()).isEqualTo(transacao.getDescricao());
        assertThat(transacaoResponse.getDataCadastro()).isEqualTo(transacao.getDataCadastro());

        assertThat(transacaoResponse.getUsuario().getId()).isEqualTo(transacao.getUsuario().getId());
        assertThat(transacaoResponse.getUsuario().getNome()).isEqualTo(transacao.getUsuario().getNome());
        assertThat(transacaoResponse.getUsuario().getUsuario()).isEqualTo(transacao.getUsuario().getUsuario());

        assertThat(transacaoResponse.getCategoria().getId()).isEqualTo(transacao.getCategoria().getId());
        assertThat(transacaoResponse.getCategoria().getNome()).isEqualTo(transacao.getCategoria().getNome());
        assertThat(transacaoResponse.getCategoria().getTipo()).isEqualTo(transacao.getCategoria().getTipo());

        assertThat(transacaoResponse.getConta().getId()).isEqualTo(transacao.getConta().getId());
        assertThat(transacaoResponse.getConta().getNome()).isEqualTo(transacao.getConta().getNome());

        Conta conta1 = contaService.buscar(conta.getId());

        assertThat(conta1.getSaldo()).isEqualTo(conta.getSaldo());

        Conta conta2 = contaService.buscar(contaResponse.getId());

        assertThat(conta2.getSaldo()).isEqualTo(contaResponse.getSaldo() - transacao.getValor());
    }

    @Test
    public void retornarSucesso_QuandoBuscarTodasTransacoes() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Claudia");
        usuario.setUsuario("claudia");
        usuario.setSenha("clau12345678");

        usuarioService.salvar(usuario);

        mockMvc.perform(get("/transacoes?dataInicial="
                        + DataHora.formatarData(new Date(), "yyyy-MM-dd")
                        + "&dataFinal="
                        + DataHora.formatarData(new Date(), "yyyy-MM-dd"))
                        .header("Authorization", "Basic Y2xhdWRpYTpjbGF1MTIzNDU2Nzg="))
                .andExpect(status().isOk());
    }

    @Test
    public void retornarSucesso_QuandoBuscarTransacoesEspecifica() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Regina");
        usuario.setUsuario("regina");
        usuario.setSenha("regina12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Categoria categoria = new Categoria();
        categoria.setNome("Categoria teste 4");
        categoria.setTipo(TipoCategoriaEnum.RECEITA);
        categoria.setUsuario(usuarioResponse);

        Categoria categoriaResponse = categoriaService.salvar(categoria);

        Conta conta = new Conta();
        conta.setNome("Conta teste 4");
        conta.setSaldo(100.00);
        conta.setUsuario(usuarioResponse);

        Conta contaResponse = contaService.salvar(conta);

        Transacao transacao = new Transacao();
        transacao.setValor(0.01);
        transacao.setDataTransacao(new Date());
        transacao.setDescricao("Transacao de teste");
        transacao.setUsuario(usuarioResponse);
        transacao.setCategoria(categoriaResponse);
        transacao.setConta(contaResponse);

        Transacao transacaoResponse = transacaoService.salvar(transacao);

        transacao.setId(transacaoResponse.getId());
        transacao.setDataCadastro(transacaoResponse.getDataCadastro());

        MockHttpServletResponse response = mockMvc.perform(get("/transacoes/{transacoeId}", transacaoResponse.getId())
                        .header("Authorization", "Basic cmVnaW5hOnJlZ2luYTEyMzQ1Njc4"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        transacaoResponse = objectMapper.readValue(response.getContentAsString(), Transacao.class);

        assertThat(transacaoResponse.getId()).isEqualTo(transacao.getId());
        assertThat(transacaoResponse.getValor()).isEqualTo(transacao.getValor());
        assertThat(transacaoResponse.getDataTransacao()).isEqualTo(transacao.getDataTransacao());
        assertThat(transacaoResponse.getDescricao()).isEqualTo(transacao.getDescricao());
        assertThat(transacaoResponse.getDataCadastro()).isEqualTo(transacao.getDataCadastro());

        assertThat(transacaoResponse.getUsuario().getId()).isEqualTo(transacao.getUsuario().getId());
        assertThat(transacaoResponse.getUsuario().getNome()).isEqualTo(transacao.getUsuario().getNome());
        assertThat(transacaoResponse.getUsuario().getUsuario()).isEqualTo(transacao.getUsuario().getUsuario());

        assertThat(transacaoResponse.getCategoria().getId()).isEqualTo(transacao.getCategoria().getId());
        assertThat(transacaoResponse.getCategoria().getNome()).isEqualTo(transacao.getCategoria().getNome());
        assertThat(transacaoResponse.getCategoria().getTipo()).isEqualTo(transacao.getCategoria().getTipo());

        assertThat(transacaoResponse.getConta().getId()).isEqualTo(transacao.getConta().getId());
        assertThat(transacaoResponse.getConta().getNome()).isEqualTo(transacao.getConta().getNome());

        Conta contaAtualiza = contaService.buscar(conta.getId());

        assertThat(contaAtualiza.getSaldo()).isEqualTo(conta.getSaldo() + transacao.getValor());
    }

    @Test
    public void retornarSucesso_QuandoDeletar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Renato");
        usuario.setUsuario("renato");
        usuario.setSenha("renato12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Categoria categoria = new Categoria();
        categoria.setNome("Categoria teste 5");
        categoria.setTipo(TipoCategoriaEnum.RECEITA);
        categoria.setUsuario(usuarioResponse);

        Categoria categoriaResponse = categoriaService.salvar(categoria);

        Conta conta = new Conta();
        conta.setNome("Conta teste 5");
        conta.setSaldo(100.00);
        conta.setUsuario(usuarioResponse);

        Conta contaResponse = contaService.salvar(conta);

        Transacao transacao = new Transacao();
        transacao.setValor(0.01);
        transacao.setDataTransacao(new Date());
        transacao.setDescricao("Transacao de teste");
        transacao.setUsuario(usuarioResponse);
        transacao.setCategoria(categoriaResponse);
        transacao.setConta(contaResponse);

        Transacao transacaoResponse = transacaoService.salvar(transacao);

        mockMvc.perform(delete("/transacoes/{transacoeId}", transacaoResponse.getId())
                        .header("Authorization", "Basic cmVuYXRvOnJlbmF0bzEyMzQ1Njc4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/transacoes/{transacoeId}", transacaoResponse.getId())
                        .header("Authorization", "Basic cmVuYXRvOnJlbmF0bzEyMzQ1Njc4"))
                .andExpect(status().isBadRequest());

        Conta contaResponseAposDeletar = contaService.buscar(contaResponse.getId());

        assertThat(contaResponseAposDeletar.getSaldo()).isEqualTo(contaResponse.getSaldo());
    }
}
