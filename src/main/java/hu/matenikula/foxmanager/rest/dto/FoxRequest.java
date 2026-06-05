package hu.matenikula.foxmanager.rest.dto;

import hu.matenikula.foxmanager.domain.Gender;
import hu.matenikula.foxmanager.rest.validation.ValueOfEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class FoxRequest {

    @NotBlank(message = "A név megadása kötelező")
    @Size(max = 255, message = "A név legfeljebb 255 karakter lehet")
    @Schema(description = "A róka neve", example = "Vuk", required = true)
    private String name;

    @Size(max = 255, message = "A faj legfeljebb 255 karakter lehet")
    @Schema(description = "A róka faja", example = "Vörös róka")
    private String species;

    @NotBlank(message = "A nem megadása kötelező")
    @ValueOfEnum(enumClass = Gender.class, message = "Érvénytelen nem, megengedett értékek: MALE, FEMALE")
    @Schema(description = "A róka neme", example = "MALE", enumeration = {"MALE", "FEMALE"})
    private String gender;

    @Schema(description = "A róka képe")
    private String image;
}
