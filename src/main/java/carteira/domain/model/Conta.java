package carteira.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Setter
@Getter
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Conta {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Indexed(unique = true)
    private String nome;

    private double saldo;

    @DBRef
    private Usuario usuario;
}
