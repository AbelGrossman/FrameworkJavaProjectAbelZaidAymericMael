package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/matchmaking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchmakingResource {
    @Inject
    QueueManager queueManager;

    @POST
    @Path("/join")
    public Response addPlayerToQueue(User user) {
        queueManager.addPlayerToQueue(user);
        return Response.ok().build();
    }

    @GET
    @Path("/match/{theme}")
    public Response findTeam(@PathParam("theme") String theme, @QueryParam("mmr-difference") int mmrDifference) {
        List<User> team = queueManager.findTeam(theme, mmrDifference);
        return Response.ok(team).build();
    }
}
