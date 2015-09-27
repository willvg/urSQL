/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ExecutorEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import systemcatalog.*;

/**
 *
 * @author jonathan.chaverri12
 */
public class ProcesadorComandosDDL {
    SystemCatalog catalog;
    
    public ProcesadorComandosDDL() throws IOException{
        try {
            catalog=new SystemCatalog();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcesadorComandosDDL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String recibir_Consulta(ArrayList<String> consulta, String db) throws FileNotFoundException, IOException{
        String palabra = consulta.get(0);
        String respuesta="";
        switch (palabra) {
            case "CREATE_TABLE":
                respuesta=ProcesarCreateTable(consulta, db);
                break;
            case "SET":
                respuesta=ProcesarSetDatabase(consulta);
                break;
            case "ALTER":
                respuesta=ProcesarAlterTable(consulta);
                break;
            case "DROP":
                respuesta=ProcesarDropTable(consulta, db);
                break;
            case "CREATE_INDEX":
                respuesta=ProcesarCreateIndex(consulta);
                break;
            default:
                return "No es un comando DDL valido";
        }
        return respuesta;
    }
    
    private String ProcesarCreateTable(ArrayList<String> consulta, String database) throws FileNotFoundException, IOException{
        String nombre= consulta.get(1);
        ArrayList<String> columnas= new ArrayList<String>();
        String PK="";
        TablaTablas tabla= new TablaTablas();
        TablaEsquemas esquema= new TablaEsquemas();
        
        boolean esColumna=false;
        boolean parInicio=true;
        for (int i= 0; i<consulta.size(); i++){
            if (consulta.get(i).contains("(") && parInicio){
                parInicio=false;
                esColumna=true;
            }
            else if (consulta.get(i).equals("PRIMARY_KEY")){
                esColumna=false;
                break;
            }
            else if (esColumna){
                columnas.add(consulta.get(i));
            }
        }
        for (int j= 0; j<consulta.size(); j++){
            if (consulta.get(j).equals("PRIMARY_KEY")){
                PK=consulta.get(j+1);
            }
        }
        try {
            RegistroTabla registro= new RegistroTabla(catalog.getTablaDeTablas().getNumeroDeRegistros(), nombre, -1,
                    esquema.obtenerIdEsquemaDeTabla(database), PK, false);
            int tmp = catalog.getTablaDeTablas().getNumeroDeRegistros();
            catalog.getTablaDeTablas().introducirTablaEnArchivo((long) tmp, registro);
        
        } catch (IOException ex) {
            return "Error al registrar la tabla en el System Catalog";
        }
        ArrayList<String> aux= new ArrayList<String>();
        for (int k=0; k<columnas.size();k++){
            aux.add(columnas.get(k));
            k=k+3;
        }
        String con = catalog.getCantidadColumnas();
        int tmp = catalog.getTablaDeTablas().obtenerIdTabla(nombre);
        String id = Integer.toString(tmp);
        for(int i = 0; i < columnas.size(); i++){
            catalog.IntroducirColumnasTabla(con, id, columnas.get(i), columnas.get(i+1));
            i=i+3;
        }
        new RandomAccessFile("C:/urSQL/"+database+"/"+nombre+".dat", "rw");
        return "Tabla creada: "+ nombre; 
    }
    
    private String ProcesarSetDatabase(ArrayList<String> consulta){
        return "Base de datos a trabajar: "+consulta.get(2);
    }
    
    private String ProcesarAlterTable(ArrayList<String> consulta){
        String nombre=consulta.get(2);
        String FK = consulta.get(8);
        String tabla2= consulta.get(11);
        String columna2= consulta.get(13);
        return "Tabla alterada: "+consulta.get(2);
    }
    
    private String ProcesarDropTable(ArrayList<String> consulta, String database) throws IOException{
        String nombre=consulta.get(2);
        nombre=nombre.substring(0, nombre.length()-1);
        int idTabla = catalog.getTablaDeTablas().obtenerIdTabla(nombre);
        RegistroTabla tabla = catalog.getTablaDeTablas().obtenerTablaDesdeArchivo((long) idTabla);
        tabla.setBorrado(true);
        catalog.getTablaDeTablas().introducirTablaEnArchivo((long)idTabla, tabla);
        String sDirectorio = "C:/urSQL/"+database+"/"+nombre+".dat";
        File f = new File(sDirectorio);
        f.delete();
        return "Tabla eliminada";
    }
    
    private String ProcesarCreateIndex(ArrayList<String> consulta){
        String nombre=consulta.get(1);
        String nombreTabla=consulta.get(3);
        String nombreColumna=consulta.get(5);
        return "Indice creado en la tabla: "+nombreTabla+", en la columna "+nombreColumna;
    }
}

