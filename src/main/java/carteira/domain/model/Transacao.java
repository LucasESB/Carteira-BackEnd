package carteira.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Getter
@Setter
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transacao {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private TipoTransacao tipo;

    @DBRef
    private Categoria categoria;

    @DBRef
    private Conta conta;
    private double valor;
    private Date dataTransacao;
    private Date dataCadastro;
    private String descricao;

    @DBRef
    private Usuario usuario;
    private boolean excluido;
}
