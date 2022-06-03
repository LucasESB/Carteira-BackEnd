package carteira.api.model;

import carteira.domain.model.TipoTransacao;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TransacaoModel {
    private String id;
    private TipoTransacao tipo;
    private CategoriaReduzidoModel categoria;
    private ContaReduzidoModel conta;
    private double valor;
    private Date dataTransacao;
    private Date dataCadastro;
    private String descricao;
    private UsuarioReduzidoModel usuario;
    private boolean excluido;
}
