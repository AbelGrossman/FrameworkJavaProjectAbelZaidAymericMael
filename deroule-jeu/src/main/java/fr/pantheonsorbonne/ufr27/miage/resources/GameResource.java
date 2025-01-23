package fr.pantheonsorbonne.ufr27.miage.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import fr.pantheonsorbonne.ufr27.miage.service.GameService;
import fr.pantheonsorbonne.ufr27.miage.dto.GameInitializationRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.AnswerRequest;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;
import java.util.Map;

@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameResource {

    @Inject
    GameService gameService;

    @POST
    @Path("/initialize")
    @Operation(summary = "Initialize the game")
    @APIResponse(responseCode = "201", description = "Game initialized successfully")
    public Response initializeGame(@RequestBody GameInitializationRequest request) {
        try {
            Long gameId = gameService.initializeGame(
                    request.playerIds(),
                    request.category(),
                    request.difficulty(),
                    request.totalQuestions(),
                    request.questions());
            return Response.status(Response.Status.CREATED).entity(Map.of("gameId", gameId)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to initialize game")).build();
        }
    }

    @POST
    @Path("/submitAnswer")
    @Operation(summary = "Submit an answer")
    @APIResponse(responseCode = "200", description = "Answer submitted successfully")
    public Response submitAnswer(@RequestBody AnswerRequest request) {
        try {
            int updatedScore = gameService.processAnswer(request.playerId(), request.answer());
            return Response.ok()
                    .entity(Map.of(
                            "score", updatedScore,
                            "message", "Answer submitted successfully",
                            "correct", updatedScore > gameService.getPlayerScore(request.playerId())))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/state")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get current game state")
    @APIResponse(responseCode = "200", description = "Game state retrieved successfully")
    public Response getGameState(@QueryParam("playerId") String playerId) {
        try {
            Map<String, Object> gameState = gameService.getCurrentGameState(playerId);
            return Response.ok(gameState).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/fetchQuestions")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Fetch questions for the game")
    @APIResponse(responseCode = "200", description = "Questions retrieved successfully")
    public Response fetchQuestions(@QueryParam("playerId") String playerId) {
        try {
            List<QuestionDTO> questions = gameService.getQuestionsForGame(playerId);
            return Response.ok(questions).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/score")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get player score")
    @APIResponse(responseCode = "200", description = "Player score retrieved successfully")
    public Response getPlayerScore(@QueryParam("playerId") String playerId) {
        try {
            int score = gameService.getPlayerScore(playerId);
            return Response.ok(Map.of("score", score)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/finish")
    @Operation(summary = "Finish the game")
    @APIResponse(responseCode = "200", description = "Game finished successfully")
    public Response finishGame() {
        try {
            gameService.finishGame();
            return Response.ok()
                    .entity(Map.of(
                            "message", "Game finished successfully",
                            "gameStatus", "complete"))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}