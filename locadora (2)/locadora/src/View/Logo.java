package View;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Logo {
    public static Image icone() {
        return new ImageIcon(Logo.class.getResource("icone.png")).getImage();
    }

    public static JLabel imagem(int largura, int altura) {
        Image img = new ImageIcon(Logo.class.getResource("logo.png")).getImage();

        Image menor = img.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(menor));
    }
}
