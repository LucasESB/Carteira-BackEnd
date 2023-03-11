package carteira.controller;

import carteira.domain.model.Conta;
import carteira.domain.model.Usuario;
import carteira.domain.service.ContaService;
import carteira.domain.service.UsuarioService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ContaService contaService;

    @Test
    public void retornarSucesso_QuandoInserir() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Jose Lucas");
        usuario.setUsuario("joselucas");
        usuario.setSenha("jose12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Conta conta = new Conta();
        conta.setNome("Nubank");
        conta.setSaldo(0.00);
        conta.setUsuario(usuarioResponse);

        MockHttpServletResponse response = mockMvc.perform(post("/contas")
                        .header("Authorization", "Basic am9zZWx1Y2FzOmpvc2UxMjM0NTY3OA==")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conta)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Conta contaResponse = objectMapper.readValue(response.getContentAsString(), Conta.class);

        assertThat(contaResponse.getId()).isNotBlank();
        assertThat(contaResponse.getNome()).isEqualTo(conta.getNome());
        assertThat(contaResponse.getSaldo()).isEqualTo(conta.getSaldo());
        assertThat(contaResponse.getUsuario().getId()).isEqualTo(conta.getUsuario().getId());
        assertThat(contaResponse.getUsuario().getNome()).isEqualTo(conta.getUsuario().getNome());
        assertThat(contaResponse.getUsuario().getUsuario()).isEqualTo(conta.getUsuario().getUsuario());
    }

    @Test
    public void retornarSucesso_QuandoAtualizar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Jose Eduardo");
        usuario.setUsuario("joseeduardo");
        usuario.setSenha("jose12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Conta conta = new Conta();
        conta.setNome("Caixa");
        conta.setSaldo(0.00);
        conta.setUsuario(usuarioResponse);

        Conta contaResponse = contaService.salvar(conta);

        usuario = new Usuario();
        usuario.setNome("Jose Eduardo 2");
        usuario.setUsuario("joseeduardo2");
        usuario.setSenha("jose12345678");

        usuarioResponse = usuarioService.salvar(usuario);

        conta.setId(contaResponse.getId());
        conta.setNome("Pic Pay");
        conta.setSaldo(10.00);
        conta.setUsuario(usuarioResponse);

        MockHttpServletResponse response = mockMvc.perform(put("/contas/{contaId}", contaResponse.getId())
                        .header("Authorization", "Basic am9zZWVkdWFyZG8yOmpvc2UxMjM0NTY3OA==")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conta)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        contaResponse = objectMapper.readValue(response.getContentAsString(), Conta.class);

        assertThat(contaResponse.getId()).isEqualTo(conta.getId());
        assertThat(contaResponse.getNome()).isEqualTo(conta.getNome());
        assertThat(contaResponse.getSaldo()).isEqualTo(conta.getSaldo());
        assertThat(contaResponse.getUsuario().getId()).isEqualTo(conta.getUsuario().getId());
        assertThat(contaResponse.getUsuario().getNome()).isEqualTo(conta.getUsuario().getNome());
        assertThat(contaResponse.getUsuario().getUsuario()).isEqualTo(conta.getUsuario().getUsuario());
    }

    @Test
    public void retornarSucesso_QuandoBuscarTodasContas() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Luciana");
        usuario.setUsuario("luciana");
        usuario.setSenha("luci12345678");

        usuarioService.salvar(usuario);

        mockMvc.perform(get("/contas")
                        .header("Authorization", "Basic bHVjaWFuYTpsdWNpMTIzNDU2Nzg="))
                .andExpect(status().isOk());
    }

    @Test
    public void retornarSucesso_QuandoBuscarContaEspecifica() throws Exception{
        Usuario usuario = new Usuario();
        usuario.setNome("Jose Antonio");
        usuario.setUsuario("joseantonio");
        usuario.setSenha("jose12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Conta conta = new Conta();
        conta.setNome("Bradesco");
        conta.setSaldo(100.00);
        conta.setUsuario(usuarioResponse);

        Conta contaResponse = contaService.salvar(conta);

        conta.setId(contaResponse.getId());

        MockHttpServletResponse response = mockMvc.perform(get("/contas/{contaId}", contaResponse.getId())
                        .header("Authorization", "Basic am9zZWFudG9uaW86am9zZTEyMzQ1Njc4"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        contaResponse = objectMapper.readValue(response.getContentAsString(), Conta.class);

        assertThat(contaResponse.getId()).isEqualTo(conta.getId());
        assertThat(contaResponse.getNome()).isEqualTo(conta.getNome());
        assertThat(contaResponse.getSaldo()).isEqualTo(conta.getSaldo());
        assertThat(contaResponse.getUsuario().getId()).isEqualTo(conta.getUsuario().getId());
        assertThat(contaResponse.getUsuario().getNome()).isEqualTo(conta.getUsuario().getNome());
        assertThat(contaResponse.getUsuario().getUsuario()).isEqualTo(conta.getUsuario().getUsuario());
    }

    @Test
    public void retornarSucesso_QuandoDeletar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Joel Junio");
        usuario.setUsuario("joeljunio");
        usuario.setSenha("joel12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Conta conta = new Conta();
        conta.setNome("Bari");
        conta.setSaldo(1000.00);
        conta.setUsuario(usuarioResponse);

        Conta contaResponse = contaService.salvar(conta);

        mockMvc.perform(delete("/contas/{contaId}", contaResponse.getId())
                        .header("Authorization", "Basic am9lbGp1bmlvOmpvZWwxMjM0NTY3OA=="))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/contas/{contaId}", contaResponse.getId())
                        .header("Authorization", "Basic am9lbGp1bmlvOmpvZWwxMjM0NTY3OA=="))
                .andExpect(status().isBadRequest());
    }
}
