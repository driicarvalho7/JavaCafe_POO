package Views;

import Models.Inventario;
import Models.Produto;
import Utils.GlobalConsts;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TelaAdmin extends JFrame {
    private static TelaAdmin instance;
    private Inventario inventario;
    private DefaultTableModel tabelaModel;
    private JTable tabelaEstoque;

    /**
     * Construtor da classe TelaAdmin
     * @param inventario O inventário do Java Café
     */
    public TelaAdmin(Inventario inventario) {
        this.inventario = inventario;
        instance = this;

        // Configuração da tela inicial
        setTitle("Java Café - Admin");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Criar Headers da tabela de estoque
        tabelaModel = new DefaultTableModel(new Object[]{"Produto", "Preço", "Quantidade", "Excluir"}, 0);
        tabelaEstoque = new JTable(tabelaModel);
        atualizarTabelaEstoque();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabelaEstoque.getColumnCount(); i++) {
            tabelaEstoque.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Criar Linhas da tabela de estoque
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

        // Configuração do painel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tabelaEstoque), BorderLayout.CENTER);

        // Configuração dos itens no painel
        JButton cadastrarButton = new JButton("Inserir / Atualizar Produto");
        cadastrarButton.addActionListener(e -> abrirTelaCadastro());
        JButton relatorioButton = new JButton("Exibir Relatório de Vendas");
        relatorioButton.addActionListener(e -> exibirRelatorioVendas());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(cadastrarButton, BorderLayout.EAST);
        topPanel.add(relatorioButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // Centralizar o frame na tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width - getWidth(), screenSize.height - getHeight() - 50);
        setVisible(true);

        // A cada atualização, verifica os estoques para exibir o alerta
        verificarEstoqueBaixo();
    }

    /**
     * Abre a tela de cadastro de produtos.
     */
    private void abrirTelaCadastro() {
        new TelaCadastroProduto(this, inventario);
    }

    /**
     * Exibe o relatório de vendas em uma nova janela.
     */
    private void exibirRelatorioVendas() {
        JFrame relatorioFrame = new JFrame("Relatório de Vendas");
        relatorioFrame.setSize(300, 400);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        try (BufferedReader reader = new BufferedReader(new FileReader(GlobalConsts.PEDIDOS_FILE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                textArea.append(linha + "\n");
            }
        } catch (IOException e) {
            textArea.append("Erro ao carregar o relatório de vendas: " + e.getMessage());
        }

        relatorioFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        relatorioFrame.setVisible(true);
        relatorioFrame.setLocationRelativeTo(this);
    }

    /**
     * Obtém a instância única de TelaAdmin.
     * @return A instância de TelaAdmin
     */
    public static TelaAdmin getInstance() {
        return instance;
    }

    /**
     * Atualiza a tabela de estoque com os dados do inventário.
     */
    public void atualizarTabelaEstoque() {
        tabelaModel.setRowCount(0); // Limpa a tabela
        for (Produto p : inventario.getProdutos().values()) {
            tabelaModel.addRow(new Object[]{p.getNome(), p.getPreco(), p.getQuantidade(), "EXCLUIR"});
        }
        verificarEstoqueBaixo();
    }

    /**
     * Verifica se algum produto no inventário está com o estoque baixo e exibe um aviso.
     */
    private void verificarEstoqueBaixo() {
        for (Produto p : inventario.getProdutos().values()) {
            if (p.getQuantidade() <= 2) {
                JOptionPane.showMessageDialog(this, "O estoque do produto " + p.getNome() + " está baixo. Por favor, reponha o estoque.", "Aviso de Estoque Baixo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
