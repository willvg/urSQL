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

        // estos son los DDL commands
        a_p.recibir_expresion("SET DATABASE universo");

        a_p.recibir_expresion("ALTER TABLE universo};");

        a_p.recibir_expresion("ADD CONSTRAINT FOREING KEY ( nombre );");

        a_p.recibir_expresion("REFERENCES estudiantes ( nombre );");

        a_p.recibir_expresion("DROP TABLE estudiantes;");

        a_p.recibir_expresion("CREATE_INDEX sa ON estudiantes ( residencia );");
        //**********************************************************************

        //estos son los DML commands
        a_p.recibir_expresion("SELECT * FROM estudiantes;");

        a_p.recibir_expresion("SELECT nombre, apellido "
                + "FROM estudiantes WHERE estudiantes.nombre = will;");

        a_p.recibir_expresion("SELECT COUNT ( apellido ) "
                + "FROM estudiantes WHERE estudinates.apellido = varela;");

        a_p.recibir_expresion("SELECT carnet FROM estudiantes INNER_JOIN cursos"
                + " WHERE estudiantes.carnet = cursos.carnet GROUP_BY carnet;");
        
        a_p.recibir_expresion("UPDATE estudinates SET nombre = eduardo, "
                + "apellido = guillen WHERE nombre = will;");
        
        a_p.recibir_expresion("DELETE FROM estudiantes WHERE nombre = juan;");
        
        a_p.recibir_expresion("INSERT INTO estudiantes ( nombre, carnet, "
                + "apellido ) VALUES ( carlos, 2012456532, arias );");
        //**********************************************************************


    }

}
