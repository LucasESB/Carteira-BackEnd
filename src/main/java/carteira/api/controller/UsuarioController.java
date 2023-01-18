package carteira.api.controller;

import carteira.api.input.UsuarioInput;
import carteira.api.model.UsuarioModel;
import carteira.assembler.UsuarioAssembler;
import carteira.domain.exception.NegocioException;
import carteira.domain.model.Usuario;
import carteira.domain.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private UsuarioService usuarioService;
    private UsuarioAssembler usuarioAssembler;

    @GetMapping
    public List<UsuarioModel> buscar() {
        return usuarioAssembler.toCollectionModel(usuarioService.buscar());
    }

    @GetMapping("/{usuarioId}")
    public UsuarioModel buscar(@PathVariable String usuarioId) {
        return usuarioAssembler.toModel(usuarioService.buscar(usuarioId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel inseir(@Valid @RequestBody UsuarioInput usuarioInput) {
        Usuario usuario = usuarioAssembler.toEntity(usuarioInput);
        return usuarioAssembler.toModel(usuarioService.salvar(usuario));
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<UsuarioModel> atualizar(@Valid @RequestBody UsuarioInput usuarioInput, @PathVariable String usuarioId) {
        usuarioService.verificarSeUsuarioExiste(usuarioId);

        Usuario usuario = usuarioAssembler.toEntity(usuarioInput);
        usuario.setId(usuarioId);
        usuario = usuarioService.salvar(usuario);

        return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> deletar(@PathVariable String usuarioId) {
        usuarioService.excluir(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
