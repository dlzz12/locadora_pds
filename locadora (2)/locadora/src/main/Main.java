package main;

import java.awt.EventQueue;
import View.Login;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Login window = new Login();
            window.getFrame().setVisible(true);
        });
    }
}
