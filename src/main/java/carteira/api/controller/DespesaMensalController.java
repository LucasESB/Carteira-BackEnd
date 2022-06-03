package carteira.api.controller;

import carteira.api.input.DespesaMensalInput;
import carteira.api.model.DespesaMensalModel;
import carteira.assembler.DespesaMensalAssembler;
import carteira.domain.model.DespesaMensal;
import carteira.domain.service.DespesaMensalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/despesasmensais")
@AllArgsConstructor
public class DespesaMensalController {

    private DespesaMensalService despesaMensalService;
    private DespesaMensalAssembler despesaMensalAssembler;

    @GetMapping
    public List<DespesaMensalModel> buscar() {
        return despesaMensalAssembler.toCollectionModel(despesaMensalService.buscar());
    }

    @GetMapping("/{despesaMensalId}")
    public DespesaMensalModel buscar(@PathVariable String despesaMensalId) {
        return despesaMensalAssembler.toModel(despesaMensalService.buscar(despesaMensalId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DespesaMensalModel inserir(@Valid @RequestBody DespesaMensalInput input) {
        DespesaMensal despesaMensal = despesaMensalAssembler.toEntity(input);
        return despesaMensalAssembler.toModel(despesaMensalService.salvar(despesaMensal));
    }

    @PutMapping("/{despesaMensalId}")
    public ResponseEntity<DespesaMensalModel> atualizar(@Valid @RequestBody DespesaMensalInput input, @PathVariable String despesaMensalId) {
        despesaMensalService.verificarSeDespesaMensalExiste(despesaMensalId);

        DespesaMensal despesaMensal = despesaMensalAssembler.toEntity(input);
        despesaMensal.setId(despesaMensalId);
        despesaMensal = despesaMensalService.salvar(despesaMensal);

        return ResponseEntity.ok(despesaMensalAssembler.toModel(despesaMensal));
    }

    @DeleteMapping("/{despesaMensalId}")
    public ResponseEntity<Void> deletar(@PathVariable String despesaMensalId) {
        despesaMensalService.excluir(despesaMensalService.buscar(despesaMensalId));
        return ResponseEntity.noContent().build();
    }
}
