package carteira.api.model;

import carteira.domain.model.TipoValor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReceitaMensalModel {

    private String id;
    private CategoriaReduzidoModel categoria;
    private String descricao;
    private TipoValor tipoValor;
    private double valorPrevisto;
    private int diaPrevisto;
    private UsuarioReduzidoModel usuario;
    private boolean excluido;
}
