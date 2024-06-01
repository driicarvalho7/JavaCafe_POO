import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaCadastroProduto extends JFrame {
    private TelaAdmin telaAdmin;
    private Inventario inventario;

    public TelaCadastroProduto(TelaAdmin telaAdmin, Inventario inventario) {
        this.telaAdmin = telaAdmin;
        this.inventario = inventario;

        setTitle("Inserir / Atualizar Produto");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel nomeLabel = new JLabel("Nome do Produto:");
        JTextField nomeField = new JTextField();
        JLabel precoLabel = new JLabel("Preço do Produto:");
        JTextField precoField = new JTextField();
        JLabel quantidadeLabel = new JLabel("Quantidade:");
        JTextField quantidadeField = new JTextField();

        JButton cadastrarButton = new JButton("Cadastrar/Atualizar Produto");

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                double preco;
                int quantidade;

                try {
                    preco = Double.parseDouble(precoField.getText());
                    quantidade = Integer.parseInt(quantidadeField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TelaCadastroProduto.this, "Por favor, insira valores válidos. O preço deve ser um número com ponto (.) e a quantidade deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Produto produto = new Produto(nome, preco, quantidade);
                inventario.adicionarProduto(produto);
                JOptionPane.showMessageDialog(TelaCadastroProduto.this, "Produto cadastrado/atualizado com sucesso!");

                telaAdmin.atualizarTabelaEstoque();
                TelaComprador.atualizarComboProdutos(inventario);
            }
        });

        panel.add(nomeLabel);
        panel.add(nomeField);
        panel.add(precoLabel);
        panel.add(precoField);
        panel.add(quantidadeLabel);
        panel.add(quantidadeField);
        panel.add(new JLabel()); // Placeholder
        panel.add(cadastrarButton);

        add(panel, BorderLayout.CENTER);

        // Centralizar o frame na tela
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
