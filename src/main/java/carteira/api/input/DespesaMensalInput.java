package carteira.api.input;

import carteira.domain.model.TipoValor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Getter
@Setter
public class DespesaMensalInput {

    @Valid
    @NotNull
    private CategoriaReduzidoInput categoria;

    @NotBlank
    @Size(max = 60)
    private String descricao;

    @NotNull
    private TipoValor tipoValor;

    @DecimalMin("0.00")
    private double valorPrevisto;

    @Min(1)
    @Max(31)
    private int diaPrevisto;

    @Valid
    @NotNull
    private UsuarioReduzidoInput usuario;
}
