package carteira.controller;

import carteira.domain.enums.RoleEnum;
import carteira.domain.model.Usuario;
import carteira.domain.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void retornarSucesso_QuandoInserir() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Lucas");
        usuario.setUsuario("lucas");
        usuario.setSenha("lucas12345678");

        MockHttpServletResponse response = mockMvc.perform(post("/usuarios")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Usuario usuarioResponse = objectMapper.readValue(response.getContentAsString(), Usuario.class);

        assertThat(usuarioResponse.getId()).isNotBlank();
        assertThat(usuarioResponse.getNome()).isEqualTo(usuario.getNome());
        assertThat(usuarioResponse.getUsuario()).isEqualTo(usuario.getUsuario());
    }

    @Test
    public void retornarSucesso_QuandoAtualizar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Jose Lucas");
        usuario.setUsuario("joselucas");
        usuario.setSenha("jose12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        usuario.setId(usuarioResponse.getId());
        usuario.setNome("Eduardo");
        usuario.setUsuario("eduardo");
        usuario.setSenha("eduardo12345678");

        MockHttpServletResponse response = mockMvc.perform(put("/usuarios/{usuarioId}", usuarioResponse.getId())
                        .header("Authorization", "Basic am9zZWx1Y2FzOmpvc2UxMjM0NTY3OA==")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        usuarioResponse = objectMapper.readValue(response.getContentAsString(), Usuario.class);

        assertThat(usuarioResponse.getId()).isEqualTo(usuario.getId());
        assertThat(usuarioResponse.getNome()).isEqualTo(usuario.getNome());
        assertThat(usuarioResponse.getUsuario()).isEqualTo(usuario.getUsuario());
    }

    @Test
    public void retornarSucesso_QuandoBuscarTodosUsuarios() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("henrrique");
        usuario.setUsuario("henrrique");
        usuario.setSenha("henrrique12345678");

        usuarioService.salvar(usuario);

        mockMvc.perform(get("/usuarios")
                        .header("Authorization", "Basic aGVucnJpcXVlOmhlbnJyaXF1ZTEyMzQ1Njc4"))
                .andExpect(status().isOk());
    }

    @Test
    public void retornarSucesso_QuandoBuscarUsuarioEspecifico() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setUsuario("carlos");
        usuario.setSenha("carlos12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        usuario.setId(usuarioResponse.getId());

        MockHttpServletResponse response = mockMvc.perform(get("/usuarios/{usuarioId}", usuarioResponse.getId())
                        .header("Authorization", "Basic Y2FybG9zOmNhcmxvczEyMzQ1Njc4"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        usuarioResponse = objectMapper.readValue(response.getContentAsString(), Usuario.class);

        assertThat(usuarioResponse.getId()).isEqualTo(usuario.getId());
        assertThat(usuarioResponse.getNome()).isEqualTo(usuario.getNome());
        assertThat(usuarioResponse.getUsuario()).isEqualTo(usuario.getUsuario());
    }

    @Test
    public void retornarSucesso_QuandoDeletar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Joel Cardoso");
        usuario.setUsuario("joelCardoso");
        usuario.setSenha("joel12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        mockMvc.perform(delete("/usuarios/{usuarioId}", usuarioResponse.getId())
                        .header("Authorization", "Basic am9lbENhcmRvc286am9lbDEyMzQ1Njc4"))
                .andExpect(status().isNoContent());

        usuario = new Usuario();
        usuario.setNome("Joel 2");
        usuario.setUsuario("joelCardoso");
        usuario.setSenha("joel12345678");

        usuarioService.salvar(usuario);

        mockMvc.perform(get("/usuarios/{usuarioId}", usuarioResponse.getId())
                .header("Authorization", "Basic am9lbENhcmRvc286am9lbDEyMzQ1Njc4"))
                .andExpect(status().isBadRequest());
    }
}
