
package estructurasDatos;

public interface IClave {
    /*metodo que devuelve un arreglo de bytes con todos los atriibutos del objeto
      que se desean guardar en el arbol*/
    public void empacar(byte[] buffer, int offset);
    public void desempacar(byte[] buffer, int offset);
    public boolean menorque(IClave pDato);
    public boolean mayorque(IClave pDato);    
    //WILL
    public boolean igualque(IClave pDato);
}
