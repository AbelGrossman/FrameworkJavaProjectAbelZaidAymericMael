package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.AuthResponse;
import fr.pantheonsorbonne.ufr27.miage.dto.JoinGameRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.LoginRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.RegisterRequest;
import fr.pantheonsorbonne.ufr27.miage.gateway.GameCreationGateway;
import fr.pantheonsorbonne.ufr27.miage.service.GameCreationService;
import fr.pantheonsorbonne.ufr27.miage.service.AuthenticationService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameRequestResource {

    @Inject
    GameCreationService gameService;
    @Inject
    GameCreationGateway gameCreationGateway;
    @Inject
    AuthenticationService authService;


    @POST
    @Path("/join")
    public Response joinGame(@HeaderParam("Authorization") String token, JoinGameRequest joinRequest) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("error", "Invalid token"))
                        .build();
            }
            gameCreationGateway.publishJoinRequest(joinRequest);



            return Response.ok(Map.of(
                    "message", "Join request accepted",
                    "playerId", joinRequest.playerId()
            )).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/leave/{playerId}")
    public Response leaveGame(@PathParam("playerId") Long playerId) {
        try {
            gameCreationGateway.publishCancelRequest(playerId);

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

    @POST
    @Path("/auth/register")
    public Response register(RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/auth/login")
    public Response login(LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }


}