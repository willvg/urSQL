/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package querycompiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class QueryCompiler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        int portNumber = Integer.parseInt("8080");
        int continuar=1;
        Protocolo kkp;
        try {
            kkp = new Protocolo();
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
      catch (FileNotFoundException ex) {
            Logger.getLogger(QueryCompiler.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
    
}
