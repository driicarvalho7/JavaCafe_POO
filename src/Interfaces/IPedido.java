package Interfaces;

import Models.Produto;

import java.io.IOException;
import java.util.List;

public interface IPedido {
    public void adicionarItem(Produto produto, int quantidade);
    public double calcularTotal();
    public List<Produto> getItens();
    public void finalizarPedido();
    public void salvarPedido(String caminhoArquivo) throws IOException;
}
