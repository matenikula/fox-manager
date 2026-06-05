package hu.matenikula.foxmanager.rest;

import hu.matenikula.foxmanager.domain.Fox;
import hu.matenikula.foxmanager.exception.RequestValidationException;
import hu.matenikula.foxmanager.rest.dto.FoxMapper;
import hu.matenikula.foxmanager.rest.dto.FoxRequest;
import hu.matenikula.foxmanager.rest.dto.FoxResponse;
import hu.matenikula.foxmanager.service.FoxService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/foxes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoxResource {

    @Inject
    private FoxService foxService;
    @Inject
    private Validator validator;

    @GET
    @Operation(summary = "Összes róka listázása")
    @APIResponse(responseCode = "200", description = "A rókák listája",
            content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = FoxResponse.class)))
    public List<FoxResponse> getAll() {
        return foxService.getAllFoxes().stream()
                .map(FoxMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Egy róka lekérése azonosító alapján")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "A megtalált róka",
                    content = @Content(schema = @Schema(implementation = FoxResponse.class))),
            @APIResponse(responseCode = "404", description = "Nincs ilyen azonosítójú róka")
    })
    public FoxResponse getById(@PathParam("id") Long id) {
        return FoxMapper.toResponse(foxService.getFoxById(id));
    }

    @POST
    @Operation(summary = "Új róka létrehozása")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "A létrehozott róka",
                    content = @Content(schema = @Schema(implementation = FoxResponse.class))),
            @APIResponse(responseCode = "400", description = "Érvénytelen kérés (validációs hiba)")
    })
    public Response create(FoxRequest request, @Context UriInfo uriInfo) {
        Set<ConstraintViolation<FoxRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new RequestValidationException(violations);
        }
        Fox created = foxService.createFox(FoxMapper.toEntity(request));
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(created.getId()))
                .build();
        return Response.created(location).entity(FoxMapper.toResponse(created)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Róka törlése azonosító alapján")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Sikeres törlés"),
            @APIResponse(responseCode = "404", description = "Nincs ilyen azonosítójú róka")
    })
    public Response delete(@PathParam("id") Long id) {
        foxService.deleteFox(id);
        return Response.noContent().build();
    }
}