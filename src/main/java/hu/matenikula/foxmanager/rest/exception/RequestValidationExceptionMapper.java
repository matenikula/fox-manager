package hu.matenikula.foxmanager.rest.exception;

import hu.matenikula.foxmanager.exception.RequestValidationException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class RequestValidationExceptionMapper implements ExceptionMapper<RequestValidationException> {
    @Override
    public Response toResponse(RequestValidationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("errors", exception.getErrors()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
