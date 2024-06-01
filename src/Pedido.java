import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private List<Produto> itens;
    private double total;

    public Pedido() {
        this.itens = new ArrayList<>();
        this.total = 0.0;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (produto.getQuantidade() >= quantidade) {
            produto.setQuantidade(produto.getQuantidade() - quantidade);
            for (int i = 0; i < quantidade; i++) {
                itens.add(produto);
            }
            total += produto.getPreco() * quantidade;
        } else {
            JOptionPane.showMessageDialog(null, "Desculpe... Atualmente possuimos somente " + produto.getQuantidade() + " unidades \n no estoque para o produto " + produto.getNome(), "Estoque Insuficiente", JOptionPane.ERROR_MESSAGE);
        }
    }

    public double calcularTotal() {
        return total;
    }

    public List<Produto> getItens() {
        return itens;
    }

    public void finalizarPedido() {
        System.out.println("Pedido finalizado. Total: R$ " + calcularTotal());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido:\n");
        for (Produto p : itens) {
            sb.append(p.getNome()).append(" - R$ ").append(p.getPreco()).append("\n");
        }
        return sb.toString();
    }
}
