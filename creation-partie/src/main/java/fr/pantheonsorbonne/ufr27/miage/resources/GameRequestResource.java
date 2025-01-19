package fr.pantheonsorbonne.ufr27.miage.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.dto.JoinGameRequest;
import fr.pantheonsorbonne.ufr27.miage.service.GameCreationService;
import fr.pantheonsorbonne.ufr27.miage.exception.DuplicateRequestException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.camel.ProducerTemplate;
import java.util.Map;
@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameRequestResource {

    @Inject
    GameCreationService gameService;

    @Inject
    ProducerTemplate producerTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    @POST
    @Path("/join")
    public Response joinGame(JoinGameRequest joinRequest) {
        try {
            gameService.validateNewRequest(joinRequest.playerId());

            // Prépare le message pour le MatchMaking
            Map<String, Object> matchmakingRequest = Map.of(
                    "playerId", joinRequest.playerId(),
                    "theme", joinRequest.theme(),
                    "mmr", joinRequest.mmr()
            );

            // Envoie à la queue Camel
            String response = producerTemplate.requestBody(
                    "sjms2:M1.MatchmakingService",
                    mapper.writeValueAsString(matchmakingRequest),
                    String.class
            );

            return Response.ok(Map.of(
                    "message", "Join request accepted",
                    "playerId", joinRequest.playerId(),
                    "response", response
            )).build();

        } catch (DuplicateRequestException e) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "An unexpected error occurred: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/leave/{playerId}")
    public Response leaveGame(@PathParam("playerId") Long playerId) {
        try {
            gameService.cancelRequest(playerId);
            return Response.ok(Map.of(
                    "message", "Successfully left the queue",
                    "playerId", playerId
            )).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to leave queue"))
                    .build();
        }
    }

    @GET
    @Path("/status/{playerId}")
    public Response getPlayerStatus(@PathParam("playerId") Long playerId) {
        try {
            String status = gameService.getPlayerStatus(playerId);
            return Response.ok(Map.of(
                    "playerId", playerId,
                    "status", status
            )).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to get status"))
                    .build();
        }
    }
}