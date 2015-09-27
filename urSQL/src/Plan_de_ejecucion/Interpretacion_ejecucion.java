
package Plan_de_ejecucion;

import java.util.ArrayList;

/**
 *
 * @author Will varela
 */
public class Interpretacion_ejecucion {

    /**
     * metodo recibir_linea lo que se encargar es recibir la linea de comando
     * dividida para poder ser analizada y asi generar un plan de ejecución 
     * del comando SELECT, en dado caso se puede implementar para otros comandos
     * @param linea_tokenizada
     * @return Temporal
     */
    public ArrayList<ArrayList<String>> recibir_linea(ArrayList<String> linea_tokenizada) {
        ArrayList<ArrayList<String>> Temporal = new ArrayList<ArrayList<String>>();
        if (linea_tokenizada.get(0).equals("SELECT")) {
            Plan_select(Temporal, linea_tokenizada);
        }
        return Temporal;
    }

    /**
     * El método Plan_select se encarga de realizar por medio de un Hash el plan 
     * de ejecución del comando SELECT
     * @param temporal
     * @param linea_tokenizada
     * @return 
     */
    private ArrayList<ArrayList<String>> Plan_select(
            ArrayList<ArrayList<String>> temporal,
            ArrayList<String> linea_tokenizada) {

        ArrayList<String> comando = new ArrayList<String>();   //variable para el comando
        ArrayList<String> columnas = new ArrayList<String>();  //variables para las columnas solicitadas
        ArrayList<String> tablas = new ArrayList<String>();    //variable para las tablas consultadas
        comando.add("Sentencia SELECT");
        temporal.add(comando);                                 //agrega el comando al Hash

        int contador_comando = 1;
        //while para agragar las columnas a la lista de columnas
        while (!linea_tokenizada.get(contador_comando).equals("FROM")) {
            if (linea_tokenizada.get(contador_comando).equals("*")) {
                columnas.add("Toda el contenido");
            } else {
                columnas.add(linea_tokenizada.get(contador_comando));
            }
            contador_comando++;
        }

        
        boolean bandera = false;
        //recorrido en dado caso si se solicita ingresar a mas de una tabla
        for (int i = 0; i < linea_tokenizada.size(); i++) {
            if (linea_tokenizada.get(i).equals("INNER_JOIN")) {
                bandera = true;
                break;
            }
        }
        //si la solicitud es mas de una tabla ingresa al if
        if (bandera == true) {
            boolean bandera_inner = false;
            for (int i = 0; i < linea_tokenizada.size(); i++) {
                if (linea_tokenizada.get(i).equals("WHERE")
                        || linea_tokenizada.get(i).equals("GROUP_BY")) {   //si llega al final de las tablas termina 
                    bandera_inner = false;
                }
                if (linea_tokenizada.get(i).equals("FROM")     //valida cuales son las tablas consultadas 
                                                               //y las ingresa a la variable de tablas 
                        || bandera_inner == true) {
                    bandera_inner = true;
                    tablas.add(linea_tokenizada.get(i + 1));
                    i++;
                }

            }
            temporal.add(tablas);                                  //ingresa a temporal las tablas solicitadas 
            temporal.add(columnas);                                //ingresa a temporal las tablas solicitadas 
        } else {                                                   //si solo ingresa una tabla a utilizar
            for (int i = 0; i < linea_tokenizada.size(); i++) {
                if (linea_tokenizada.get(i).equals("FROM")) {      //valida cual es la tabla a consultar 
                                                                   //y las ingresa la tabla 
                    tablas.add(linea_tokenizada.get(i + 1));
                    i++;
                }
            }
            temporal.add(tablas);                                  //ingresa a temporal las tablas solicitadas 
            temporal.add(columnas);                                //ingresa a temporal las tablas solicitadas 
        }
        
        return temporal;
    }

}
