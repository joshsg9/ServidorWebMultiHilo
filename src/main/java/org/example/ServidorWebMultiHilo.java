package org.example;
import java.net.*;

public class ServidorWebMultiHilo {

    public static void main(String[] args) throws Exception {

        ServerSocket socketdeEscucha = new ServerSocket(6789);
        System.out.println("Servidor escuchando en el puerto 6789");

        while (true) {
            Socket socketdeConexion = socketdeEscucha.accept();
            new HiloCliente(socketdeConexion).start();
        }
    }
}
