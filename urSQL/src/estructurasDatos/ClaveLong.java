
package estructurasDatos;

public class ClaveLong implements IClave{

    private long _clave;
    
    public ClaveLong(long pClave){
        _clave = pClave;
    }
    
    public ClaveLong(){
        _clave = -1;
    }
    
    
    public long getClave(){
        return _clave;
    }
    
    public void setClave(long pClave){
        _clave = pClave;
    }
    
    
    @Override
    public void empacar(byte[] buffer, int offset) {
        Empaquetamiento.empacarLong(_clave, buffer, offset);
    }

    @Override
    public void desempacar(byte[] buffer, int offset) {
        _clave = Empaquetamiento.desempacarLong(buffer, offset);
    }

    @Override
    public boolean menorque(IClave pDato) {
        return _clave < ((ClaveLong) pDato).getClave();
    }

    @Override
    public boolean mayorque(IClave pDato) {
        if (_clave > ((ClaveLong) pDato).getClave() == true){
            return true;   
        } else {
            return false;
        }
    }

    @Override
    public boolean igualque(IClave pDato) {
        return _clave == ((ClaveLong) pDato).getClave();
    }
    
}
