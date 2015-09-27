
package estructurasDatos;

public class DatoLong implements IDato{
    
    private ClaveLong _clave;
    private byte [] _cuerpo;
    
    public DatoLong(long pClave, byte[] pCuerpo){
        _clave = new ClaveLong(pClave);
        _cuerpo = pCuerpo;
    }
    
    public DatoLong(int pTamanoCuerpo){
        _clave = new ClaveLong();
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
        copiarDeCuerpo(buffer, offset + 8);
    }

    @Override
    public void desempacar(byte[] buffer, int offset) {
        _clave.desempacar(buffer, offset);
        copiarACuerpo(buffer, offset + 8);
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
