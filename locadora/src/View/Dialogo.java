package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * Janelas de mensagem e de confirmação com as cores do sistema
 * (fundo azul, texto e botões amarelos) e botões em português.
 */
public class Dialogo {

    private static final Color AZUL = new Color(0, 0, 160);

    // =============================================================
    // MENSAGEM (BOTÃO OK)
    // =============================================================

    public static void mostrar(Component telaAnterior, String mensagem) {

        exibir(
            telaAnterior,
            mensagem,
            "Locadora de Veículos",
            JOptionPane.INFORMATION_MESSAGE,
            new String[] { "OK" }
        );
    }

    // =============================================================
    // CONFIRMAÇÃO (BOTÕES SIM / NÃO)
    // =============================================================

    // Retorna true somente se o usuário clicar em "Sim"
    public static boolean confirmar(Component telaAnterior, String mensagem, String titulo) {

        Object escolha = exibir(
            telaAnterior,
            mensagem,
            titulo,
            JOptionPane.QUESTION_MESSAGE,
            new String[] { "Sim", "Não" }
        );

        return "Sim".equals(escolha);
    }

    // =============================================================
    // MONTA E EXIBE A JANELA
    // =============================================================

    private static Object exibir(
        Component telaAnterior,
        String mensagem,
        String titulo,
        int tipo,
        String[] botoes
    ) {

        JOptionPane painel = new JOptionPane(
            mensagem,
            tipo,
            JOptionPane.DEFAULT_OPTION,
            null,
            botoes,
            botoes[0]
        );

        aplicarCores(painel);

        JDialog janela = painel.createDialog(telaAnterior, titulo);

        janela.getContentPane().setBackground(AZUL);

        janela.setVisible(true);

        janela.dispose();

        return painel.getValue();
    }

    // Pinta o painel e tudo que existe dentro dele
    private static void aplicarCores(Container container) {

        if (container instanceof JOptionPane || container instanceof JPanel) {

            container.setBackground(AZUL);

            ((JComponent) container).setOpaque(true);
        }

        for (Component componente : container.getComponents()) {

            if (componente instanceof JButton) {

                JButton botao = (JButton) componente;

                botao.setBackground(Color.YELLOW);
                botao.setForeground(Color.BLACK);
                botao.setFont(new Font("Arial", Font.BOLD, 14));
                botao.setFocusPainted(false);

            } else if (componente instanceof JLabel) {

                componente.setForeground(Color.YELLOW);
                componente.setFont(new Font("Arial", Font.BOLD, 14));
            }

            if (componente instanceof Container) {
                aplicarCores((Container) componente);
            }
        }
    }
}
