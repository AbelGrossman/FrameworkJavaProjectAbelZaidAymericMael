package fr.pantheonsorbonne.ufr27.miage.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/")
@RegisterRestClient(baseUri = "https://the-trivia-api.com/v2/questions?")
public interface TheTriviaRestClient {

    @GET
    String getQuestions(
            @QueryParam("limit") int limit,
            @QueryParam("categories") String categories,
            @QueryParam("difficulties") String difficulties
    );
}
