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

public class Cadastro_Usuario {

    private JFrame frame;

    private JTextField textUsuario;
    private JPasswordField textSenha;
    private JPasswordField textConfirmarSenha;

    public Cadastro_Usuario() {
        initialize();
    }

    private void initialize() {

        // =========================================================
        // JANELA
        // =========================================================

        frame = new JFrame();

        frame.setTitle(
            "Locadora de Veículos - Cadastro de Usuário"
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
                "[100px][70px][55px][55px][55px][70px][70px][grow]"
            )
        );

        // =========================================================
        // TÍTULO
        // =========================================================

        JLabel lblTitulo =
            new JLabel("Cadastro de Usuário");

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
        // USUÁRIO
        // =========================================================

        JLabel lblUsuario =
            new JLabel("Email:");

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
        // CONFIRMAR SENHA
        // =========================================================

        JLabel lblConfirmarSenha =
            new JLabel("Confirmar senha:");

        lblConfirmarSenha.setForeground(
            Color.YELLOW
        );

        lblConfirmarSenha.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblConfirmarSenha,
            "cell 1 4,align right"
        );

        textConfirmarSenha =
            new JPasswordField();

        textConfirmarSenha.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        frame.getContentPane().add(
            textConfirmarSenha,
            "cell 2 4 2 1,grow"
        );

        // =========================================================
        // BOTÃO LIMPAR
        // =========================================================

        JButton btnLimpar =
            new JButton("Limpar");

        btnLimpar.setBackground(
            Color.YELLOW
        );

        btnLimpar.setForeground(
            Color.BLACK
        );

        btnLimpar.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        btnLimpar.setFocusPainted(false);

        frame.getContentPane().add(
            btnLimpar,
            "cell 2 5 1 1,grow"
        );

        btnLimpar.addActionListener(
            e -> limparCampos()
        );

        // =========================================================
        // BOTÃO CADASTRAR
        // =========================================================

        JButton btnCadastrar =
            new JButton("Cadastrar");

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
            "cell 3 5 1 1,grow"
        );

        btnCadastrar.addActionListener(
            e -> cadastrarUsuario()
        );

        // =========================================================
        // BOTÃO VOLTAR
        // =========================================================

        JButton btnVoltar =
            new JButton("Voltar para o login");

        btnVoltar.setBackground(
            Color.YELLOW
        );

        btnVoltar.setForeground(
            Color.BLACK
        );

        btnVoltar.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        btnVoltar.setFocusPainted(false);

        frame.getContentPane().add(
            btnVoltar,
            "cell 2 6 2 1,grow"
        );

        btnVoltar.addActionListener(
            e -> voltarParaLogin()
        );

        frame.getRootPane().setDefaultButton(btnCadastrar);

        // =========================================================
        // FOCO INICIAL
        // =========================================================

        textUsuario.requestFocus();
    }

    // =============================================================
    // CADASTRAR USUÁRIO
    // =============================================================

    private void cadastrarUsuario() {

        String usuario =
            textUsuario
                .getText()
                .trim();

        String senha =
            new String(
                textSenha.getPassword()
            );

        String confirmarSenha =
            new String(
                textConfirmarSenha.getPassword()
            );

        // =========================================================
        // VALIDA USUÁRIO
        // =========================================================

        if (usuario.isEmpty()) {

            Dialogo.mostrar(
                frame,
                "Informe o usuário."
            );

            textUsuario.requestFocus();

            return;
        }

        // =========================================================
        // VALIDA SENHA
        // =========================================================

        if (senha.isEmpty()) {

            Dialogo.mostrar(
                frame,
                "Informe a senha."
            );

            textSenha.requestFocus();

            return;
        }

        // =========================================================
        // CONFIRMA SENHA
        // =========================================================

        if (confirmarSenha.isEmpty()) {

            Dialogo.mostrar(
                frame,
                "Confirme a senha."
            );

            textConfirmarSenha.requestFocus();

            return;
        }

        // =========================================================
        // VERIFICA SE AS SENHAS SÃO IGUAIS
        // =========================================================

        if (!senha.equals(confirmarSenha)) {

            Dialogo.mostrar(
                frame,
                "As senhas não são iguais."
            );

            textConfirmarSenha.setText("");

            textConfirmarSenha.requestFocus();

            return;
        }

        // =========================================================
        // CADASTRO
        // =========================================================

        try {

            new UsuarioController().cadastrar(usuario, senha);

        } catch (IllegalArgumentException e) {

            Dialogo.mostrar(
                frame,
                e.getMessage()
            );

            return;

        } catch (SQLException e) {

            Dialogo.mostrar(
                frame,
                "Erro ao acessar o banco de dados:\n"
                + e.getMessage()
            );

            return;
        }

        Dialogo.mostrar(
            frame,
            "Usuário cadastrado com sucesso!\n"
            + "Agora faça o login."
        );

        voltarParaLogin();
    }

    // =============================================================
    // VOLTAR PARA O LOGIN
    // =============================================================

    private void voltarParaLogin() {

        Login tela =
            new Login();

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
    // GET CONFIRMAR SENHA
    // =============================================================

    public String getConfirmarSenha() {

        return new String(
            textConfirmarSenha.getPassword()
        );
    }

    // =============================================================
    // LIMPAR CAMPOS
    // =============================================================

    public void limparCampos() {

        textUsuario.setText("");

        textSenha.setText("");

        textConfirmarSenha.setText("");

        textUsuario.requestFocus();
    }

    // =============================================================
    // MAIN
    // =============================================================

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(
            () -> {

                try {

                    Cadastro_Usuario tela =
                        new Cadastro_Usuario();

                    tela.getFrame().setVisible(true);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        );
    }
}
