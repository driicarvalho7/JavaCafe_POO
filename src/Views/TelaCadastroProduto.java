package Views;

import Models.Inventario;
import Models.Produto;
import Utils.GlobalConsts;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TelaCadastroProduto extends JFrame {
    private TelaAdmin telaAdmin;
    private Inventario inventario;

    /**
     * Construtor da classe TelaCadastroProduto
     * @param inventario O inventário do Java Café
     * @param telaAdmin A tela de Administrador
     */
    public TelaCadastroProduto(TelaAdmin telaAdmin, Inventario inventario) {
        this.telaAdmin = telaAdmin;
        this.inventario = inventario;

        // Configuração da tela inicial
        setTitle("Inserir / Atualizar Produto");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Configuração do painel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        // Configuração dos itens no painel
        JLabel nomeLabel = new JLabel("Nome do Produto:");
        JTextField nomeField = new JTextField();
        JLabel precoLabel = new JLabel("Preço do Produto:");
        JTextField precoField = new JTextField();
        JLabel quantidadeLabel = new JLabel("Quantidade:");
        JTextField quantidadeField = new JTextField();
        JButton cadastrarButton = new JButton("Cadastrar/Atualizar Produto");

        // Escuta as ações executadas no botoão de cadastrar/atualizar
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

                try {
                    inventario.salvarInventario(GlobalConsts.INVENTARIO_FILE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                telaAdmin.atualizarTabelaEstoque();
                TelaComprador.atualizarComboProdutos(inventario);
            }
        });

        // Adiciona os itens no painel
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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        setVisible(true);
    }
}
