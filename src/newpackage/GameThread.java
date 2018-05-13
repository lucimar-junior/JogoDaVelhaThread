package newpackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread{
    private String jogador1;
    private String jogador2;

    public GameThread(String jogador1, String jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
    }

    public String getJogador1() {
        return jogador1;
    }

    public void setJogador1(String jogador1) {
        this.jogador1 = jogador1;
    }

    public String getJogador2() {
        return jogador2;
    }

    public void setJogador2(String jogador2) {
        this.jogador2 = jogador2;
    }
    
    @Override
    public void run(){
        try {
            ServerSocket socketGame = new ServerSocket(12346);
            Socket socketCliente;
            while(true){
                socketCliente = socketGame.accept();
                ObjectInputStream entrada = new ObjectInputStream(socketCliente.getInputStream());
                String mensagem = (String) entrada.readObject();
                //String[] mensagemQuebrada = mensagem.split(":");
                /*String letra = mensagemQuebrada[0];
                String botao = mensagemQuebrada[1];*/
                
                InetAddress ip = InetAddress.getByName("239.0.0.3");
                byte[] msg = mensagem.getBytes();
                DatagramSocket ds = new DatagramSocket();
                DatagramPacket pkg = new DatagramPacket(msg, msg.length, ip, 12312);
                ds.send(pkg);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
