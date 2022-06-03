package carteira.api.model;

import carteira.domain.model.TipoCategoria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaReduzidoModel {

    private String id;
    private String nome;
    private TipoCategoria tipo;
}
