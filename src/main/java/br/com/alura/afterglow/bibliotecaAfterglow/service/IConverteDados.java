package br.com.alura.afterglow.bibliotecaAfterglow.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
