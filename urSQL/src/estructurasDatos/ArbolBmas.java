package estructurasDatos;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArbolBmas {
    /*
    posicion en la que se encuentra el nodo raiz
    en los metodos en el que la raiz del arbol pueda cambiar entonces, este mismo 
    metodo retornara la posicion en la que se encuentra almacenada la nueva raiz
    */
    private long _raiz;
    //archivo en el que se encuentra almacenado el arbol
    private RandomAccessFile _archivo;
    //Estas variables son necesesarias para conocer el tamano del buffer que se 
    //utilizara para leer y escribir en disco
    private int _tamanoDato;
    private int _tamanoClave;
    private String _tipoDato;
    
    private NodoHoja _nodoDefecto;
    private boolean _secuencial;   
    private int _contTuplaDeNodo;
    
    /*
    metodo contructor del arbol
    */
    public ArbolBmas(long pRaiz, RandomAccessFile pArchivo, int pTamanoDato,
        int pTamanoClave, String pTipoDato){
        _raiz = pRaiz;
        _archivo = pArchivo;
        _tamanoDato = pTamanoDato;
        _tamanoClave = pTamanoClave;
        _tipoDato = pTipoDato;
        _secuencial = false;
        _contTuplaDeNodo=0;
    }
    
    private boolean raizEsHoja(){
        byte [] buffer = new byte [1];
        try {
            _archivo.seek(_raiz);
            _archivo.read(buffer);
        } catch (IOException ex) {
            Logger.getLogger(ArbolBmas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Empaquetamiento.desempacarBoolean(buffer, 0);
    }
    
    /*
    la posicion de la raiz del arbol si el proceso de insersion fue 
    satisfactorio, o -1 de lo contrario
      
    Este metodo delega todo el trabajo de insersion al objeto 
    NodoArbolBmas
    */
    public long insertar(IDato pDato){
        //esto quiere decir que todavia no existe ningun nodo creado
        if(_raiz != -1){
            if (raizEsHoja() == true){
                NodoHoja nodo = new NodoHoja(_archivo, _raiz, _tamanoDato, _tamanoClave, _tipoDato);
                _raiz = nodo.insertar(pDato, _raiz);
            } else {
                /*
                debo buscar el nodo hoja en el que se debe insertar y 
                apartir de ahi realizar un proceso normal
                */
                NodoInterno nodoInterno = new NodoInterno(_archivo, _raiz, _tamanoDato, _tamanoClave, _tipoDato);
                long posHoja = nodoInterno.buscarNodoHoja(pDato.getClave());
                NodoHoja nodo = new NodoHoja(_archivo, posHoja, _tamanoDato, _tamanoClave, _tipoDato);
                _raiz = nodo.insertar(pDato, _raiz);
            }
        } else {
            NodoHoja nuevaRaiz= new NodoHoja(_archivo, _tamanoDato, _tamanoClave,_tipoDato);
            _raiz = nuevaRaiz.almacenarComoNuevoNodo();
            nuevaRaiz.insertar(pDato, _raiz);
        }
        return _raiz;
    }
    
    public void imprimir(){
        if (raizEsHoja() == true){
            NodoHoja nodo = new NodoHoja(_archivo, _raiz, _tamanoDato, _tamanoClave, _tipoDato);
            nodo.imprimir();
        } else {
            NodoInterno nodoInterno = new NodoInterno(_archivo, _raiz, _tamanoDato, _tamanoClave, _tipoDato);
            long primeraHoja = nodoInterno.buscarPrimeraHoja();
            NodoHoja nodo = new NodoHoja(_archivo, primeraHoja,
                    _tamanoDato, _tamanoClave, _tipoDato);
            nodo.imprimir();
        }
    }
    
    public void recorrerNodos(){
        NodoInterno nodo = new NodoInterno(_archivo, _raiz, _tamanoDato, _tamanoClave, _tipoDato);
        nodo.recorrerNodo();
    }
    
    
    //WILL****************************
    public byte[] Buscar(IClave pDato) {
        byte[] buffer_inicial = new byte[_tamanoDato + 1];
        if (_raiz != -1) {
            if (raizEsHoja() == true) {
                NodoHoja nodo = new NodoHoja(_archivo, _raiz, _tamanoDato, _tamanoClave, _tipoDato);
                buffer_inicial = nodo.buscarDato(pDato);

            } else {
                NodoInterno nodoInterno = new NodoInterno(_archivo, _raiz,_tamanoDato, _tamanoClave, _tipoDato);
                long posHoja = nodoInterno.buscarNodoHoja(pDato);
                NodoHoja nodo = new NodoHoja(_archivo, posHoja, _tamanoDato, _tamanoClave, _tipoDato);
                buffer_inicial = nodo.buscarDato(pDato);
            }
            return buffer_inicial;
        } else {
            return buffer_inicial;
        }

    }

    public long actualizar(IDato pDato, IClave pClave) {
        if (_raiz != -1) {
            if (raizEsHoja() == true) {
                NodoHoja nodo = new NodoHoja(_archivo, _raiz, _tamanoDato, _tamanoClave, _tipoDato);
                nodo.busqueda_actualizar(pDato, pClave);
            } else {
                NodoInterno nodoInterno = new NodoInterno(_archivo, _raiz,_tamanoDato, _tamanoClave, _tipoDato);
                long posHoja = nodoInterno.buscarNodoHoja(pClave);
                NodoHoja nodo = new NodoHoja(_archivo, posHoja, _tamanoDato, _tamanoClave, _tipoDato);
                nodo.busqueda_actualizar(pDato, pClave);
            }
        } else {
            System.out.println("No se encuentra elementos en la lista");
        }

        return _raiz;
    }

    public long borrar(IClave pClave) {
        if (_raiz != -1) {
            if (raizEsHoja() == true) {
                NodoHoja nodo = new NodoHoja(_archivo, _raiz, _tamanoDato, _tamanoClave, _tipoDato);
                
                
            } else {
                NodoInterno nodoInterno = new NodoInterno(_archivo, _raiz, _tamanoDato, _tamanoClave, _tipoDato);
                long posHoja = nodoInterno.buscarNodoHoja(pClave);
                NodoHoja nodo = new NodoHoja(_archivo, posHoja, _tamanoDato, _tamanoClave, _tipoDato);
                nodo.borrarDato(pClave);
            }
            return _raiz;
        } else {
            System.out.println("No hay elementos en el árbol");
            return _raiz;
        }

    }
    
    
    
    //Michael obtener tuplas secuencialmente------------------------------------
    
    /*
    retorna todas las  tuplas contenidas en el arbol de una en una
    retorna null en caso que se hayan acabado los datos del arbol
    */
    public byte [] tuplasSecuencial(){
        if(_secuencial == true){
            if (_contTuplaDeNodo < _nodoDefecto.getCantElementos()){
                _contTuplaDeNodo++;
                return _nodoDefecto.getTupla(_contTuplaDeNodo - 1);
            } else {
                if(_nodoDefecto.cargarSiguienteNodo()){
                    _contTuplaDeNodo = 0;
                    return _nodoDefecto.getTupla(_contTuplaDeNodo++);
                } else {
                    return null;
                }
            }
        } else {
            NodoInterno nodo = new NodoInterno(_archivo, _raiz, _tamanoDato, 
                    _tamanoClave, _tipoDato);
            _nodoDefecto = new NodoHoja(_archivo, nodo.buscarPrimeraHoja(), _tamanoDato,
                    _tamanoClave, _tipoDato);
            _secuencial = true;
            _contTuplaDeNodo = 1;
            return _nodoDefecto.getTupla(0);
        }
    }
    
    public void reiniciarBusquedaSecuencial(){
        _secuencial = true;
    }
}