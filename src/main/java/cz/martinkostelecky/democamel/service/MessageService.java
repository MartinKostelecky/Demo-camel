package cz.martinkostelecky.democamel.service;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService extends RouteBuilder {

    private String messageProcessor(String message) {
        return message;
    }

    @Override
    public void configure() throws Exception {
        from("direct:start")
                .routeId("Person1")
                .process(this::processMessage)
                .log("Message whispered to second person - ${body}")
                .to("direct:passMessage");

        from("direct:passMessage")
                .routeId("Person2")
                .process(this::processMessage)
                .log("Message whispered to third person - ${body}")
                .to("direct:showMessage");

        from("direct:showMessage")
                .routeId("Person3")
                //another way without extra method
                .process(e -> e.getIn().setBody(
                        shuffleCharacters(e.getIn().getBody(String.class))))
                .log("Delivered message is: ${body}")
                .stop()
                .end();
    }

    private void processMessage(Exchange exchange) {
        String message = exchange.getIn().getBody(String.class);
        exchange.getIn().setBody(shuffleCharacters(message));
    }


    private String shuffleCharacters(String message) {
        List<Character> chars = new ArrayList<>();

        for (int i = 0; i < message.length(); i++) {
            chars.add(message.charAt(i));
        }

        Collections.shuffle(chars);

        StringBuilder sb = new StringBuilder(message.length());

        for(char c : chars) {
            sb.append(c);
        }

        return sb.toString();
    }

}