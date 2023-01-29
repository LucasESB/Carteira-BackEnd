package carteira.api.controller;

import carteira.api.input.CategoriaInput;
import carteira.api.model.CategoriaModel;
import carteira.assembler.CategoriaAssembler;
import carteira.domain.model.Categoria;
import carteira.domain.service.CategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@AllArgsConstructor
public class CategoriaController {

    private CategoriaService categoriaService;
    private CategoriaAssembler categoriaAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaModel inserir(@Valid @RequestBody CategoriaInput input) {
        Categoria categoria = categoriaAssembler.toEntity(input);
        return categoriaAssembler.toModel(categoriaService.salvar(categoria));
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<CategoriaModel> atualizar(@Valid @RequestBody CategoriaInput input, @PathVariable String categoriaId) {
        categoriaService.verificarSeCategoriaExiste(categoriaId);

        Categoria categoria = categoriaAssembler.toEntity(input);
        categoria.setId(categoriaId);
        categoria = categoriaService.salvar(categoria);

        return ResponseEntity.ok(categoriaAssembler.toModel(categoria));
    }

    @GetMapping
    public List<CategoriaModel> buscar() {
        return categoriaAssembler.toCollectionModel(categoriaService.buscar());
    }

    @GetMapping("/{categoriaId}")
    public CategoriaModel buscar(@PathVariable String categoriaId) {
        return categoriaAssembler.toModel(categoriaService.buscar(categoriaId));
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> deletar(@PathVariable String categoriaId) {
        categoriaService.excluir(categoriaId);
        return ResponseEntity.noContent().build();
    }
}
