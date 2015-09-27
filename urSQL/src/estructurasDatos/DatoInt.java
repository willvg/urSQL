package estructurasDatos;

public class DatoInt implements IDato{
    private ClaveInt _clave;
    private byte [] _cuerpo;
    
    public DatoInt(int pClave, byte[] pCuerpo){
        _clave = new ClaveInt(pClave);
        _cuerpo = pCuerpo;
    }
    
    /*
    crea un dato sin clave y sin cuerpo sin embargo recibe el tamano de el cuerpo 
    para poder asignarle un tamano al arreglo
    */
    public DatoInt(int pTamanoCuerpo){
        _clave = new ClaveInt();
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
        Empaquetamiento.empacarInt(_clave.getClave(), buffer, offset);
        copiarDeCuerpo(buffer, offset + 4);
    }

    @Override
    public void desempacar(byte[] buffer, int offset) {
        _clave.setClave(Empaquetamiento.desempacarEntero(buffer, offset));
        copiarACuerpo(buffer, offset + 4);
    }

    @Override
    public IClave getClave() {
        return _clave;
    }
    
    public void imprimirClave(){
        System.out.println(_clave.getClave());
    }
    
}
