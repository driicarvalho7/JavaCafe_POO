import java.util.HashMap;
import java.util.Map;

public class Inventario {
    Map<String, Produto> produtos;

    public Inventario() {
        produtos = new HashMap<>();
    }

    public void adicionarProduto(Produto produto) {
        produtos.put(produto.getNome(), produto);
    }

    public Produto buscarProduto(String nome) {
        return produtos.get(nome);
    }

    public void removerProduto(String nomeProduto) {
        produtos.remove(nomeProduto);
    }

    public void diminuirQuantidade(String nomeProduto, int quantidade) {
        Produto produto = produtos.get(nomeProduto);
        if (produto != null) {
            produto.setQuantidade(produto.getQuantidade() - quantidade);
        }
    }
}
