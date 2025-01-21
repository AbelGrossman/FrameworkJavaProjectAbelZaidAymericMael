package fr.pantheonsorbonne.ufr27.miage.resources;



import fr.pantheonsorbonne.ufr27.miage.dto.UserWithoutMmrRequest;
import fr.pantheonsorbonne.ufr27.miage.service.UserEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Named("UserProcessor")
public class UserProcessor {

    @Inject 
    UserEmitter userEmitter;

    @POST
    @Path("/processNewUser")
    public Response processNewUser(UserWithoutMmrRequest user) {
        // Add the incoming player to the queue
        userEmitter.processRankedUser(user);
        return Response.ok().build();
    }
}
