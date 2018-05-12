package newpackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static newpackage.Servidor.arrayClientes;

public class Game {
    private Socket cliente, clienteOponente;
    
    public boolean jogar(String ip, int porta, String meuNomeUsuario, String ipOponente, int portaOponente, String nomeOponente) {
        try{            
            cliente = new Socket(ip, porta);
            
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            
            saida.writeObject("jogar");
            saida.flush();
            
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(meuNomeUsuario);
            saida.flush();
            
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());            

            String msg = (String) entrada.readObject();
            String[] msgSplit = msg.split(":");

            /*if(msgSplit[0].equalsIgnoreCase("iniciarTela") && msgSplit[1].equalsIgnoreCase(meuNomeUsuario)){
                new Velha().setVisible(true);
            }*/
            
            //cliente.close();
            
            //clienteOponente = new Socket(ipOponente, portaOponente);
            
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(nomeOponente);
            saida.flush();
            
            /*entrada = new ObjectInputStream(clienteOponente.getInputStream());            

            msg = (String) entrada.readObject();
            msgSplit = msg.split(":");

            if(msgSplit[0].equalsIgnoreCase("iniciarTela") && msgSplit[1].equalsIgnoreCase(meuNomeUsuario)){
                return true;
            }*/
            
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível iniciar o jogo: " + ex.getMessage(), "Erro", ERROR_MESSAGE);
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível iniciar o jogo: " + ex.getMessage(), "Erro", ERROR_MESSAGE);
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
