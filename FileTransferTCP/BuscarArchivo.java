package FileTransferTCP;

import java.io.*;

public class BuscarArchivo
{
    File archivoEncontrado = null; //si retorna null es que no lo ha encontrado

    public BuscarArchivo() { }

    public File buscador(String nombre, File raiz){

        File[] lista = raiz.listFiles();

        if(lista != null) {
            for(File elemento : lista) {
                if (elemento.isDirectory())  { //si es directorio vuelve a llamarse a si misma
                    buscador(nombre, elemento);
                }
                else if (nombre.equalsIgnoreCase(elemento.getName())) { //sino, si es igual encontrado
                 archivoEncontrado = elemento; System.out.println("Archivo encontrado.");
                }
            }
        }
        return archivoEncontrado;
    } //cierre buscador
}