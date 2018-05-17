package newpackage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import static newpackage.ClienteFrame.tblUsuarios;

public class AtualizaTabelaThread extends Thread{
    @Override
    public void run(){
        try {
            MulticastSocket ms = new MulticastSocket(12312);
            InetAddress grp = InetAddress.getByName("239.0.0.1");
            ms.joinGroup(grp);
            byte[] resp = new byte[2048];
            while(true){
                ArrayList<Object> object = new ArrayList<>();
                Cliente cliente = new Cliente();
                DatagramPacket pkg = new DatagramPacket(resp, resp.length);
                ms.receive(pkg);
                object = (ArrayList)deserialize(pkg.getData());

                removeLinhas();

                for(int i = 0; i < object.size(); i++){
                    cliente = (Cliente)object.get(i);
                    addClientesTabela(cliente);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    
    private static void addClientesTabela(Cliente cliente){
        if(tblUsuarios.isEnabled() && !cliente.getNome().equalsIgnoreCase(ClienteFrame.nome)){
            DefaultTableModel model = (DefaultTableModel) tblUsuarios.getModel();

            //TODO: Colocar os pontos ao invés do IP
            model.addRow(new Object[]{cliente.getNome(), cliente.getStatus(), cliente.getIp(), cliente.getPorta()});
        }
    }
    
    private static void removeLinhas(){
        DefaultTableModel model = (DefaultTableModel) tblUsuarios.getModel();
        
        int rowCount = model.getRowCount();
        
        //Remove rows one by one from the end of the table
        if(rowCount > 0){
            for (int i = rowCount - 1; i >= 0; i--) {
                model.removeRow(i);
            }
        }
    }
}
