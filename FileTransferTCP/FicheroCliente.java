package FileTransferTCP;

import java.io.*;
import java.net.*;

public class FicheroCliente
{
    public static void main(String[] args) throws IOException {

        BufferedOutputStream bos = null;
        Socket socketCliente = null;
        Socket connection = null;
        BufferedInputStream bis = null;
        int in;
        String file;
        ServerSocket server;
        DataOutputStream output;
        byte[] receivedData;
        PrintWriter salida = null;

        String hostName = InetAddress.getLocalHost().getHostName();

        try{ socketCliente = new Socket(hostName, 4444);
            System.out.println("servidor conectado:" + hostName);

            salida = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socketCliente.getOutputStream())),true);
        }
        catch(IOException e){
            System.err.println("No puede establecer conexion");
            System.exit(-1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String linea = "";

        while(!linea.equalsIgnoreCase("bye")){

            do{
                System.out.println("Introduzca comando válido:");
                //Leo la entrada del usuario
                linea = stdIn.readLine();
            }
            while (!linea.matches("[a-z][a-z][a-z] \".*\"") && !linea.equalsIgnoreCase("bye"));
            //La envia al servidor
            salida.println(linea);



            try{
                server = new ServerSocket( 5000 );
                while ( true ) {
                    connection = server.accept();
                    receivedData = new byte[1024];
                    bis = new BufferedInputStream(connection.getInputStream());
                    DataInputStream dis=new DataInputStream(connection.getInputStream());

                    //Recibimos el nombre del fichero
                    file = dis.readUTF();
                    file = file.substring(file.indexOf('\\')+1);
                    //Para guardar fichero recibido
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    while ((in = bis.read(receivedData)) != -1){
                        bos.write(receivedData,0,in);
                    }
                    bos.close();
                    dis.close();
                }
            }

            catch(IOException e){ System.out.println("Error en la transmisión.");
                if (bis != null) bis.close();
                if (bos != null) bos.close();
                if (socketCliente != null) socketCliente.close(); }
        }

        if (bis != null) bis.close();
        if (bos != null) bos.close();
        if (socketCliente != null) socketCliente.close();

    }
}