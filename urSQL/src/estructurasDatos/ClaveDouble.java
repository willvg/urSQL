
package estructurasDatos;

public class ClaveDouble implements IClave{
    
    private double _clave;
    
    public ClaveDouble(double pClave){
        _clave = pClave;
    }
    
    public ClaveDouble(){
        _clave = -1;
    }
    
    public double getClave(){
        return _clave;
    }
    
    public void setClave(Double pClave){
        _clave = pClave;
    }
    
    @Override
    public void empacar(byte[] buffer, int offset) {
        Empaquetamiento.empacarDouble(_clave, buffer, offset);
    }

    @Override
    public void desempacar(byte[] buffer, int offset) {
        _clave = Empaquetamiento.desempacarDouble(buffer, offset);
    }

    @Override
    public boolean menorque(IClave pDato) {
        return _clave < ((ClaveDouble) pDato).getClave();
    }

    @Override
    public boolean mayorque(IClave pDato) {
        return _clave > ((ClaveDouble) pDato).getClave();
    }
    
    //*WILL
    public boolean igualque(IClave pDato) {
        return _clave == ((ClaveDouble) pDato).getClave();
    }
    
}
