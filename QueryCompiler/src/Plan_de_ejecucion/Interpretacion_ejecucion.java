/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Plan_de_ejecucion;

import java.util.ArrayList;

/**
 *
 * @author Will varela
 */
public class Interpretacion_ejecucion {

    ArrayList<ArrayList<String>> Plan_ejecucion_lista = new ArrayList<ArrayList<String>>();

    public void imprimir(ArrayList<String> linea_tokenizada) {
        int contador = 0;
        while (contador < linea_tokenizada.size()) {
            System.out.println("la palabra es: " + linea_tokenizada.get(contador));
            contador++;
        }
    }

    public ArrayList<ArrayList<String>> recibir_linea(ArrayList<String> linea_tokenizada) {
        ArrayList<ArrayList<String>> Temporal = new ArrayList<ArrayList<String>>();
        if (linea_tokenizada.get(0).equals("SELECT")) {
            Plan_select(Temporal, linea_tokenizada);
        }
        if (linea_tokenizada.get(0).equals("UPDATE")) {

        }
        if (linea_tokenizada.get(0).equals("DELETE")) {

        }
        if (linea_tokenizada.get(0).equals("INSERT")) {

        }

        return Plan_ejecucion_lista;
    }

    private ArrayList<ArrayList<String>> Plan_select(
            ArrayList<ArrayList<String>> temporal,
            ArrayList<String> linea_tokenizada) {

        ArrayList<String> comando = new ArrayList<String>();
        ArrayList<String> columnas = new ArrayList<String>();
        ArrayList<String> tablas = new ArrayList<String>();
        comando.add("Sentencia SELECT");
        temporal.add(comando);

        int contador_comando = 1;
        while (!linea_tokenizada.get(contador_comando).equals("FROM")) {
            if (linea_tokenizada.get(contador_comando).equals("*")) {
                columnas.add("Toda el contenido");
            } else {
                columnas.add(linea_tokenizada.get(contador_comando));
            }
            contador_comando++;
        }

        /*int contador = 0;
         while (contador < columnas.size()) {
         System.out.println("las columnas son: " + columnas.get(contador));
         contador++;
         }*/
        boolean bandera = false;
        for (int i = 0; i < linea_tokenizada.size(); i++) {
            if (linea_tokenizada.get(i).equals("INNER_JOIN")) {
                bandera = true;
                break;
            }
        }
        if (bandera == true) {
            boolean bandera_inner = false;
            for (int i = 0; i < linea_tokenizada.size(); i++) {
                if (linea_tokenizada.get(i).equals("WHERE")
                        || linea_tokenizada.get(i).equals("GROUP_BY")) {
                    bandera_inner = false;
                }
                if (linea_tokenizada.get(i).equals("FROM")
                        || bandera_inner == true) {
                    bandera_inner = true;
                    tablas.add(linea_tokenizada.get(i + 1));
                    i++;
                }

            }
            temporal.add(tablas);
            temporal.add(columnas);
        } else {
            for (int i = 0; i < linea_tokenizada.size(); i++) {
                if (linea_tokenizada.get(i).equals("FROM")) {
                    tablas.add(linea_tokenizada.get(i + 1));
                    i++;
                }
            }
            temporal.add(tablas);
            temporal.add(columnas);
        }
        
        return temporal;
    }

}
