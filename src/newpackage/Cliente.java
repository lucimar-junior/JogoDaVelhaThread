package newpackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Cliente implements Serializable{   
    public Socket cliente;
    private ClienteFrame clienteFrame;
    private Servidor servidor;
    private String nome, status, ip;
    private int id, porta;
    
    //private boolean isConnected = false;
        
    /*public static void main(String[] args) {
        String mensagem;
        
        try {
            MulticastSocket ms = new MulticastSocket(12312);
            InetAddress grp = InetAddress.getByName("239.0.0.1");
            ms.joinGroup(grp);
            byte[] resp = new byte[256];
            while(true){
                DatagramPacket pkg = new DatagramPacket(resp, resp.length);
                ms.receive(pkg);
                mensagem = "";
                mensagem = new String(pkg.getData());
                System.out.println("Servidor falou: " + mensagem);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
        
    public boolean conectar(String ip, int porta, String nomeUsuario) {
        try {            
            nome = nomeUsuario;
            status = "Disponível";
            cliente = new Socket(ip, porta);
            
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject("conectar");
            saida.flush();
            
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(nome);
            saida.flush();
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao servidor: " + ex.getMessage(), "Erro", ERROR_MESSAGE);
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean desconectar(String ip, int porta, String nomeUsuario){
        try {
            cliente = new Socket(ip, porta);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject("desconectar");
            saida.flush();
            
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(nome);
            saida.flush();
            
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());            
                
            String msg = (String) entrada.readObject();
            
            if(msg.equalsIgnoreCase("desconectado")){
                cliente.close();
                return true;
            }
            
            return false;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível desconectar: " + ex.getMessage(), "Erro", ERROR_MESSAGE);
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível desconectar: " + ex.getMessage(), "Erro", ERROR_MESSAGE);
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    } 

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }
    
    
}
