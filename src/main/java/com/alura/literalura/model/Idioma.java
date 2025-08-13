package com.alura.literalura.model;

public enum Idioma {
    ES("es"),
    EN("en"),
    FR("fr"),
    PT("pt");

    private String codigo;

    Idioma(String codigo) {
        this.codigo = codigo;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.codigo.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Nenhum idioma encontrado para a string fornecida: " + text);
    }
}