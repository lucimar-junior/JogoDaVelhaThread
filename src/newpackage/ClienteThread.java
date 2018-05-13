package newpackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class ClienteThread extends Thread{
    String nomeUsuario;
    JFrame velha = new Velha(false);

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
                        Velha.txtVezJogador.setText(mensagemQuebrada[2]);
                        Velha.txtJogador.setText(mensagemQuebrada[2] + ":");
                        Velha.txtOponente.setText(nome + ":");
                        Velha.txtJogadorVs.setText(mensagemQuebrada[2] + " X " + nome);
                        desabilitaBotoes();
                        new GameClienteThread().start();
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
    
    static public void enviaDados(String msg) {
        Socket cliente;
        try {
            cliente = new Socket("127.0.0.1", 12346);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(msg + ":2");
            saida.flush();
            
            desabilitaBotoes();
        } catch (IOException ex) {
            Logger.getLogger(Velha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static public void desabilitaBotoes(){
        Velha.btn1.setEnabled(false);
        Velha.btn2.setEnabled(false);
        Velha.btn3.setEnabled(false);
        Velha.btn4.setEnabled(false);
        Velha.btn5.setEnabled(false);
        Velha.btn6.setEnabled(false);
        Velha.btn7.setEnabled(false);
        Velha.btn8.setEnabled(false);
        Velha.btn9.setEnabled(false);
    }
}
