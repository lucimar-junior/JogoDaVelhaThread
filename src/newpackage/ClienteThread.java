package newpackage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteThread extends Thread{
    @Override
    public void run() {
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
                
                /*if(mensagemQuebrada[0].equals("iniciarTela") && mensagemQuebrada[1].equals(ClienteFrame.txtNomeUsuario.getText())){
                    new Velha().setVisible(true);
                }*/
                
                if(mensagemQuebrada[0].equals("iniciarTela")){
                    new Velha().setVisible(true);
                }
                
                System.out.println("Txt: " + ClienteFrame.txtNomeUsuario.getText());
                System.out.println("Msg Quebrada: " + mensagemQuebrada[1]);
                System.out.println(ClienteFrame.txtNomeUsuario.getText().contains(mensagemQuebrada[1]));
            }   
        } catch (IOException ex) {
            Logger.getLogger(ClienteThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
