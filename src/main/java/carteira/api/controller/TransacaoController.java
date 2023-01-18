package carteira.api.controller;

import carteira.api.input.TransacaoInput;
import carteira.api.model.TransacaoModel;
import carteira.assembler.TransacaoAssembler;
import carteira.domain.model.Transacao;
import carteira.domain.service.TransacaoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/transacoes")
@AllArgsConstructor
public class TransacaoController {

    private TransacaoService transacaoService;
    private TransacaoAssembler transacaoAssembler;

    @GetMapping
    public List<TransacaoModel> buscar(@RequestParam(value = "dataInicial") String dataInicial,
                                       @RequestParam(value = "dataFinal") String dataFinal) {
        return transacaoAssembler.toCollectionModel(transacaoService.buscar(dataInicial, dataFinal));
    }

    @GetMapping("/{transacaoId}")
    public TransacaoModel buscar(@PathVariable String transacaoId) {
        return transacaoAssembler.toModel(transacaoService.buscar(transacaoId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoModel inserir(@Valid @RequestBody TransacaoInput input) {
        Transacao transacao = transacaoAssembler.toEntity(input);

        transacao.setDataCadastro(new Date());

        return transacaoAssembler.toModel(transacaoService.salvar(transacao));
    }

    @PutMapping("/{transacaoId}")
    public ResponseEntity<TransacaoModel> atualizar(@Valid @RequestBody TransacaoInput input, @PathVariable String transacaoId) {
        transacaoService.verificarSeTransacaoExiste(transacaoId);

        Transacao transacao = transacaoAssembler.toEntity(input);
        transacao.setId(transacaoId);
        transacao = transacaoService.salvar(transacao);

        return ResponseEntity.ok(transacaoAssembler.toModel(transacao));
    }

    @DeleteMapping("/{transacaoId}")
    public ResponseEntity<Void> deletar(@PathVariable String transacaoId) {
        transacaoService.excluir(transacaoId);
        return ResponseEntity.noContent().build();
    }
}
