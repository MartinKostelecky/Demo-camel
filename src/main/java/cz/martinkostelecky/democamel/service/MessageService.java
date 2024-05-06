package cz.martinkostelecky.democamel.service;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MessageService extends RouteBuilder {

    private String messageProcessor(String message) {
        return message;
    }

    @Override
    public void configure() throws Exception {
        from("direct:start")
                .routeId("Person1")
                .process(exchange -> {
                    log.info("First person received message: " + exchange.getIn().getBody(String.class));

                    String message = exchange.getIn().getBody(String.class);

                    log.info("Message whispered on - " + message);

                })

                .to("direct:passMessage");

        from("direct:passMessage")
                .routeId("Person2")
                .process(exchange -> {
                    log.info("Second person received message: " + exchange.getIn().getBody(String.class));

                    String message = exchange.getIn().getBody(String.class);

                    log.info("Message whispered on - " + message);
                })

                .to("direct:showMessage");

        from("direct:showMessage")
                .routeId("Person3")
                .process(exchange -> {
                    log.info("Third person received message: " + exchange.getIn().getBody(String.class));

                    String message = exchange.getIn().getBody(String.class);
                    String deliveredMessage = messageProcessor(message);
                    exchange.getIn().setBody(deliveredMessage);

                    log.info("Delivered message is - " + deliveredMessage);
                })
                .stop()
                .end();
    }

}