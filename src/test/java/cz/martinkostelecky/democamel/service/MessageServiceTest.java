package cz.martinkostelecky.democamel.service;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@CamelSpringBootTest
@MockEndpoints("direct:showMessage")
class MessageServiceTest {

    @Autowired
    private ProducerTemplate producerTemplate;

    @EndpointInject("mock:direct:showMessage")
    private MockEndpoint mockEndpoint;

    @Test
    void MessageShouldBeReceived() throws InterruptedException {
        mockEndpoint.expectedBodiesReceived("Ahoj");
        producerTemplate.sendBody("direct:start", "Ahoj");
        mockEndpoint.assertIsSatisfied();
    }
}