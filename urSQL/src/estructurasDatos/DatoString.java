
package estructurasDatos;

public class DatoString implements IDato{

    private ClaveString _clave;
    private byte [] _cuerpo;
    
    public DatoString (String pClave, byte [] pCuerpo, int pTamanoMax){
        _clave = new ClaveString(pClave, pTamanoMax);
        _cuerpo = pCuerpo;
    }
    
    public DatoString (int pTamanoMaxClave, int pTamanoCuerpo){
        _clave = new ClaveString(pTamanoMaxClave);
        _cuerpo = new byte [pTamanoCuerpo];
    }
    
    public void copiarDeCuerpo(byte [] buffer, int offset){
        for(int i = 0; i < _cuerpo.length; i++){
            buffer[offset] = _cuerpo [i];
            offset++;
        }
    }
    
    public void copiarACuerpo(byte[] buffer, int offset){
        for(int i = 0; i < _cuerpo.length; i++){
            _cuerpo[i] = buffer[offset++];
        }
    }
    
    
    @Override
    public void empacar(byte[] buffer, int offset) {
         _clave.empacar(buffer, offset);
         copiarDeCuerpo(buffer, offset + ((ClaveString) _clave).getTamanoMax());
    }

    @Override
    public void desempacar(byte[] buffer, int offset) {
        _clave.desempacar(buffer, offset);
        copiarACuerpo(buffer, offset + ((ClaveString) _clave).getTamanoMax());
    }

    @Override
    public IClave getClave() {
        return _clave;
    }

    @Override
    public void imprimirClave() {
        System.out.println(_clave.getClave());
    }
    
}
