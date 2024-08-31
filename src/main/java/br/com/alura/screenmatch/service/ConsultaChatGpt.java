//package br.com.alura.screenmatch.service;
//import com.theokanning.openai.completion.CompletionRequest;
//import com.theokanning.openai.service.OpenAiService;
//public class ConsultaChatGpt {
//    public static String obterTraducao(String texto) {
//        OpenAiService service = new OpenAiService("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");
//
//        CompletionRequest requisicao = CompletionRequest.builder()
//                .model("gpt-3.5-turbo-instruct")
//                .prompt("traduza para o português o texto: " + texto)
//                .maxTokens(1000)
//                .temperature(0.7)
//                .build();
//
//        var resposta = service.createCompletion(requisicao);
//        return resposta.getChoices().get(0).getText();
//    }
//}
//
