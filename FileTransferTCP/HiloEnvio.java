package FileTransferTCP;

        import java.net.*;
        import java.io.*;

public class HiloEnvio extends Thread
{
    private File archivo;
    private Socket socketCliente = null;
    private FileInputStream fileChannel = null;
//    private BufferedInputStream lectorArchivo = null;
//    private BufferedOutputStream enviar = null;
    DataInputStream input;
    BufferedInputStream bis;
    BufferedOutputStream bos;
    int in;
    byte[] byteArray;


    public HiloEnvio(Socket socketCliente, File archivo){ this.socketCliente = socketCliente; this.archivo = archivo; }

    public void run() {

        try{
            byte [] mybytearray  = new byte [(int)archivo.length()];
            fileChannel = new FileInputStream(archivo);
            bos = new BufferedOutputStream(socketCliente.getOutputStream());
            bis = new BufferedInputStream(fileChannel);
            DataOutputStream dos = new DataOutputStream(socketCliente.getOutputStream());
            dos.writeUTF(archivo.getName());
            System.out.println("Sending " + archivo + "(" + mybytearray.length + " bytes)");
            //Enviamos el fichero
            byteArray = new byte[8192];
            while ((in = bis.read(byteArray)) != -1){
                bos.write(byteArray,0,in);
            }
            System.out.println("Done.");

            //todo
//            lectorArchivo.read(mybytearray,0,mybytearray.length);
//            bos.write(mybytearray,0,mybytearray.length);
//            bos.flush();
        }
        catch (IOException | NullPointerException e) { System.out.println("Interrumpido. ");
            interrupt();}
    }
}