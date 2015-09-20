/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package querycompiler;

/**
 *
 * @author Jonatan
 */
public class Protocolo {
    String[] administradores= {"Tec", "1234"};
    String [] usuarios={"Usuario1", "1234"};
   
    String [] comandosDML={"SELECT", "FROM", "WHERE", "GROUP BY", "FOR", "UPDATE",
        "SET", "DELETE", "INSERT INTO", "VALUES", "[WHERE", "[GROUP BY", "[FOR"};
    String [] comandosDDL={"SET DATABASE", "CREATE TABLE", "ALTER TABLE", "ADD CONSTRAINT", 
        "DROP TABLE", "CREATE INDEX"};
    String [] comandosCLP= {"CREATE DATABASE", "DROP DATABASE", "LIST DATABASES", 
        "START", "GET STATUS", "STOP", "DISPLAY DATABASE"};
    
    public String processInput(String theInput) {
        String theOutput = "";
        if (theInput.equals("Prueba")) {
            theOutput = "Connected";
        } else if (theInput.charAt(0)=='C' && theInput.charAt(1)==',') { //comando C, para verificar usuario
            theOutput= this.tipoUsuario(theInput.substring(2));
        } 
        else if (theInput.charAt(0)=='T' && theInput.charAt(1)==','){ //si solicita el tipo de consulta
            theOutput= this.TipoConsulta(theInput.substring(2));
            }
        else{
            theOutput=this.TipoConsulta(theInput);
        }
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
        }
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
        if (igualdad){
            resultado=resultado.substring(0, 3);
        }
        return resultado;
    }
}
