
package systemcatalog;

import estructurasDatos.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TablaTablas{

    private RandomAccessFile raf;//modificar!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private String FILE_NAME = "TablasRegistros.txt";
    private long numeroDeRegistros;
    
    public TablaTablas(){
        try {
            this.raf = new RandomAccessFile("C:/urSQL/SystemCatalog/TablaTablas.dat", "rw");
        } catch (FileNotFoundException ex) {
            File directorio= new File ("C:\\urSQL\\SystemCatalog");
            directorio.mkdirs();
            try {
                this.raf=new RandomAccessFile("C:/urSQL/SystemCatalog/TablaTablas.dat", "rw");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(TablaTablas.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        long cont = 0;
        String linea = "";
        try {
            FileReader reader = new FileReader(this.FILE_NAME);
            BufferedReader buffReader = new BufferedReader(reader);
            try {
                linea = buffReader.readLine();
                cont = Integer.parseInt(linea);
                
            } catch (IOException ex) {
                Logger.getLogger(SystemCatalog.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SystemCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.numeroDeRegistros = cont;
    }

    public int getNumeroDeRegistros() {
        return (int) numeroDeRegistros;
    }
    
    
    public void introducirTablaEnArchivo(long num, RegistroTabla registro){
        try {
            this.raf.seek(num * registro.TAMANO);
            byte[] registroB = registro.toBytes();
            this.raf.write(registroB);
            incrementarRegistros();
        } catch (IOException ex) {
            Logger.getLogger(TablaTablas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void incrementarRegistros() throws IOException{
        this.numeroDeRegistros = this.numeroDeRegistros + 1;
        File archivo=new File(this.FILE_NAME);
        archivo.delete();
        File directorio= new File (this.FILE_NAME);
        directorio.mkdirs();
        //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
        FileWriter escribir=new FileWriter(directorio,false);

        //Escribimos en el archivo con el metodo write 
        int r= (int) this.numeroDeRegistros;
        
        escribir.write(Integer.toString(r));

        try {
            //Cerramos la conexion
            escribir.close();
        } catch (IOException ex) {
            Logger.getLogger(TablaTablas.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }
    
    public RegistroTabla obtenerTablaDesdeArchivo(long num)throws IOException{
        this.raf.seek(num * RegistroTabla.TAMANO);
        byte[] registroB = new byte[RegistroTabla.TAMANO];
        this.raf.read(registroB);
        return RegistroTabla.registroDesdeBytes(registroB);
    }
    
    public long obtenerUbicacionDeRaiz(int idTabla) throws IOException{
        long resp = -1;
        if(this.numeroDeRegistros>0){
            for(long i = 0; i < this.numeroDeRegistros; i++){
                RegistroTabla tmp = obtenerTablaDesdeArchivo(i);
                if(tmp.getTablaId() == idTabla){
                    resp = tmp.getUbicacionTabla();
                    
                }
            }
        }
        return resp;
    }
    
    public int obtenerIdEsquemaDeTabla(int idTabla)throws IOException{
        int resp = -1;
        if(this.numeroDeRegistros>0){
            for(long i = 0; i < this.numeroDeRegistros; i++){
                RegistroTabla tmp = obtenerTablaDesdeArchivo(i);
                if(tmp.getTablaId() == idTabla){
                    resp = tmp.getEsquemaDeTablaID();
                }
            }
        }
        return resp;
    }
    
   public String obtenerTodasLasTablas() throws IOException{// public ArrayList<String> listaDeNombres() throws IOException{
        if(this.numeroDeRegistros>0){
            String resp = "";
            for(long i = 0; i < this.numeroDeRegistros; i++){
                RegistroTabla tmp = obtenerTablaDesdeArchivo(i);
                resp = resp + "@" + tmp.getNombreTabla();
            }
            return resp;
        }
        else{
            return null;
        }
            
    }
    public ArrayList<String> obtenerTablasDeEsquema(int idEsquema) throws IOException{
        ArrayList<String> resp = new ArrayList<String>();
        if(this.numeroDeRegistros>0){
            for(long i = 0; i < this.numeroDeRegistros; i++){
                RegistroTabla tmp = obtenerTablaDesdeArchivo(i);
                if(tmp.getEsquemaDeTablaID() == idEsquema){
                    resp.add(tmp.getNombreTabla());
                }
            }
            return resp;
        }
        else{
            return null;
        }
    }
    
    public String obtenerColumnaIdTabla(int idTabla) throws IOException{
        if(this.numeroDeRegistros>0){
            String resp = "";
            for(int i = 0; i < this.numeroDeRegistros; i++){
                RegistroTabla tmp = obtenerTablaDesdeArchivo(i);
                if(tmp.getTablaId()== idTabla){
                    resp = tmp.getColumnaLlave();
                }
            }
            return resp;
        }
        else{
            return null;
        }
    }
    
    public int obtenerIdTabla (String nombre) throws IOException{
        if(this.numeroDeRegistros>0){
            int resp = -2;
            for(long i = 0; i < this.numeroDeRegistros; i++){
                RegistroTabla tmp = obtenerTablaDesdeArchivo(i);
                if(tmp.getNombreTabla().equals(nombre)){
                   resp = tmp.getTablaId();
                }
            }
            return resp;
        }
        else{
            return -1;
        }
    }
   
   

}
