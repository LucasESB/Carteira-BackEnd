package carteira.api.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UsuarioInput {

    @NotBlank
    @Size(min = 5, max = 60)
    private String nome;

    @NotBlank
    @Size(min = 5, max = 16)
    private String usuario;

    @NotBlank
    @Size(min = 5, max = 16)
    private String senha;
}
