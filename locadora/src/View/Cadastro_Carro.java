package View;

import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import controller.CarroController;
import net.miginfocom.swing.MigLayout;

public class Cadastro_Carro {

    private JFrame frame;

    private JFormattedTextField textRenavam;
    private JTextField textMarca;
    private JTextField textModelo;
    private JFormattedTextField textAno;
    private JFormattedTextField textPlaca;

    private JTable table;

    private CarroController controller;

    private String usuarioLogado;

    public Cadastro_Carro(String usuarioLogado) {

        this.usuarioLogado = usuarioLogado;

        initialize();

        controller = new CarroController(this);

        // Carrega os carros ao abrir a tela
        controller.listar();
    }

    private void initialize() {

        // =========================================================
        // JANELA
        // =========================================================

        frame = new JFrame();

        frame.setTitle(
            "Locadora de Veículos - Cadastro de Carro"
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
                "[50px][45px][45px][45px][45px][45px][55px][grow]"
            )
        );

        // =========================================================
        // TÍTULO
        // =========================================================

        JLabel lblTitulo =
            new JLabel("Cadastro de Veículo");

        lblTitulo.setForeground(
            Color.YELLOW
        );

        lblTitulo.setFont(
            new Font(
                "Arial",
                Font.BOLD | Font.ITALIC,
                24
            )
        );

        frame.getContentPane().add(
            lblTitulo,
            "cell 1 0 4 1,align center"
        );

        // =========================================================
        // BOTÃO SAIR (LOGOUT)
        // =========================================================

        JButton btnSair =
            new JButton("Sair (" + usuarioLogado + ")");

        btnSair.setBackground(
            Color.YELLOW
        );

        btnSair.setForeground(
            Color.BLACK
        );

        btnSair.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                12
            )
        );

        btnSair.setFocusPainted(false);

        frame.getContentPane().add(
            btnSair,
            "cell 5 0,align right"
        );

        btnSair.addActionListener(
            e -> sair()
        );

        // =========================================================
        // RENAVAM
        // =========================================================

        JLabel lblRenavam =
            new JLabel("RENAVAM:");

        lblRenavam.setForeground(
            Color.YELLOW
        );

        lblRenavam.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblRenavam,
            "cell 0 1,align right"
        );

        try {

            /*
             * RENAVAM
             *
             * 9 números (a coluna renavam do banco é INT,
             * que aceita no máximo 2.147.483.647)
             *
             * Aparece:
             *
             * _________
             */

            MaskFormatter mascaraRenavam =
                new MaskFormatter("#########");

            mascaraRenavam.setPlaceholderCharacter('_');

            mascaraRenavam.setAllowsInvalid(false);

            textRenavam =
                new JFormattedTextField(
                    mascaraRenavam
                );

            textRenavam.setFont(
                new Font(
                    "Arial",
                    Font.PLAIN,
                    16
                )
            );

        } catch (ParseException e) {

            e.printStackTrace();

            textRenavam =
                new JFormattedTextField();
        }

        frame.getContentPane().add(
            textRenavam,
            "cell 1 1 4 1,grow"
        );

        // =========================================================
        // MARCA
        // =========================================================

        JLabel lblMarca =
            new JLabel("Marca:");

        lblMarca.setForeground(
            Color.YELLOW
        );

        lblMarca.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblMarca,
            "cell 0 2,align right"
        );

        textMarca =
            new JTextField();

        textMarca.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        frame.getContentPane().add(
            textMarca,
            "cell 1 2 4 1,grow"
        );

        // =========================================================
        // MODELO
        // =========================================================

        JLabel lblModelo =
            new JLabel("Modelo:");

        lblModelo.setForeground(
            Color.YELLOW
        );

        lblModelo.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblModelo,
            "cell 0 3,align right"
        );

        textModelo =
            new JTextField();

        textModelo.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        frame.getContentPane().add(
            textModelo,
            "cell 1 3 4 1,grow"
        );

        // =========================================================
        // ANO
        // =========================================================

        JLabel lblAno =
            new JLabel("Ano:");

        lblAno.setForeground(
            Color.YELLOW
        );

        lblAno.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblAno,
            "cell 0 4,align right"
        );

        try {

            /*
             * ANO
             *
             * 4 números
             *
             * Aparece:
             *
             * ____
             */

            MaskFormatter mascaraAno =
                new MaskFormatter("####");

            mascaraAno.setPlaceholderCharacter('_');

            mascaraAno.setAllowsInvalid(false);

            textAno =
                new JFormattedTextField(
                    mascaraAno
                );

            textAno.setFont(
                new Font(
                    "Arial",
                    Font.PLAIN,
                    16
                )
            );

        } catch (ParseException e) {

            e.printStackTrace();

            textAno =
                new JFormattedTextField();
        }

        frame.getContentPane().add(
            textAno,
            "cell 1 4 4 1,grow"
        );

        // =========================================================
        // PLACA
        // =========================================================

        JLabel lblPlaca =
            new JLabel("Placa:");

        lblPlaca.setForeground(
            Color.YELLOW
        );

        lblPlaca.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblPlaca,
            "cell 0 5,align right"
        );

        try {

            /*
             * PLACA
             *
             * Máscara visual:
             *
             * ___-____
             *
             * Exemplo:
             *
             * ABC-1234
             *
             * Os 7 caracteres reais são:
             *
             * ABC1234
             *
             * O hífen é somente visual.
             */

            MaskFormatter mascaraPlaca =
                new MaskFormatter("UUU-####");

            mascaraPlaca.setPlaceholderCharacter('_');

            mascaraPlaca.setAllowsInvalid(false);

            textPlaca =
                new JFormattedTextField(
                    mascaraPlaca
                );

            textPlaca.setFont(
                new Font(
                    "Arial",
                    Font.PLAIN,
                    16
                )
            );

        } catch (ParseException e) {

            e.printStackTrace();

            textPlaca =
                new JFormattedTextField();
        }

        frame.getContentPane().add(
            textPlaca,
            "cell 1 5 4 1,grow"
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
            "cell 1 6 2 1,grow"
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
            "cell 3 6 2 1,grow"
        );

        btnCadastrar.addActionListener(
            e -> controller.cadastrar()
        );

        // =========================================================
        // BOTÃO PESQUISAR
        // =========================================================

        JButton btnPesquisar =
            new JButton("Pesquisar");

        btnPesquisar.setBackground(
            Color.YELLOW
        );

        btnPesquisar.setForeground(
            Color.BLACK
        );

        btnPesquisar.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        btnPesquisar.setFocusPainted(false);

        frame.getContentPane().add(
            btnPesquisar,
            "cell 5 6,grow"
        );

        btnPesquisar.addActionListener(
            e -> abrirPesquisa()
        );

        // =========================================================
        // TABELA
        // =========================================================

        DefaultTableModel modeloTabela =
            new DefaultTableModel(
                new Object[][] {},
                new String[] {
                    "RENAVAM",
                    "Marca",
                    "Modelo",
                    "Ano",
                    "Placa"
                }
            );

        table =
            new JTable(modeloTabela);

        table.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                13
            )
        );

        table.setRowHeight(28);

        table.getTableHeader().setFont(
            new Font(
                "Arial",
                Font.BOLD,
                13
            )
        );

        JScrollPane scrollPane =
            new JScrollPane(table);

        frame.getContentPane().add(
            scrollPane,
            "cell 0 7 6 1,grow"
        );
    }

    // =============================================================
    // GET FRAME
    // =============================================================

    public JFrame getFrame() {
        return frame;
    }

    // =============================================================
    // GET RENAVAM
    // =============================================================

    public String getRenavam() {

        String valor =
            textRenavam.getText();

        /*
         * Remove qualquer caractere que não seja número.
         *
         * Exemplo:
         *
         * 123456789
         */

        return valor
            .replaceAll("[^0-9]", "")
            .trim();
    }

    // =============================================================
    // GET MARCA
    // =============================================================

    public String getMarca() {

        return textMarca
            .getText()
            .trim();
    }

    // =============================================================
    // GET MODELO
    // =============================================================

    public String getModelo() {

        return textModelo
            .getText()
            .trim();
    }

    // =============================================================
    // GET ANO
    // =============================================================

    public String getAno() {

        String valor =
            textAno.getText();

        return valor
            .replaceAll("[^0-9]", "")
            .trim();
    }

    // =============================================================
    // GET PLACA
    // =============================================================

    public String getPlaca() {

        String placa =
            textPlaca.getText();

        /*
         * Remove:
         *
         * -
         * _
         *
         * deixando somente os 7 caracteres.
         */

        return placa
            .replace("-", "")
            .replace("_", "")
            .trim()
            .toUpperCase();
    }

    // =============================================================
    // GET USUÁRIO LOGADO
    // =============================================================

    public String getUsuarioLogado() {
        return usuarioLogado;
    }

    // =============================================================
    // GET TABLE
    // =============================================================

    public JTable getTable() {
        return table;
    }

    // =============================================================
    // LIMPAR CAMPOS
    // =============================================================

    public void limparCampos() {

        textRenavam.setValue(null);

        textMarca.setText("");

        textModelo.setText("");

        textAno.setValue(null);

        textPlaca.setValue(null);

        textRenavam.requestFocus();
    }

    // =============================================================
    // ABRIR PESQUISA DE CARROS
    // =============================================================

    private void abrirPesquisa() {

        Pesquisa_Carro tela =
            new Pesquisa_Carro(usuarioLogado);

        tela.getFrame().setVisible(true);
    }

    // =============================================================
    // SAIR (VOLTA PARA O LOGIN)
    // =============================================================

    private void sair() {

        Login tela =
            new Login();

        tela.getFrame().setVisible(true);

        frame.dispose();
    }

    // =============================================================
    // MOSTRAR MENSAGEM
    // =============================================================

    public void mostrarMensagem(
        String mensagem
    ) {

        Dialogo.mostrar(
            frame,
            mensagem
        );
    }
}
