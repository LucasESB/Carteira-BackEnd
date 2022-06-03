package carteira.assembler;

import carteira.api.input.ContaInput;
import carteira.api.model.ContaModel;
import carteira.domain.model.Conta;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ContaAssembler {

    private ModelMapper modelMapper;

    public ContaModel toModel(Conta conta) {
        return modelMapper.map(conta, ContaModel.class);
    }

    public List<ContaModel> toCollectionModel(List<Conta> list) {
        return list.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public Conta toEntity(ContaInput input) {
        return modelMapper.map(input, Conta.class);
    }
}
