
package systemcatalog;
import estructurasDatos.*;


public class RegistroEsquema {
    
    private int esquemaId;
    private String nombreEsquema;
    private static final int LIMITE_NOMBRE = 100;
    private boolean borrado;
    public static final int TAMANO = 4 + 2 * LIMITE_NOMBRE +  1;
    
    public RegistroEsquema(int id, String nombre, boolean borrado){
        this.esquemaId = id;
        this.nombreEsquema = nombre;
        this.borrado = borrado;
    }

    public void setEsquemaId(int esquemaId) {
        this.esquemaId = esquemaId;
    }

    public void setNombreEsquema(String nombreEsquema) {
        this.nombreEsquema = nombreEsquema;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public int getEsquemaId() {
        return esquemaId;
    }

    public String getNombreEsquema() {
        return nombreEsquema;
    }

    public boolean isBorrado() {
        return borrado;
    }
    
    public byte[] toBytes(){
        byte[] nuevoRegistro = new byte[TAMANO];
        int contador = 0;
        Empaquetamiento.empacarInt(esquemaId, nuevoRegistro, contador);
        contador +=4;
        Empaquetamiento.empacarStringLimitado(nombreEsquema, LIMITE_NOMBRE, nuevoRegistro, contador);
        contador += 2*LIMITE_NOMBRE;
        Empaquetamiento.desempacarBoolean(nuevoRegistro, contador);
        return nuevoRegistro;
    }
    
     public static RegistroEsquema registroDesdeBytes(byte[]record){
        int contador = 0;
        int id = Empaquetamiento.desempacarEntero(record, contador);
        contador += 4;
        String nombre = Empaquetamiento.desempacarStringLimitado(LIMITE_NOMBRE, record, contador);
        contador += 2*LIMITE_NOMBRE;
        boolean borrado = Empaquetamiento.desempacarBoolean(record, contador);
        return new RegistroEsquema(id, nombre, borrado);
     }
            
}
