package FileTransferTCP;

import java.io.*;

public class BuscarArchivo
{
    File archivoEncontrado = null;

    public BuscarArchivo() { }

    public File buscador(String nombre, File raiz){

        File[] lista = raiz.listFiles();

        if(lista != null) {
            for(File elemento : lista) {
                if (elemento.isDirectory())  {
                    buscador(nombre, elemento);
                }
                else if (nombre.equalsIgnoreCase(elemento.getName())) {
                 archivoEncontrado = elemento; System.out.println("Archivo encontrado.");
                }
            }
        }
        return archivoEncontrado;
    }
}