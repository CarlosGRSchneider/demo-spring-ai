package br.com.example.demo_ai.controller;

import br.com.example.demo_ai.service.RedisReaderService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat-contextualizado")
public class ChatContextualizadoController {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private RedisReaderService reader;

    private static final String CONTEXTO_NEGOCIO = """
            Assuma o papel de um assistente de uma empresa para candidatos de estagio, respondendo as suas perguntas segundo o seguinte contexto:
            {contexto}
            
            A pergunta realizada pelo usuário é esta: {pergunta}
            Para melhor atender a seu usuário, siga as seguintes regras
            1 - Responda de maneira objetiva, porém gentil e profissional
            2 - Responda sempre em português, mesmo que o usuário pergunte em outras linguas
            3 - Não responda a perguntas fora do contexto do manual. Se a pergunta não possuir relação com o tema do manual, devolva a recusa padrão
            4 - Caso você não obtenha a resposta no manual fornecido, devolva a recusa padrão
            Recusa padrão = Sinto muito, mas não tenho a informação para responder esta questão.
            """;

    @PostMapping
    public ResponseEntity<String> geraRespostaContextualizada(@RequestBody String pergunta) {

        List<Document> contexto = reader.searchData(pergunta);

        PromptTemplate promptTemplate = new PromptTemplate(CONTEXTO_NEGOCIO);
        promptTemplate.add("contexto", contexto);
        promptTemplate.add("pergunta", pergunta);
        String perguntaComTemplate = promptTemplate.render();

        return ResponseEntity.ok().body(chatModel.call(perguntaComTemplate));
    }
}