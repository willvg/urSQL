/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procesador;

import java.util.ArrayList;

/**
 *
 * @author Will varela
 */
public class Analizador_tabla {

    private int contador = 2;

    public boolean Frase(ArrayList<String> frase_entrada) {
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
        if (frase_columnas.get(frase_columnas.size() - 3).equals("PRIMARY_KEY")) {
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
}
