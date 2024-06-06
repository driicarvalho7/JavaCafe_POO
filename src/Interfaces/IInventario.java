package Interfaces;

import Models.Produto;

import java.io.IOException;

public interface IInventario {
    public void adicionarProduto(Produto produto);
    public Produto buscarProduto(String nome);
    public void removerProduto(String nomeProduto);
    public void salvarInventario(String caminhoArquivo) throws IOException;

}
