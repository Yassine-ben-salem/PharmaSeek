package mappers;

import dtos.DrugDto;
import entities.Drug;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T14:09:20+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class DrugMapperImpl implements DrugMapper {

    @Override
    public DrugDto toDrugDto(Drug drug) {
        if ( drug == null ) {
            return null;
        }

        DrugDto drugDto = new DrugDto();

        drugDto.setId( drug.getId() );
        drugDto.setName( drug.getName() );
        drugDto.setDescription( drug.getDescription() );
        drugDto.setRequiresPrescription( drug.getRequiresPrescription() );

        return drugDto;
    }

    @Override
    public Drug toDrug(DrugDto drugDto) {
        if ( drugDto == null ) {
            return null;
        }

        Drug drug = new Drug();

        drug.setId( drugDto.getId() );
        drug.setName( drugDto.getName() );
        drug.setDescription( drugDto.getDescription() );
        drug.setRequiresPrescription( drugDto.getRequiresPrescription() );

        return drug;
    }
}
