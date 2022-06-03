package carteira.api.model;

import carteira.domain.model.TipoCategoria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaModel {

    private String id;
    private String nome;
    private TipoCategoria tipo;
    private UsuarioReduzidoModel usuario;
    private boolean excluido;
}
