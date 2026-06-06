package hu.matenikula.foxmanager.rest.dto;

import hu.matenikula.foxmanager.domain.Fox;
import hu.matenikula.foxmanager.domain.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoxMapperTest {

    @Test
    void toEntity_mapsFieldsAndLeavesIdNull() {
        FoxRequest request = new FoxRequest();
        request.setName("Vuk");
        request.setSpecies("Vörös róka");
        request.setGender("MALE");

        Fox fox = FoxMapper.toEntity(request);

        assertEquals("Vuk", fox.getName());
        assertEquals("Vörös róka", fox.getSpecies());
        assertEquals(Gender.MALE, fox.getGender());
        assertNull(fox.getId());
    }

    @Test
    void toResponse_mapsAllFields() {
        Fox fox = new Fox();
        fox.setId(5L);
        fox.setName("Reynard");
        fox.setSpecies("Vörös róka");
        fox.setGender(Gender.FEMALE);
        fox.setImage("http://img/5.jpg");

        FoxResponse response = FoxMapper.toResponse(fox);

        assertEquals(5L, response.getId());
        assertEquals("Reynard", response.getName());
        assertEquals("Vörös róka", response.getSpecies());
        assertEquals(Gender.FEMALE, response.getGender());
        assertEquals("http://img/5.jpg", response.getImage());
    }
}