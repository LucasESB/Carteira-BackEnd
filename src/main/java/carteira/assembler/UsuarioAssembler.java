package carteira.assembler;

import carteira.api.input.UsuarioInput;
import carteira.api.model.UsuarioModel;
import carteira.domain.model.Usuario;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UsuarioAssembler {

    private ModelMapper modelMapper;

    public UsuarioModel toModel(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioModel.class);
    }

    public List<UsuarioModel> toCollectionModel(List<Usuario> listUsuario) {
        return listUsuario.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public Usuario toEntity(UsuarioInput input) {
        return modelMapper.map(input, Usuario.class);
    }
}
