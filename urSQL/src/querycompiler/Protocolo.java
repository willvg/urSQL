/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package querycompiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import systemcatalog.SystemCatalog;

/**
 *
 * @author Jonatan
 */
public class Protocolo {
    String[] administradores= {"Tec", "1234"};
    String [] usuarios={"Usuario1", "1234"};
   
    String [] comandosDML={"SELECT", "FROM", "WHERE", "GROUP BY", "FOR", "UPDATE",
        "SET", "DELETE", "INSERT INTO", "VALUES", "[WHERE", "[GROUP BY", "[FOR",
    "INSERT_INTO","GROUP_BY" };
    String [] comandosDDL={"SET DATABASE", "CREATE_TABLE", "ALTER TABLE", "ADD CONSTRAINT", 
        "DROP TABLE", "CREATE INDEX", "CREATE_INDEX", "ALTER_TABLE", "ADD_CONSTRAINT",
    "DROP_TABLE"};
    String [] comandosCLP= {"CREATE DATABASE", "DROP DATABASE", "LIST DATABASES", 
        "START", "GET STATUS", "STOP", "DISPLAY DATABASE"};
    
    String ejemploTabla= "@Columna1/dato1,dato2,dato3/Columnalarga2/datolargo1,dato4,dato5"
            + "@Columna1tabla2/dato1,dato2,dato3/Columnalarga2/datolargo1,dato4,dato5";
    
    String database=""; 
    SystemCatalog catalog;
    
    public Protocolo() throws FileNotFoundException{
        try {
            catalog=new SystemCatalog();
        } catch (IOException ex) {
            Logger.getLogger(Protocolo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String processInput(String theInput) throws IOException {
        String theOutput = "";
        if (theInput.equals("Prueba")) {
            theOutput = "Connected";
        } else if (theInput.charAt(0)=='C' && theInput.charAt(1)==',') { //comando C, para verificar usuario
            theOutput= this.tipoUsuario(theInput.substring(2));
        } 
        else if (theInput.charAt(0)=='T' && theInput.charAt(1)==','){ //si solicita el tipo de consulta
            theOutput= this.TipoConsulta(theInput.substring(2));
            }
        else if (theInput.charAt(0)=='B' && theInput.charAt(1)==','){ //si solicita el nombre de la base
            theOutput= this.database;
            }
        else{
            theOutput= this.ProcesarConsulta(theInput);
        }
        System.out.println("output: "+theOutput);
        return theOutput;
    }
    
    public String tipoUsuario(String datos){
        String usuario= "";
        String contrasena="";
        int i= datos.indexOf(",");
        usuario= datos.substring(0, i);
        contrasena= datos.substring(i+1);
        for (int j=0; j<administradores.length;j++){
            if (administradores[j].equals(usuario) && administradores[j+1].equals(contrasena)){
                return "Administrador";
            }
            else{
                j=j+1;
            }
        }
        for (int k=0; k<usuarios.length;k++){
            if (usuarios[k].equals(usuario) && usuarios[k+1].equals(contrasena)){
                return "Usuario";
            }
            else{
                k=k+1;
            }
        }
        return "False";
    }
    
    public String TipoConsulta(String consulta){
        String resultado="";
        String palabra="";
        int i=0;
        while(consulta.length()!=0){
            i= consulta.indexOf("/");
            palabra= consulta.substring(0, i);
            if (!(consulta.substring(i).length()==1)){
                consulta= consulta.substring(i+1);
            }
            else{
                consulta="";
            }
            for (int k=0; k<comandosDML.length; k++){
                if (comandosDML[k].contains(" ")){
                    if (consulta.length()!=0){
                        int j= consulta.indexOf("/");
                        String palabra1= palabra +" "+consulta.substring(0, j);
                        if (palabra1.equals(comandosDML[k])){
                            resultado= resultado+"DML";
                            if (!(consulta.substring(j).length()==1)){
                                consulta= consulta.substring(j+1);
                            }
                            else{
                                consulta="";
                            }
                        }
                    }
                }
                else if (palabra.equals("SET")){
                    if (consulta.length()!=0){
                        int j= consulta.indexOf("/");
                        String palabra1= palabra +" "+consulta.substring(0, j);
                        if (palabra1.equals("SET DATABASE")){
                            break;
                        }
                        else{
                            if (palabra.equals(comandosDML[k])){
                                  resultado= resultado+"DML";
                            }
                        }
                    }
                }
                else{
                    if (palabra.equals(comandosDML[k])){
                        resultado= resultado+"DML";
                    }
                }
            }
            for (int k=0; k<comandosDDL.length; k++){
                if (comandosDDL[k].contains(" ")){
                    if (consulta.length()!=0){
                        int j= consulta.indexOf("/");
                        String palabra1= palabra +" "+consulta.substring(0, j);
                        if (palabra1.equals(comandosDDL[k])){
                            resultado= resultado+"DDL";
                            if (!(consulta.substring(j).length()==1)){
                                consulta= consulta.substring(j+1);
                            }
                            else{
                                consulta="";
                            }
                        }
                    }
                }
                else{
                    if (palabra.equals(comandosDDL[k])){
                        resultado= resultado+"DDL";
                    }
                }             
            }
            for (int k=0; k<comandosCLP.length; k++){
                if (comandosCLP[k].contains(" ")){
                    if (consulta.length()!=0){
                        int j= consulta.indexOf("/");
                        String palabra1= palabra +" "+consulta.substring(0, j);
                        if (palabra1.equals(comandosCLP[k])){
                            resultado= resultado+"CLP";
                            if (!(consulta.substring(j).length()==1)){
                                consulta= consulta.substring(j+1);
                            }
                            else{
                                consulta="";
                            }
                        }
                    }
                }
                else{
                    if (palabra.equals(comandosCLP[k])){
                        resultado= resultado+"CLP";
                    }
                }         
            }
            palabra="";
        }
        char uno=' ';
        char dos=' ';
        char tres=' ';
        boolean igualdad=true;
        if (resultado.length()!=0){
            uno=resultado.charAt(0);
            dos= resultado.charAt(1);
            tres= resultado.charAt(2);
            for (int h=3; h<resultado.length();h++){
                if (uno!=resultado.charAt(h)){
                    igualdad=false;
                    break;
                }
                else if (dos!=resultado.charAt(h+1)){
                    igualdad=false;
                    break;
                }
                else if (tres!=resultado.charAt(h+2)){
                    igualdad=false;
                    break;
                }
                else{
                    h=h+2;
                }
            }
        }
        if (igualdad && resultado.length()!=0){
            resultado=resultado.substring(0, 3);
        }
        return resultado;
    }
    
    public String ProcesarConsulta(String consulta) throws IOException{
        String linea1= "";
        int i=0;
        String tipo=this.TipoConsulta(consulta);
        Analizamiento_Procesamiento analizador= new Analizamiento_Procesamiento();
        String respuesta="No se ha recibido ningún dato";
        while (consulta.length()!=0){
            if (consulta.contains(";")){
                i= consulta.indexOf(";");
                linea1= consulta.substring(0, i);
                consulta= consulta.substring(i+1);
                String lineaFinal="";
 
                for (int j=0; j<linea1.length();j++){
                    if (linea1.charAt(j)=='/'){
                        if (linea1.length()!=j+1 && j!=0){
                            lineaFinal=lineaFinal+" ";
                        }      
                    }
                    else{
                        lineaFinal=lineaFinal+linea1.charAt(j);
                    }
                }
                lineaFinal=lineaFinal+";";
                boolean buscarBase=this.setDatabase(lineaFinal);
                if (!buscarBase){
                    return "Error al obtener el nombre de la base";
                }
                try{
                    if (tipo.equals("CLP")){
                        analizador.ComandosCLP(lineaFinal.substring(0, lineaFinal.length()-1));
                    }
                    else{
                        analizador.recibir_expresion(lineaFinal, database);
                    }
                    
                }
                catch(Exception e){
                    return "Error al procesar";
                }
                respuesta= analizador.getRespuesta();
                //respuesta=this.ejemploTabla;
            }
            else{
                if (consulta.length()!=1){
                    return "Error -121: error al declarar, no se encontro ;";
                }
                else{
                    break;
                }
            }
        }
        return respuesta;
    }
    
    private boolean setDatabase(String consulta){ //toma la consulta y verifica si viene un set database y cambia la base
        if (consulta.length()<13){
            return true;
        }
        if (consulta.substring(0, 12).equals("SET DATABASE") || consulta.substring(0, 12).equals("SET_DATABASE")){
            try {
                int i= consulta.indexOf(";");
                String base= consulta.substring(13, i);
                if (!(base.contains(" ")) && base.length()>1){
                        this.database= base;
                        return true;
                }
                else{
                    return false;
                }
            } catch(Exception e){
                return false;
            }
        }
        return true;
    }
    
    public void setDatabase2(String db){
        this.database=db;
    }
    
}


