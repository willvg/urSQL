
package ExecutorEngine;
import java.util.ArrayList;
import estructurasDatos.*;

public class ConversorDeSelect {
     
    private ArrayList<String> columnasTabla;
    private ArrayList<String> columnasRequeridas;//solo select
    private ArrayList<String> tiposDeDatos;
    private ArrayList<TuplaDeConversor> listaObjetos;
    private static final int LIMITE_STRING = 100;
    
    public ConversorDeSelect(){
        this.columnasTabla = new ArrayList<String>();
        this.columnasRequeridas = new ArrayList<String>();
        this.tiposDeDatos = new ArrayList<String>();
        this.listaObjetos = new ArrayList<TuplaDeConversor>();
    }
    
    public ConversorDeSelect(ArrayList<String> columnasTabla, ArrayList<String> columnasRequeridas,
            ArrayList<String> tiposDeDatos ){
        this.columnasTabla = columnasTabla;
        this.columnasRequeridas = columnasRequeridas;
        this.tiposDeDatos = tiposDeDatos;
        this.listaObjetos = new ArrayList<TuplaDeConversor>();
    }
    

    public void setColumnasTabla(ArrayList<String> columnasTabla) {
        this.columnasTabla = columnasTabla;
    }

    public void setColumnasRequeridas(ArrayList<String> columnasRequeridas) {
        this.columnasRequeridas = columnasRequeridas;
    }

    public void setTiposDeDatos(ArrayList<String> tiposDeDatos) {
        this.tiposDeDatos = tiposDeDatos;
    }

    public ArrayList<String> getColumnasTabla() {
        return columnasTabla;
    }

    public ArrayList<String> getColumnasRequeridas() {
        return columnasRequeridas;
    }

    public ArrayList<String> getTiposDeDatos() {
        return tiposDeDatos;
    }
    
    //////////////////////////Metodos para pasar de arreglo de bytes a string///////////////////////////////////////
    
    public void convertir(byte[]datos){
        int contaByte = 0;
        int contaCol = 0;
        int contaCD = 0;
        int limiteRequeridas = this.columnasRequeridas.size();
        //String resp = "";
        String dato = "";
        TuplaDeConversor tupla = new TuplaDeConversor();
        while(contaByte < datos.length){
            if(this.columnasTabla.get(contaCol).equals(this.columnasRequeridas.get(contaCD))){
                dato = convertirAux(contaByte, this.tiposDeDatos.get(contaCol), datos);
                tupla.setColumna(this.columnasRequeridas.get(contaCD));
                tupla.setDato(dato);
                this.listaObjetos.add(tupla);
                contaCD++;
            }
            if(contaCD == this.columnasRequeridas.size()){
                break;
            }
            contaByte = moverCont(contaByte, this.tiposDeDatos.get(contaCol));
            contaCol++;
            
        }
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
    
    private String convertirAux(int contador, String tipo, byte[]datos){
        if(tipo.equals("boolean")){
            boolean a = Empaquetamiento.desempacarBoolean(datos, contador);
            return Boolean.toString(a);
        }
        if(tipo.equals("char")){
            char a = Empaquetamiento.desempacarChar(datos, contador);
            return Character.toString(a);
        }
        if(tipo.equals("String")){
            String a = Empaquetamiento.desempacarStringLimitado(LIMITE_STRING, datos, contador);
            return a;
        }
        if(tipo.equals("double")){
            double a = Empaquetamiento.desempacarDouble(datos, contador);
            return Double.toString(a);
        }
        if(tipo.equals("int")){
            int a = Empaquetamiento.desempacarEntero(datos, contador);
            return Integer.toString(a);
        }
        else{
            long a = Empaquetamiento.desempacarLong(datos, contador);
            return Long.toString(a);
        }
    }
    
    public String obtenerStringConvertido(){
        String resp = "";
        int tamano = this.columnasRequeridas.size();
        for(int i = 0; i < tamano; i++){
            resp = this.columnasRequeridas.get(i)+"/"+ 
                    SConvertidoAux(this.columnasRequeridas.get(i))
                            .substring(0, (SConvertidoAux(this.columnasRequeridas.get(i)).length()-2))+"/";
        }
        if(resp.length()>=1){
         return resp.substring(0, (resp.length()- 1));
        }
        else{
            return resp;
        }
    }
    
    private String SConvertidoAux(String columna){
        String resp = "";
        int tamano = this.listaObjetos.size();
        for(int i = 0; i < tamano; i++){
            if(this.listaObjetos.get(i).getColumna().equals(columna)){
                resp = resp + this.listaObjetos.get(i).getDato()+ ", ";
            } 
        }
        return resp;
    }
     
        
}
