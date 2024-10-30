package br.com.alura.literalura.main;

import br.com.alura.literalura.services.ConsumoApi;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private final String ENDERECO = "https://gutendex.com/books?search=";
    private ConsumoApi consumo = new ConsumoApi();
    private Scanner leitura = new Scanner(System.in);


    public void obterDados() {
        System.out.println("Informe o livro desejado para busca:");
        String livroInserido = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + livroInserido.replace(" ","+"));
    }
}
