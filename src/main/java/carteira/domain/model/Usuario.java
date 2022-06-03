package carteira.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @NotBlank
    @EqualsAndHashCode.Include
    private String id;

    private String nome;
    private String usuario;
    private String senha;
    private boolean adm;
    private boolean excluido;
}
