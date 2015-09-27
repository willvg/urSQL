
package systemcatalog;
import estructurasDatos.*;


public class RegistroTabla {
    
    private int tablaId;
    private String nombreTabla;
    private long ubicacionTabla;
    private int esquemaDeTablaID;
    private String columnaLlave;
    private boolean borrado;
    private static final int LIMITE_NOMBRE = 100;
    public static final int TAMANO = 4 + 2 * LIMITE_NOMBRE + 8 + 4 + 2 * LIMITE_NOMBRE + 1;
     
    
    public RegistroTabla(int id, String nombre, long ubicacion, int esquemaId, String llave, boolean borrado){
        this.tablaId = id;
        this.nombreTabla = nombre;
        this.ubicacionTabla = ubicacion;
        this.esquemaDeTablaID = esquemaId;
        this.columnaLlave = llave;
        //this.borrado = false; 
    }        

    public void setTablaId(int tablaId) {
        this.tablaId = tablaId;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public void setUbicacionTabla(long ubicacionTabla) {
        this.ubicacionTabla = ubicacionTabla;
    }

    public void setEsquemaDeTablaID(int esquemaDeTablaID) {
        this.esquemaDeTablaID = esquemaDeTablaID;
    }

    public void setColumnaLlave(String columnaLlave) {
        this.columnaLlave = columnaLlave;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }
   
    public int getTablaId() {
        return tablaId;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public long getUbicacionTabla() {
        return ubicacionTabla;
    }

    public int getEsquemaDeTablaID() {
        return esquemaDeTablaID;
    }

    public String getColumnaLlave() {
        return columnaLlave;
    }

    public boolean isBorrado() {
        return borrado;
    }
    
    
    
    public byte[] toBytes(){
        byte[] nuevoRegistro = new byte[TAMANO];
        int contador = 0;
        Empaquetamiento.empacarInt(tablaId, nuevoRegistro, contador);
        contador += 4;
        Empaquetamiento.empacarStringLimitado(nombreTabla, LIMITE_NOMBRE, nuevoRegistro, contador);
        contador += 2*LIMITE_NOMBRE;
        Empaquetamiento.empacarLong(ubicacionTabla, nuevoRegistro, contador);
        contador += 8;
        Empaquetamiento.empacarInt(esquemaDeTablaID, nuevoRegistro, contador);
        contador += 4;
        Empaquetamiento.empacarStringLimitado(columnaLlave, LIMITE_NOMBRE, nuevoRegistro, contador);//ojo con el limite que se usa
        contador += 2*LIMITE_NOMBRE;
        Empaquetamiento.empaquetarBoolean(borrado, nuevoRegistro, contador);
        return nuevoRegistro;
    }
    
    public static RegistroTabla registroDesdeBytes(byte[]record){
        int contador = 0;
        int id = Empaquetamiento.desempacarEntero(record, contador);
        contador += 4;
        String nombre = Empaquetamiento.desempacarStringLimitado(LIMITE_NOMBRE, record, contador);
        contador += 2*LIMITE_NOMBRE;
        long ubicacion = Empaquetamiento.desempacarLong(record, contador);
        contador += 8;
        int esqueId = Empaquetamiento.desempacarEntero(record, contador);
        contador += 4;
        String llave = Empaquetamiento.desempacarStringLimitado(LIMITE_NOMBRE, record, contador);//ojo con el lim
        contador += 2*LIMITE_NOMBRE;
        boolean borrado = Empaquetamiento.desempacarBoolean(record, contador);
        return new RegistroTabla(id,nombre,ubicacion, esqueId, llave, borrado);
    }
    
    
}
