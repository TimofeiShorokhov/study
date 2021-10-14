package com.example.study.Controllers;

import com.example.study.models.Message;
import com.example.study.models.User;
import com.example.study.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Controller
public class MainContorller {

    private final MessageRepository messageRepository;

    public MainContorller(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String greeting(
            @RequestParam(name="name",required = false,defaultValue="World") String name, Map<String, Object> model){
        return "greeting";
    }


    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,Model model){
        Iterable<Message> messages = messageRepository.findAll();
        if(filter != null && !filter.isEmpty()) {
            messages = messageRepository.findByTag(filter);
        } else{
            messages = messageRepository.findAll();
        }
        model.addAttribute("messages",messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String mainAdd(@AuthenticationPrincipal User user, @RequestParam String text, @RequestParam String tag, Map<String, Object> model){
        Message message = new Message(text,tag, user);

        messageRepository.save(message);

        Iterable<Message> messages = messageRepository.findAll();

        model.put("messages",messages);

        return "main";
    }


}
