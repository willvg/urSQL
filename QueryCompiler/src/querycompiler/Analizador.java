/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package querycompiler;

import static com.oracle.jrockit.jfr.DataType.INTEGER;
import java.util.ArrayList;

/**
 *
 * @author curso
 */
public class Analizador {

    public boolean Frase_create_tabla(ArrayList<String> frase_entrada) {
        int contador = 2;
        boolean caso = true;
        if (frase_entrada.get(contador).equals("AS") && frase_entrada.get(contador + 1).equals("(")) {
            contador += 2;
            caso = Verifica_columnas(frase_entrada, contador);
        }

        if (caso == true) {
            return true;
        } else {
            return false;
        }
    }

    private boolean Verifica_columnas(ArrayList<String> frase_columnas, int contador) {
        String retornar = "";

        ArrayList<String> arreglo_columnas = new ArrayList<String>();
        int contador_columnas = 0;
        //OPCIONAL*****************************************************
        /*String palchar = "CHAR(";
         ArrayList<String> arreglo_char = new ArrayList<String>();
         for (int i = 0; i <= 256; i++) {
         arreglo_char.add(palchar + String.valueOf(i) + ")");
         }*/
        //*************************************************************
        for (int i = contador; i < (frase_columnas.size() - 3); i += 3) {
            String tipo = frase_columnas.get(i + 1);
            switch (tipo) {
                case "INTEGER":
                    retornar = retornar + "s";
                    break;
                case "DATETIME":
                    retornar = retornar + "s";
                    break;
                case "VARCHAR":
                    retornar = retornar + "s";
                    break;
                case "DECIMAL":
                    retornar = retornar + "s";
                    break;
                case "CHAR":
                    retornar = retornar + "s";
                    break;
                default:
                    retornar = retornar + "n";
                    break;
            }
            if (tipo.equals("CHAR") || tipo.equals("DECIMAL")) {
                String contraint = frase_columnas.get(i + 3);
                switch (contraint) {
                    case "NULL,":
                        retornar = retornar + "i";
                        arreglo_columnas.add(retornar);
                        retornar = "";
                        i++;
                        contador_columnas++;
                        break;
                    case "NOT_NULL,":
                        retornar = retornar + "i";
                        arreglo_columnas.add(retornar);
                        contador_columnas++;
                        i++;
                        retornar = "";
                        break;
                    default:
                        retornar = retornar + "o";
                        arreglo_columnas.add(retornar);
                        retornar = "";
                        i++;
                        contador_columnas++;
                        break;
                }
            } else {
                String contraint = frase_columnas.get(i + 2);
                switch (contraint) {
                    case "NULL,":
                        retornar = retornar + "i";
                        arreglo_columnas.add(retornar);
                        retornar = "";
                        contador_columnas++;
                        break;
                    case "NOT_NULL,":
                        retornar = retornar + "i";
                        arreglo_columnas.add(retornar);
                        contador_columnas++;
                        retornar = "";
                        break;
                    default:
                        retornar = retornar + "o";
                        arreglo_columnas.add(retornar);
                        retornar = "";
                        contador_columnas++;
                        break;
                }
            }

        }
        if(frase_columnas.get(frase_columnas.size() - 3).equals("PRIMARY_KEY")){
            arreglo_columnas.add("si");
        } else {
            arreglo_columnas.add("no");
        }

        if (frase_columnas.get(frase_columnas.size() - 1).equals(")")) {
            arreglo_columnas.add("si");
        } else {
            arreglo_columnas.add("no");
        }

        //******************************************
        String fal = "";
        for (int i = 0; i < arreglo_columnas.size(); i++) {
            if (arreglo_columnas.get(i).equals("no")) {
                fal = "no";
                break;
            }
        }
        if (fal.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    //********************************************
    //setdabase
    public boolean Frase_set_dabase(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(1).equals("DATABASE")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Frase_alter(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(1).equals("TABLE")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Frase_add(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(1).equals("CONSTRAINT")
                && frase_entrada.get(2).equals("FOREING")
                && frase_entrada.get(3).equals("KEY")
                && frase_entrada.get(4).equals("(")
                && frase_entrada.get(6).equals(");")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Frase_referencia(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(2).equals("(")
                && frase_entrada.get(4).equals(");")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Frase_drop(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(1).equals("TABLE")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Frase_index(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(2).equals("ON")
                && frase_entrada.get(4).equals("(")
                && frase_entrada.get(6).equals(");")) {
            return true;
        } else {
            return false;
        }
    }

    private int for_where(ArrayList<String> frase_entrada) {
        int temporal = 0;
        for (int j = 0; j < frase_entrada.size(); j++) {
            if (frase_entrada.get(j).equals("INNER_JOIN")) {
                temporal = 3;

            } else {
                if ((j + 1) == frase_entrada.size()) {
                    temporal = 2;
                }
            }
        }
        return temporal;
    }

    public boolean Frase_select(ArrayList<String> frase_entrada) {
        ArrayList<Integer> arreglo_contador = new ArrayList<Integer>();
        boolean bandera_join = false;
        boolean bandera_where = false;
        boolean bandera_group_by = false;
        boolean bandera_else = false;
        if (frase_entrada.get(1).equals("*")
                && frase_entrada.get(2).equals("FROM")) {
            return true;
        } else {
            bandera_else = true;
            for (int i = 0; i < frase_entrada.size(); i++) {
                if (frase_entrada.get(i).equals("FROM")) {
                    arreglo_contador.add(1);
                }
                if (frase_entrada.get(i).equals("INNER_JOIN")
                        && bandera_join == false) {
                    arreglo_contador.add(2);
                    bandera_join = true;
                }
                if (frase_entrada.get(i).equals("WHERE")
                        && bandera_where == false) {
                    arreglo_contador.add(for_where(frase_entrada));
                    bandera_where = true;
                }
                if (frase_entrada.get(i).equals("GROUP_BY")
                        && bandera_group_by == false) {
                    String inner = "";
                    String where = "";
                    for (int j = 0; j < frase_entrada.size(); j++) {
                        if (frase_entrada.get(j).equals("INNER_JOIN")) {
                            inner = "si";
                        }
                        if (frase_entrada.get(j).equals("WHERE")) {
                            where = "si";
                        }
                    }
                    if ((inner.equals("si") && where.equals(""))
                            || (inner.equals("") && where.equals("si"))) {
                        arreglo_contador.add(3);
                    }
                    if (inner.equals("si") && where.equals("si")) {
                        arreglo_contador.add(4);
                    }
                    bandera_group_by = true;
                }
            }
        }
        boolean respuesta = false;
        if (bandera_else == true) {
            for (int i = 0; i < arreglo_contador.size(); i++) {
                if (arreglo_contador.get(i) == (i + 1)) {
                    respuesta = true;
                } else {
                    respuesta = false;
                }
            }
        }
        if (respuesta == true) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean Frase_update(ArrayList<String> frase_entrada){
        boolean bandera = false;
        if(frase_entrada.get(2).equals("SET")){
            for (int i = 2; i < frase_entrada.size(); i++) {
                if (frase_entrada.get(i).equals("WHERE")){
                    bandera=true;
                }
            }
            return bandera;
        }else{
            return false;
        }
    }
    
    public boolean Frase_delete(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(1).equals("FROM")
                && frase_entrada.get(3).equals("WHERE")) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean Frase_insert(ArrayList<String> frase_entrada){
        boolean bandera = false;
        if(frase_entrada.get(1).equals("INTO")){
            for (int i = 2; i < frase_entrada.size(); i++) {
                if (frase_entrada.get(i).equals("VALUES")){
                    bandera=true;
                }
            }
            return bandera;
        }else{
            return false;
        }
    }
    
}
