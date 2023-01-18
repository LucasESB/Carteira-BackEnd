package carteira.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaModel {

    private String id;
    private String nome;
    private double saldo;
    private UsuarioReduzidoModel usuario;
}
