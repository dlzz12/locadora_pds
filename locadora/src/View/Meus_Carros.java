package View;

import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.LocacaoController;
import net.miginfocom.swing.MigLayout;

public class Meus_Carros {
    private JDialog dialog;

    public Meus_Carros(JFrame telaAnterior, String usuarioLogado) {
        dialog = new JDialog(telaAnterior, "Locadora de Veículos - Meus Carros Locados", true);

        dialog.setIconImage(Logo.icone());

        dialog.setSize(750, 450);

        dialog.setLocationRelativeTo(telaAnterior);

        dialog.getContentPane().setBackground(new Color(0, 0, 160));

        dialog.getContentPane().setLayout(
            new MigLayout("", "[grow][150px]", "[50px][grow][45px]")
        );

        JLabel lblTitulo = new JLabel("Meus Carros Locados");

        lblTitulo.setForeground(Color.YELLOW);

        lblTitulo.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));

        dialog.getContentPane().add(lblTitulo, "cell 0 0 2 1,align center");

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[][] {},
            new String[] { "Marca", "Modelo", "Placa", "Início", "Término" }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(modelo);

        table.setFont(new Font("Arial", Font.PLAIN, 13));

        table.setRowHeight(28);

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        dialog.getContentPane().add(new JScrollPane(table), "cell 0 1 2 1,grow");

        JLabel lblTotal = new JLabel();

        lblTotal.setForeground(Color.YELLOW);

        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));

        dialog.getContentPane().add(lblTotal, "cell 0 2");

        JButton btnFechar = new JButton("Fechar");

        btnFechar.setBackground(Color.YELLOW);

        btnFechar.setForeground(Color.BLACK);

        btnFechar.setFont(new Font("Arial", Font.BOLD, 14));

        btnFechar.setFocusPainted(false);

        dialog.getContentPane().add(btnFechar, "cell 1 2,grow");

        btnFechar.addActionListener(e -> dialog.dispose());

        try {
            List<String[]> carros = LocacaoController.meusCarros(usuarioLogado);

            for (String[] carro : carros) {
                modelo.addRow(carro);
            }

            lblTotal.setText("Total de locações: " + carros.size());
        } catch (SQLException e) {
            Dialogo.mostrar(telaAnterior, "Erro ao acessar o banco de dados:\n" + e.getMessage());
        }
    }

    public JDialog getDialog() {
        return dialog;
    }
}
