import Models.Inventario;
import Utils.GlobalConsts;
import Views.TelaAdmin;
import Views.TelaComprador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class JavaCafeApp extends JFrame {
    private Inventario inventario;

    /**
     * Construtor da classe JavaCafeApp
     * Inicializa o inventário carregando os dados do arquivo especificado.
     * Configura a tela inicial com botões para comprador e dono.
     */
    public JavaCafeApp() {
        // Verifica se é possível ler o arquivo de inventário
        try {
            inventario = Inventario.carregarInventario(GlobalConsts.INVENTARIO_FILE);
        } catch (IOException e) {
            inventario = new Inventario();
        }

        // Configuração da tela inicial
        setTitle("Java Café");
        setSize(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Configuração do painel
        JPanel panel = new JPanel();
        JButton compradorButton = new JButton("Sou Comprador");
        JButton donoButton = new JButton("Sou Dono do JavaCafé");

        // Configuração dos itens no painel
        compradorButton.addActionListener(e -> abrirTelaComprador());
        donoButton.addActionListener(e -> autenticarDono());
        panel.add(compradorButton);
        panel.add(donoButton);
        add(panel, BorderLayout.CENTER);

        // Centralizar o frame na tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 8);

        // Adiciona listener para salvar inventário ao fechar a aplicação
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    inventario.salvarInventario(GlobalConsts.INVENTARIO_FILE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                super.windowClosing(e);
            }
        });

        setVisible(true);
    }

    /**
     * Abre a tela de comprador em uma nova thread.
     */
    private void abrirTelaComprador() {
        SwingUtilities.invokeLater(() -> new TelaComprador(inventario));
    }

    /**
     * Autentica o dono do café solicitando uma senha.
     * Se a senha estiver correta, abre a tela de administrador.
     */
    private void autenticarDono() {
        JPasswordField passwordField = new JPasswordField(10);
        int action = JOptionPane.showConfirmDialog(this, passwordField, "Digite a senha:", JOptionPane.OK_CANCEL_OPTION);
        if (action == JOptionPane.OK_OPTION) {
            String senha = new String(passwordField.getPassword());
            if (senha.equals("123")) {
                abrirTelaAdmin();
            } else {
                JOptionPane.showMessageDialog(this, "Senha incorreta!", "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Abre a tela de administrador em uma nova thread.
     */
    private void abrirTelaAdmin() {
        SwingUtilities.invokeLater(() -> new TelaAdmin(inventario));
    }

    /**
     * Método principal para iniciar a aplicação JavaCafeApp.
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JavaCafeApp::new);
    }
}
