
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
import ExecutorEngine.*;

public class SystemCatalog {
    
    TablaEsquemas TablaDeEsquemas;
    TablaTablas TablaDeTablas;
    private RandomAccessFile raf;
    private RandomAccessFile raf2;
    private ArrayList<String> tiposColumnas;
    private ArrayList<String> columnasColumnas;
    private String FILE_NAME = "raicesDelArbolCatalog.txt";
    private int cantidadColumnas;
    private static final int LIMITE_STRING = 100;
    
    public SystemCatalog() throws FileNotFoundException, IOException{
        try {
            this.TablaDeEsquemas = new TablaEsquemas();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SystemCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.TablaDeTablas = new TablaTablas();
        try {
            this.raf = new RandomAccessFile("C:/urSQL/SystemCatalog/TablaColumnas.dat", "rw");
        } catch (FileNotFoundException ex) {
            File directorio= new File ("C:\\urSQL\\SystemCatalog");
            directorio.mkdirs();
            this.raf = new RandomAccessFile("C:/urSQL/SystemCatalog/TablaColumnas.dat", "rw");
        }
        try {
            this.raf2 = new RandomAccessFile("C:/urSQL/SystemCatalog/TablaRestricciones.dat", "rw");
            //inicia el arbol desde 0 con -1, con mas registros cambia y hay que ir actualizando  
        } catch (FileNotFoundException ex) {
            File directorio= new File ("C:\\urSQL\\SystemCatalog");
            directorio.mkdirs();
            try {
                this.raf = new RandomAccessFile("C:/urSQL/SystemCatalog/TablaColumnas.dat", "rw");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(SystemCatalog.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        int cont = 0;
        String linea = "";
        String[] raices = new String[2];
        try {
            FileReader reader = new FileReader(this.FILE_NAME);
            BufferedReader buffReader = new BufferedReader(reader);
            try {
                while((linea = buffReader.readLine()) != null && cont < 3){
                    raices[cont] = linea;
                            cont++;
                }
            } catch (IOException ex) {
                Logger.getLogger(SystemCatalog.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SystemCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int pTamanoDato1 = 4 + 4 + 2*LIMITE_STRING + 2*LIMITE_STRING;
        int pTamanoDato2 = 4 + 4 + 2*LIMITE_STRING;
        
        this.columnasColumnas = new ArrayList<String>();
        this.tiposColumnas = new ArrayList<String>();
        
        this.columnasColumnas.add("idColumna");
        this.columnasColumnas.add("idTabla");
        this.columnasColumnas.add("nombre");
        this.columnasColumnas.add("tipo");
        
        this.tiposColumnas.add("int");
        this.tiposColumnas.add("int");
        this.tiposColumnas.add("string");
        this.tiposColumnas.add("string");
        
        File directorio= new File ("CantidadColumnas.txt");
        //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
        FileReader escribir = new FileReader(directorio);

        //Escribimos en el archivo con el metodo write 
        BufferedReader br = new BufferedReader(escribir);
        this.cantidadColumnas=Integer.parseInt(br.readLine());

        try {
            //Cerramos la conexion
            escribir.close();
        } catch (IOException ex) {
            Logger.getLogger(TablaTablas.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        //ArbolBmas arbol1 = new ArbolBmas(Long.parseLong(raices[0]), raf, pTamanoDato1, 4); // cedula / numTelefono tramanodato
        //ArbolBmas arbol2 = new ArbolBmas(Long.parseLong(raices[1]), raf2, pTamanoDato2, 4);//   4B   /     4B 
    }

    public TablaEsquemas getTablaDeEsquemas() {
        return TablaDeEsquemas;
    }

    public TablaTablas getTablaDeTablas() {
        return TablaDeTablas;
    }

    public String getCantidadColumnas() {
        return Integer.toString(cantidadColumnas);
    }
    
    public void incrementar() throws IOException{
        cantidadColumnas++;
        File directorio= new File ("CantidadColumnas.txt");
        //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
        FileWriter escribir=new FileWriter(directorio,false);

        //Escribimos en el archivo con el metodo write 
        int r= (int) this.cantidadColumnas;
        
        escribir.write(Integer.toString(r));

        try {
            //Cerramos la conexion
            escribir.close();
        } catch (IOException ex) {
            Logger.getLogger(TablaTablas.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public String DisplayDataBaseCatalog(String nombre) throws IOException{
        int idEsquema = this.TablaDeEsquemas.obtenerIdEsquemaDeTabla(nombre);
        int idTabla = 0;
        int idColumna = 0;
        String resp = "";
        //String tmp = "";
        ArrayList<String> restriccionesDeUnaColumna = new ArrayList<String>();
        ArrayList<String> nombresDeTablas = this.TablaDeTablas.obtenerTablasDeEsquema(idEsquema);
        for(int i = 0; i < nombresDeTablas.size(); i++){
            idTabla = this.TablaDeTablas.obtenerIdTabla(nombre);
            ArrayList<String> columnasDeUnaTabla = ObtenerColumnasTablas(idTabla);
            ArrayList<String> tiposDeDatosColumnas = ObtenerTiposDeColumna(idTabla);
            for(int j = 0; j < columnasDeUnaTabla.size(); j++){
                idColumna = ObtenerIdColumna();
                restriccionesDeUnaColumna = ObtenerRestriccionesColumna(idColumna);
            }
            resp = "@"+nombresDeTablas.get(i)+"/" +DisplayDataAux(columnasDeUnaTabla.get(i), tiposDeDatosColumnas.get(i), restriccionesDeUnaColumna) + ", ";
            resp = resp.substring(0, (resp.length()-2));
        }
        return resp;
    }
    
    private String DisplayDataAux( String columna, String tipo, ArrayList<String> restricciones){
        String restric = "";
        for(int i = 0; i < restricciones.size(); i++){
            restric = restric + " " +restricciones.get(i);
        }
        return columna + " [" +  tipo + " ("+ restric +")]";
    }

    public ArrayList<String> ObtenerColumnasTablas(int idTabla) throws IOException {
        ArrayList<String> select = new ArrayList<String> ();
        select.add("SELECT");
        select.add("nombre");
        select.add("FROM");
        select.add("TablaColumnas");
        select.add("WHERE");
        select.add(Integer.toString(idTabla));
        ProcesadorComandosDML proDML = new ProcesadorComandosDML(this.columnasColumnas, this.tiposColumnas);
        String a = proDML.Comando_select(select);
        return conversorArray(a, "nombre");
    }

    public ArrayList<String> ObtenerTiposDeColumna(int idTabla) throws IOException {
       ArrayList<String> select = new ArrayList<String> ();
       select.add("SELECT");
       select.add("tipo");
       select.add("FROM");
       select.add("TablaColumnas");
       select.add("WHERE");
       select.add(Integer.toString(idTabla)); 
       ProcesadorComandosDML proDML = new ProcesadorComandosDML(this.columnasColumnas,this.tiposColumnas);
       proDML.Comando_select(select);
       String a = proDML.Comando_select(select);
       return conversorArray(a, "tipo");
    }
    
    private ArrayList<String> conversorArray(String entrada, String columna){
        String tmp = "";
        entrada = entrada.substring(columna.length() + 1, entrada.length());
        ArrayList<String> resp = new ArrayList<String>();
        for(int i = 0; i < entrada.length(); i++){
            tmp = tmp + entrada.charAt(i);
            if(entrada.charAt(i) == ','){
                tmp = tmp.substring(0, (tmp.length()-1));
                resp.add(tmp);
                tmp = "";
            }
        }
        return resp;
        
    }
    
    
    public void IntroducirColumnasTabla(String cantidadColumnas,String idTabla, String nombre, String tipo) throws IOException{ 
       ArrayList<String> insert = new ArrayList<String> ();
       insert.add("INSERT");
       insert.add("INTO");
       insert.add("TablaColumnas");
       insert.add("(");
       insert.add("idColumna");
       insert.add("idTabla");
       insert.add("nombre");
       insert.add("tipo");
       insert.add(")");
       insert.add("VALUES");
       insert.add("(");
       insert.add(cantidadColumnas);
       insert.add(idTabla);
       insert.add(nombre);
       insert.add(tipo);
       insert.add(")");
       
       ProcesadorComandosDML proDML = new ProcesadorComandosDML(this.columnasColumnas,this.tiposColumnas);
       proDML.Comando_insert(insert);
       
    }  
    private int ObtenerIdColumna() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<String> ObtenerRestriccionesColumna(int idColumna) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
