package com.alura.literalura.service; // ou o nome do seu pacote

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}