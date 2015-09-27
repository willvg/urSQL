
package estructurasDatos;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodoInterno {
    private IClave[] _claves;
    private long _miPosicion;
    private long _padre;
    private long _hijos[];
    private RandomAccessFile _archivo;
    private int _tamanoClave;
    private int _tamanoDato;
    private int _cantElementos;
    private String _tipoDato;
    private byte [] _buffer;
    
    /*
    Todo nodo interno debe tener como minimo un dato y dos hijos
    este constructor crea un nodo que aun no se ha guardado en disco
    */
    NodoInterno(RandomAccessFile pArchivo, long pPrimerHijo, long pSegundoHijo,
            IClave pPrimeraClave,int pTamanoDato, int pTamanoClave, String pTipoDato){
        _claves = new IClave [4];
        _hijos = new long [5];
        _archivo = pArchivo;
        _miPosicion = 0;
        _tamanoClave = pTamanoClave;
        _tamanoDato = pTamanoDato;
        _buffer = new byte [61 + _tamanoClave * 4];
        _hijos[0] = pPrimerHijo;
        _hijos[1] = pSegundoHijo;
        _claves[0] = pPrimeraClave;
        _tipoDato = pTipoDato;
        _cantElementos = 1;
    }

    /*
    crea un nodo que ya existe en disco y de una vez lo carga a memoria
    */
    NodoInterno(RandomAccessFile pArchivo, long pPosicion, int pTamanoDato, int pTamanoClave, String pTipoDato){
        _claves = new IClave [4];
        _hijos = new long [5];
        _archivo = pArchivo;
        _miPosicion = pPosicion;
        _tamanoClave = pTamanoClave;
        _tamanoDato = pTamanoDato;
        _tipoDato = pTipoDato;
        _buffer = new byte [61 + _tamanoClave * 4];
        cargarNodo();
    }
    
    /*
    carga el nodo con los daotos que se encuentre en _miPosicion
    */
    private void cargarNodo(){
        try{
            _archivo.seek(_miPosicion);
            _archivo.read(_buffer);
        }catch (IOException ex){
            System.out.println("Ha ocurrido un error cargando el nodo!");
        }
        
        _padre = Empaquetamiento.desempacarLong(_buffer, 9);
        _cantElementos = Empaquetamiento.desempacarEntero(_buffer, 17);
        
        for (int i = 0; i <= 4; i++){
            _hijos[i] = Empaquetamiento.desempacarLong(_buffer, 21 + 8 * i);
        }

        construirDatos();
        for (int i = 0; i <= 3; i++)
            _claves[i].desempacar(_buffer, 61 + _tamanoClave * i);
        
    }
    
    
    private void construirDatos(){
        switch(_tipoDato){
            case "int": {
                System.out.println("entro a construir clave int");
                for(int i = 0; i <= 3; i++)
                    _claves[i] = new ClaveInt();
            }break;
            case "double":{
                for(int i = 0; i <= 3; i++)
                    _claves[i] = new ClaveDouble();
            }break;
            case "string":{
                for(int i = 0; i <= 3; i++)
                    _claves[i] = new ClaveString(100);
            }break;
            case "long":{
                System.out.println("entro a construir clave long");
                for(int i = 0; i <= 3; i++)
                    _claves[i] = new ClaveLong();
            }break;
            default: System.out.println("Error:  Tipo de dato no permitido");   break;
        }
    }
    
    private long insertarReventando(IClave pClave, long pNuevoHijo, long pRaiz){
        IClave auxClave;
        long auxHijo;
        
        /*
        acomoda la clave en la posicion que le corresponde y el dato extra lo
        almacena en la varible aux
        */
        if(_claves[3].menorque(pClave)){
            auxClave = pClave;
            auxHijo = pNuevoHijo;
        }else{
            auxClave = _claves[3];
            auxHijo = _hijos[4];
            for (int i = 0; i <= 3; i++ ){
                if (_claves[i].mayorque(pClave)){
                    for(int j = 3; j > i; j--){
                        _claves[j] = _claves[j - 1];
                        _hijos[j + 1] = _hijos[j];
                    }
                    _claves[i] = pClave;
                    _hijos[i+1] = pNuevoHijo;
                    break;
                }
            }
        }
        
        this.guardarCambios();
        
        if (pRaiz == _miPosicion){
            /*
            empezamos el proceso de crear nuevos nodos y reacomodar los datos
            */
            NodoInterno nuevoHijo = new NodoInterno(_archivo, _hijos[3], _hijos[4],
                                                    _claves[3], _tamanoDato, _tamanoClave, _tipoDato);
            long posNuevoHijo = nuevoHijo.almacenarComoNuevoNodo();
            nuevoHijo.insertar(auxClave, auxHijo, pRaiz);
            nuevoHijo.reasignarPadres();
            
            NodoInterno nuevaRaiz = new NodoInterno(_archivo, _miPosicion, posNuevoHijo,
                                                    _claves[2], _tamanoDato, _tamanoClave, _tipoDato);
            pRaiz = nuevaRaiz.almacenarComoNuevoNodo();
            nuevaRaiz.guardarCambios();
            
            nuevoHijo.setPadre(pRaiz);
            nuevoHijo.guardarCambios();
            _padre = pRaiz;
            _cantElementos = 2;
            
        } else {
            NodoInterno nuevoHijo = new NodoInterno(_archivo, _hijos[3], _hijos[4],
                                                    _claves[3], _tamanoDato, _tamanoClave, _tipoDato);
            long posNuevoHijo = nuevoHijo.almacenarComoNuevoNodo();
            nuevoHijo.insertar(auxClave, auxHijo, pRaiz);
            nuevoHijo.setPadre(_padre);
            nuevoHijo.reasignarPadres();
            nuevoHijo.guardarCambios();
            
            
            NodoInterno padre = new NodoInterno(_archivo, _padre, _tamanoDato, _tamanoClave, _tipoDato);
            pRaiz = padre.insertar(_claves[2], posNuevoHijo, pRaiz);
            _cantElementos = 2;
            
            //cargarNodo();
        }
        return pRaiz;
  
    }
    
    private void insertarSinReventar(IClave pClave, long pNuevoHijo){
        if (_claves[_cantElementos - 1].menorque(pClave) == false){
            for (int i = 0; i < _cantElementos; i++ ){
                if (_claves[i].mayorque(pClave)){
                    for(int j = _cantElementos; j > i; j--){
                        _claves[j] = _claves[j - 1];
                        _hijos[j + 1] = _hijos[j];
                    }
                    _claves[i] = pClave;
                    _hijos[i + 1] = pNuevoHijo;
                    _cantElementos++;
                    return;
                }
            }
        } else {
            //en caso de que quede como el ultimo de los elementos
            _claves[_cantElementos] = pClave;
            _hijos[_cantElementos + 1] = pNuevoHijo;
            _cantElementos++;
        }
    }
    
    private byte[] empacarNodo(){
        byte[] buffer = new byte [61 + _tamanoClave * 4];
        Empaquetamiento.empaquetarBoolean(false, buffer, 0);
        Empaquetamiento.empacarLong(_miPosicion, buffer, 1);
        Empaquetamiento.empacarLong(_padre, buffer, 9);
        Empaquetamiento.empacarInt(_cantElementos, buffer, 17);
        
        for(int i = 0; i <= 4; i++){
            Empaquetamiento.empacarLong(_hijos[i], buffer, 21 + 8 * i);
        }
        
        for(int i = 0; i < _cantElementos; i++){
            _claves[i].empacar(buffer, 61 + (_tamanoClave * i));
        }
        return buffer;
    }
    
    public void guardarCambios(){
        try{
            _archivo.seek(_miPosicion);
            _archivo.write(empacarNodo());
        } catch (IOException ex){
            System.out.println("Error al almacenar un nodo!");
        }
    }
    
    /*
    crea el espacio en el archivo para el nuevo nodo y lo almacena
    retorna la posicion en la que se almaceno
    */
    public long almacenarComoNuevoNodo(){
        try{
            long posicion = _archivo.length();
            _archivo.seek(_archivo.length());
            _archivo.write(_buffer);
            _miPosicion = posicion;
            return posicion;
        } catch (IOException ex){
            System.out.println("Error al almacenar un nodo!");
            return -1;
        }
    }
    
    
    
    public long getPosicion(){
        return _miPosicion;
    }
    
    public long getPadre(){
        return _padre;
    }
    
    public void setPadre(long pPadre){
        _padre = pPadre;
    }
    
    /*
    inserta un elemento en el arbol
    este metodo se utiliza si el nodo ya existia
    */
    public long insertar(IClave pClave, long pNuevoHijo, long pRaiz){
        if (_cantElementos < 4){
            insertarSinReventar(pClave, pNuevoHijo);
        }else{
            pRaiz = insertarReventando(pClave, pNuevoHijo, pRaiz);
        }
        guardarCambios();
        return pRaiz;
    }
    
    /*
    toma todo los nodo hijos y les asigna como padre la posicion de este nodo 
    se utiliza en caso de que el nodo interno reviente, ya que en ese caso el
    el padre de los nodos hijos cambiaria 
    */
    public void reasignarPadres(){
        if (sigNodoEsHoja(_hijos[0])){
            NodoHoja [] nodos = new NodoHoja [_cantElementos + 1];
            for (int i = 0; i <= _cantElementos; i++){
                nodos [i] = new NodoHoja(_archivo, _hijos[i], _tamanoDato, _tamanoClave, _tipoDato);
                nodos [i].setPadre(_miPosicion);
                nodos [i].guardarCambios();
            }
        }else{
            NodoInterno [] nodos = new NodoInterno [_cantElementos + 1];
            for (int i = 0; i <= _cantElementos; i++){
                nodos [i] = new NodoInterno(_archivo, _hijos[i], _tamanoDato, _tamanoClave, _tipoDato);
                nodos [i].setPadre(_miPosicion);
                nodos [i].guardarCambios();
            }
        }
    }
    
    /*
    a partir de este nodo interno buscar el nodo hoja en el que deberia estar
    insertado el dato que contiene la clave que se recibe como parametro 
    para realizar un busqueda correcta se debe ejecutar este metodo con el nodo raiz
    */
    public long buscarNodoHoja(IClave pClave){
        long sigNodo;
        sigNodo = siguienteNodo(pClave);
        while(sigNodoEsHoja(sigNodo) == false){
            _miPosicion = sigNodo;
            cargarNodo();
            sigNodo = siguienteNodo(pClave);
        }
        return sigNodo;
    }
    
    public long buscarPrimeraHoja(){
        while(sigNodoEsHoja(_hijos[0]) == false){
            _miPosicion = _hijos[0];
            cargarNodo();
        }
        return _hijos[0];
    }
    
private boolean sigNodoEsHoja(long pNodo){
        byte [] buffer = new byte [1];
            try {
                _archivo.seek(pNodo);
                _archivo.read(buffer);
            } catch (IOException ex) {
                Logger.getLogger(ArbolBmas.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Empaquetamiento.desempacarBoolean(buffer, 0);
    }
    
    
    /*
    busca el hijo por el que se debe descender
    */
    private long siguienteNodo(IClave pClave){
        ///////////////////////////////////////////////////
        for (int i = 0; i < _cantElementos; i++){
            if (_claves[i].mayorque(pClave)){
                return _hijos[i];
            }
        }
        return _hijos[_cantElementos];
    }
    
    
    public void recorrerNodo(){
        if (this.sigNodoEsHoja(_hijos[0])){
            NodoHoja [] nodos = new NodoHoja [_cantElementos + 1];
            
            for (int i = 0; i <= _cantElementos; i++){
                nodos[i] = new NodoHoja(_archivo, _hijos[i], _tamanoDato, _tamanoClave, _tipoDato);
                i=i;
            }
            
        } else {
            NodoInterno [] nodos = new NodoInterno [_cantElementos + 1];
            for (int i = 0; i <= _cantElementos; i++){
                nodos[i] = new NodoInterno(_archivo, _hijos[i], _tamanoDato, _tamanoClave, _tipoDato);
                nodos[i].recorrerNodo();
            }
        }
        
        
        
        
    }
}
