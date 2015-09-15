/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procesador;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Will varela
 */
public class Analizamiento_Procesamiento {

    private static Analizador analisis = new Analizador();

    public void recibir_expresion(String linea) {
        ArrayList<String> linea_tokenizada = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(linea);
        while (st.hasMoreTokens()) {
            String palabra = st.nextToken();
            linea_tokenizada.add(palabra);
        }

        //System.out.println("palabra: "+ linea_tokenizada.get(0));
        /*int contador =0;
         while(contador<linea_tokenizada.size()){
         System.out.println("la palabra es: "+linea_tokenizada.get(contador));
         contador++;
         }
         System.out.println("tamaño: "+linea_tokenizada.size());
         System.out.println("termina: "+ linea_tokenizada.get(linea_tokenizada.size()-3));
         System.out.println("termina: "+ linea_tokenizada.get(linea_tokenizada.size()-1));*/
        identificador(linea_tokenizada);

    }

    private void identificador(ArrayList<String> linea_tokenizada) {
        String palabra = linea_tokenizada.get(0);
        switch (palabra) {
            case "CREATE_TABLA":
                Create_tabla(linea_tokenizada);
                break;
            case "SET":
                Set_database(linea_tokenizada);
                break;
            case "ALTER":
                alter_command(linea_tokenizada);
                break;
            case "ADD":
                add_command(linea_tokenizada);
                break;
            case "REFERENCES":
                References_command(linea_tokenizada);
                break;
            case "DROP":
                drop_command(linea_tokenizada);
                break;
            case "CREATE_INDEX":
                create_index(linea_tokenizada);
                break;
            case "SELECT":
                Select(linea_tokenizada);
                break;
            case "UPDATE":
                Update(linea_tokenizada);
                break;
            case "DELETE":
                Delete(linea_tokenizada);
                break;
            case "INSERT":
                Insert (linea_tokenizada);
                break;
            default:
                System.out.println("No se encuentra ese comando");
                break;
        }
    }

    //commands ddl
    private void Create_tabla(ArrayList<String> linea_tokenizada) {
        boolean caso = analisis.Frase_create_tabla(linea_tokenizada);
        if (caso) {
            System.out.println("exito create_table");
        } else {
            System.out.println("mal a la hora de crear tabla");
        }
    }

    private void Set_database(ArrayList<String> linea_tokenizada) {
        boolean caso = analisis.Frase_set_dabase(linea_tokenizada);
        if (caso) {
            System.out.println("exito set dababase");
        } else {
            System.out.println("mal set database");
        }
    }

    private void alter_command(ArrayList<String> linea_tokenizada) {
        boolean caso = analisis.Frase_alter(linea_tokenizada);
        if (caso) {
            System.out.println("exito alter");
        } else {
            System.out.println("mal alter");
        }
    }

    private void add_command(ArrayList<String> linea_tokenizada) {
        boolean caso = analisis.Frase_add(linea_tokenizada);
        if (caso) {
            System.out.println("exito add_command");
        } else {
            System.out.println("mal add_comand");
        }
    }

    private void References_command(ArrayList<String> linea_tokenizada) {
        boolean caso = analisis.Frase_referencia(linea_tokenizada);
        if (caso) {
            System.out.println("exito referencia_command");
        } else {
            System.out.println("mal referencia_comand");
        }
    }

    private void drop_command(ArrayList<String> linea_tokenizada) {
        boolean caso = analisis.Frase_drop(linea_tokenizada);
        if (caso) {
            System.out.println("exito drop table");
        } else {
            System.out.println("mal drop table");
        }
    }
    private void create_index(ArrayList<String> linea_tokenizada){
        boolean caso = analisis.Frase_index(linea_tokenizada);
        if (caso) {
            System.out.println("exito en crear en indice");
        } else {
            System.out.println("mal en crear el indice");
        }
    }
    //******************************************************
    
    //commands DML
    private void Select (ArrayList<String> linea_tokenizada){
        boolean caso = analisis.Frase_select(linea_tokenizada);// cambiar
        if (caso) {
            System.out.println("exito en realizar la sentencia select");
        } else {
            System.out.println("mal en realizar la sentencia select");
        } 
    }
    private void Update (ArrayList<String> linea_tokenizada){
        boolean caso = analisis.Frase_update(linea_tokenizada);// cambiar
        if (caso) {
            System.out.println("exito en realizar la sentencia update");
        } else {
            System.out.println("mal en realizar la sentencia update");
        } 
    }
    
    private void Delete (ArrayList<String> linea_tokenizada){
        boolean caso = analisis.Frase_delete(linea_tokenizada);// cambiar
        if (caso) {
            System.out.println("exito en realizar la sentencia delete");
        } else {
            System.out.println("mal en realizar la sentencia delete");
        } 
    }
    private void Insert (ArrayList<String> linea_tokenizada){
        boolean caso = analisis.Frase_insert(linea_tokenizada);// cambiar
        if (caso) {
            System.out.println("exito en realizar la sentencia delete");
        } else {
            System.out.println("mal en realizar la sentencia delete");
        } 
    }

}
