package newpackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static newpackage.Velha.btn1;
import static newpackage.Velha.btn2;
import static newpackage.Velha.btn3;
import static newpackage.Velha.btn4;
import static newpackage.Velha.btn5;
import static newpackage.Velha.btn6;
import static newpackage.Velha.btn7;
import static newpackage.Velha.btn8;
import static newpackage.Velha.btn9;

public class GameClienteThread extends Thread{

    @Override
    public void run(){
        recebeDados();
    }
    
    private void recebeDados() {
        while(true){
            try{
                MulticastSocket ms = new MulticastSocket(12312);
                InetAddress grp = InetAddress.getByName("239.0.0.3");
                ms.joinGroup(grp);
                byte[] resp = new byte[256];
                DatagramPacket pkgResposta = new DatagramPacket(resp, resp.length);
                ms.receive(pkgResposta);
                String mensagemRecebida = new String(pkgResposta.getData());
                String[] mensagemQuebrada = mensagemRecebida.split(":");
                String letra = mensagemQuebrada[0];
                String botao = mensagemQuebrada[1];

                preencheBotao(letra, botao);
                habilitarBotoes();
                
                Velha.trocaLabelVez();
            }
            catch (IOException ex) {
                Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void preencheBotao(String letra, String botao){
        switch(botao){
            case "1":
                btn1.setText(letra);
                break;
            
            case "2":
                btn2.setText(letra);
                break;
                
            case "3":
                btn3.setText(letra);
                break;
                
            case "4":
                btn4.setText(letra);
                break;
                
            case "5":
                btn5.setText(letra);
                break;
                
            case "6":
                btn6.setText(letra);
                break;
                
            case "7":
                btn7.setText(letra);
                break;
                
            case "8":
                btn8.setText(letra);
                break;
                
            default:
                btn9.setText(letra);
        }
    }
    
    private void habilitarBotoes(){
        Velha.btn1.setEnabled(true);
        Velha.btn2.setEnabled(true);
        Velha.btn3.setEnabled(true);
        Velha.btn4.setEnabled(true);
        Velha.btn5.setEnabled(true);
        Velha.btn6.setEnabled(true);
        Velha.btn7.setEnabled(true);
        Velha.btn8.setEnabled(true);
        Velha.btn9.setEnabled(true);
    }
}
