
package estructurasDatos;

public class ClaveString implements IClave{
    
    private String _clave;
    private int _tamanoMax;
    
    
    //debe recibir el tamano maximo en caracteres del string
    public ClaveString(String pClave, int pTamanoMax){
        _clave = pClave;
        _tamanoMax = pTamanoMax;
    }
    
    public ClaveString(int pTamanoMax){
        _clave = "";
    }
    
    public String getClave(){
        return _clave;
    }
    
    public void setClave(String pClave){
        _clave = pClave;
    }
    
    public int getTamanoMax(){
        return _tamanoMax;
    }

    @Override
    public void empacar(byte[] buffer, int offset) {
        Empaquetamiento.empacarStringLimitado(_clave, _tamanoMax, buffer, offset);
    }

    @Override
    public void desempacar(byte[] buffer, int offset) {
        Empaquetamiento.desempacarStringLimitado(_tamanoMax, buffer, offset);
    }

    @Override
    public boolean menorque(IClave pDato) {
        return _clave.compareTo(((ClaveString) pDato).getClave()) < 0;
    }

    @Override
    public boolean mayorque(IClave pDato) {
        return _clave.compareTo(((ClaveString) pDato).getClave()) > 0;
    }

    @Override
    public boolean igualque(IClave pDato) {
        return _clave.compareTo(((ClaveString) pDato).getClave()) == 0;
    }
    
    
            
}
