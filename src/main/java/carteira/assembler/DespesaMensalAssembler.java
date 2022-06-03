package carteira.assembler;

import carteira.api.input.DespesaMensalInput;
import carteira.api.model.DespesaMensalModel;
import carteira.domain.model.DespesaMensal;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DespesaMensalAssembler {

    private ModelMapper modelMapper;

    public DespesaMensalModel toModel(DespesaMensal deseDespesaMensal) {
        return modelMapper.map(deseDespesaMensal, DespesaMensalModel.class);
    }

    public List<DespesaMensalModel> toCollectionModel(List<DespesaMensal> list) {
        return list.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public DespesaMensal toEntity(DespesaMensalInput input) {
        return modelMapper.map(input, DespesaMensal.class);
    }
}
