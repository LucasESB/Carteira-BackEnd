package carteira.api.controller;

import carteira.api.input.ReceitaMensalInput;
import carteira.api.model.ReceitaMensalModel;
import carteira.assembler.ReceitaMensalAssembler;
import carteira.domain.model.ReceitaMensal;
import carteira.domain.service.ReceitaMensalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/receitasmensais")
@AllArgsConstructor
public class ReceitaMensalController {

    private ReceitaMensalService receitaMensalService;
    private ReceitaMensalAssembler receitaMensalAssembler;

    @GetMapping
    public List<ReceitaMensalModel> buscar() {
        return receitaMensalAssembler.toCollectionModel(receitaMensalService.buscar());
    }

    @GetMapping("/{receitaMensalId}")
    public ReceitaMensalModel buscar(@PathVariable String receitaMensalId) {
        return receitaMensalAssembler.toModel(receitaMensalService.buscar(receitaMensalId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReceitaMensalModel inserir(@Valid @RequestBody ReceitaMensalInput input) {
        ReceitaMensal receitaMensal = receitaMensalAssembler.toEntity(input);
        return receitaMensalAssembler.toModel(receitaMensalService.salvar(receitaMensal));
    }

    @PutMapping("/{receitaMensalId}")
    public ResponseEntity<ReceitaMensalModel> atualizar(@Valid @RequestBody ReceitaMensalInput input, @PathVariable String receitaMensalId) {
        receitaMensalService.verificarSeReceitaMensalExiste(receitaMensalId);

        ReceitaMensal receitaMensal = receitaMensalAssembler.toEntity(input);
        receitaMensal.setId(receitaMensalId);
        receitaMensal = receitaMensalService.salvar(receitaMensal);

        return ResponseEntity.ok(receitaMensalAssembler.toModel(receitaMensal));
    }

    @DeleteMapping("/{receitaMensalId}")
    public ResponseEntity<Void> deletar(@PathVariable String receitaMensalId) {
        receitaMensalService.excluir(receitaMensalService.buscar(receitaMensalId));
        return ResponseEntity.noContent().build();
    }
}
