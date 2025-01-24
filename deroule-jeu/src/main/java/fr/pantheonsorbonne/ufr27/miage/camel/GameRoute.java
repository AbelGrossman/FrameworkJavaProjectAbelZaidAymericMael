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
//
// /*  --> reçoit de creation-partie : "{
//   "playerIds": ["1", "2", "3", "4", "5", "6"],
//   "difficulty": "easy",
//   "category": "General Knowledge",
//   "totalQuestions": 1,
//   "questions": [
//     {
//       "type": "multiple",
//       "difficulty": "easy",
//       "category": "General Knowledge",
//       "question": "What is the capital of France?",
//       "correct_answer": "Paris",
//       "incorrect_answers": ["London", "Berlin", "Madrid"]
//     }
//   ]
// }"
//

// *Quand la partie est finie (isOver=1)

//  Envoie à statistiques : "{
//   "gameId": 1,
//   "category": "General Knowledge",
//   "totalQuestions": 1
//   "playerResults": [
//     {
//       "playerId": "1",
//       "score": 1,
//       "averageResponseTime": 1000,
//       "rank": 1
//     },
//     {
//       "playerId": "2",
//       "score": 4,
//       "averageResponseTime": 1333,
//       "rank": 4
//     }                                          --> etc pour chaque joueur de la partie
//   ]
// }"


//  Envoie à creation-partie : "{
//   "teamId": 1,
//   "isOver": 1
// }"



