package carteira.domain.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.TipoCategoria;
import carteira.domain.model.Transacao;
import carteira.domain.repository.TransacaoRepository;
import carteira.utilitarios.DataHora;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TransacaoService {

    private TransacaoRepository repository;
    private UsuarioService usuarioService;
    private CategoriaService categoriaService;
    private ContaService contaService;

    @Transactional
    public List<Transacao> buscar(String dataTransacaoInicial, String dataTransacaoFinal) {
        try {
            Date data1 = DataHora.stringParseDate(dataTransacaoInicial, "yyyy-MM-dd");
            Date data2 = DataHora.stringParseDate(dataTransacaoFinal, "yyyy-MM-dd");

            return repository.findByDataTransacao(data1, data2);
        } catch (ParseException ex) {
            throw new NegocioException("A data deve ser informada no formato 'yyyy-MM-dd'");
        }
    }

    @Transactional
    public Transacao buscar(String transacaoId) {
        return repository.findById(transacaoId)
                .orElseThrow(() -> new NegocioException("Transação não encontrada"));
    }

    @Transactional
    public void verificarSeTransacaoExiste(String transacaoId) {
        if (!repository.existsById(transacaoId)) {
            throw new NegocioException("Transação não encontrada");
        }
    }

    @Transactional
    public Transacao salvar(Transacao transacao) {
        transacao.setCategoria(categoriaService.buscar(transacao.getCategoria().getId()));
        transacao.setUsuario(usuarioService.buscar(transacao.getUsuario().getId()));
        transacao.setConta(contaService.buscar(transacao.getConta().getId()));

        Transacao transacaoAnterior = null;

        if (transacao.getId() != null && !transacao.getId().isEmpty()) {
            transacaoAnterior = buscar(transacao.getId());
        }

        //TODO: Criar testes atualizando com contas diferentes, de categorias de tipo diferentes
        varificarSaldoParaTransacaoInvalido(transacao, transacaoAnterior);

        if (transacaoAnterior != null) {
            estornarValorTransacao(transacaoAnterior);
            transacao.setDataCadastro(transacaoAnterior.getDataCadastro());
        }

        transacao = repository.save(transacao);

        if (transacao.getCategoria().getTipo().equals(TipoCategoria.RECEITA)) {
            contaService.adicionarValor(transacao.getConta().getId(), transacao.getValor());
        } else {
            contaService.removerValor(transacao.getConta().getId(), transacao.getValor());
        }

        return transacao;
    }

    private void varificarSaldoParaTransacaoInvalido(Transacao transacao, Transacao transacaoAnterior) {
        double saldoConta = 0.00;

        if (!transacao.getCategoria().getTipo().equals(TipoCategoria.DESPESA)) {
            return;
        }

        if (transacaoAnterior != null) {
            if (!transacaoAnterior.getConta().getId().equals(transacao.getConta().getId())) {
                saldoConta = transacao.getConta().getSaldo();
            } else if (transacaoAnterior.getCategoria().getTipo().equals(TipoCategoria.RECEITA)) {
                saldoConta = transacao.getConta().getSaldo() - transacaoAnterior.getValor();
            } else {
                saldoConta = transacao.getConta().getSaldo() + transacaoAnterior.getValor();
            }
        } else {
            saldoConta = transacao.getConta().getSaldo();
        }

        if (transacao.getValor() > saldoConta) {
            throw new NegocioException("O saldo da conta " + transacao.getConta().getNome() + " não é suficiente para realizar a transação");
        }
    }

    private void estornarValorTransacao(Transacao transacao) {
        if (transacao.getCategoria().getTipo().equals(TipoCategoria.RECEITA)) {
            contaService.removerValor(transacao.getConta().getId(), transacao.getValor());
        } else {
            contaService.adicionarValor(transacao.getConta().getId(), transacao.getValor());
        }
    }

    @Transactional
    public void excluir(String transacaoId) {
        repository.deleteById(transacaoId);
    }
}
