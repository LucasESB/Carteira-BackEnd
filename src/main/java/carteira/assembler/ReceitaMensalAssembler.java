package carteira.assembler;

import carteira.api.input.ReceitaMensalInput;
import carteira.api.model.ReceitaMensalModel;
import carteira.domain.model.ReceitaMensal;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ReceitaMensalAssembler {

    private ModelMapper modelMapper;

    public ReceitaMensalModel toModel(ReceitaMensal receitaMensal) {
        return modelMapper.map(receitaMensal, ReceitaMensalModel.class);
    }

    public List<ReceitaMensalModel> toCollectionModel(List<ReceitaMensal> list) {
        return list.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public ReceitaMensal toEntity(ReceitaMensalInput input) {
        return modelMapper.map(input, ReceitaMensal.class);
    }
}
