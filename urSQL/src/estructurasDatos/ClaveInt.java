package estructurasDatos;

public class ClaveInt implements IClave{
    private int _clave;
    
    public ClaveInt(int pClave){
        _clave = pClave;
    }
    
    public ClaveInt(){
        _clave = -1;
    }
    
    public int getClave(){
        return _clave;
    }
    
    public void setClave(int pClave){
        _clave = pClave;
    }
    
    @Override
    public void empacar(byte[] buffer, int offset) {
        Empaquetamiento.empacarInt(_clave, buffer, offset);
    }

    @Override
    public void desempacar(byte[] buffer, int offset) {
        _clave = Empaquetamiento.desempacarEntero(buffer, offset);
    }

    @Override
    public boolean menorque(IClave pDato) {
        if (_clave < ((ClaveInt) pDato).getClave()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mayorque(IClave pDato) {
        if (_clave > ((ClaveInt) pDato).getClave()){
            return true;
        } else {
            return false;
        }
    }
    
    //*WILL
    public boolean igualque(IClave pDato) {
        if (_clave == ((ClaveInt) pDato).getClave()){
            return true;
        } else {
            return false;
        }
    }
}
