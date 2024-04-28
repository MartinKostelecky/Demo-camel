package cz.martinkostelecky.democamel.controller;

import cz.martinkostelecky.democamel.model.Message;
import cz.martinkostelecky.democamel.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@Slf4j
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/chinesewhispers", method = GET)
    public String createMessage(Model model) {
        Message message = new Message();
        model.addAttribute("message", message);
        return "chinesewhispers";
    }

    @RequestMapping(value = "/chinesewhispers", method = POST)
    public String getDeliveredMessage(@ModelAttribute("message") Message message)  {
        log.info("Whispered message is - " + message.getMessageText());
        messageService.getContext().createProducerTemplate().requestBody("direct:start", message.getMessageText(), String.class);
        log.info("Redirecting to /chinesewhispers");
        return "chinesewhispers";
    }
}
