package hu.matenikula.foxmanager.rest.dto;

import hu.matenikula.foxmanager.domain.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoxResponse {

    private Long id;
    private String name;
    private String species;
    private Gender gender;
    private String image;
}