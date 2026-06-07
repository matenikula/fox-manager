package hu.matenikula.foxmanager.rest.exception;

import hu.matenikula.foxmanager.exception.FoxNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class FoxNotFoundExceptionMapper implements ExceptionMapper<FoxNotFoundException> {
    @Override
    public Response toResponse(FoxNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("message", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}