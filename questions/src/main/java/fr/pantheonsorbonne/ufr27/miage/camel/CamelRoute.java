package fr.pantheonsorbonne.ufr27.miage.camel;


import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.service.QuestionServicesImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;

import java.util.List;

@ApplicationScoped
public class CamelRoute extends RouteBuilder {

    @Inject
    QuestionServicesImpl questionServices;

    @Override
    public void configure() throws Exception {
        /*
        from("direct:fetchQuestions")
                .routeId("fetchTriviaQuestionsRoute")
                .log("Fetching questions for category: ${header.category}, difficulty: ${header.difficulty}")
                .process(exchange -> {
                    String category = exchange.getMessage().getHeader("category", String.class);
                    String difficulty = exchange.getMessage().getHeader("difficulty", String.class);

                    List<QuestionDTO> questions = questionServices.askAPIQuestions(category, difficulty);

                    exchange.getMessage().setBody(questions);
                })
                .log("Returning questions: ${body}");
         */

        from("sjms2:fetchQuestions")
                .log("Réponse de la requête : ${body}")
                //.bean(questionServices, "askAPIQuestions")
                .log("Fetching questions for category: ${header.theme}, difficulty: ${header.difficulty}")
                .process(exchange -> {
                    String category = exchange.getMessage().getHeader("theme", String.class);
                    String difficulty = exchange.getMessage().getHeader("difficulty", String.class);

                    List<QuestionDTO> questions = questionServices.askAPIQuestions(category, difficulty);

                    exchange.getMessage().setBody(questions);
                })
                .log("Returning questions: ${body}")
                .marshal().json()
                .to("sjms2:fetchQuestionsResponses");
    }

}



