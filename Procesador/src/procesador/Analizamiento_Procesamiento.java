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

   

    public void recibir_expresion(String linea) {
        ArrayList<String> linea_tokenizada = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(linea);
        while (st.hasMoreTokens()) {
            String palabra = st.nextToken();
            linea_tokenizada.add(palabra);
        }
        
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

    public void identificador(ArrayList<String> linea_tokenizada) {
        String palabra = linea_tokenizada.get(0);
        switch (palabra) {
            case "CREATE_TABLA":
                Create_tabla(linea_tokenizada);
                break;
            default:
                System.out.println("No se encuentra ese comando");
                break;
        }
    }
    
    public void Create_tabla(ArrayList<String> linea_tokenizada){
        Analizador_tabla a_t = new Analizador_tabla();
        boolean caso = a_t.Frase(linea_tokenizada);
        if(caso){
            System.out.println("exito");
        }
        else{
            System.out.println("mal");
        }
    }

}
