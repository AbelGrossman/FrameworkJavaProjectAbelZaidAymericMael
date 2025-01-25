package fr.pantheonsorbonne.ufr27.miage.ressource;

import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
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
        queueManager.getOrCreateQueue(theme);
        return Response.ok().build();
    }

    @POST
    @Path("/{theme}/{userId}/{userMmr}/{userTheme}/addPlayerToQueueMapping")
    public Response addPlayerToQueueMapping(@PathParam("userId") Long userId, @PathParam("userMmr") int userMmr, @PathParam("userTheme") String userTheme) {
        UserWithMmr user = new UserWithMmr(userId, userTheme, userMmr);
        queueManager.addPlayerToQueue(user);
        return Response.ok().build();
    }

    @POST
    @Path("/{theme}/formTeams")
    public Response formTeams(@PathParam("theme") String theme) {
        queueManager.formTeams(theme);
        return Response.ok().build();
    }
}
