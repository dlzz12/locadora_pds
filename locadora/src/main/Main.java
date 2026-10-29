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


//Colocar logo no projeto, nas janelas

//Fazer um usuario para dono, só ele pode cadastrar carro 

//Fazer um gui de como usar o site 