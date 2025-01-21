// package fr.pantheonsorbonne.ufr27.miage.camel;

// import org.apache.camel.builder.RouteBuilder;
// import org.apache.camel.model.dataformat.JsonLibrary;
// import fr.pantheonsorbonne.ufr27.miage.dto.QuizResponseDTO;

// public class GameRoute extends RouteBuilder {

//     @Override
//     public void configure() throws Exception {
//         from("jms:queue:questions")
//                 .unmarshal().json(JsonLibrary.Jackson, QuizResponseDTO.class) // Unmarshal JSON into QuizResponseDTO
//                 .to("bean:gameService?method=processQuestions"); // Pass to the game service
//     }
// }