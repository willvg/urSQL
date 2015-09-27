
package ursql;

import estructurasDatos.ArbolBmas;
import estructurasDatos.DatoInt;
import estructurasDatos.Empaquetamiento;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Stack;

public class UrSQL {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            RandomAccessFile archivo = new RandomAccessFile("arbol1.bin", "rw");
            byte [] cuerpo = new byte [4];
            ArbolBmas arbol = new ArbolBmas(-1, archivo, 8, 4,"int");
            
            int pos;
            int nCartas = 10;
            Stack < Integer > pCartas = new Stack < Integer > ();
            for (int i = 0; i < nCartas ; i++) {
              pos = (int) Math.floor(Math.random() * nCartas );
              while (pCartas.contains(pos)) {
                pos = (int) Math.floor(Math.random() * nCartas );
              }
              arbol.insertar(new DatoInt(pos, cuerpo));
              pCartas.push(pos);
            }
            System.out.println("Núm. aleatorios sin repetición:");
            System.out.println(pCartas.toString());
            
            /*
            for (int i = 0; i < 50; i++){
                arbol.insertar(new DatoInt((int) (Math.random() * 100), cuerpo));
                //arbol.insertar(new DatoInt(i, cuerpo));    
            }*/
            /*
            DatoInt dato1 = new DatoInt (3, cuerpo);
            DatoInt dato2 = new DatoInt (1, cuerpo);
            DatoInt dato3 = new DatoInt (7, cuerpo);
            DatoInt dato4 = new DatoInt (8, cuerpo);
            DatoInt dato5 = new DatoInt (4, cuerpo);
            DatoInt dato6 = new DatoInt (0, cuerpo);
            DatoInt dato7 = new DatoInt (9, cuerpo);
            DatoInt dato8 = new DatoInt (6, cuerpo);
            DatoInt dato9 = new DatoInt (5, cuerpo);
            DatoInt dato10 = new DatoInt (2, cuerpo);
                
            Empaquetamiento.empacarInt(7, cuerpo, 0);
            */
            /*
            arbol.insertar(dato1);
            arbol.insertar(dato2);
            arbol.insertar(dato3);
            arbol.insertar(dato4);
            arbol.insertar(dato5);
            arbol.insertar(dato6);
            arbol.insertar(dato7);
            arbol.insertar(dato8);
            arbol.insertar(dato9);
            arbol.insertar(dato10);
            */
            
            System.out.println("datos del arbol");
            arbol.imprimir();
            
            for (int i = 1; i <= 10; i++){
                System.out.println(Empaquetamiento.desempacarEntero(arbol.tuplasSecuencial(), 0));
            }
            
        } catch (IOException ex){
            System.out.println("ocurrio un error!!!");    
        }
            
            
    }
    
}
