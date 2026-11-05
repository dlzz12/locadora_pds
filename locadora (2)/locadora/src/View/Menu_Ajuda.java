package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import controller.LocacaoController;
import net.miginfocom.swing.MigLayout;

public class Menu_Ajuda {
    private static final Color AZUL = new Color(0, 0, 160);

    private static final String VERSAO = "1.0";

    private static final String TEXTO_AJUDA = """
        <html>
        <body style="font-family: Arial; font-size: 12px; color: white;">

        <h2 style="color: yellow;">Entrar no sistema</h2>
        <ul>
          <li>Informe o usuário e a senha e clique em <b>Entrar</b>.</li>
          <li>Se ainda não tem acesso, clique em <b>Cadastrar novo usuário</b>.</li>
          <li>Depois do login, o operador vai para o cadastro de veículos
              e o cliente vai para a pesquisa de veículos.</li>
        </ul>

        <h2 style="color: yellow;">Cadastrar usuário</h2>
        <ul>
          <li>Preencha o email, a senha e a confirmação da senha.</li>
          <li>A senha deve ter pelo menos 4 caracteres.</li>
        </ul>

        <h2 style="color: yellow;">Cadastrar veículo (operador)</h2>
        <ul>
          <li>Preencha RENAVAM, marca, modelo, ano, placa e preço por dia, e clique em <b>Cadastrar</b>.</li>
          <li>O preço por dia aceita valores como 150 ou 150,00.</li>
          <li>A placa usa o formato ABC-1234 e não pode repetir a de outro carro.</li>
          <li>A tabela mostra os carros cadastrados por você. Os carros locados ficam em laranja.</li>
          <li>Clique em um carro locado para ver a data de locação, a data de término e o preço.</li>
          <li>O botão <b>Pesquisar</b> abre a pesquisa de veículos.</li>
        </ul>

        <h2 style="color: yellow;">Pesquisar veículos</h2>
        <ul>
          <li>Digite no campo <b>Pesquisar</b>: a lista é filtrada enquanto você digita.</li>
          <li>A busca considera RENAVAM, marca, modelo, ano, placa e usuário.</li>
          <li>Só aparecem os carros disponíveis para locação.</li>
        </ul>

        <h2 style="color: yellow;">Locar um carro</h2>
        <ul>
          <li>Na pesquisa, clique no carro desejado e confirme.</li>
          <li>Informe as datas de início e de término no formato dd/mm/aaaa.</li>
          <li>O início não pode ser anterior a hoje, e o término não pode ser anterior ao início.</li>
          <li>O valor total é o preço por dia multiplicado pelos dias do período, contando o dia de início e o de término.</li>
          <li>Ao concluir, uma mensagem confirma a locação com o valor total e o endereço de retirada.</li>
          <li>Não é possível locar um carro que você mesmo cadastrou.</li>
          <li>O botão <b>Meus carros locados</b> mostra as suas locações.</li>
        </ul>

        </body>
        </html>
        """;

    private static final String TEXTO_SOBRE = """
        <html>
        <div style="text-align: center;">
        Sistema para cadastro, pesquisa e locação de veículos.<br><br>
        Desenvolvido em Java com Swing, MigLayout e MySQL.<br><br>
        Endereço: %s
        </div>
        </html>
        """.formatted(LocacaoController.ENDERECO_LOCADORA);

    public static void instalar(JFrame frame) {
        JMenuBar barra = new JMenuBar();

        barra.setBackground(Color.YELLOW);
        barra.setBorder(BorderFactory.createEmptyBorder());

        JMenu menuAjuda = criarMenu("Ajuda", KeyEvent.VK_A);

        JMenuItem itemComoUsar = criarItem("Como usar o sistema");

        itemComoUsar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

        itemComoUsar.addActionListener(e -> mostrarAjuda(frame));

        menuAjuda.add(itemComoUsar);

        JMenu menuSobre = criarMenu("Sobre", KeyEvent.VK_S);

        JMenuItem itemSobre = criarItem("Sobre o sistema");

        itemSobre.addActionListener(e -> mostrarSobre(frame));

        menuSobre.add(itemSobre);

        barra.add(menuAjuda);
        barra.add(menuSobre);

        frame.setJMenuBar(barra);
    }

    private static JMenu criarMenu(String texto, int atalho) {
        JMenu menu = new JMenu(texto);

        menu.setForeground(Color.BLACK);
        menu.setFont(new Font("Arial", Font.BOLD, 14));
        menu.setMnemonic(atalho);

        return menu;
    }

    private static JMenuItem criarItem(String texto) {
        JMenuItem item = new JMenuItem(texto);

        item.setFont(new Font("Arial", Font.PLAIN, 14));

        return item;
    }

    private static void mostrarAjuda(JFrame frame) {
        JDialog dialog = criarJanela(frame, "Locadora de Veículos - Ajuda", 650, 550);

        dialog.getContentPane().setLayout(
            new MigLayout("", "[grow][150px]", "[50px][grow][45px]")
        );

        dialog.getContentPane().add(criarTitulo("Como usar o sistema"), "cell 0 0 2 1,align center");

        JEditorPane texto = new JEditorPane("text/html", TEXTO_AJUDA);

        texto.setEditable(false);
        texto.setBackground(AZUL);
        texto.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(texto);

        scrollPane.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

        dialog.getContentPane().add(scrollPane, "cell 0 1 2 1,grow");

        dialog.getContentPane().add(criarBotaoFechar(dialog), "cell 1 2,grow");

        dialog.setVisible(true);
    }

    private static void mostrarSobre(JFrame frame) {
        JDialog dialog = criarJanela(frame, "Locadora de Veículos - Sobre", 520, 500);

        dialog.getContentPane().setLayout(
            new MigLayout("", "[grow][150px][grow]", "[190px][40px][30px][grow][45px]")
        );

        dialog.getContentPane().add(Logo.imagem(240, 175), "cell 0 0 3 1,align center");

        dialog.getContentPane().add(criarTitulo("Locadora de Veículos"), "cell 0 1 3 1,align center");

        JLabel lblVersao = new JLabel("Versão " + VERSAO);

        lblVersao.setForeground(Color.WHITE);
        lblVersao.setFont(new Font("Arial", Font.BOLD, 14));

        dialog.getContentPane().add(lblVersao, "cell 0 2 3 1,align center");

        JLabel lblDescricao = new JLabel(TEXTO_SOBRE, SwingConstants.CENTER);

        lblDescricao.setForeground(Color.YELLOW);
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 14));

        dialog.getContentPane().add(lblDescricao, "cell 0 3 3 1,align center");

        dialog.getContentPane().add(criarBotaoFechar(dialog), "cell 1 4,grow");

        dialog.setVisible(true);
    }

    private static JDialog criarJanela(JFrame frame, String titulo, int largura, int altura) {
        JDialog dialog = new JDialog(frame, titulo, true);

        dialog.setIconImage(Logo.icone());
        dialog.setSize(largura, altura);
        dialog.setLocationRelativeTo(frame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().setBackground(AZUL);

        dialog.getRootPane().registerKeyboardAction(
            e -> dialog.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        return dialog;
    }

    private static JLabel criarTitulo(String texto) {
        JLabel titulo = new JLabel(texto);

        titulo.setForeground(Color.YELLOW);
        titulo.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));

        return titulo;
    }

    private static JButton criarBotaoFechar(JDialog dialog) {
        JButton botao = new JButton("Fechar");

        botao.setBackground(Color.YELLOW);
        botao.setForeground(Color.BLACK);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setFocusPainted(false);

        botao.addActionListener(e -> dialog.dispose());

        dialog.getRootPane().setDefaultButton(botao);

        return botao;
    }
}
