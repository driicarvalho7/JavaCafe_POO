package Models;

import Interfaces.IPedido;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Pedido implements IPedido, Serializable {
    private static final long serialVersionUID = 1L;
    private List<Produto> itens;
    private double total;

    public Pedido() {
        this.itens = new ArrayList<>();
        this.total = 0.0;
    }

    @Override
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

    @Override
    public double calcularTotal() {
        return total;
    }

    @Override
    public List<Produto> getItens() {
        return itens;
    }

    @Override
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

    @Override
    public void salvarPedido(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            writer.write("Data do Pedido: " + sdf.format(new Date()));
            writer.newLine();
            writer.newLine();

            // Agrupar itens por nome para evitar duplicatas
            Map<String, Integer> quantidadeMap = new HashMap<>();
            Map<String, Double> precoMap = new HashMap<>();

            for (Produto p : itens) {
                quantidadeMap.put(p.getNome(), quantidadeMap.getOrDefault(p.getNome(), 0) + 1);
                precoMap.put(p.getNome(), p.getPreco()); // O preço unitário é o mesmo para produtos do mesmo nome
            }

            for (String nome : quantidadeMap.keySet()) {
                writer.write(String.format("Produto: " + nome + " | Preço Unitário: " + precoMap.get(nome) + " | Quantidade: " + quantidadeMap.get(nome)));
                writer.newLine();
            }

            writer.newLine();
            writer.write("________________________________________");
            writer.newLine();
            writer.write("Total: R$ " + String.format("%.2f", total));
            writer.newLine();
            writer.newLine();
            writer.write("########################################");
            writer.newLine();
            writer.newLine();
        }
    }
}
