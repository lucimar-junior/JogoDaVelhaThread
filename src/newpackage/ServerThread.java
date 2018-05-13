package newpackage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static newpackage.Servidor.arrayClientes;

public class ServerThread extends Thread{
    protected Socket socket;
    private int idController = 0;

    public ServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }
    
    @Override
    public void run() {
        ObjectInputStream entrada;
        
        try{
            while(true){
                entrada = new ObjectInputStream(socket.getInputStream());
                String mensagem = (String) entrada.readObject();
                
                if(mensagem.equalsIgnoreCase("conectar")) {
                    aceitarConexoes();
                }
                
                else if(mensagem.equalsIgnoreCase("desconectar")) {
                    desconectarUsuarios();
                }
                
                else if(mensagem.equalsIgnoreCase("jogar")) {
                    jogar();
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private void aceitarConexoes() throws IOException, ClassNotFoundException{
        Cliente cliente = new Cliente();
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                
        String usuarioConectado = (String) entrada.readObject();
        System.out.println("Usuário " + usuarioConectado + " se conectou ao servidor.");
        
        cliente.setNome(usuarioConectado);
        cliente.setStatus("Disponível");
        cliente.setIp(socket.getInetAddress().getHostAddress());
        cliente.setId(idController);
        cliente.setPorta(socket.getPort());
        
        arrayClientes.add(cliente);
        
        //Log para servidor
        for(Cliente cliente2 : arrayClientes){
            if(cliente2.getStatus().equals("Disponível")){
                System.out.println("Cliente " + cliente2.getNome() + " disponível.");
            }
        }
        
        atualizarUsuarios();
        idController++;
    }
    
    private void desconectarUsuarios() throws IOException, ClassNotFoundException{
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());                     
                
        String usuarioDesconectado = (String) entrada.readObject();
        System.out.println("Usuário " + usuarioDesconectado + " se desconectou do servidor.");
        
        arrayClientes.removeIf(cliente -> cliente.getNome().equals(usuarioDesconectado));

        ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
        saida.writeObject("desconectado");
        saida.flush();
        atualizarUsuarios();
        socket.close();
    }
    
    private void atualizarUsuarios(){        
        InetAddress ip;
        
        try {
            byte[] msg = serialize(arrayClientes);
            ip = InetAddress.getByName("239.0.0.1");
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket pkg = new DatagramPacket(msg, msg.length, ip, 12312);
            ds.send(pkg);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    public byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    private void jogar() throws IOException, ClassNotFoundException {
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                
        String usuario = (String) entrada.readObject();
        
        int i = 0;
        
        for(Cliente cliente : arrayClientes){
            if(cliente.getNome().equals(usuario)){
                cliente.setStatus("Jogando");
                arrayClientes.set(i, cliente);
                
                //Enviar mensagem para executar a tela
                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                saida.writeObject("iniciarTela:" + usuario + ":1");
                saida.flush();
            }
            
            i++;
        }
        
        entrada = new ObjectInputStream(socket.getInputStream());
                
        String usuarioOponente = (String) entrada.readObject();
        String mensagem = "iniciarTela:" + usuarioOponente + ":" + usuario;
        
        i = 0;
        
        InetAddress ip = InetAddress.getByName("239.0.0.2");
        byte[] msg = mensagem.getBytes();
        DatagramSocket ds = new DatagramSocket();
        DatagramPacket pkg = new DatagramPacket(msg, msg.length, ip, 12312);
        ds.send(pkg);
            
        for(Cliente cliente : arrayClientes){
            if(cliente.getNome().equals(usuarioOponente)){
                cliente.setStatus("Jogando");
                arrayClientes.set(i, cliente);
                
                //Enviar mensagem para executar a tela
                /*ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                saida.writeObject("iniciarTela:" + usuarioOponente);
                saida.flush();*/
            }
            
            i++;
        }
        
        atualizarUsuarios();
        new GameThread(usuario, usuarioOponente).start();
    }
}
