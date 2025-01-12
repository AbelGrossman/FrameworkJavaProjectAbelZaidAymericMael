package fr.pantheonsorbonne.ufr27.miage.camel;

import org.apache.camel.builder.RouteBuilder;

public class MatchmakingRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("direct:receiveQueue")
            .log("Queue received: ${body}")
            .process(exchange -> {
                // Logic to process queue
            })
            .end();
    }
}
