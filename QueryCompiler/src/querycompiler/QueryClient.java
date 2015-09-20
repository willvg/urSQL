/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package querycompiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author David
 */
public class QueryClient {
    public QueryClient(){
        String hostName = "localhost";
        int portNumber = Integer.parseInt("8080");
 
        try (
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(kkSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
            
            while ((fromServer = in.readLine()) != null) { //comunicación con el socket
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;
                sendMessage("Who's there?");
            }
        }catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }   
    }
    
    public void sendMessage (String mensaje){
        if (mensaje != null) {
                    System.out.println("Client: " + mensaje);
                    out.println(mensaje);
                }
    }
}
