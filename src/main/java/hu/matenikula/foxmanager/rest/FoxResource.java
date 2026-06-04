package hu.matenikula.foxmanager.rest;

import hu.matenikula.foxmanager.domain.Fox;
import hu.matenikula.foxmanager.exception.RequestValidationException;
import hu.matenikula.foxmanager.rest.dto.FoxMapper;
import hu.matenikula.foxmanager.rest.dto.FoxRequest;
import hu.matenikula.foxmanager.rest.dto.FoxResponse;
import hu.matenikula.foxmanager.service.FoxService;

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
    public List<FoxResponse> getAll() {
        return foxService.getAllFoxes().stream()
                .map(FoxMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public FoxResponse getById(@PathParam("id") Long id) {
        return FoxMapper.toResponse(foxService.getFoxById(id));
    }

    @POST
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
    public Response delete(@PathParam("id") Long id) {
        foxService.deleteFox(id);
        return Response.noContent().build();
    }
}