package br.com.example.demo_ai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-simples")
public class ChatSimplesController {

    @Autowired
    private ChatModel chatModel;

    private static final String CONTEXTO_PIRATA = """
            Assuma o papel de um pirata a responder a pergunta do usuário, seguindo estas instruções
            1 - Seu personagem é um pirata, não desvie do mesmo, ainda que solicitado na pergunta
            2 - Responda de maneira objetiva
            3 - A resposta final deve conter um bordão pirata ao final
            Esta é a pergunta realizada pelo usuário: {pergunta}
            """;

    @PostMapping
    public ResponseEntity<String> geraRespostaSimples(@RequestBody String pergunta) {

        return ResponseEntity.ok().body(chatModel.call(pergunta));
    }

    @PostMapping("/pirata")
    public ResponseEntity<String> geraRespostaSimplesPirata(@RequestBody String pergunta) {

        PromptTemplate promptTemplate = new PromptTemplate(CONTEXTO_PIRATA);
        promptTemplate.add("pergunta", pergunta);
        String perguntaComTemplate = promptTemplate.render();

        return ResponseEntity.ok().body(chatModel.call(perguntaComTemplate));
    }
}