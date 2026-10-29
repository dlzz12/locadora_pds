package View;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.sql.SQLException;

import controller.UsuarioController;
import net.miginfocom.swing.MigLayout;

public class Login {

    private JFrame frame;

    private JTextField textUsuario;
    private JPasswordField textSenha;

    public Login() {
        initialize();
    }

    private void initialize() {

        // =========================================================
        // JANELA
        // =========================================================

        frame = new JFrame();

        frame.setTitle(
            "Locadora de Veículos - Login"
        );

        frame.setSize(900, 700);

        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE
        );

        frame.getContentPane().setBackground(
            new Color(0, 0, 160)
        );

        // =========================================================
        // LAYOUT
        // =========================================================

        frame.getContentPane().setLayout(
            new MigLayout(
                "",
                "[110px][grow][grow][grow][110px][110px]",
                "[100px][70px][55px][55px][70px][70px][grow]"
            )
        );

        // =========================================================
        // TÍTULO
        // =========================================================

        JLabel lblTitulo =
            new JLabel("Locadora de Veículos");

        lblTitulo.setForeground(
            Color.YELLOW
        );

        lblTitulo.setFont(
            new Font(
                "Arial",
                Font.BOLD | Font.ITALIC,
                28
            )
        );

        frame.getContentPane().add(
            lblTitulo,
            "cell 1 0 4 1,align center"
        );

        // =========================================================
        // SUBTÍTULO
        // =========================================================

        JLabel lblLogin =
            new JLabel("Login");

        lblLogin.setForeground(
            Color.YELLOW
        );

        lblLogin.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                22
            )
        );

        frame.getContentPane().add(
            lblLogin,
            "cell 1 1 4 1,align center"
        );

        // =========================================================
        // USUÁRIO
        // =========================================================

        JLabel lblUsuario =
            new JLabel("Usuário:");

        lblUsuario.setForeground(
            Color.YELLOW
        );

        lblUsuario.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblUsuario,
            "cell 1 2,align right"
        );

        textUsuario =
            new JTextField();

        textUsuario.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        frame.getContentPane().add(
            textUsuario,
            "cell 2 2 2 1,grow"
        );

        // =========================================================
        // SENHA
        // =========================================================

        JLabel lblSenha =
            new JLabel("Senha:");

        lblSenha.setForeground(
            Color.YELLOW
        );

        lblSenha.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblSenha,
            "cell 1 3,align right"
        );

        textSenha =
            new JPasswordField();

        textSenha.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        frame.getContentPane().add(
            textSenha,
            "cell 2 3 2 1,grow"
        );

        // =========================================================
        // BOTÃO ENTRAR
        // =========================================================

        JButton btnEntrar =
            new JButton("Entrar");

        btnEntrar.setBackground(
            Color.YELLOW
        );

        btnEntrar.setForeground(
            Color.BLACK
        );

        btnEntrar.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        btnEntrar.setFocusPainted(false);

        frame.getContentPane().add(
            btnEntrar,
            "cell 2 4 2 1,grow"
        );

        btnEntrar.addActionListener(
            e -> realizarLogin()
        );

        // =========================================================
        // BOTÃO CADASTRAR NOVO USUÁRIO
        // =========================================================

        JButton btnCadastrar =
            new JButton("Cadastrar novo usuário");

        btnCadastrar.setBackground(
            Color.YELLOW
        );

        btnCadastrar.setForeground(
            Color.BLACK
        );

        btnCadastrar.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        btnCadastrar.setFocusPainted(false);

        frame.getContentPane().add(
            btnCadastrar,
            "cell 2 5 2 1,grow"
        );

        btnCadastrar.addActionListener(
            e -> cadastrarNovoUsuario()
        );

        // Tecla Enter aciona o botão Entrar
        frame.getRootPane().setDefaultButton(btnEntrar);

        // =========================================================
        // FOCO INICIAL
        // =========================================================

        textUsuario.requestFocus();
    }

    // =============================================================
    // REALIZAR LOGIN
    // =============================================================

    private void realizarLogin() {

        String usuario =
            textUsuario.getText().trim();

        String senha =
            new String(
                textSenha.getPassword()
            );

        if (usuario.isEmpty()) {

            Dialogo.mostrar(
                frame,
                "Informe o usuário."
            );

            textUsuario.requestFocus();

            return;
        }

        if (senha.isEmpty()) {

            Dialogo.mostrar(
                frame,
                "Informe a senha."
            );

            textSenha.requestFocus();

            return;
        }

        try {

            UsuarioController controller =
                new UsuarioController();

            // Usuário ainda não cadastrado: pede para se cadastrar
            if (!controller.usuarioExiste(usuario)) {

                boolean cadastrar = Dialogo.confirmar(
                    frame,
                    "Usuário não encontrado.\n"
                    + "Você precisa se cadastrar para acessar o sistema.\n"
                    + "Deseja se cadastrar agora?",
                    "Login"
                );

                if (cadastrar) {
                    cadastrarNovoUsuario();
                }

                return;
            }

            if (!controller.autenticar(usuario, senha)) {

                Dialogo.mostrar(
                    frame,
                    "Senha incorreta."
                );

                textSenha.setText("");

                textSenha.requestFocus();

                return;
            }

            // Login correto: abre o sistema
            Cadastro_Carro tela =
                new Cadastro_Carro(usuario);

            tela.getFrame().setVisible(true);

            frame.dispose();

        } catch (SQLException e) {

            Dialogo.mostrar(
                frame,
                "Erro ao acessar o banco de dados:\n"
                + e.getMessage()
            );
        }
    }

    // =============================================================
    // CADASTRAR NOVO USUÁRIO
    // =============================================================

    private void cadastrarNovoUsuario() {

        Cadastro_Usuario tela =
            new Cadastro_Usuario();

        tela.getFrame().setVisible(true);

        frame.dispose();
    }

    // =============================================================
    // GET FRAME
    // =============================================================

    public JFrame getFrame() {
        return frame;
    }

    // =============================================================
    // GET USUÁRIO
    // =============================================================

    public String getUsuario() {
        return textUsuario
            .getText()
            .trim();
    }

    // =============================================================
    // GET SENHA
    // =============================================================

    public String getSenha() {
        return new String(
            textSenha.getPassword()
        );
    }

    // =============================================================
    // LIMPAR CAMPOS
    // =============================================================

    public void limparCampos() {

        textUsuario.setText("");

        textSenha.setText("");

        textUsuario.requestFocus();
    }

    // =============================================================
    // MAIN
    // =============================================================

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(
            () -> {

                try {

                    Login tela =
                        new Login();

                    tela.getFrame().setVisible(true);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        );
    }
}