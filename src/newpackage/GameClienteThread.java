package newpackage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static newpackage.Velha.btn1;
import static newpackage.Velha.btn2;
import static newpackage.Velha.btn3;
import static newpackage.Velha.btn4;
import static newpackage.Velha.btn5;
import static newpackage.Velha.btn6;
import static newpackage.Velha.btn7;
import static newpackage.Velha.btn8;
import static newpackage.Velha.btn9;
import static newpackage.Velha.txtJogador;
import static newpackage.Velha.txtOponente;
import static newpackage.Velha.txtVezJogador;

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
                checarVelha();
                Velha.trocaLabelVez();
                
                if(Velha.minhaVez){
                    habilitarBotoes();
                }               
            
                else if(!Velha.minhaVez){
                    desabilitaBotoes();
                }
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
                Velha.board[0][0] = letra;
                break;
            
            case "2":
                btn2.setText(letra);
                Velha.board[0][1] = letra;
                break;
                
            case "3":
                btn3.setText(letra);
                Velha.board[0][2] = letra;
                break;
                
            case "4":
                btn4.setText(letra);
                Velha.board[1][0] = letra;
                break;
                
            case "5":
                btn5.setText(letra);
                Velha.board[1][1] = letra;
                break;
                
            case "6":
                btn6.setText(letra);
                Velha.board[1][2] = letra;
                break;
                
            case "7":
                btn7.setText(letra);
                Velha.board[2][0] = letra;
                break;
                
            case "8":
                btn8.setText(letra);
                Velha.board[2][1] = letra;
                break;
                
            default:
                btn9.setText(letra);
                Velha.board[2][2] = letra;
        }
    }
    
    private void habilitarBotoes(){
        if(btn1.getText().equals("")){
            btn1.setEnabled(true);
        }
        
        if(btn2.getText().equals("")){
            btn2.setEnabled(true);
        }
        
        if(btn3.getText().equals("")){
            btn3.setEnabled(true);
        }
        
        if(btn4.getText().equals("")){
            btn4.setEnabled(true);
        }
        
        if(btn5.getText().equals("")){
            btn5.setEnabled(true);
        }
        
        if(btn6.getText().equals("")){
            btn6.setEnabled(true);
        }
        
        if(btn7.getText().equals("")){
            btn7.setEnabled(true);
        }
        
        if(btn8.getText().equals("")){
            btn8.setEnabled(true);
        }
        
        if(btn9.getText().equals("")){
            btn9.setEnabled(true);
        }
    }
    
    static public void desabilitaBotoes(){
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);
        btn5.setEnabled(false);
        btn6.setEnabled(false);
        btn7.setEnabled(false);
        btn8.setEnabled(false);
        btn9.setEnabled(false);
    }
    
    // Returns true if there is a win, false otherwise.
    // This calls our other win check functions to check the entire board.
    static private void checarVelha(){
        boolean check = (checkRowsForWin() || checkColumnsForWin() || checkDiagonalsForWin());
        
        if(check){
            if(Velha.minhaVez){
                JOptionPane.showMessageDialog(null, "Parabéns, você venceu!!!", "Fim de jogo", INFORMATION_MESSAGE);
            }
            
            else{
                JOptionPane.showMessageDialog(null, "Que pena, " + txtVezJogador.getText() + " venceu.", "Fim de jogo", INFORMATION_MESSAGE);
            }
        }
        
        else if(isBoardFull()){
            JOptionPane.showMessageDialog(null, "Ninguém venceu!!!", "Fim de jogo", INFORMATION_MESSAGE);
        }
    }	
    
    static private boolean isBoardFull(){
        boolean isFull = true;	         

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Velha.board[i][j].equals("-")) {
                    isFull = false;
                }
            }
        }         

        return isFull;
    }
    
    // Loop through rows and see if any are winners.
    static private boolean checkRowsForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(Velha.board[i][0], Velha.board[i][1], Velha.board[i][2]) == true) {
                return true;
            }
        }
        return false;
    }	
	
    // Loop through columns and see if any are winners.
    static private boolean checkColumnsForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(Velha.board[0][i], Velha.board[1][i], Velha.board[2][i]) == true) {
                return true;
            }
        }
        return false;
    }	
	
    // Check the two diagonals to see if either is a win. Return true if either wins.
    static private boolean checkDiagonalsForWin() {
        return ((checkRowCol(Velha.board[0][0], Velha.board[1][1], Velha.board[2][2]) == true) || (checkRowCol(Velha.board[0][2], Velha.board[1][1], Velha.board[2][0]) == true));
    }	
	
    // Check to see if all three values are the same (and not empty) indicating a win.
    static private boolean checkRowCol(String c1, String c2, String c3) {
        return ((!c1.equals("-")) && (c1.equals(c2)) && (c2.equals(c3)));
    }
}
