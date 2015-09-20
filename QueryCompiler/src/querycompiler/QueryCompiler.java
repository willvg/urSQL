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
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author David
 */
public class QueryCompiler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int portNumber = Integer.parseInt("8080");
        int continuar=1;
        while (continuar==1){
            try ( 
                ServerSocket comunicacion = new ServerSocket(portNumber);
                Socket clientSocket = comunicacion.accept();
                PrintWriter salida =
                    new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            ) {

                String inputLine, outputLine;

                // Initiate conversation with client
                Protocolo kkp = new Protocolo();
                while ((inputLine = entrada.readLine()) != null) {
                    if (inputLine.equals("Terminar")){
                        continuar=0;
                        break;
                    }
                    outputLine = kkp.processInput(inputLine);
                    salida.println(outputLine);
                }

            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
      }
  }
    
}
