
package systemcatalog;

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


public class TablaEsquemas {
    private RandomAccessFile raf;// = new RandomAccessFile("/SystemCatalog/TablaEsquema.dat", "rw");;//modificar!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private String FILE_NAME = "EsquemasRegistros.txt";
    private long numeroDeRegistros;
    
    public TablaEsquemas() throws FileNotFoundException{
        try {
            this.raf = new RandomAccessFile("C:/urSQL/SystemCatalog/TablaEsquema.dat", "rw");
        } catch (FileNotFoundException ex) {
            File directorio= new File ("C:\\urSQL\\SystemCatalog");
            directorio.mkdirs();
            this.raf = new RandomAccessFile("C:/urSQL/SystemCatalog/TablaEsquema.dat", "rw");
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
    public void introducirEsquemaEnArchivo(long num, RegistroEsquema registro)throws IOException{
        this.raf.seek(num * registro.TAMANO);
        byte[] registroB = registro.toBytes();
        this.raf.write(registroB);
        this.numeroDeRegistros++;
    }
    
    private void incrementarRegistros() throws IOException{
        this.numeroDeRegistros = this.numeroDeRegistros + 1;
        File archivo=new File(this.FILE_NAME);

        //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
        FileWriter escribir=new FileWriter(archivo,false);

        //Escribimos en el archivo con el metodo write 
        escribir.write(Integer.toString((int) this.numeroDeRegistros));

        try {
            //Cerramos la conexion
            escribir.close();
        } catch (IOException ex) {
            Logger.getLogger(TablaTablas.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }
    public RegistroEsquema obtenerEsquemaDesdeArchivo(long num)throws IOException{
        this.raf.seek(num * RegistroEsquema.TAMANO);
        byte[] registroB = new byte[RegistroEsquema.TAMANO];
        this.raf.read(registroB);
        return RegistroEsquema.registroDesdeBytes(registroB);
    }
    
    public int obtenerIdEsquemaDeTabla(String nombre)throws IOException{
        int resp = -1;
        if(this.numeroDeRegistros>0){
            for(long i = 0; i < this.numeroDeRegistros; i++){
                RegistroEsquema tmp = obtenerEsquemaDesdeArchivo(i);
                if(tmp.getNombreEsquema().equals(nombre)){
                    resp = tmp.getEsquemaId();
                }
            }
        }
        return resp;
    }
    public ArrayList<String> obtenerTodosLosEsquemas() throws IOException{
        if(this.numeroDeRegistros>0){
            ArrayList<String> resp = new ArrayList<String>();
            for(long i = 0; i < this.numeroDeRegistros; i++){
                RegistroEsquema tmp = obtenerEsquemaDesdeArchivo(i);
                resp.add(tmp.getNombreEsquema());
            }
            return resp;
        }
        else{
            return null;
        }
    }
}
