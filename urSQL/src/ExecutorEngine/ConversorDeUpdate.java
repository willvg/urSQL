package ExecutorEngine;

import estructurasDatos.*;
import java.util.ArrayList;


public class ConversorDeUpdate {
    private ArrayList<String> columnasTabla;
    private ArrayList<String> tiposDeDatos;
    private ArrayList<TuplaDeConversor> listaObjetos;
    private String columnaId;
    private byte[] datosAntiguos;
    private int identificador;
    
    private static final int LIMITE_STRING = 100;
    
    public ConversorDeUpdate(int tamanoBytes){
        this.columnasTabla = new ArrayList<String>();
        this.tiposDeDatos = new ArrayList<String>();
        this.listaObjetos = new ArrayList<TuplaDeConversor>();
        this.datosAntiguos = new byte[tamanoBytes];
        this.columnaId = "";
        this.identificador = 0;
    }
    
    public ConversorDeUpdate(ArrayList<String> columnasTabla, ArrayList<String> tiposDeDatos, String columnaId, int id, byte[] datosAntiguos){
        int a = columnasTabla.indexOf(columnaId);
        columnasTabla.remove(a);
        tiposDeDatos.remove(a);
        this.columnasTabla = columnasTabla;
        this.tiposDeDatos = tiposDeDatos;
        this.datosAntiguos = datosAntiguos;
        //this.datosAntiguos = new byte[calculaTamano(tiposDeDatos)];
        this.listaObjetos = new ArrayList<TuplaDeConversor>();
        this.columnaId = columnaId;
        this.identificador = id;
    }

    public void setColumnasTabla(ArrayList<String> columnasTabla) {//eliminar antes llave
        this.columnasTabla = columnasTabla;
    }

    public void setTiposDeDatos(ArrayList<String> tiposDeDatos) {//eliminar antes llave
        this.tiposDeDatos = tiposDeDatos;
    }

    public void setListaObjetos(ArrayList<TuplaDeConversor> listaObjetos) {
        this.listaObjetos = listaObjetos;
    }

    public void setColumnaId(String columnaId) {
        this.columnaId = columnaId;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public ArrayList<String> getColumnasTabla() {
        return columnasTabla;
    }

    public ArrayList<String> getTiposDeDatos() {
        return tiposDeDatos;
    }

    public ArrayList<TuplaDeConversor> getListaObjetos() {
        return listaObjetos;
    }

    public String getColumnaId() {
        return columnaId;
    }

    public int getIdentificador() {
        return identificador;
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
    
     public void insertarTupla(String columna, String valor){
         TuplaDeConversor a = new TuplaDeConversor(columna, valor);
         this.listaObjetos.add(a);
     }
     
     public DatoInt obtenerDatoInt(){
         byte[] datosNuevos = this.datosAntiguos;
         for(int i = 0; i < this.listaObjetos.size(); i++){
            actualizarByteArray(datosNuevos,this.listaObjetos.get(i).getColumna(), this.listaObjetos.get(i).getDato());
         }
         return new DatoInt(this.identificador, datosNuevos);
         
     }
     
     private byte[] actualizarByteArray(byte[] datos, String columna, String datoNuevo){
         int posicionLista = this.columnasTabla.indexOf(columna);
         String tipo = this.tiposDeDatos.get(posicionLista);
         int posicionB;
         if(tipo.equals("boolean")){
             boolean a = Boolean.parseBoolean(datoNuevo);
             posicionB = obtenerPosicionEnByteArray(posicionLista);
             Empaquetamiento.empaquetarBoolean(a, datos, posicionB);
         }
         if(tipo.equals("char")){
             char a = datoNuevo.charAt(0);
             posicionB = obtenerPosicionEnByteArray(posicionLista);
             Empaquetamiento.empacarChar(a, datos, posicionB);
         }
         if(tipo.equals("String")){
             posicionB = obtenerPosicionEnByteArray(posicionLista);
             Empaquetamiento.empacarStringLimitado(datoNuevo, LIMITE_STRING, datos, posicionB);
         }
         if(tipo.equals("double")){
             double a = Double.parseDouble(datoNuevo);
             posicionB = obtenerPosicionEnByteArray(posicionLista);
             Empaquetamiento.empacarDouble(a, datos, posicionB);
         }
         if(tipo.equals("int")){
             int a = Integer.parseInt(datoNuevo);
             posicionB = obtenerPosicionEnByteArray(posicionLista);
             Empaquetamiento.empacarInt(a, datos, a);
         }
         else{//long
             long a = Long.parseLong(datoNuevo);
             posicionB = obtenerPosicionEnByteArray(posicionLista);
             Empaquetamiento.empacarLong(a, datos, posicionB);
         }
         return datos;
     }
     
     private int obtenerPosicionEnByteArray(int limite){
         int respCont = 0;
         for(int i = 0; i < limite; i++){
             respCont = moverCont(respCont, this.tiposDeDatos.get(i));
         }
         return respCont;
     }
}
