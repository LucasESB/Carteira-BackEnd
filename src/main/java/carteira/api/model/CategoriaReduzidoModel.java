package carteira.api.model;

import carteira.domain.enums.TipoCategoriaEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaReduzidoModel {

    private String id;
    private String nome;
    private TipoCategoriaEnum tipo;
}
