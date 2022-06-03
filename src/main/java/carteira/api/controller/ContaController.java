package carteira.api.controller;

import carteira.api.input.ContaInput;
import carteira.api.model.ContaModel;
import carteira.assembler.ContaAssembler;
import carteira.domain.model.Conta;
import carteira.domain.service.ContaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contas")
@AllArgsConstructor
public class ContaController {

    private ContaService contaService;
    private ContaAssembler contaAssembler;

    @GetMapping
    public List<ContaModel> buscar() {
        return contaAssembler.toCollectionModel(contaService.buscar());
    }

    @GetMapping("/{contaId}")
    public ContaModel buscar(@PathVariable String contaId) {
        return contaAssembler.toModel(contaService.buscar(contaId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaModel inserir(@Valid @RequestBody ContaInput input) {
        Conta conta = contaAssembler.toEntity(input);
        return contaAssembler.toModel(contaService.salvar(conta));
    }

    @PutMapping("/{contaId}")
    public ResponseEntity<ContaModel> atualizar(@Valid @RequestBody ContaInput input, @PathVariable String contaId) {
        contaService.verificarSeContaExiste(contaId);

        Conta conta = contaAssembler.toEntity(input);
        conta.setId(contaId);
        conta = contaService.salvar(conta);

        return ResponseEntity.ok(contaAssembler.toModel(conta));
    }

    @DeleteMapping("/{contaId}")
    public ResponseEntity<Void> deletar(@PathVariable String contaId) {
        contaService.excluir(contaService.buscar(contaId));
        return ResponseEntity.noContent().build();
    }
}
