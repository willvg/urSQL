/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExecutorEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import systemcatalog.*;

/**
 *
 * @author AndresMaria
 */
public class ProcesadorComandosCLP {
    
    SystemCatalog catalog;
    
    public ProcesadorComandosCLP() throws IOException{
        try {
            catalog=new SystemCatalog();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcesadorComandosCLP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String ProcesarCreateDatabase(ArrayList<String> consulta){
        try{
              String nombre= consulta.get(2);
              File directorio= new File ("C:\\urSQL\\"+nombre);
              directorio.mkdirs();
              RegistroEsquema registro= new RegistroEsquema(catalog.getTablaDeEsquemas().getNumeroDeRegistros(), nombre, false);
              catalog.getTablaDeEsquemas().introducirEsquemaEnArchivo(catalog.getTablaDeEsquemas().getNumeroDeRegistros(), registro);
              return "Base creada";
        }
        catch(Exception e){
            return "Error al crear la base";
        }
    }
    
    private String ProcesarDropDatabase(ArrayList<String> consulta) throws IOException{
        String nombre= consulta.get(2);
        String sDirectorio = "C:\\urSQL\\"+nombre;
        File f = new File(sDirectorio);
        RegistroEsquema esquema = catalog.getTablaDeEsquemas().obtenerEsquemaDesdeArchivo((long) catalog.getTablaDeEsquemas().obtenerIdEsquemaDeTabla(nombre));
        esquema.setBorrado(true);
        catalog.getTablaDeEsquemas().introducirEsquemaEnArchivo((long) catalog.getTablaDeEsquemas().obtenerIdEsquemaDeTabla(nombre), esquema);
        f.delete();
        return "Base eliminada";
    }
    
    private String ProcesarListDatabases (ArrayList<String> consulta){
        try{
            ArrayList<String> bases= catalog.getTablaDeEsquemas().obtenerTodosLosEsquemas();
            String out="";
            for (int i=0; i<bases.size(); i++){
                out= out+"/"+bases.get(i);
            }
            out=out+"/";
            return out;
        }
        catch(Exception e){
            return "Error al listar las bases de datos";
        }
        
    }
    
    private String ProcesarDisplayDatabase(ArrayList<String> consulta) throws IOException{
        String nombrebase= consulta.get(2); 
        return catalog.DisplayDataBaseCatalog(nombrebase);
    }
    
    public String recibir_Consulta(ArrayList<String> consulta) throws IOException{
        String comando= consulta.get(0);
        if (comando.equals("CREATE")){
            return ProcesarCreateDatabase(consulta);
        }
        else if (comando.equals("DROP")){
            return ProcesarDropDatabase(consulta);
        }
        else if (comando.equals("LIST")){
            return ProcesarListDatabases(consulta);
        }
        else if (comando.equals("START")){
            return "Iniciado SystemCatalog"+"/"+"Iniciado Execution Engine"+
                    "/"+"Iniciado ArbolBmas"+"/"+"ningun proceso ejecutandose";
        }
        else if (comando.equals("GET")){
            return "QueryCompiler: no ejecutando"+"/"+"Execution Engine: no ejecutando"+
                    "/"+"ArbolBmas: no ejecutando";
        }
        else if (comando.equals("STOP")){
            return "Todos los procesos detenidos";
        }
        else if (comando.equals("DISPLAY")){
            return ProcesarDisplayDatabase(consulta);
        }
        else{
            return "Comando CLP no encontrado";
        }
    }
}
