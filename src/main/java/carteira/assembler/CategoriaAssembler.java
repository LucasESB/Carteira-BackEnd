package carteira.assembler;

import carteira.api.input.CategoriaInput;
import carteira.api.model.CategoriaModel;
import carteira.domain.model.Categoria;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CategoriaAssembler {

    private ModelMapper modelMapper;

    public CategoriaModel toModel(Categoria categoria) {
        return modelMapper.map(categoria, CategoriaModel.class);
    }

    public List<CategoriaModel> toCollectionModel(List<Categoria> list) {
        return list.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public Categoria toEntity(CategoriaInput input) {
        return modelMapper.map(input, Categoria.class);
    }
}
