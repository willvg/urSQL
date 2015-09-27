

package querycompiler;

import ExecutorEngine.Procesador;
import java.util.ArrayList;
import java.util.StringTokenizer;
import Plan_de_ejecucion.Interpretacion_ejecucion;
import java.io.IOException;

/**
 *
 * @author Will varela
 */
public class Analizamiento_Procesamiento {

    /**
     * Intancia del objeto Analizador
     */
    private static Analizador analisis = new Analizador();
    private String respuesta="";
    private String database="";
    Procesador procesador=new Procesador();
    /**
     * recibir_expresion lo que realiza es recibir la linea de comando y la
     * divide para poder ser analizada 
     * @param linea 
     */
    public void recibir_expresion(String linea, String db) throws IOException {
        database= db;
        ArrayList<String> linea_tokenizada = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(linea);
        while (st.hasMoreTokens()) {
            String palabra = st.nextToken();
            linea_tokenizada.add(palabra);
        }

        int contador =0;
         while(contador<linea_tokenizada.size()){
         System.out.println("la palabra es: "+linea_tokenizada.get(contador));
         contador++;
         }
        identificador(linea_tokenizada);

    }

    /**
     * identificador clasifica que comando es que se esta ingresando en cierto 
     * momento y lo envia a donde corresponde para su respectivo analisis
     * y ejecución
     * @param linea_tokenizada 
     */
    private void identificador(ArrayList<String> linea_tokenizada) throws IOException {
        String palabra = linea_tokenizada.get(0);
        switch (palabra) {
            case "CREATE_TABLE":
                Create_tabla(linea_tokenizada);
                break;
            case "SET":
                Set_database(linea_tokenizada);
                break;
            case "ALTER":
                alter_command(linea_tokenizada);
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
                respuesta= respuesta+"Error -121: error al declarar, error de sintaxis"+"//";
                break;
        }
    }

    //*******************************************************************\\
    //***************************commands DDL***************************\\
    
    /**
     * Create_tabla realiza la sentencia de de crear la tabla
     * @param linea_tokenizada 
     */
    
    public void ComandosCLP(String expresion) throws IOException{
        ArrayList<String> linea_tokenizada = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(expresion);
        while (st.hasMoreTokens()) {
            String palabra = st.nextToken();
            linea_tokenizada.add(palabra);
        }
        respuesta=respuesta+procesador.procesarConsultaCLP(linea_tokenizada)+"//";
    }
    
    private void Create_tabla(ArrayList<String> linea_tokenizada) throws IOException {
        boolean caso = analisis.Frase_create_tabla(linea_tokenizada);
        if (caso) {
            respuesta=respuesta+procesador.procesarConsultaDDL(linea_tokenizada,database)+"//";
        } else {
            respuesta= respuesta+"Error en el CREATE TABLE//";
        }
        
    }

    /**
     * Set_database realiza la validacion y ejecución de el comando SET DATABASE
     * @param linea_tokenizada 
     */
    private void Set_database(ArrayList<String> linea_tokenizada) throws IOException {
        boolean caso = analisis.Frase_set_dabase(linea_tokenizada);
        if (caso) {
            respuesta=respuesta+procesador.procesarConsultaDDL(linea_tokenizada, database)+"//";
        } else {
            respuesta= respuesta+"Error en el SET DATABASE//";
        }
        
    }

    /**
     * alter_command realiza la validación y ejecución del comando ALTER
     * @param linea_tokenizada 
     */
    private void alter_command(ArrayList<String> linea_tokenizada) throws IOException {
        boolean caso = analisis.Frase_alter(linea_tokenizada);
        if (caso) {
            respuesta=respuesta+procesador.procesarConsultaDDL(linea_tokenizada,database)+"//";
        } else {
            respuesta= respuesta+"Error en el ALTER//";
        }
        
    }

    /**
     * add_command realiza la validación y ejecución del comando ADD CONSTRAINT
     * @param linea_tokenizada 
     */
    /*private void add_command(ArrayList<String> linea_tokenizada) {
        boolean caso = analisis.Frase_add(linea_tokenizada);
        if (caso) {
            respuesta=respuesta+procesador.procesarConsultaDDL(linea_tokenizada)+"//";
        } else {
            respuesta= respuesta+"Error en el add//";
        }
    }*/

    /**
     * References_command realiza la validación y ejecución del comando 
     * REFERENCES
     * @param linea_tokenizada 
     */
    /*private void References_command(ArrayList<String> linea_tokenizada) {
        boolean caso = analisis.Frase_referencia(linea_tokenizada);
        if (caso) {
            respuesta=respuesta+procesador.procesarConsultaDDL(linea_tokenizada)+"//";
        } else {
            respuesta= respuesta+"Error en el References//";
        }
    }*/

    /**
     * drop_command realiza la validación y ejecución del comando DROP TABLE
     * @param linea_tokenizada 
     */
    private void drop_command(ArrayList<String> linea_tokenizada) throws IOException {
        boolean caso = analisis.Frase_drop(linea_tokenizada);
        if (caso) {
            respuesta=respuesta+procesador.procesarConsultaDDL(linea_tokenizada,database)+"//";
        } else {
            respuesta= respuesta+"Error en el DROP TABLE//";
        }
    }
    
    /**
     * create_index realiza la validación y ejecución del comando CREATE INDEX
     * @param linea_tokenizada 
     */
    private void create_index(ArrayList<String> linea_tokenizada) throws IOException{
        boolean caso = analisis.Frase_index(linea_tokenizada);
        if (caso) {
            respuesta=respuesta+procesador.procesarConsultaDDL(linea_tokenizada,database)+"//";
        } else {
            respuesta= respuesta+"Error en el CREATE_INDEX//";
        }
        
    }
    //*******************************************************************\\
    
    //*******************************************************************\\
    //***************************commands DML***************************\\
    
    /**
     * Select realiza la validación y ejecución del comando SELECT
     * @param linea_tokenizada 
     */
    private void Select (ArrayList<String> linea_tokenizada) throws IOException{
        boolean caso = analisis.Frase_select(linea_tokenizada);// cambiar
        ArrayList<ArrayList<String>> Plan_de_ejecucion = new ArrayList<ArrayList<String>>();
        if (caso) {
            System.out.println("exito en realizar la sentencia select");
            Interpretacion_ejecucion i_e = new Interpretacion_ejecucion();  //instancia el objeto Interpretacion_ejecucion
            Plan_de_ejecucion = i_e.recibir_linea(linea_tokenizada);  //genera el plan de ejecución del comando SELECT
            respuesta=respuesta+"@"+procesador.procesarConsultaDML(linea_tokenizada, database)+"//";
        } else {
            System.out.println("mal en realizar la sentencia select");
        } 
    }
    
    /**
     * Update realiza la validación y ejecución del comando UPDATE
     * @param linea_tokenizada 
     */
    private void Update (ArrayList<String> linea_tokenizada) throws IOException{
        boolean caso = analisis.Frase_update(linea_tokenizada);// cambiar
        if (caso) {
            System.out.println("exito en realizar la sentencia update");
            respuesta=respuesta+procesador.procesarConsultaDML(linea_tokenizada, database)+"//";
        } else {
            System.out.println("mal en realizar la sentencia update");
        } 
    }
    
    /**
     * Delete realiza la validación y ejecución del comando DELETE
     * @param linea_tokenizada 
     */
    private void Delete (ArrayList<String> linea_tokenizada) throws IOException{
        boolean caso = analisis.Frase_delete(linea_tokenizada);// cambiar
        if (caso) {
            System.out.println("exito en realizar la sentencia delete");
            respuesta=respuesta+procesador.procesarConsultaDML(linea_tokenizada, database)+"//";
        } else {
            System.out.println("mal en realizar la sentencia delete");
        } 
    }
    
    /**
     * Insert realiza la validación y ejecución del comando INSERT
     * @param linea_tokenizada 
     */
    private void Insert (ArrayList<String> linea_tokenizada) throws IOException{
        boolean caso = analisis.Frase_insert(linea_tokenizada);// cambiar
        if (caso) {
            System.out.println("exito en realizar la sentencia delete");
            respuesta=respuesta+procesador.procesarConsultaDML(linea_tokenizada, database)+"//";
        } else {
            System.out.println("mal en realizar la sentencia delete");
        } 
    }
    //*******************************************************************\\
   public String getRespuesta(){
       return this.respuesta;
   }
}
