package carteira.api.input;

import carteira.domain.model.TipoCategoria;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategoriaInput {

    @NotBlank
    @Size(min = 5, max = 30)
    private String nome;

    @NotNull
    private TipoCategoria tipo;

    @Valid
    @NotNull
    private UsuarioReduzidoInput usuario;
}
