package fr.pantheonsorbonne.ufr27.miage.service;

import org.apache.camel.ProducerTemplate;

import fr.pantheonsorbonne.ufr27.miage.dto.MatchmakingRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
public class UserEmitter {

    @Inject
    ProducerTemplate producerTemplate;

    @POST
    @Path("/processRankedUser")
    public Response processRankedUser(MatchmakingRequest user){
        producerTemplate.sendBody("direct:userMmrRequest", user);
        return Response.ok().build();
    }
    
}
