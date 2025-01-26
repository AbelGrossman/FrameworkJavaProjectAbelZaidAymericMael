package fr.pantheonsorbonne.ufr27.miage.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import fr.pantheonsorbonne.ufr27.miage.service.GameService;
import fr.pantheonsorbonne.ufr27.miage.dto.*;
import fr.pantheonsorbonne.ufr27.miage.exception.GameException;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

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
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Game created successfully"),
        @APIResponse(responseCode = "400", description = "Invalid game parameters"),
        @APIResponse(responseCode = "401", description = "Missing team ID")
    })
    public Response initializeGame(@RequestBody GameInitializationRequest request,
            @HeaderParam("teamId") String teamId) {
        try {
            if (teamId == null) {
                throw new GameException.TeamIdMissingException();
            }
            Long gameId = gameService.initializeGame(
                    request.playerIds(),
                    request.category(),
                    request.difficulty(),
                    request.totalQuestions(),
                    request.questions(),
                    teamId);
            return Response.status(Response.Status.CREATED)
                .entity(Map.of("gameId", gameId))
                .build();
        } catch (GameException.TeamIdMissingException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (GameException.InvalidPlayerCountException | 
                GameException.InvalidTotalQuestionsException | 
                GameException.QuestionCountMismatchException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/currentGameID")
    @Operation(summary = "Get the current game ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Game ID retrieved successfully"),
        @APIResponse(responseCode = "404", description = "No active game found")
    })
    public Response getCurrentGameID(@QueryParam("playerId") String playerId) {
        try {
            Long gameId = gameService.getCurrentGameId();
            return Response.ok(Map.of("gameId", gameId)).build();
        } catch (GameException.GameNotInitializedException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @POST
    @Path("/submitAnswer")
    @Operation(summary = "Submit an answer")
    @APIResponses(value = {
        @APIResponse(responseCode = "202", description = "Answer accepted"),
        @APIResponse(responseCode = "404", description = "Game or player not found"),
        @APIResponse(responseCode = "409", description = "Answer already submitted")
    })
    public Response submitAnswer(@RequestBody AnswerRequest request) {
        try {
            int updatedScore = gameService.processAnswer(
                request.playerId(), 
                request.answer(), 
                request.responseTime()
            );
            return Response.status(Response.Status.ACCEPTED)
                .entity(Map.of(
                    "score", updatedScore,
                    "message", "Answer submitted successfully",
                    "correct", updatedScore > gameService.getPlayerScore(request.playerId())
                ))
                .build();
        } catch (GameException.AnswerAlreadySubmittedException e) {
            return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (GameException.PlayerNotFoundException | 
                GameException.GameNotInitializedException |
                GameException.NoCurrentQuestionException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/state")
    @Operation(summary = "Get current game state")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Game state retrieved successfully"),
        @APIResponse(responseCode = "403", description = "Player not authorized"),
        @APIResponse(responseCode = "404", description = "Game not found")
    })
    public Response getGameState(@QueryParam("playerId") String playerId) {
        try {
            Map<String, Object> gameState = gameService.getCurrentGameState(playerId);
            return Response.ok(gameState).build();
        } catch (GameException.PlayerNotAllowedException e) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (GameException.GameNotInitializedException |
                GameException.PlayerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/fetchQuestions")
    @Operation(summary = "Fetch questions for the game")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Questions retrieved successfully"),
        @APIResponse(responseCode = "403", description = "Player not authorized"),
        @APIResponse(responseCode = "404", description = "Game not found"),
        @APIResponse(responseCode = "410", description = "Game has ended")
    })
    public Response fetchQuestions(@QueryParam("playerId") String playerId) {
        try {
            List<QuestionDTO> questions = gameService.getQuestionsForGame(playerId);
            return Response.ok(questions).build();
        } catch (GameException.PlayerNotAllowedException e) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (GameException.GameEndedException e) {
            return Response.status(Response.Status.GONE)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (GameException.GameNotInitializedException |
                GameException.PlayerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/score")
    @Operation(summary = "Get player score")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Score retrieved successfully"),
        @APIResponse(responseCode = "404", description = "Player or game not found")
    })
    public Response getPlayerScore(@QueryParam("playerId") String playerId) {
        try {
            int score = gameService.getPlayerScore(playerId);
            return Response.ok(Map.of("score", score)).build();
        } catch (GameException.PlayerNotFoundException |
                GameException.GameNotInitializedException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @POST
    @Path("/finish")
    @Operation(summary = "Internal endpoint - Do not use", hidden = true)
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Game finished successfully"),
        @APIResponse(responseCode = "404", description = "Game not found"),
        @APIResponse(responseCode = "409", description = "Game already finished"),
        @APIResponse(responseCode = "403", description = "Direct access forbidden")
    })
    public Response finishGame(@QueryParam("gameId") Long gameId, @HeaderParam("Origin") String origin) {
        if (origin == null || !origin.contains("game.html")) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", "This endpoint can only be called from the game interface"))
                .build();
        }
        try {
            gameService.finishGame(gameId);
            return Response.ok()
                .entity(Map.of(
                    "message", "Game finished successfully",
                    "gameStatus", "complete"
                ))
                .build();
        } catch (GameException.GameAlreadyFinishedException e) {
            return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (GameException.GameNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @POST
    @Path("/createPlayerResult")
    @Operation(summary = "Create player result")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Player result created successfully"),
        @APIResponse(responseCode = "404", description = "Game not found"),
        @APIResponse(responseCode = "409", description = "Result already exists")
    })
    public Response createPlayerResult(@RequestBody PlayerResultsRequest request) {
        try {
            gameService.savePlayerResult(
                request.playerId(),
                request.gameId(),
                request.score(),
                request.averageResponseTime(),
                0,
                request.category(),
                request.totalQuestions()
            );
            return Response.status(Response.Status.CREATED)
                .entity(Map.of("message", "Player result created successfully"))
                .build();
        } catch (GameException.PlayerResultAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (GameException.GameNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/rankings/{gameId}")
    @Operation(summary = "Get game rankings")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Rankings retrieved successfully"),
        @APIResponse(responseCode = "404", description = "Game not found"),
        @APIResponse(responseCode = "425", description = "Game still in progress")
    })
    public Response getGameRankings(@PathParam("gameId") Long gameId) {
        try {
            Map<String, Integer> rankings = gameService.getGameRankings(gameId);
            gameService.sendToGameCompletionGateway(gameId);
            return Response.ok(rankings).build();
        } catch (GameException.GameStillInProgressException e) {
            return Response.status(425)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (GameException.GameNotFoundException |
                GameException.NoResultsFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
}