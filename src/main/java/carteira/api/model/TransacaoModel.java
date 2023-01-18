package carteira.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TransacaoModel {
    private String id;
    private CategoriaReduzidoModel categoria;
    private ContaReduzidoModel conta;
    private double valor;
    private Date dataTransacao;
    private Date dataCadastro;
    private String descricao;
    private UsuarioReduzidoModel usuario;
}
