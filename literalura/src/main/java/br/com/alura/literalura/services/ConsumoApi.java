package br.com.alura.literalura.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {

    public String obterDados(String endereco) {
        System.out.println("Instanciando cliente HTTP...");
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL) // Ativar o seguimento automático de redirecionamentos
                .build();

        System.out.println("Definindo requisição para " + endereco);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();

        HttpResponse<String> response;
        System.out.println("Tentando obter resposta...");

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Resposta recebida com status: " + response.statusCode());

            // Verificar se a resposta foi bem-sucedida
            if (response.statusCode() != 200) {
                throw new RuntimeException("Falha na requisição: código " + response.statusCode());
            }
        } catch (IOException e) {
            System.out.println("Erro IO: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("Erro de interrupção: " + e.getMessage());
            throw new RuntimeException(e);
        }

        String json = response.body();

        // Verificar se a resposta está vazia
        if (json == null || json.isEmpty()) {
            throw new RuntimeException("Resposta JSON vazia.");
        }

        System.out.println("Json definido.");
        System.out.println(json);
        return json;
    }
}
