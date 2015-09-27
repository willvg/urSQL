/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExecutorEngine;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author AndresMaria
 */
public class Procesador {
      
      public String procesarConsultaCLP(ArrayList<String> consulta) throws IOException{
          ProcesadorComandosCLP procesador= new ProcesadorComandosCLP();
          return procesador.recibir_Consulta(consulta);
          
      }
      
      public String procesarConsultaDDL(ArrayList<String> consulta, String db) throws IOException{
          ProcesadorComandosDDL procesador= new ProcesadorComandosDDL();
          return procesador.recibir_Consulta(consulta, db);
      }
      
      public String procesarConsultaDML(ArrayList<String> consulta, String db) throws IOException{
           ProcesadorComandosDML procesadorDML= new ProcesadorComandosDML();
           return procesadorDML.recibir_consulta(consulta, db);
      }
      
}
