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
        //PrintWriter salida = null;
        OutputStream sendChannel = null;
        String cadena = ""; String comando = "";

        try{
            servidor = new ServerSocket(PORT);
        }
        catch(IOException e){System.out.println("Error al conectar con el servidor");
        System.exit(-1);}

        /* Se bloquea mientras escucha */
        System.out.println("Servidor escuchando: " + servidor + " " + servidor.getInetAddress());

        cliente = servidor.accept();
        while(!cliente.isClosed()){  //mientras sea distinto a close, sigue escuchando al usuario
            try{
                //Establece canal de entrada
                entrada = new BufferedReader (new InputStreamReader(cliente.getInputStream()));
                //Establece canal envio archivos
                sendChannel = cliente.getOutputStream();
                //lectura entrada usuario
                cadena = entrada.readLine();
            } catch(IOException e ) { System.out.println(e.getMessage()); }

            System.out.println("Buscando archivo solicitado en comando:" + cadena);
            //bucle for quita espacios a la cadena de entrada y lo guarda en comando
            for (int x = 0; x < cadena.length(); x++){
                if(!cadena.substring(x, x+1).equals(" ") && !cadena.substring(x, x+1).equals("\"")){ comando = comando + cadena.substring(x, x+1); }/*final for*/ }
            cadena = comando.substring(0, 3); //reasignamos a la cadena de entrada solo las 3 letras de la orden del usuario

            switch(cadena){  //coge los tres caracteres primeros y compara si es bye, get o otro que en ese caso no seria valido
                case "bye": System.out.println("Finalizada la conexion con el cliente."); cliente.close();  break;

                case "get":  //crea instancia de clase buscar archivo y invoca a la funcion buscador para encontrar el archivo solicitado
                    BuscarArchivo find = new BuscarArchivo();
                    File archivoEncontrado = find.buscador(comando.substring(3, comando.length() ), new File("C:\\"));
                    if(archivoEncontrado != null){   //si es null no encontró el archivo sino sí que lo manda
                        new HiloEnvio (cliente, archivoEncontrado).start();  } else { sendChannel.write(-1); }
                    cadena = ""; comando = "";
                    break;
                default: sendChannel.write(-1); cadena = ""; comando = ""; //envio archivo vacio comando mal introducido
            }

        }

    }
}