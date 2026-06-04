package hu.matenikula.foxmanager.rest.dto;

import hu.matenikula.foxmanager.domain.Fox;

public final class FoxMapper {

    private FoxMapper() {
    }

    public static Fox toEntity(FoxRequest request) {
        Fox fox = new Fox();
        fox.setName(request.getName());
        fox.setSpecies(request.getSpecies());
        fox.setGender(request.getGender());
        fox.setImage(request.getImage());
        return fox;
    }

    public static FoxResponse toResponse(Fox fox) {
        FoxResponse response = new FoxResponse();
        response.setId(fox.getId());
        response.setName(fox.getName());
        response.setSpecies(fox.getSpecies());
        response.setGender(fox.getGender());
        response.setImage(fox.getImage());
        return response;
    }
}