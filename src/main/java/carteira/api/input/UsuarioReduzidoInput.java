package carteira.api.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioReduzidoInput {

    @NotBlank
    private String id;
}
