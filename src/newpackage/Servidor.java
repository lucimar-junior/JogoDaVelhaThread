package newpackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    static ArrayList<Cliente> arrayClientes = new ArrayList();
    static Socket socketCliente;
    
    public static void main(String[] args) {
        try {
            System.out.println("Servidor iniciou.");
            ServerSocket servidor = new ServerSocket(12345);
            
            while(true){
                socketCliente = servidor.accept();
                new ServerThread(socketCliente).start();
            }

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
