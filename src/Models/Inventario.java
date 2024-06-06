package Models;

import Interfaces.IInventario;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Inventario implements IInventario, Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Produto> produtos;

    public Inventario() {
        produtos = new HashMap<>();
    }

    @Override
    public void adicionarProduto(Produto produto) {
        produtos.put(produto.getNome(), produto);
    }

    public Produto buscarProduto(String nome) {
        return produtos.get(nome);
    }

    public void removerProduto(String nomeProduto) {
        produtos.remove(nomeProduto);
    }

    public void salvarInventario(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (Produto produto : produtos.values()) {
                writer.write(produto.getNome() + "," + produto.getPreco() + "," + produto.getQuantidade());
                writer.newLine();
            }
        }
    }

    public static Inventario carregarInventario(String caminhoArquivo) throws IOException {
        Inventario inventario = new Inventario();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                String nome = partes[0];
                double preco = Double.parseDouble(partes[1]);
                int quantidade = Integer.parseInt(partes[2]);
                inventario.adicionarProduto(new Produto(nome, preco, quantidade));
            }
        }
        return inventario;
    }

    // Getter para o campo produtos
    public Map<String, Produto> getProdutos() {
        return produtos;
    }
}
