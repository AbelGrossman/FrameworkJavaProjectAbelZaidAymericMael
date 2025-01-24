package fr.pantheonsorbonne.ufr27.miage.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(baseUri = "https://opentdb.com/api.php")
public interface OpenDataRestClient {

    @GET
    String getQuestions(
            @QueryParam("amount") int amount,
            @QueryParam("category") String category,
            @QueryParam("difficulty") String difficulty
    );

    /*
    Cette API a des numéros pour représenter ses catégories :
    25 = art
    9 = general knowledge
    17 = science
    21 = sports
    22 = geography
    23 = history
     */
}
