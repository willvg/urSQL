package ExecutorEngine;

import java.io.IOException;
import java.util.ArrayList;

import systemcatalog.*;
import estructurasDatos.*;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathan.chaverri12
 */
public class ProcesadorComandosDML {

    private String respuesta = "";
    private String direccion = "C:/urSQL/";
    private String Esquema = "";
    SystemCatalog systemcatalog;

    ArbolBmas arbol;

    //cambiar//
    ArrayList<String> columnasprevias = new ArrayList<String>();
    ArrayList<String> tipos_datos = new ArrayList<String>();
    
    private void llenar() {
        columnasprevias.add("carnet");
        columnasprevias.add("telefono");
        columnasprevias.add("edad");

        tipos_datos.add("int");
        tipos_datos.add("int");
        tipos_datos.add("int");
    }
    //********************

    public ProcesadorComandosDML() throws IOException {
        try {
            systemcatalog=new SystemCatalog();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcesadorComandosDML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ProcesadorComandosDML(ArrayList<String> columnas, ArrayList<String> tipos) throws IOException {
        this.columnasprevias = columnas;
        this.tipos_datos = tipos;
        try {
            systemcatalog=new SystemCatalog();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcesadorComandosDML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String recibir_consulta(ArrayList<String> arreglo_linea, String db) throws IOException {
        Esquema = db;
        direccion = direccion + "/" + Esquema;
        String palabra = arreglo_linea.get(0);
        String retorno = "";
        switch (palabra) {
            case "SELECT":
                Comando_select(arreglo_linea);
                break;
            case "UPDATE":
                retorno = Comando_update(arreglo_linea);
                break;
            case "DELETE":
                retorno = Comando_delete(arreglo_linea);
                break;
            case "INSERT":
                retorno = Comando_insert(arreglo_linea);
                break;
            default:
                return "Error: no se encontro comando DML";
        }
        return retorno;
    }

    public String Comando_select(ArrayList<String> arreglo_linea) throws FileNotFoundException, IOException {
        String tabla = arreglo_linea.get(2);
        direccion = direccion + "/" + tabla + ".bin";
        String clave = "";
        String total = "";
        //arbol
        int idTabla = systemcatalog.getTablaDeTablas().obtenerIdTabla(tabla);
        long _raiz = systemcatalog.getTablaDeTablas().obtenerUbicacionDeRaiz(idTabla);
        ////**************************
        int id = systemcatalog.getTablaDeEsquemas().obtenerIdEsquemaDeTabla(Esquema);
        ArrayList<String> temporal = new ArrayList<String>();
        temporal = systemcatalog.getTablaDeTablas().obtenerTablasDeEsquema(id);
        for (int i = 0; i < temporal.size(); i++) {
            if (arreglo_linea.get(1).equals(temporal.get(i))) {
                clave = systemcatalog.getTablaDeTablas().obtenerColumnaIdTabla(idTabla);

            }
        }

        ArrayList<String> columnas = new ArrayList<String>();
        ArrayList<String> datos = new ArrayList<String>();
        boolean bandera1 = false;
        boolean bandera2 = false;
        boolean bandera3 = false;
        for (int i = 0; i < arreglo_linea.size(); i++) {
            if (arreglo_linea.get(i).equals(")")) {
                bandera1 = false;
            }
            if (bandera1 == true) {
                columnas.add(arreglo_linea.get(i));
            }
            if (arreglo_linea.get(i).equals("(")) {
                bandera1 = true;
            }
        }
        for (int i = 0; i < arreglo_linea.size(); i++) {
            if (arreglo_linea.get(i).equals(")")) {
                bandera2 = false;
            }
            if (bandera2 == true) {
                datos.add(arreglo_linea.get(i));
            }
            if (arreglo_linea.get(i).equals("VALUES")) {
                i++;
                bandera2 = true;
            }
        }
        ConversorDeInsert conInsert = new ConversorDeInsert(columnasprevias, tipos_datos, columnas, clave);

        conInsert.getColumnasTabla();

        int byte_clave = byte_Clave(tipos_datos.get(0));

        int byte_tamano_datos = bytes_tamano(datos);
        byte[] cuerpo = new byte[byte_tamano_datos];
        byte[] buffer_total = new byte[byte_tamano_datos];
        ArrayList<String> buffer_to = new ArrayList<String>();
        byte[] buffer_tupla = new byte[byte_tamano_datos];
        String respuesta_total="";
        if (clave.equals("int")) {
            DatoInt dato = new DatoInt(Integer.parseInt(clave), cuerpo);
            //dato = conInsert.obtenerByteDeArrayDatoInt(datos);
            arbol = new ArbolBmas(_raiz, new RandomAccessFile(direccion, "rw"), byte_tamano_datos, byte_clave, "int");
            int conta = 0;
            while (arbol.tuplasSecuencial() != null) {
                buffer_tupla = arbol.tuplasSecuencial();
                ConversorDeSelect conselect = new ConversorDeSelect(columnasprevias, columnas, tipos_datos);
                boolean caso = encontrar(buffer_tupla, columnasprevias, tipos_datos, arreglo_linea.get(-4), arreglo_linea.get(-2));
                if (caso == true) {
                    for (int i = 0; i < buffer_tupla.length; i++) {
                        conselect.convertir(buffer_tupla);
                        //buffer_to.add(conselect.obtenerStringConvertido());
                        //buffer_total[conta] = buffer_tupla[i];
                        conta++;
                    }
                }
                respuesta_total = respuesta_total + conselect.obtenerStringConvertido();
                
            }
        }
        if (clave.equals("String")) {
            DatoString dato = new DatoString(clave, cuerpo, 100);
            //dato = conInsert.obtenerByteDeArrayDatoInt(datos);
            arbol = new ArbolBmas(_raiz, new RandomAccessFile(direccion, "rw"), byte_tamano_datos, byte_clave, "int");
            int conta = 0;
            while (arbol.tuplasSecuencial() != null) {
                buffer_tupla = arbol.tuplasSecuencial();
                ConversorDeSelect conselect = new ConversorDeSelect(columnasprevias, columnas, tipos_datos);
                boolean caso = encontrar(buffer_tupla, columnasprevias, tipos_datos, arreglo_linea.get(-4), arreglo_linea.get(-2));
                if (caso == true) {
                    for (int i = 0; i < buffer_tupla.length; i++) {
                        conselect.convertir(buffer_tupla);
                        //buffer_to.add(conselect.obtenerStringConvertido());
                        //buffer_total[conta] = buffer_tupla[i];
                        conta++;
                    }
                }
                respuesta_total = respuesta_total + conselect.obtenerStringConvertido();
            }
            //ConversorDeSelect conselect = new ConversorDeSelect(columnasprevias, columnas, tipos_datos);
            //conselect.convertir(buffer_total);
            //total = conselect.obtenerStringConvertido();
        }
        if (clave.equals("long")) {
            DatoLong dato = new DatoLong(Long.parseLong(clave), cuerpo);
            //dato = conInsert.obtenerByteDeArrayDatoInt(datos);
            arbol = new ArbolBmas(_raiz, new RandomAccessFile(direccion, "rw"), byte_tamano_datos, byte_clave, "int");
            int conta = 0;
            while (arbol.tuplasSecuencial() != null) {
                buffer_tupla = arbol.tuplasSecuencial();
                ConversorDeSelect conselect = new ConversorDeSelect(columnasprevias, columnas, tipos_datos);
                boolean caso = encontrar(buffer_tupla, columnasprevias, tipos_datos, arreglo_linea.get(-4), arreglo_linea.get(-2));
                if (caso == true) {
                    for (int i = 0; i < buffer_tupla.length; i++) {
                        conselect.convertir(buffer_tupla);
                        //buffer_to.add(conselect.obtenerStringConvertido());
                        //buffer_total[conta] = buffer_tupla[i];
                        conta++;
                    }
                }
                respuesta_total = respuesta_total + conselect.obtenerStringConvertido();
            }
            //ConversorDeSelect conselect = new ConversorDeSelect(columnasprevias, columnas, tipos_datos);
            //conselect.convertir(buffer_total);
            //total = conselect.obtenerStringConvertido();

        }
        if (clave.equals("double")) {
            DatoDouble dato = new DatoDouble(Double.parseDouble(clave), cuerpo);
            //dato = conInsert.obtenerByteDeArrayDatoInt(datos);
            arbol = new ArbolBmas(_raiz, new RandomAccessFile(direccion, "rw"), byte_tamano_datos, byte_clave, "int");
            int conta = 0;
            while (arbol.tuplasSecuencial() != null) {
                buffer_tupla = arbol.tuplasSecuencial();
                ConversorDeSelect conselect = new ConversorDeSelect(columnasprevias, columnas, tipos_datos);
                boolean caso = encontrar(buffer_tupla, columnasprevias, tipos_datos, arreglo_linea.get(-4), arreglo_linea.get(-2));
                if (caso == true) {
                    for (int i = 0; i < buffer_tupla.length; i++) {
                        conselect.convertir(buffer_tupla);
                        //buffer_to.add(conselect.obtenerStringConvertido());
                        //buffer_total[conta] = buffer_tupla[i];
                        conta++;
                    }
                }
                respuesta_total = respuesta_total + conselect.obtenerStringConvertido();
            }
            //ConversorDeSelect conselect = new ConversorDeSelect(columnasprevias, columnas, tipos_datos);
            //conselect.convertir(buffer_total);
            //total = conselect.obtenerStringConvertido();
        }
        
        
        return respuesta_total;
    }

    public String Comando_update(ArrayList<String> arreglo_linea) throws IOException {
        int id = systemcatalog.getTablaDeEsquemas().obtenerIdEsquemaDeTabla(Esquema);
        ArrayList<String> temporal = new ArrayList<String>();

        temporal = systemcatalog.getTablaDeTablas().obtenerTablasDeEsquema(id);
        boolean bandera = false;
        for (int i = 0; i < temporal.size(); i++) {
            if (arreglo_linea.get(1).equals(temporal.get(i))) {
                //String clave = systemcatalog.getTablaDeTablas().obtenerColumnaIdTabla(arreglo_linea.get(1));

            }
        }
        if (bandera == false) {
            System.out.println("No se encontro la tabla");
        }

        return null;
    }

    public String Comando_delete(ArrayList<String> arreglo_linea) {
        return null;
    }

    public String Comando_insert(ArrayList<String> arreglo_linea) throws IOException {
        String tabla = arreglo_linea.get(2);
        direccion = direccion + "/" + tabla + ".bin";
        String clave = "";
        //arbol
        int idTabla = systemcatalog.getTablaDeTablas().obtenerIdTabla(tabla);
        long _raiz = systemcatalog.getTablaDeTablas().obtenerUbicacionDeRaiz(idTabla);
        ////**************************
        int id = systemcatalog.getTablaDeEsquemas().obtenerIdEsquemaDeTabla(Esquema);
        ArrayList<String> temporal = new ArrayList<String>();
        temporal = systemcatalog.getTablaDeTablas().obtenerTablasDeEsquema(id);
        for (int i = 0; i < temporal.size(); i++) {
            if (arreglo_linea.get(1).equals(temporal.get(i))) {
                clave = systemcatalog.getTablaDeTablas().obtenerColumnaIdTabla(idTabla);

            }
        }

        ArrayList<String> columnas = new ArrayList<String>();
        ArrayList<String> datos = new ArrayList<String>();
        boolean bandera1 = false;
        boolean bandera2 = false;
        boolean bandera3 = false;
        for (int i = 0; i < arreglo_linea.size(); i++) {
            if (arreglo_linea.get(i).equals(")")) {
                bandera1 = false;
            }
            if (bandera1 == true) {
                columnas.add(arreglo_linea.get(i));
            }
            if (arreglo_linea.get(i).equals("(")) {
                bandera1 = true;
            }
        }
        for (int i = 0; i < arreglo_linea.size(); i++) {
            if (arreglo_linea.get(i).equals(")")) {
                bandera2 = false;
            }
            if (bandera2 == true) {
                datos.add(arreglo_linea.get(i));
            }
            if (arreglo_linea.get(i).equals("VALUES")) {
                i++;
                bandera2 = true;
            }
        }
        ConversorDeInsert conInsert = new ConversorDeInsert(columnasprevias, tipos_datos, columnas, clave);

        conInsert.getColumnasTabla();

        int byte_clave = byte_Clave(tipos_datos.get(0));

        int byte_tamano_datos = bytes_tamano(datos);
        byte[] cuerpo = new byte[byte_tamano_datos];

        if (clave.equals("int")) {
            DatoInt dato = new DatoInt(Integer.parseInt(clave), cuerpo);
            //dato = conInsert.obtenerByteDeArrayDatoInt(datos);
            arbol = new ArbolBmas(_raiz, new RandomAccessFile(direccion, "rw"), byte_tamano_datos, byte_clave, "int");
            arbol.insertar(dato);
        }
        if (clave.equals("String")) {
            DatoString dato = new DatoString(clave, cuerpo, 100);
            //dato = conInsert.obtenerByteDeArrayDatoInt(datos);
            arbol = new ArbolBmas(_raiz, new RandomAccessFile(direccion, "rw"), byte_tamano_datos, byte_clave, "int");
            arbol.insertar(dato);
        }
        if (clave.equals("long")) {
            DatoLong dato = new DatoLong(Long.parseLong(clave), cuerpo);
            //dato = conInsert.obtenerByteDeArrayDatoInt(datos);
            arbol = new ArbolBmas(_raiz, new RandomAccessFile(direccion, "rw"), byte_tamano_datos, byte_clave, "int");
            arbol.insertar(dato);

        }
        if (clave.equals("double")) {
            DatoDouble dato = new DatoDouble(Double.parseDouble(clave), cuerpo);
            //dato = conInsert.obtenerByteDeArrayDatoInt(datos);
            arbol = new ArbolBmas(_raiz, new RandomAccessFile(direccion, "rw"), byte_tamano_datos, byte_clave, "int");
            arbol.insertar(dato);
        }

        return null;
    }

    private int bytes_tamano(ArrayList<String> tipos) {
        int retorna = 0;
        for (int i = 0; i < tipos.size(); i++) {
            if (tipos.get(i).equals("int")) {
                retorna += 4;
            }
            if (tipos.get(i).equals("String")) {
                retorna += 100;
            }
            if (tipos.get(i).equals("long")) {
                retorna += 8;
            }
            if (tipos.get(i).equals("double")) {
                retorna += 8;
            }
            if (tipos.get(i).equals("char")) {
                retorna += 2;
            }
        }
        return retorna;
    }

    public boolean encontrar(byte[] tupla, ArrayList<String> columnas,
            ArrayList<String> tipos_datos, String columna, String dato) {
        int contador = 0;
        int cantidad_bytes_antes = 0;
        int tamano_dato = 0;
        int dato_int = 0;
        String dato_str = "";
        long dato_long = 0;
        double dato_double = 0;
        char dato_char = '\u0000';
        boolean bandera = false;

        for (int i = 0; i < columnas.size(); i++) {
            if (columnas.get(i).equals(columna)) {
                break;
            } else {
                contador++;
            }
        }
        for (int i = 0; i < contador; i++) {
            if (tipos_datos.get(i).equals("int")) {
                cantidad_bytes_antes += 4;
            }
            if (tipos_datos.get(i).equals("String")) {
                cantidad_bytes_antes += 100;
            }
            if (tipos_datos.get(i).equals("long")) {
                cantidad_bytes_antes += 8;
            }
            if (tipos_datos.get(i).equals("double")) {
                cantidad_bytes_antes += 8;
            }
            if (tipos_datos.get(i).equals("char")) {
                cantidad_bytes_antes += 2;
            }
        }
        String Tipo = tipos_datos.get(contador + 1);
        int tamano_dat = byte_Clave(tipos_datos.get(contador + 1));

        byte[] buffer_dato = new byte[tamano_dat];

        int cont = 0;
        for (int i = cantidad_bytes_antes; i < cantidad_bytes_antes + tamano_dat; i++) {
            buffer_dato[cont] = tupla[i];
        }

        if (Tipo.equals("int")) {
            dato_int = Empaquetamiento.desempacarEntero(buffer_dato, 0);
            if (dato_int == Integer.valueOf(dato)) {
                bandera = true;
            }
        }
        if (Tipo.equals("String")) {
            dato_str = Empaquetamiento.desempacarStringLimitado(100, buffer_dato, 0);
            if (dato_str.equals(dato)) {
                bandera = true;
            }
        }
        if (Tipo.equals("long")) {
            dato_long = Empaquetamiento.desempacarLong(buffer_dato, 0);
            if (dato_long == Long.parseLong(dato)) {
                bandera = true;
            }
        }
        if (Tipo.equals("double")) {
            dato_double = Empaquetamiento.desempacarDouble(buffer_dato, 0);
            if (dato_double == Double.valueOf(dato)) {
                bandera = true;
            }
        }
        if (Tipo.equals("char")) {
            dato_char = Empaquetamiento.desempacarChar(buffer_dato, 0);
            if (String.valueOf(dato_char).equals(dato)) {
                bandera = true;
            }
        }
        return bandera;

    }

    private int byte_Clave(String clave) {
        int retorna = 0;
        if (clave.equals("int")) {
            retorna = 4;
        }
        if (clave.equals("String")) {
            retorna = 100;
        }
        if (clave.equals("long")) {
            retorna = 8;
        }
        if (clave.equals("double")) {
            retorna = 8;
        }
        if (clave.equals("char")) {
            retorna = 2;
        }
        return retorna;
    }

}
