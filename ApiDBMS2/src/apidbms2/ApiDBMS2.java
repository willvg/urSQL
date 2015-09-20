/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apidbms2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Jonatan
 */
public class ApiDBMS2 {
String hostName;
    int portNumber;
    
    public ApiDBMS2(String host, int port){
        this.hostName=host;
        this.portNumber= port;
    }
    
    public String SendReceive( String mensaje){
        try (
            Socket comunicacion = new Socket(hostName, portNumber);
            PrintWriter salida = new PrintWriter(comunicacion.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(comunicacion.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
            salida.println(mensaje);
            fromServer = entrada.readLine();
            return fromServer;
            
        }catch (UnknownHostException e) {
            return "No existe el Host";
            
        } catch (IOException e) {
            return "No se pudo conectar";
        }   
    }
}
