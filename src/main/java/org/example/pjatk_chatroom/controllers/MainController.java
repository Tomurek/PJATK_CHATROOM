package org.example.pjatk_chatroom.controllers;

import org.example.pjatk_chatroom.service.MessageService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class MainController {

    private final MessageService messageService;

    public MainController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public ModelAndView chat() {
        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        var model = new HashMap<String, Object>();
        model.put("loggedUserName", loggedUserName);
        model.put("messages", messageService.lastForActiveUser(100, loggedUserName));
        return new ModelAndView("index", model);
    }
}
