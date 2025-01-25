package fr.pantheonsorbonne.ufr27.miage.ressource;

import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import fr.pantheonsorbonne.ufr27.miage.exception.QueueException.NotEnoughPlayersException;
import fr.pantheonsorbonne.ufr27.miage.exception.QueueException.PlayerAlreadyInQueueException;
import fr.pantheonsorbonne.ufr27.miage.exception.QueueException.QueueNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;


@Path("/queue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class QueueManagerRessource {
    
    @Inject
    QueueManager queueManager;

    @POST
    @Path("/{theme}getOrCreateQueue")
    public Response getQueueMapping(@PathParam("theme") String theme) {
        try {
            Object queue = queueManager.getOrCreateQueue(theme);
            return Response.ok(queue)
                    .entity(String.format("Queue for theme '%s' was successfully retrieved or created.", theme))
                    .build();
        } catch (QueueNotFoundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("Failed to get or create queue for theme '%s': %s", theme, e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/{theme}/{userId}/{userMmr}/{userTheme}/addPlayerToQueueMapping")
    public Response addPlayerToQueueMapping(
            @PathParam("userId") Long userId,
            @PathParam("userMmr") int userMmr,
            @PathParam("userTheme") String userTheme) {
        try {
            UserWithMmr user = new UserWithMmr(userId, userTheme, userMmr);
            queueManager.addPlayerToQueue(user);
            return Response.ok()
                    .entity(String.format("User with ID '%d' was successfully added to the queue for theme '%s'.", userId, userTheme))
                    .build();
        } catch (PlayerAlreadyInQueueException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("Failed to add user with ID '%d' to the queue: %s", userId, e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/{theme}/formTeams")
    public Response formTeams(@PathParam("theme") String theme) {
        try{
            queueManager.formTeams(theme);
            return Response.ok("Teams formed successfully for theme: " + theme).build();
        } catch (NotEnoughPlayersException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to form teams for theme: " + theme)
                           .build();
        }
    }
}
