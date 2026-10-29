package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import controller.PesquisaCarroController;
import net.miginfocom.swing.MigLayout;

public class Pesquisa_Carro {

    private JFrame frame;

    private JTextField textPesquisa;

    private JLabel lblTotal;

    private JTable table;

    private PesquisaCarroController controller;

    private String usuarioLogado;

    public Pesquisa_Carro(String usuarioLogado) {

        this.usuarioLogado = usuarioLogado;

        initialize();

        controller = new PesquisaCarroController(this);

        // Mostra todos os carros ao abrir a tela
        controller.pesquisar();
    }

    private void initialize() {

        // =========================================================
        // JANELA
        // =========================================================

        frame = new JFrame();

        frame.setTitle(
            "Locadora de Veículos - Pesquisa de Carros"
        );

        frame.setSize(900, 700);

        frame.setLocationRelativeTo(null);

        // Fecha só esta janela, sem encerrar o sistema
        frame.setDefaultCloseOperation(
            JFrame.DISPOSE_ON_CLOSE
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
                "[50px][45px][30px][grow][45px]"
            )
        );

        // =========================================================
        // TÍTULO
        // =========================================================

        JLabel lblTitulo =
            new JLabel("Pesquisa de Veículos");

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
        // CAMPO DE PESQUISA
        // =========================================================

        JLabel lblPesquisa =
            new JLabel("Pesquisar:");

        lblPesquisa.setForeground(
            Color.YELLOW
        );

        lblPesquisa.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblPesquisa,
            "cell 0 1,align right"
        );

        textPesquisa =
            new JTextField();

        textPesquisa.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        textPesquisa.setToolTipText(
            "Digite RENAVAM, marca, modelo, ano, placa ou usuário"
        );

        frame.getContentPane().add(
            textPesquisa,
            "cell 1 1 4 1,grow"
        );

        // Pesquisa automaticamente enquanto o usuário digita
        textPesquisa.getDocument().addDocumentListener(
            new DocumentListener() {

                public void insertUpdate(DocumentEvent e) {
                    controller.pesquisar();
                }

                public void removeUpdate(DocumentEvent e) {
                    controller.pesquisar();
                }

                public void changedUpdate(DocumentEvent e) {
                    controller.pesquisar();
                }
            }
        );

        // =========================================================
        // BOTÃO LIMPAR PESQUISA
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
            "cell 5 1,grow"
        );

        btnLimpar.addActionListener(
            e -> {
                textPesquisa.setText("");
                textPesquisa.requestFocus();
            }
        );

        // =========================================================
        // DICA
        // =========================================================

        JLabel lblDica =
            new JLabel(
                "Pesquise por RENAVAM, marca, modelo, ano, placa ou usuário. "
                + "Clique em um carro para locá-lo."
            );

        lblDica.setForeground(
            Color.WHITE
        );

        lblDica.setFont(
            new Font(
                "Arial",
                Font.ITALIC,
                12
            )
        );

        frame.getContentPane().add(
            lblDica,
            "cell 1 2 4 1"
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
                    "Placa",
                    "Cadastrado por"
                }
            ) {

                // Tabela somente para consulta
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

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

        // Clicar em um carro oferece a opção de locar
        table.addMouseListener(
            new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {

                    int linha = table.rowAtPoint(e.getPoint());

                    if (linha >= 0) {
                        perguntarLocacao(linha);
                    }
                }
            }
        );

        JScrollPane scrollPane =
            new JScrollPane(table);

        frame.getContentPane().add(
            scrollPane,
            "cell 0 3 6 1,grow"
        );

        // =========================================================
        // TOTAL ENCONTRADO
        // =========================================================

        lblTotal =
            new JLabel();

        lblTotal.setForeground(
            Color.YELLOW
        );

        lblTotal.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        frame.getContentPane().add(
            lblTotal,
            "cell 0 4 4 1"
        );

        // =========================================================
        // BOTÃO FECHAR
        // =========================================================

        JButton btnFechar =
            new JButton("Voltar");

        btnFechar.setBackground(
            Color.YELLOW
        );

        btnFechar.setForeground(
            Color.BLACK
        );

        btnFechar.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
        );

        btnFechar.setFocusPainted(false);

        frame.getContentPane().add(
            btnFechar,
            "cell 4 4 2 1,grow"
        );

        btnFechar.addActionListener(
            e -> frame.dispose()
        );
    }

    // =============================================================
    // PERGUNTAR SE DESEJA LOCAR O CARRO CLICADO
    // =============================================================

    private void perguntarLocacao(int linha) {

        Object dono =
            table.getValueAt(linha, 5);

        // Ninguém pode locar o próprio carro
        if (dono != null && dono.toString().equalsIgnoreCase(usuarioLogado)) {

            Dialogo.mostrar(
                frame,
                "Você não pode locar um carro que você mesmo cadastrou."
            );

            return;
        }

        int renavam =
            (Integer) table.getValueAt(linha, 0);

        String descricao =
            table.getValueAt(linha, 1) + " "
            + table.getValueAt(linha, 2)
            + " - Placa " + table.getValueAt(linha, 4);

        boolean locar = Dialogo.confirmar(
            frame,
            descricao + "\n\nDeseja locar este carro?",
            "Locar carro"
        );

        if (locar) {

            Locacao_Carro tela =
                new Locacao_Carro(
                    frame,
                    renavam,
                    descricao,
                    usuarioLogado
                );

            tela.getDialog().setVisible(true);

            // Atualiza a lista: o carro locado deixa de aparecer
            controller.pesquisar();
        }
    }

    // =============================================================
    // GET FRAME
    // =============================================================

    public JFrame getFrame() {
        return frame;
    }

    // =============================================================
    // GET TERMO PESQUISADO
    // =============================================================

    public String getTermo() {

        return textPesquisa
            .getText()
            .trim();
    }

    // =============================================================
    // GET TABLE
    // =============================================================

    public JTable getTable() {
        return table;
    }

    // =============================================================
    // MOSTRAR TOTAL
    // =============================================================

    public void mostrarTotal(int total) {

        lblTotal.setText(
            total == 1
                ? "1 carro encontrado"
                : total + " carros encontrados"
        );
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
