package hu.matenikula.foxmanager.rest.dto;

import hu.matenikula.foxmanager.domain.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class FoxRequest {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 255)
    private String species;

    @NotNull
    private Gender gender;

    private String image;
}
