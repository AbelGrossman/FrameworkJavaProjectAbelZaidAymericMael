package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.service.QuestionServices;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.camel.ProducerTemplate;

import java.util.List;

@Path("/questions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuestionResources {

    @Inject
    QuestionServices questionServices;

    @Inject
    ProducerTemplate producerTemplate;

    @GET
    @Path("/api/{category}/{difficulty}")
    public List<QuestionDTO> getQuestions(@PathParam("category") String category, @PathParam("difficulty") String difficulty) {
        return questionServices.askAPIQuestions(category, difficulty);
    }

}
