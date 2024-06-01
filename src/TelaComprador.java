import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class TelaComprador extends JFrame {
    private static Pedido pedidoAtual;
    private static JTable tabelaPedido;
    private static DefaultTableModel tabelaModel;
    private static JLabel totalLabel;
    private static JComboBox<String> comboProdutos;

    public TelaComprador(Inventario inventario) {
        pedidoAtual = new Pedido();

        setTitle("Java Café POS - Comprador");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        comboProdutos = new JComboBox<>();
        atualizarComboProdutos(inventario);

        if (comboProdutos.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Ainda estamos passando o seu café... Aguarde alguns instantes até nossos produtos ficarem prontos :)", "Sem Produtos", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Fecha a tela de comprador
            return;
        }

        JTextField quantidadeField = new JTextField(5);
        JButton adicionarButton = new JButton("Adicionar ao Pedido");
        JButton finalizarButton = new JButton("Finalizar Pedido");

        tabelaModel = new DefaultTableModel(new Object[]{"ITEM", "QUANTIDADE", "VALOR"}, 0);
        tabelaPedido = new JTable(tabelaModel);
        tabelaPedido.setEnabled(false); // Para desabilitar a edição direta na tabela

        adicionarButton.addActionListener(e -> {
            String nomeProduto = (String) comboProdutos.getSelectedItem();
            int quantidade = Integer.parseInt(quantidadeField.getText());
            Produto produto = inventario.buscarProduto(nomeProduto);
            if (produto != null) {
                pedidoAtual.adicionarItem(produto, quantidade);
                atualizarAreaPedido();
                TelaAdmin telaAdmin = TelaAdmin.getInstance();
                if (telaAdmin != null) {
                    telaAdmin.atualizarTabelaEstoque(); // Atualiza o estoque na visão do administrador
                }
            }
        });

        finalizarButton.addActionListener(e -> {
            double total = pedidoAtual.calcularTotal();
            JOptionPane.showMessageDialog(TelaComprador.this, "Sucesso! Valor total da compra: R$ " + String.format("%.2f", total));
            pedidoAtual.finalizarPedido();
            pedidoAtual = new Pedido();  // Reinicia o pedido
            atualizarAreaPedido();
        });

        topPanel.add(comboProdutos);
        topPanel.add(quantidadeField);
        topPanel.add(adicionarButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: R$ 0.00");
        totalLabel.setFont(new Font("Serif", Font.BOLD, 16)); // Aumentar a fonte do texto "Total"
        bottomPanel.add(totalLabel, BorderLayout.WEST); // Canto inferior esquerdo
        bottomPanel.add(finalizarButton, BorderLayout.EAST); // Canto inferior direito

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(tabelaPedido), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Centralizar o frame na tela
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void atualizarComboProdutos(Inventario inventario) {
        if(comboProdutos != null){
            comboProdutos.removeAllItems();
            for (Produto p : inventario.produtos.values()) {
                comboProdutos.addItem(p.getNome());
            }
        }
    }

    private void atualizarAreaPedido() {
        tabelaModel.setRowCount(0); // Limpa a tabela

        // Map para agrupar itens
        Map<String, Integer> quantidadeMap = new HashMap<>();
        Map<String, Double> valorMap = new HashMap<>();

        for (Produto p : pedidoAtual.getItens()) {
            quantidadeMap.put(p.getNome(), quantidadeMap.getOrDefault(p.getNome(), 0) + 1);
            valorMap.put(p.getNome(), valorMap.getOrDefault(p.getNome(), 0.0) + p.getPreco());
        }

        for (String nome : quantidadeMap.keySet()) {
            tabelaModel.addRow(new Object[]{
                    nome, quantidadeMap.get(nome), String.format("R$ %.2f", valorMap.get(nome))
            });
        }

        totalLabel.setText("Total: R$ " + String.format("%.2f", pedidoAtual.calcularTotal()));
    }
}
