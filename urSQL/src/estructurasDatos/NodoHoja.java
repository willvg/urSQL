
package estructurasDatos;

import java.io.IOException;
import java.io.RandomAccessFile;

public class NodoHoja {
    
    private IDato[] _datos;
    private long _miPosicion;
    private long _padre;
    private long _siguienteNodo;
    private RandomAccessFile _archivo;
    private int _tamanoDato;
    private int _tamanoClave;
    private int _cantElementos;
    private boolean [] _activo;
    private byte [] _buffer;
    private String _tipoDato;
    
    /*
    crea un nodo que aun no existe en disco 
    */
    NodoHoja(RandomAccessFile pArchivo, int pTamanoDato, int pTamanoClave, String pTipoDato){
        
        //cuando agregue los otros tipos de datos tengo que cambiar esto
        _archivo = pArchivo;
        _tamanoDato = pTamanoDato;
        _tamanoClave = pTamanoClave;
        _tipoDato = pTipoDato;
        _datos = new IDato[4];
        _activo = new boolean [4];
        _miPosicion = 0;
        _padre = 0;
        _cantElementos = 0;
        _siguienteNodo = 0;
        _buffer = new byte[33 + _tamanoDato * 4];
        
        for (int i = 0; i <= 3; i++)
            _activo[i] = true;
    }
     
    /*
    carga el nodo con los datos alamacenados en la posicion recibida
    como parametro
    */
    NodoHoja(RandomAccessFile pArchivo, long pPosicion, int pTamanoDato,
            int pTamanoClave, String pTipoDato) {
        _datos = new IDato [4];
        _activo = new boolean [4];
        _archivo = pArchivo;
        _miPosicion = pPosicion;
        _tamanoDato = pTamanoDato;
        _tamanoClave = pTamanoClave;
        _tipoDato = pTipoDato;
        _siguienteNodo = 0;
        _buffer = new byte [33 + _tamanoDato * 4];
        cargarNodo();
    }
    
    /*
    carga el nodo con los daotos que se encuentre en _miPosicion
    */
    private void cargarNodo(){
        try{
            _archivo.seek(_miPosicion);
            _archivo.read(_buffer);
            
            for (int i = 0; i <= 3; i++){
                _activo[i] = Empaquetamiento.desempacarBoolean(_buffer, i+1);
            }
            _siguienteNodo = Empaquetamiento.desempacarLong(_buffer, 5);
            _miPosicion = Empaquetamiento.desempacarLong(_buffer, 13);
            _padre = Empaquetamiento.desempacarLong(_buffer, 21);
            _cantElementos = Empaquetamiento.desempacarEntero(_buffer, 29);
            
            construirDatos();
            for (int i = 0; i <= 3; i++)
                _datos[i].desempacar(_buffer, 33 + _tamanoDato * i);
            
        }catch (IOException ex){
            System.out.println("Ha ocurrido un error cargando el nodo!");
        }
    }
    
    private void construirDatos(){
        switch(_tipoDato){
            case "int": {
                System.out.println("entro a contruir int");
                for(int i = 0; i <= 3; i++)
                    _datos[i] = new DatoInt(_tamanoDato - _tamanoClave);
            }break;
            case "double":{
                for(int i = 0; i <= 3; i++)
                    _datos[i] = new DatoDouble(_tamanoDato - _tamanoClave);
            }break;
            case "string":{
                for(int i = 0; i <= 3; i++)
                    _datos[i] = new DatoInt(_tamanoDato - _tamanoClave);
            }break;
            case "long":{
                System.out.println("entro a construir long");
                for(int i = 0; i <= 3; i++)
                    _datos[i] = new DatoLong(_tamanoDato - _tamanoClave);
            }break;
            default: System.out.println("Error:  Tipo de dato no permitido");   break;
        }
    }
    
    private long insertarReventando(IDato pDato, long pRaiz){
        IDato aux;
        
        /*
        acomodo el dato en la posicion que le corresponde y el dato extra lo
        almacena en la varible aux
        */
        if(_datos[3].getClave().menorque(pDato.getClave())){
            aux = pDato;
        }else{
            aux = _datos[3];
            for (int i = 0; i < 4; i++ ){
                if(_datos[i].getClave().mayorque(pDato.getClave())){
                    for (int j = 3; j > i; j--){
                        _datos[j] = _datos [j - 1];
                    }
                    _datos[i] = pDato;
                    break;
                }
            }
        }
        this.guardarCambios();
        
        /*
        empezamos el proceso de crear nuevos nodo y reacomodar los datos
        */
        if(pRaiz == _miPosicion){
            
            NodoHoja nuevoHijo = new NodoHoja(_archivo, _tamanoDato, _tamanoClave, _tipoDato);
            long posNuevoHijo = nuevoHijo.almacenarComoNuevoNodo();
            nuevoHijo.insertar(_datos[2], 0);
            nuevoHijo.insertar(_datos[3], 0);
            nuevoHijo.insertar(aux, 0);
            
            NodoInterno nuevaRaiz = new NodoInterno(_archivo, _miPosicion ,posNuevoHijo,
                                        _datos[2].getClave(), _tamanoDato, _tamanoClave, _tipoDato);
            pRaiz = nuevaRaiz.almacenarComoNuevoNodo();
            nuevaRaiz.guardarCambios();
            
            nuevoHijo.setPadre(pRaiz);
            nuevoHijo.guardarCambios();
            
            _padre = pRaiz;
            _siguienteNodo = posNuevoHijo;
            _cantElementos = 2;
            
        } else {
            NodoHoja nuevoHijo = new NodoHoja(_archivo, _tamanoDato, _tamanoClave, _tipoDato);
            long posNuevoHijo = nuevoHijo.almacenarComoNuevoNodo();
            nuevoHijo.setPadre(_padre);
            nuevoHijo.insertar(_datos[2], 0);
            nuevoHijo.insertar(_datos[3], 0);
            nuevoHijo.insertar(aux, 0);
            nuevoHijo.setSigNodo(_siguienteNodo);
            nuevoHijo.guardarCambios();
            
            
            _siguienteNodo = posNuevoHijo;
            _cantElementos = 2;
            this.guardarCambios();
            NodoInterno padre = new NodoInterno(_archivo, _padre, _tamanoDato, _tamanoClave, _tipoDato);
            pRaiz = padre.insertar(_datos[2].getClave(), posNuevoHijo, pRaiz);
            cargarNodo();
        }
        return pRaiz;
    }
    
    private void insertarSinReventar(IDato pDato){
        if (_cantElementos != 0){
            for(int i = 0; i < _cantElementos; i++){
                if(_datos[i].getClave().mayorque(pDato.getClave())){
                    for (int j = _cantElementos; j > i; j--){
                        _datos[j] = _datos [j - 1];
                    }
                    _datos[i] = pDato;
                    _cantElementos++;
                    return;
                }    
            }
            _datos[_cantElementos] = pDato;
            _cantElementos++;
        } else {
            _datos [0] = pDato;
            _cantElementos++;
        }     
    }
    
    private byte[] empacarNodo(){
        byte[] buffer = new byte [33 + _tamanoDato * 4];
        Empaquetamiento.empaquetarBoolean(true, buffer, 0);
        for (int i = 0; i <=3; i++){
            Empaquetamiento.empaquetarBoolean(_activo[i], buffer, i+1);
        }
        Empaquetamiento.empacarLong(_siguienteNodo, buffer, 5);
        Empaquetamiento.empacarLong(_miPosicion, buffer, 13);
        Empaquetamiento.empacarLong(_padre, buffer, 21);
        Empaquetamiento.empacarInt(_cantElementos, buffer, 29);
        for(int i = 0; i < _cantElementos; i++){
            _datos[i].empacar(buffer, 33 + (_tamanoDato * i));
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
        this.guardarCambios();
    }
    
    /*
    inserta un elemento en el arbol
    este metodo se utiliza si el nodo ya existia
    */
    public long insertar(IDato pDato, long pRaiz){
        if (_cantElementos < 4){
            insertarSinReventar(pDato);
            guardarCambios();
        }else{
            pRaiz = insertarReventando(pDato, pRaiz);
        }
        guardarCambios();
        return pRaiz;
    }

    
    public void setSigNodo(long pSigNodo){
        _siguienteNodo = pSigNodo;
    }
    
    
     //WIL*****************************************************************
    //***************************************
    public void imprimir() {
        int j = 1;
        System.out.println("nodo " + j);
        for (int i = 0; i < _cantElementos; i++) {
            if (_activo[i] == true) {
                _datos[i].imprimirClave();
            }

        }

        while (_siguienteNodo != 0) {
            System.out.println("nodo " + ++j);
            _miPosicion = _siguienteNodo;
            cargarNodo();
            for (int i = 0; i < _cantElementos; i++) {
                if (_activo[i] == true) {
                    _datos[i].imprimirClave();
                }
            }
        }
    }

    
    public byte[] buscarDato(IClave pDato) {
        int contador = 0;
        byte[] buffer_return = new byte[_tamanoDato + 1];
        boolean bandera = false;
        while (contador < 4) {
            if (_datos[contador].getClave().igualque(pDato)) {
                if (_activo[contador] == true) {
                    Empaquetamiento.empaquetarBoolean(true, buffer_return, 0);
                    int cont = 1;
                    for (int i = 33 + _tamanoDato * contador; i < 33 + _tamanoDato * contador + _tamanoDato; i++) {
                        buffer_return[cont] = _buffer[i];
                        cont++;
                    }
                    bandera = true;
                }
            }
            contador++;
        }
        if (bandera == false) {
            Empaquetamiento.empaquetarBoolean(false, buffer_return, 0);
        }
        return buffer_return;
    }

    public void busqueda_actualizar(IDato pDato, IClave pClave) {
        boolean bandera = false;
        for (int i = 0; i < 4; i++) {
            if (_datos[i].getClave().igualque(pClave)) {
                if (_activo[i] == true) {
                    _datos[i] = pDato;
                    bandera = true;
                }
            }
        }
        if (bandera == false) {
            System.out.println("El dato para actualizar no se encuentra");
        }
        guardarCambios();
    }

    public void borrarDato(IClave pClave) {
        boolean bandera = false;
        for (int i = 0; i < 4; i++) {
            if (_datos[i].getClave().igualque(pClave)) {
                _activo[i] = false;
                bandera = true;
            }
        }
        if (bandera == false) {
            System.out.println("No se encontro el elemento a eliminar");
        }
        guardarCambios();
    }
    
    
    
    //michael----------------------------------------------------------
    public boolean cargarSiguienteNodo(){
        if (_siguienteNodo != 0){
            this._miPosicion = this._siguienteNodo;
            this.cargarNodo();
            return true;
        } else {
            return false;
        }
    }
    
    public byte [] getTupla(int pPosicion){
        byte [] buffer = new byte [_tamanoDato];
        _datos[pPosicion].empacar(buffer, 0);
        return buffer;
    }
    
    public int getCantElementos(){
        return _cantElementos;
    }
}
