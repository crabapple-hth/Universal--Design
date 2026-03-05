package com.example.forum.Controller.Ai;


import com.example.forum.Entity.RestBean;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @GetMapping("/ai")
    public RestBean<String> AiChat(@RequestParam(value="message") String message){
        return RestBean.success(chatClient.prompt(message).call().content());
    }
}
