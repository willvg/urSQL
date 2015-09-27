
package ExecutorEngine;


public class TuplaDeConversor {
    private String columna;
    private String dato;
    
    public TuplaDeConversor(){
        this.columna = null;
        this.dato = null;
    }
    
    public TuplaDeConversor(String columna, String dato){
        this.columna = columna;
        this.dato = dato;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getColumna() {
        return columna;
    }

    public String getDato() {
        return dato;
    }
    
    
    
}
