/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procesador;

/**
 *
 * @author Will varela
 */
public class Procesador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Analizamiento_Procesamiento a_p = new Analizamiento_Procesamiento();
        a_p.recibir_expresion("CREATE_TABLA estudiantes AS ( "
                + "nombre CHAR (45) NOT_NULL,"
                + " carnet INTEGER NOT_NULL,"
                + " residencia CHAR (50) NULL,"
                + " PRIMARY_KEY carnet"
                + " )");
    }

}
