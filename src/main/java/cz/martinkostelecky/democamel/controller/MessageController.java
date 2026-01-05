package cz.martinkostelecky.democamel.controller;

import cz.martinkostelecky.democamel.model.Message;
import cz.martinkostelecky.democamel.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(value = "/chinesewhispers", method = GET)
    public String createMessage(Model model) {
        Message message = new Message();
        model.addAttribute("message", message);
        return "chinesewhispers";
    }

    @RequestMapping(value = "/chinesewhispers", method = POST)
    public String getDeliveredMessage(@ModelAttribute("message") Message message, Model model) {
        log.info("Whispered message is - " + message.getMessageText());
        String deliveredMessage = messageService.getContext().createProducerTemplate().requestBody(
                "direct:start", message.getMessageText(), String.class);

        //model update
        message.setMessageText(deliveredMessage);
        model.addAttribute("message", message);

        log.info("Redirecting to /chinesewhispers");
        return "chinesewhispers";
    }
}
