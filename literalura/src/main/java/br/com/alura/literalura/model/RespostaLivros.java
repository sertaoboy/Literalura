package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RespostaLivros(
        @JsonProperty("count") Integer count,
        @JsonProperty("next") String next,
        @JsonProperty("previous") String previous,
        @JsonProperty("results") List<DadosLivro> resultados
) {}