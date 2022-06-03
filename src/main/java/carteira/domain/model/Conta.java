package carteira.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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

    private String nome;
    private double saldo;

    @DBRef
    private Usuario usuario;

    private boolean excluido;
}
