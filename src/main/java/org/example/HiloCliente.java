package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class HiloCliente extends Thread {

    private Socket socketdeConexion;

    public HiloCliente(Socket socket) {
        this.socketdeConexion = socket;
    }

    public void run() {
        try {
            BufferedReader mensajeDesdeCliente =
                    new BufferedReader(
                            new InputStreamReader(socketdeConexion.getInputStream())
                    );

            String lineaDeLaSolicitudHttp =
                    mensajeDesdeCliente.readLine();

            if (lineaDeLaSolicitudHttp == null || lineaDeLaSolicitudHttp.isEmpty()) {
                socketdeConexion.close();
                return;
            }

            StringTokenizer lineaSeparada =
                    new StringTokenizer(lineaDeLaSolicitudHttp);

            if (lineaSeparada.nextToken().equals("GET")) {

                String nombreArchivo = lineaSeparada.nextToken();
                if (nombreArchivo.startsWith("/"))
                    nombreArchivo = nombreArchivo.substring(1);

                File archivo = new File(nombreArchivo);

                DataOutputStream mensajeParaCliente =
                        new DataOutputStream(socketdeConexion.getOutputStream());

                if (!archivo.exists()) {
                    mensajeParaCliente.writeBytes(
                            "HTTP/1.0 404 Not Found\r\n\r\n"
                    );
                    socketdeConexion.close();
                    return;
                }

                FileInputStream archivoDeEntrada =
                        new FileInputStream(archivo);

                int cantidadDeBytes = (int) archivo.length();
                byte[] archivoEnBytes = new byte[cantidadDeBytes];
                archivoDeEntrada.read(archivoEnBytes);

                mensajeParaCliente.writeBytes(
                        "HTTP/1.0 200 OK\r\n"
                );
                mensajeParaCliente.writeBytes(
                        "Content-Length: " + cantidadDeBytes + "\r\n"
                );
                mensajeParaCliente.writeBytes("\r\n");
                mensajeParaCliente.write(archivoEnBytes);

                archivoDeEntrada.close();
                socketdeConexion.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
