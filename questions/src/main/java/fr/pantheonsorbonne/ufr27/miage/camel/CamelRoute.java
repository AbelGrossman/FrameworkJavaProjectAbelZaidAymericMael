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
        from("sjms2:fetchQuestions")
                .onException(Exception.class)
                    .log("Error occurred: ${exception.message}")
                    .handled(true)
                    .to("sjms2:errorQueue")
                    .end()
                .log("Réponse de la requête : ${headers}")
//                .bean(questionServices, "askAPIQuestions")
                .log("Fetching questions for category:ID=${header.id}, ${header.theme}, difficulty: ${header.difficulty}")
                .process(exchange -> {
                    String id = exchange.getMessage().getHeader("id", String.class);
                    String category = exchange.getMessage().getHeader("theme", String.class);
                    String difficulty = exchange.getMessage().getHeader("difficulty", String.class);

                    List<QuestionDTO> questions = questionServices.askAPIQuestions(category, difficulty);

                    exchange.getMessage().setBody(questions);

                    exchange.getMessage().setHeader("id", id);
                })
                .log("Returning questions for ID=${header.id}: ${body}")
                .marshal().json()
                .to("sjms2:fetchQuestionsResponses");
    }
}



