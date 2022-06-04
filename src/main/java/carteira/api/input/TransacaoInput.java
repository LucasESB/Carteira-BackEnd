package carteira.api.input;

import carteira.domain.model.TipoTransacao;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class TransacaoInput {

    @NotNull
    private TipoTransacao tipo;

    @Valid
    private CategoriaReduzidoInput categoria;

    @Valid
    @NotNull
    private ContaReduzidoInput conta;

    @DecimalMin("0.00")
    private double valor;

    @NotNull
    private Date dataTransacao;

    @NotBlank
    @Size(max = 60)
    private String descricao;

    @Valid
    @NotNull
    private UsuarioReduzidoInput usuario;
}
