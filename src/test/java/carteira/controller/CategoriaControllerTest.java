package carteira.controller;

import carteira.domain.model.Categoria;
import carteira.domain.enums.TipoCategoriaEnum;
import carteira.domain.model.Usuario;
import carteira.domain.service.CategoriaService;
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
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaService categoriaService;

    @Test
    public void retornarSucesso_QuandoInserir() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Vitor");
        usuario.setUsuario("vitor");
        usuario.setSenha("vitor12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Categoria categoria = new Categoria();
        categoria.setNome("Salario");
        categoria.setTipo(TipoCategoriaEnum.RECEITA);
        categoria.setUsuario(usuarioResponse);

        MockHttpServletResponse response = mockMvc.perform(post("/categorias")
                        .header("Authorization", "Basic dml0b3I6dml0b3IxMjM0NTY3OA==")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Categoria categoriaResponse = objectMapper.readValue(response.getContentAsString(), Categoria.class);

        assertThat(categoriaResponse.getId()).isNotBlank();
        assertThat(categoriaResponse.getNome()).isEqualTo(categoria.getNome());
        assertThat(categoriaResponse.getTipo()).isEqualTo(categoria.getTipo());
        assertThat(categoriaResponse.getUsuario().getId()).isEqualTo(categoria.getUsuario().getId());
        assertThat(categoriaResponse.getUsuario().getNome()).isEqualTo(categoria.getUsuario().getNome());
        assertThat(categoriaResponse.getUsuario().getUsuario()).isEqualTo(categoria.getUsuario().getUsuario());
    }

    @Test
    public void retornarSucesso_QuandoAtualizar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Fernando");
        usuario.setUsuario("fernando");
        usuario.setSenha("fern12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Categoria categoria = new Categoria();
        categoria.setNome("Internet");
        categoria.setTipo(TipoCategoriaEnum.RECEITA);
        categoria.setUsuario(usuarioResponse);

        Categoria categoriaResponse = categoriaService.salvar(categoria);

        usuario = new Usuario();
        usuario.setNome("Gabriel");
        usuario.setUsuario("gabriel");
        usuario.setSenha("gabriel12345678");

        usuarioResponse = usuarioService.salvar(usuario);

        categoria.setId(categoriaResponse.getId());
        categoria.setNome("Lanche");
        categoria.setTipo(TipoCategoriaEnum.DESPESA);
        categoria.setUsuario(usuarioResponse);

        MockHttpServletResponse response = mockMvc.perform(put("/categorias/{categoriaId}", categoriaResponse.getId())
                        .header("Authorization", "Basic Z2FicmllbDpnYWJyaWVsMTIzNDU2Nzg=")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        categoriaResponse = objectMapper.readValue(response.getContentAsString(), Categoria.class);

        assertThat(categoriaResponse.getId()).isEqualTo(categoria.getId());
        assertThat(categoriaResponse.getNome()).isEqualTo(categoria.getNome());
        assertThat(categoriaResponse.getTipo()).isEqualTo(categoria.getTipo());
        assertThat(categoriaResponse.getUsuario().getId()).isEqualTo(categoria.getUsuario().getId());
        assertThat(categoriaResponse.getUsuario().getNome()).isEqualTo(categoria.getUsuario().getNome());
        assertThat(categoriaResponse.getUsuario().getUsuario()).isEqualTo(categoria.getUsuario().getUsuario());
    }

    @Test
    public void retornarSucesso_QuandoBuscarTodasCategorias() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Rosa");
        usuario.setUsuario("rosa");
        usuario.setSenha("rosa12345678");

        usuarioService.salvar(usuario);

        mockMvc.perform(get("/categorias")
                        .header("Authorization", "Basic cm9zYTpyb3NhMTIzNDU2Nzg="))
                .andExpect(status().isOk());
    }

    @Test
    public void retornarSucesso_QuandoBuscarCategoriaEspecifica() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Jose Herique");
        usuario.setUsuario("josehenrique");
        usuario.setSenha("jose12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Categoria categoria = new Categoria();
        categoria.setNome("Investimentos");
        categoria.setTipo(TipoCategoriaEnum.RECEITA);
        categoria.setUsuario(usuarioResponse);

        Categoria categoriaResponse = categoriaService.salvar(categoria);

        categoria.setId(categoriaResponse.getId());

        MockHttpServletResponse response = mockMvc.perform(get("/categorias/{categoriaId}", categoriaResponse.getId())
                        .header("Authorization", "Basic am9zZWhlbnJpcXVlOmpvc2UxMjM0NTY3OA=="))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        categoriaResponse = objectMapper.readValue(response.getContentAsString(), Categoria.class);

        assertThat(categoriaResponse.getId()).isEqualTo(categoria.getId());
        assertThat(categoriaResponse.getNome()).isEqualTo(categoria.getNome());
        assertThat(categoriaResponse.getTipo()).isEqualTo(categoria.getTipo());
        assertThat(categoriaResponse.getUsuario().getId()).isEqualTo(categoria.getUsuario().getId());
        assertThat(categoriaResponse.getUsuario().getNome()).isEqualTo(categoria.getUsuario().getNome());
        assertThat(categoriaResponse.getUsuario().getUsuario()).isEqualTo(categoria.getUsuario().getUsuario());
    }

    @Test
    public void retornarSucesso_QuandoDeletar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Edimar");
        usuario.setUsuario("edimar");
        usuario.setSenha("edimar12345678");

        Usuario usuarioResponse = usuarioService.salvar(usuario);

        Categoria categoria = new Categoria();
        categoria.setNome("Faculdade");
        categoria.setTipo(TipoCategoriaEnum.DESPESA);
        categoria.setUsuario(usuarioResponse);

        Categoria categoriaResponse = categoriaService.salvar(categoria);

        mockMvc.perform(delete("/categorias/{categoriaId}", categoriaResponse.getId())
                        .header("Authorization", "Basic ZWRpbWFyOmVkaW1hcjEyMzQ1Njc4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categorias/{categoriaId}", categoriaResponse.getId())
                        .header("Authorization", "Basic ZWRpbWFyOmVkaW1hcjEyMzQ1Njc4"))
                .andExpect(status().isBadRequest());
    }
}
