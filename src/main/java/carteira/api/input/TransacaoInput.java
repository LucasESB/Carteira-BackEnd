package carteira.api.input;

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
    @Valid
    @NotNull
    private CategoriaReduzidoInput categoria;

    @Valid
    @NotNull
    private ContaReduzidoInput conta;

    @DecimalMin("0.01")
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
