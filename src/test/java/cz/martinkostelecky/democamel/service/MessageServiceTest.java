package cz.martinkostelecky.democamel.service;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@CamelSpringBootTest
@MockEndpoints("direct:showMessage")
class MessageServiceTest {

    @Autowired
    private ProducerTemplate producerTemplate;

    @EndpointInject("mock:direct:showMessage")
    private MockEndpoint mockEndpoint;

    @Test
    void MessageShouldBeReceivedPermutated() throws InterruptedException {

        String originalMessage = "Ahoj";

        // Send message
        producerTemplate.sendBody("direct:start", originalMessage);

        // Assert mock received exactly 1 message
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();

        // Get the body that arrived at the mock
        String receivedAtMock = mockEndpoint.getReceivedExchanges()
                .getFirst()
                .getIn()
                .getBody(String.class);

        // Check permutation
        assertNotEquals(originalMessage, receivedAtMock);
    }
}
