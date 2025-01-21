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

@Path("/game")
@Produces("application/json")
@Consumes("application/json")
public class GameResource {

    @Inject
    GameService gameService;

    @POST
    @Path("/initialize")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Initialize the game", description = "Initialize the game with player IDs, difficulty, category, total questions, and questions.")
    @APIResponse(responseCode = "200", description = "Game initialized successfully")
    public Response initializeGame(@RequestBody GameInitializationRequest request) {
        String gameId = gameService.initializeGame(request.getPlayerIds(), request.getTotalQuestions(), request.getQuestions());
        return Response.ok().entity("Game initialized with ID: " + gameId).build();
    }

    @POST
    @Path("/finish")
    @Operation(summary = "Finish the game", description = "Finish the game and display final scores.")
    @APIResponse(responseCode = "200", description = "Game finished successfully")
    public Response finishGame() {
        gameService.finishGame();
        return Response.ok().entity("Game finished").build();
    }

    @GET
    @Path("/questions")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get questions for the game", description = "Retrieve the list of questions for the current game.")
    @APIResponse(responseCode = "200", description = "Questions retrieved successfully")
    public Response getQuestionsForGame(@QueryParam("playerId") String playerId) {
        try {
            List<QuestionDTO> questions = gameService.getQuestionsForGame(playerId);
            return Response.ok(questions).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/answer")
    @Operation(summary = "Submit an answer", description = "Submit an answer for the current question.")
    @APIResponse(responseCode = "200", description = "Answer submitted successfully")
    public Response submitAnswer(@RequestBody AnswerRequest request) {
        int updatedScore = gameService.processAnswer(request.getPlayerId(), request.getAnswer());
        return Response.ok().entity("Answer submitted. Updated score: " + updatedScore).build();
    }

    @GET
    @Path("/current-question")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get the current question", description = "Retrieve the current question for the game.")
    @APIResponse(responseCode = "200", description = "Current question retrieved successfully")
    public Response getCurrentQuestion(@QueryParam("playerId") String playerId) {
        try {
            QuestionDTO question = gameService.getCurrentQuestion(playerId);
            if (question != null) {
                return Response.ok(question).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("No more questions").build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/next-question")
    @Operation(summary = "Move to the next question", description = "Move to the next question in the game.")
    @APIResponse(responseCode = "200", description = "Moved to the next question successfully")
    public Response nextQuestion() {
        gameService.nextQuestion();
        return Response.ok().entity("Moved to the next question").build();
    }
}