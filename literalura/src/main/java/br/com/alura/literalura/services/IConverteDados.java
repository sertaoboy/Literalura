package br.com.alura.literalura.services;

public interface IConverteDados {
    <T> T  obterDados(String json, Class<T> classe);
}
