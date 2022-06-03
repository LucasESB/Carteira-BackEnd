package carteira.assembler;

import carteira.api.input.TransacaoInput;
import carteira.api.model.TransacaoModel;
import carteira.domain.model.Transacao;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TransacaoAssembler {

    private ModelMapper modelMapper;

    public TransacaoModel toModel(Transacao transacao) {
        return modelMapper.map(transacao, TransacaoModel.class);
    }

    public List<TransacaoModel> toCollectionModel(List<Transacao> list) {
        return list.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public Transacao toEntity(TransacaoInput input) {
        return modelMapper.map(input, Transacao.class);
    }
}
