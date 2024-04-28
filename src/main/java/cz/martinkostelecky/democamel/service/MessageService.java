package cz.martinkostelecky.democamel.service;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MessageService extends RouteBuilder {

    private Random random = new Random();

    private String[] abc = {"abcdefghijklmnopqrstuvwyz"};

    private String messageProcessor(String message) {
        return message;
    }

    @Override
    public void configure() throws Exception {
        from("direct:start")

                .process(exchange -> {
                    log.info("Received message - " + exchange.getIn().getBody(String.class));

                    String message = exchange.getIn().getBody(String.class);

                    log.info("Message whispered on - " + message);

                })

                .to("direct:passMessage");

        from("direct:passMessage")

                .process(exchange -> {
                    log.info("Received message - " + exchange.getIn().getBody(String.class));

                    String message = exchange.getIn().getBody(String.class);

                    log.info("Message whispered on - " + message);
                })

                .to("direct:showMessage");

        from("direct:showMessage")

                .process(exchange -> {
                    log.info("Received message - " + exchange.getIn().getBody(String.class));

                    String message = exchange.getIn().getBody(String.class);
                    String deliveredMessage = messageProcessor(message);
                    exchange.getIn().setBody(deliveredMessage);

                    log.info("Delivered message is - " + deliveredMessage);
                })
                .stop()
                .end();
    }

}