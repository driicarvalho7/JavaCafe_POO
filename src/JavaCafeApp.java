import javax.swing.*;
import java.awt.*;
public class JavaCafeApp extends JFrame {
    private Inventario inventario;

    public JavaCafeApp() {
        inventario = new Inventario();

        // Configuração da tela inicial
        setTitle("Java Café POS");
        setSize(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        JButton compradorButton = new JButton("Sou Comprador");
        JButton donoButton = new JButton("Sou Dono do JavaCafé");

        compradorButton.addActionListener(e -> abrirTelaComprador());
        donoButton.addActionListener(e -> autenticarDono());

        panel.add(compradorButton);
        panel.add(donoButton);

        add(panel, BorderLayout.CENTER);

        // Centralizar o frame na tela
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void abrirTelaComprador() {
        SwingUtilities.invokeLater(() -> new TelaComprador(inventario));
    }

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

    private void abrirTelaAdmin() {
        SwingUtilities.invokeLater(() -> new TelaAdmin(inventario));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JavaCafeApp::new);
    }
}
