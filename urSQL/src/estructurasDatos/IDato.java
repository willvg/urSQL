
package estructurasDatos;

public interface IDato {
    /*metodo que devuelve un arreglo de bytes con todos los atriibutos del objeto
      que se desean guardar en el arbol*/
    public void empacar(byte[] buffer, int offset);
    public void desempacar(byte[] buffer, int offset);
    public IClave getClave();
    public void imprimirClave();
}
