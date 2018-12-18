package FileTransferTCP;

import java.io.*;
import java.net.*;

public class FicheroServidor
{
    public static final int PORT = 4444;

    public static void main (String args[]) throws IOException {
        ServerSocket servidor = null;
        Socket cliente;
        BufferedReader entrada = null;
        OutputStream sendChannel = null;
        String cadena = ""; String comando = "";

        try{
            servidor = new ServerSocket(PORT);
        }
        catch(IOException e){System.out.println("Error al conectar con el servidor");
        System.exit(-1);}

        System.out.println("Servidor escuchando: " + servidor + " " + servidor.getInetAddress());

        cliente = servidor.accept();
        while(!cliente.isClosed()){
            try{

                entrada = new BufferedReader (new InputStreamReader(cliente.getInputStream()));

                sendChannel = cliente.getOutputStream();

                cadena = entrada.readLine();
            }
            catch(IOException e ) {
                System.out.println(e.getMessage());
            }

            System.out.println("Buscando archivo solicitado en comando:" + cadena);
            for (int x = 0; x < cadena.length(); x++){
                if(!cadena.substring(x, x+1).equals(" ") && !cadena.substring(x, x+1).equals("\"")){ comando = comando + cadena.substring(x, x+1); }/*final for*/ }
            cadena = comando.substring(0, 3);

            switch(cadena){
                case "bye": System.out.println("Finalizada la conexion con el cliente."); cliente.close();  break;

                case "get":
                    BuscarArchivo find = new BuscarArchivo();
                    File archivoEncontrado = find.buscador(comando.substring(3), new File("C:\\"));
                    if(archivoEncontrado != null){
                        BufferedInputStream bis;
                        BufferedOutputStream bos;
                        int in;
                        byte[] byteArray;

                        try{
                            Socket client = new Socket("localhost", 5000);
                            bis = new BufferedInputStream(new FileInputStream(archivoEncontrado));
                            bos = new BufferedOutputStream(client.getOutputStream());
                            //Enviamos el nombre del fichero
                            DataOutputStream dos=new DataOutputStream(client.getOutputStream());
                            dos.writeUTF(archivoEncontrado.getName());
                            //Enviamos el fichero
                            byteArray = new byte[8192];
                            while ((in = bis.read(byteArray)) != -1){
                                bos.write(byteArray,0,in);
                            }

                            bis.close();
                            bos.close();

                        }catch ( Exception e ) {
                            System.err.println(e);
                        }                    }
                    else {
                        sendChannel.write(-1);
                    }
                    cadena = ""; comando = "";
                    break;
                default: sendChannel.write(-1); cadena = ""; comando = "";
            }

        }

    }
}