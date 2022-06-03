package carteira.domain.service;

import carteira.domain.exception.NegocioException;
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
        transacao.setCategoria(categoriaService.buscar(transacao.getCategoria().getId()));
        transacao.setConta(contaService.buscar(transacao.getConta().getId()));
        transacao.setUsuario(usuarioService.buscar(transacao.getUsuario().getId()));

        return repository.save(transacao);
    }

    @Transactional
    public void excluir(Transacao transacao) {
        transacao.setExcluido(true);
        repository.save(transacao);
    }
}
