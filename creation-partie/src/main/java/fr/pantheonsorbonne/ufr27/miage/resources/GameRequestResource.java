package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.AuthResponse;
import fr.pantheonsorbonne.ufr27.miage.dto.JoinGameRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.LoginRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.RegisterRequest;
import fr.pantheonsorbonne.ufr27.miage.exception.*;
import fr.pantheonsorbonne.ufr27.miage.gateway.GameCreationGateway;
import fr.pantheonsorbonne.ufr27.miage.service.GameCreationService;
import fr.pantheonsorbonne.ufr27.miage.service.AuthenticationService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

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
    @Operation(
            summary = "Join a game",
            description = """
        Allows a player to join a game by sending a join request. 

        **Note**: 
        To test error cases other than `401 Unauthorized`, 
        you can comment out the following code block in the method implementation:

        ```java
        if (token == null || !token.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of(
                            "error", "Invalid or missing token",
                            "code", "INVALID_TOKEN"
                    )).build();
        }
        ```
        """

    )
    public Response joinGame(@HeaderParam("Authorization") String token, JoinGameRequest joinRequest) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of(
                                "error", "Invalid or missing token",
                                "code", "INVALID_TOKEN"
                        )).build();
            }

            gameCreationGateway.publishJoinRequest(joinRequest);

            return Response.ok(Map.of(
                    "message", "Join request accepted successfully",
                    "playerId", joinRequest.playerId()
            )).build();
        } catch (DuplicateRequestException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of(
                            "error", e.getMessage(),
                            "code", "DUPLICATE_REQUEST"
                    )).build();
        } catch (PlayerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "error", e.getMessage(),
                            "code", "PLAYER_NOT_FOUND"
                    )).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                            "error", "An unexpected error occurred while processing the join request",
                            "code", "INTERNAL_ERROR"
                    )).build();
        }
    }



    @DELETE
    @Path("/leave/{playerId}")
    @Operation(
            summary = "Leave a game",
            description = "Allows a player to leave the game queue."
    )
    public Response leaveGame(@PathParam("playerId") Long playerId) {
        try {
            gameCreationGateway.publishCancelRequest(playerId);

            return Response.ok(Map.of(
                    "message", "Successfully left the queue",
                    "playerId", playerId
            )).build();
        } catch (JoinRequestNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "error", e.getMessage(),
                            "code", "JOIN_REQUEST_NOT_FOUND"
                    )).build();
        } catch (PlayerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "error", e.getMessage(),
                            "code", "PLAYER_NOT_FOUND"
                    )).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                            "error", "An unexpected error occurred while processing the leave request",
                            "code", "INTERNAL_ERROR"
                    )).build();
        }
    }


    @GET
    @Path("/status/{playerId}")
    @Operation(
            summary = "Get player status",
            description = "Fetches the current status of a player in the game."
    )
    public Response getPlayerStatus(@PathParam("playerId") Long playerId) {
        try {
            String status = gameService.getPlayerStatus(playerId);
            return Response.ok(Map.of(
                    "playerId", playerId,
                    "status", status
            )).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                            "error", "Failed to retrieve player status",
                            "code", "INTERNAL_ERROR"
                    )).build();
        }
    }


    @POST
    @Path("/auth/register")
    @Operation(
            summary = "Register a new player",
            description = "Registers a new player with a username and password."
    )
    public Response register(RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return Response.ok(Map.of(
                    "message", "Registration successful",
                    "playerId", response.playerId(),
                    "username", response.username()
            )).build();
        } catch (UsernameAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of(
                            "error", e.getMessage(),
                            "code", "USERNAME_ALREADY_EXISTS"
                    )).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "error", "Failed to process registration request",
                            "code", "BAD_REQUEST"
                    )).build();
        }
    }



    @POST
    @Path("/auth/login")
    @Operation(
            summary = "Login a player",
            description = "Authenticates a player using their username and password."
    )
    public Response login(LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return Response.ok(Map.of(
                    "message", "Login successful",
                    "playerId", response.playerId(),
                    "username", response.username(),
                    "token", response.token()
            )).build();
        } catch (InvalidCredentialsException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of(
                            "error", e.getMessage(),
                            "code", "INVALID_CREDENTIALS"
                    )).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                            "error", "An unexpected error occurred while processing the login request",
                            "code", "INTERNAL_ERROR"
                    )).build();
        }
    }



}