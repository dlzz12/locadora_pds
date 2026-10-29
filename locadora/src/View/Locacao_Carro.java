package View;

import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.MaskFormatter;

import controller.LocacaoController;
import net.miginfocom.swing.MigLayout;

public class Locacao_Carro {

    private JDialog dialog;

    private JFormattedTextField textDataInicio;
    private JFormattedTextField textDataTermino;

    private int renavam;
    private String descricaoCarro;
    private String usuarioLogado;

    private LocacaoController controller;

    public Locacao_Carro(
        JFrame telaAnterior,
        int renavam,
        String descricaoCarro,
        String usuarioLogado
    ) {

        this.renavam = renavam;
        this.descricaoCarro = descricaoCarro;
        this.usuarioLogado = usuarioLogado;

        initialize(telaAnterior);

        controller = new LocacaoController(this);
    }

    private void initialize(JFrame telaAnterior) {

        // =========================================================
        // JANELA
        // =========================================================

        // Modal: a tela de pesquisa fica bloqueada até fechar esta
        dialog = new JDialog(
            telaAnterior,
            "Locadora de Veículos - Locar Carro",
            true
        );

        dialog.setSize(700, 340);

        dialog.setLocationRelativeTo(telaAnterior);

        dialog.setDefaultCloseOperation(
            JDialog.DISPOSE_ON_CLOSE
        );

        dialog.getContentPane().setBackground(
            new Color(0, 0, 160)
        );

        // =========================================================
        // LAYOUT
        // =========================================================

        dialog.getContentPane().setLayout(
            new MigLayout(
                "",
                "[150px][grow][grow][110px]",
                "[50px][35px][45px][45px][55px]"
            )
        );

        // =========================================================
        // TÍTULO
        // =========================================================

        JLabel lblTitulo =
            new JLabel("Locação de Veículo");

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

        dialog.getContentPane().add(
            lblTitulo,
            "cell 0 0 4 1,align center"
        );

        // =========================================================
        // CARRO ESCOLHIDO
        // =========================================================

        JLabel lblCarro =
            new JLabel("Carro: " + descricaoCarro);

        lblCarro.setForeground(
            Color.WHITE
        );

        lblCarro.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                15
            )
        );

        dialog.getContentPane().add(
            lblCarro,
            "cell 0 1 4 1,align center"
        );

        // =========================================================
        // DATA DE INÍCIO
        // =========================================================

        JLabel lblDataInicio =
            new JLabel("Data de início:");

        lblDataInicio.setForeground(
            Color.YELLOW
        );

        lblDataInicio.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        dialog.getContentPane().add(
            lblDataInicio,
            "cell 0 2,align right"
        );

        textDataInicio =
            criarCampoData();

        // Começa preenchida com a data de hoje
        textDataInicio.setValue(
            LocacaoController.formatar(LocalDate.now())
        );

        dialog.getContentPane().add(
            textDataInicio,
            "cell 1 2 2 1,grow"
        );

        // =========================================================
        // DATA DE TÉRMINO
        // =========================================================

        JLabel lblDataTermino =
            new JLabel("Data de término:");

        lblDataTermino.setForeground(
            Color.YELLOW
        );

        lblDataTermino.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        dialog.getContentPane().add(
            lblDataTermino,
            "cell 0 3,align right"
        );

        textDataTermino =
            criarCampoData();

        dialog.getContentPane().add(
            textDataTermino,
            "cell 1 3 2 1,grow"
        );

        // =========================================================
        // BOTÃO CANCELAR
        // =========================================================

        JButton btnCancelar =
            new JButton("Cancelar");

        btnCancelar.setBackground(
            Color.YELLOW
        );

        btnCancelar.setForeground(
            Color.BLACK
        );

        btnCancelar.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        btnCancelar.setFocusPainted(false);

        dialog.getContentPane().add(
            btnCancelar,
            "cell 1 4,grow"
        );

        btnCancelar.addActionListener(
            e -> dialog.dispose()
        );

        // =========================================================
        // BOTÃO LOCAR
        // =========================================================

        JButton btnLocar =
            new JButton("Locar");

        btnLocar.setBackground(
            Color.YELLOW
        );

        btnLocar.setForeground(
            Color.BLACK
        );

        btnLocar.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        btnLocar.setFocusPainted(false);

        dialog.getContentPane().add(
            btnLocar,
            "cell 2 4,grow"
        );

        btnLocar.addActionListener(
            e -> {
                if (controller.locar()) {
                    dialog.dispose();
                }
            }
        );

        dialog.getRootPane().setDefaultButton(btnLocar);
    }

    // =============================================================
    // CAMPO DE DATA COM MÁSCARA __/__/____
    // =============================================================

    private JFormattedTextField criarCampoData() {

        JFormattedTextField campo;

        try {

            MaskFormatter mascaraData =
                new MaskFormatter("##/##/####");

            mascaraData.setPlaceholderCharacter('_');

            campo =
                new JFormattedTextField(
                    mascaraData
                );

        } catch (ParseException e) {

            e.printStackTrace();

            campo =
                new JFormattedTextField();
        }

        campo.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        return campo;
    }

    // =============================================================
    // GET DIALOG
    // =============================================================

    public JDialog getDialog() {
        return dialog;
    }

    // =============================================================
    // GETTERS DOS DADOS
    // =============================================================

    public String getDataInicio() {
        return textDataInicio.getText();
    }

    public String getDataTermino() {
        return textDataTermino.getText();
    }

    public int getRenavam() {
        return renavam;
    }

    public String getUsuarioLogado() {
        return usuarioLogado;
    }

    // =============================================================
    // MOSTRAR MENSAGEM
    // =============================================================

    public void mostrarMensagem(
        String mensagem
    ) {

        Dialogo.mostrar(
            dialog,
            mensagem
        );
    }
}
