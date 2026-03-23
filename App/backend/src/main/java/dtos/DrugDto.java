package dtos;

import lombok.Data;


@Data
public class DrugDto {
    private Long id;
    private String name;
    private String description;
    private Boolean requiresPrescription;
}