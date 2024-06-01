import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaAdmin extends JFrame {
    private static TelaAdmin instance;
    private Inventario inventario;
    private DefaultTableModel tabelaModel;
    private JTable tabelaEstoque;

    public TelaAdmin(Inventario inventario) {
        this.inventario = inventario;
        instance = this;

        setTitle("Java Café POS - Admin");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tabelaModel = new DefaultTableModel(new Object[]{"Produto", "Preço", "Quantidade", "Excluir"}, 0);
        tabelaEstoque = new JTable(tabelaModel);
        atualizarTabelaEstoque();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabelaEstoque.getColumnCount(); i++) {
            tabelaEstoque.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tabelaEstoque.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tabelaEstoque.rowAtPoint(e.getPoint());
                int column = tabelaEstoque.columnAtPoint(e.getPoint());
                if (column == 3) { // Coluna "Excluir"
                    String nomeProduto = (String) tabelaModel.getValueAt(row, 0);
                    int confirm = JOptionPane.showConfirmDialog(null, "Você realmente deseja excluir o produto " + nomeProduto + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        inventario.removerProduto(nomeProduto);
                        atualizarTabelaEstoque();
                        TelaComprador.atualizarComboProdutos(inventario);
                    }
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tabelaEstoque), BorderLayout.CENTER);

        JButton cadastrarButton = new JButton("Inserir / Atualizar Produto");
        cadastrarButton.addActionListener(e -> abrirTelaCadastro());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(cadastrarButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // Centralizar o frame na tela
        setLocationRelativeTo(null);
        setVisible(true);

        verificarEstoqueBaixo();
    }

    private void abrirTelaCadastro() {
        new TelaCadastroProduto(this, inventario);
    }

    public static TelaAdmin getInstance() {
        return instance;
    }

    public void atualizarTabelaEstoque() {
        tabelaModel.setRowCount(0); // Limpa a tabela
        for (Produto p : inventario.produtos.values()) {
            tabelaModel.addRow(new Object[]{p.getNome(), p.getPreco(), p.getQuantidade(), "EXCLUIR"});
        }
        verificarEstoqueBaixo();
    }

    private void verificarEstoqueBaixo() {
        for (Produto p : inventario.produtos.values()) {
            if (p.getQuantidade() <= 2) {
                JOptionPane.showMessageDialog(this, "O estoque do produto " + p.getNome() + " está baixo. Por favor, reponha o estoque.", "Aviso de Estoque Baixo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
