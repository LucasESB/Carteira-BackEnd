package carteira.api.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ContaInput {

    @NotBlank
    @Size(min = 5, max = 20)
    private String nome;

    @NotNull
    private double saldo;

    @Valid
    @NotNull
    private UsuarioReduzidoInput usuario;
}
