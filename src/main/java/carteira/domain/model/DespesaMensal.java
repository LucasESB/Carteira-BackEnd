package carteira.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DespesaMensal {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @DBRef
    private Categoria categoria;

    private String descricao;

    private TipoValor tipoValor;

    private double valorPrevisto;

    private int diaPrevisto;

    @DBRef
    private Usuario usuario;

    private boolean excluido;
}
