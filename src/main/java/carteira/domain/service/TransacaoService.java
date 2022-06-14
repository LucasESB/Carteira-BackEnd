package carteira.domain.service;

import carteira.domain.exception.NegocioException;
import carteira.domain.model.TipoCategoria;
import carteira.domain.model.TipoTransacao;
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
            throw new NegocioException("A data informada deve está no formato yyyy-MM-dd");
        }
    }

    @Transactional
    public Transacao buscar(String transacaoId) {
        return repository.findById(transacaoId)
                .orElseThrow(() -> new NegocioException("Transação não encontrada"));
    }

    @Transactional
    public void verificarSeTransacaoExiste(String transacaoId) {
        buscar(transacaoId);
    }

    @Transactional
    public Transacao salvar(Transacao transacao) {

        if (!transacao.getTipo().equals(TipoTransacao.TRANSFERENCIA) && transacao.getCategoria() == null) {
            throw new NegocioException("Categoria é um campo obrigatório");
        } else if (transacao.getCategoria() != null) {
            transacao.setCategoria(categoriaService.buscar(transacao.getCategoria().getId()));

            if (transacao.getTipo().equals(TipoTransacao.RECEITA) && !transacao.getCategoria().getTipo().equals(TipoCategoria.RECEITA)) {
                throw new NegocioException("O tipo da categoria deve ser do mesmo tipo da transação");
            } else if (transacao.getTipo().equals(TipoTransacao.DESPESA) && !transacao.getCategoria().getTipo().equals(TipoCategoria.DESPESA)) {
                throw new NegocioException("O tipo da categoria deve ser do mesmo tipo da transação");
            }
        }

        transacao.setConta(contaService.buscar(transacao.getConta().getId()));
        transacao.setUsuario(usuarioService.buscar(transacao.getUsuario().getId()));
        Transacao transacaoAnterior = buscar(transacao.getId());

        transacao = repository.save(transacao);

        if (transacao.getId() != null && !transacao.getId().isEmpty()) {
            estornarValorTransacao(transacaoAnterior);
        }

        if (transacao.getTipo().equals(TipoTransacao.RECEITA)) {
            contaService.adicionarValor(transacao.getConta(), transacao.getValor());
        } else if (transacao.getTipo().equals(TipoTransacao.DESPESA)) {
            contaService.removerValor(transacao.getConta(), transacao.getValor());
        }

        return transacao;
    }

    private void estornarValorTransacao(Transacao transacao) {
        if (transacao.getTipo().equals(TipoTransacao.RECEITA)) {
            contaService.removerValor(transacao.getConta(), transacao.getValor());
        } else if (transacao.getTipo().equals(TipoTransacao.DESPESA)) {
            contaService.adicionarValor(transacao.getConta(), transacao.getValor());
        }
    }

    @Transactional
    public void excluir(Transacao transacao) {
        transacao.setExcluido(true);
        repository.save(transacao);
    }
}
