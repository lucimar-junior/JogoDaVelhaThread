package newpackage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class ClienteThread extends Thread{
    String nomeUsuario;
    JFrame velha = new Velha();

    public ClienteThread(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    
    @Override
    public void run() {
        velha.setVisible(false);
        
        try {
            MulticastSocket ms = new MulticastSocket(12312);
            InetAddress grp = InetAddress.getByName("239.0.0.2");
            ms.joinGroup(grp);
            byte[] resp = new byte[256];
            while(true){
                DatagramPacket pkg = new DatagramPacket(resp, resp.length);
                ms.receive(pkg);
                String mensagem = new String(pkg.getData());
                String[] mensagemQuebrada = mensagem.split(":");
                String nome = mensagemQuebrada[1];
                
                if(mensagemQuebrada[0].equals("iniciarTela") && nomeUsuario.equals(nome)){
                    if(!velha.isVisible()){
                        velha.setVisible(true);
                        Velha.txtJogador.setText(nome + ":");
                        Velha.txtOponente.setText(mensagemQuebrada[2] + ":");
                        Velha.txtJogadorVs.setText(nome + " X " + mensagemQuebrada[2]);
                    }
                }
                
                System.out.println("Txt: " + nomeUsuario);
                System.out.println("Msg Quebrada: " + nome);
                System.out.println(nomeUsuario.equals(nome));
            }   
        } catch (IOException ex) {
            Logger.getLogger(ClienteThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
