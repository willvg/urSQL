package ExecutorEngine;

import estructurasDatos.*;
import java.util.ArrayList;

public class ConversorDeInsert {
    private ArrayList<String> columnasTabla;
    private ArrayList<String> tiposDeDatos;
    private ArrayList<String> columnasInsertadas;
    private String columnaId;
    //private byte[] datos;
    private int tamanoBytes; 
    private String enteroP = "0";
    private String longP = "0";
    private String doubleP = "0.0";
    private String bolP = "true";
    private String charP = "\u0000";
    private String stringP = "";
    
    private static final int LIMITE_STRING = 100;
    
    public ConversorDeInsert(int tamanoBytes){
        this.columnasTabla = new ArrayList<String>();
        this.tiposDeDatos = new ArrayList<String>();
        this.columnasInsertadas = new ArrayList<String>(); 
        this.columnaId = "";
        this.tamanoBytes = tamanoBytes;
        //this.datos = new byte[tamanoBytes];
    }
    
    public ConversorDeInsert(ArrayList<String> columnasTabla, ArrayList<String> tiposDeDatos,
           ArrayList<String> columnasInsertadas , String coluId){
        this.columnasTabla = columnasTabla;
        this.tiposDeDatos = tiposDeDatos;
        this.columnasInsertadas = columnasInsertadas;
        this.columnaId = coluId;
        this.tamanoBytes = calculaTamano(tiposDeDatos);
        //this.datos = new byte[tamanoBytes];
    }
    
    private int calculaTamano(ArrayList<String> tiposDeDatos){
        int resp = 0;
        for(int i = 0; i < tiposDeDatos.size();i++){
           if(tiposDeDatos.get(i).equals("boolean")){
               resp +=1;
           }
           if(tiposDeDatos.get(i).equals("char")){
               resp +=2;
           }
           if(tiposDeDatos.get(i).equals("String")){
               resp += 2*LIMITE_STRING;
           }
           if(tiposDeDatos.get(i).equals("double")){
               resp += 8;
           }
           if(tiposDeDatos.get(i).equals("int")){
               resp +=4;
            }
            else{//long
               resp +=4;
            }
        }
        return resp;
    }

    public void setColumnasTabla(ArrayList<String> columnasTabla) {
        this.columnasTabla = columnasTabla;
    }

    public void setTiposDeDatos(ArrayList<String> tiposDeDatos) {
        this.tiposDeDatos = tiposDeDatos;
    }

    public void setColumnasInsertadas(ArrayList<String> columnasInsertadas) {
        this.columnasInsertadas = columnasInsertadas;
    }

    public void setColumnaId(String columnaId) {
        this.columnaId = columnaId;
    }

    public ArrayList<String> getColumnasTabla() {
        return columnasTabla;
    }

    public ArrayList<String> getTiposDeDatos() {
        return tiposDeDatos;
    }

    public ArrayList<String> getColumnasInsertadas() {
        return columnasInsertadas;
    }

    public String getColumnaId() {
        return columnaId;
    }

    //////////////////////////Metodos principales////////////////////////////////////////////
   
    
    public DatoInt obtenerByteDeArrayDatoInt( ArrayList<String> datos){//datos ya viene sin id
        int posId = this.columnasTabla.indexOf(this.columnaId);
        //String tipoDatoId = this.tiposDeDatos.get(posId);
        String datoIdS = datos.get(posId);
        int datoId = Integer.parseInt(datoIdS);
        ArrayList<String> columnas = this.columnasTabla;
        ArrayList<String> tipos = this.tiposDeDatos;
        columnas.remove(posId);
        tipos.remove(posId);
        byte[] nuevosDatos = rellenarByteArray( columnas, tipos, datos);
        return new DatoInt(datoId, nuevosDatos);
    }
    
    private int moverCont(int cont, String tipo){ 
        if(tipo.equals("boolean")){
            cont +=1;
            return cont;
        }
        if(tipo.equals("char")){
            cont+=2;
            return cont;
        }
        if(tipo.equals("String")){
            cont += 2*LIMITE_STRING;
            return cont;
        }
        if(tipo.equals("double")){
            cont += 8;
            return cont;
        }
        if(tipo.equals("int")){
            cont +=4;
            return cont;
        }
        else{//long
            cont +=4;
            return cont;
        }
    }
    
    private byte[] rellenarByteArray( ArrayList<String> columnas, ArrayList<String> tipos, 
            ArrayList<String> datos){
        byte[] resp = new byte[tamanoBytes];
        int contB = 0;//contador de arreglo de bytes 
        int contColInsertadas = 0;//contador columnas de interes
        for(int i = 0; i < datos.size();i++){
            if(columnas.get(i).equals(this.columnasInsertadas.get(contColInsertadas))){//i es contador de columnas y tipos
                if(tipos.get(i).equals("boolean")){///meter los de datos
                   boolean a = Boolean.parseBoolean(datos.get(contColInsertadas));
                   Empaquetamiento.empaquetarBoolean(a, resp, contB);
                }
                if(tipos.get(i).equals("char")){
                   char a = datos.get(contColInsertadas).charAt(0);
                   Empaquetamiento.empacarChar(a, resp, contB);
                }
                if(tipos.get(i).equals("String")){
                   Empaquetamiento.empacarStringLimitado(datos.get(contColInsertadas),
                          LIMITE_STRING, resp, contB);
                }
                if(tipos.get(i).equals("double")){
                   double a = Double.parseDouble(datos.get(contColInsertadas));
                   Empaquetamiento.empacarDouble(a, resp, contB);
                }
                if(tipos.get(i).equals("int")){
                   int a = Integer.parseInt(datos.get(contColInsertadas));
                   Empaquetamiento.empacarInt(a, resp, contB);
                }
                else{//long
                   long a = Long.parseLong(datos.get(contColInsertadas));
                   Empaquetamiento.empacarLong(a, resp, contB);
                }
                contColInsertadas++;
            }
            else{////meter prederminados
                if(tipos.get(i).equals("boolean")){
                   boolean a = Boolean.parseBoolean(this.bolP);
                   Empaquetamiento.empaquetarBoolean(a, resp, contB);
                }
                if(tipos.get(i).equals("char")){
                   char a = this.charP.charAt(0);
                   Empaquetamiento.empacarChar(a, resp, contB);
                }
                if(tipos.get(i).equals("String")){
                    Empaquetamiento.empacarStringLimitado(datos.get(contColInsertadas),
                          LIMITE_STRING, resp, contB);
                }
                if(tipos.get(i).equals("double")){
                   double a = Double.parseDouble(this.doubleP);
                   Empaquetamiento.empacarDouble(a, resp, contB);
                }
                if(tipos.get(i).equals("int")){
                   int a = Integer.parseInt(this.enteroP);
                   Empaquetamiento.empacarInt(a, resp, contB);
                }
                else{//long
                    long a = Long.parseLong(this.longP);
                    Empaquetamiento.empacarLong(a, resp, contB);
                }
            }
            contB = moverCont(contB, tipos.get(i));
        }
        return resp;
    }
    
   
}
