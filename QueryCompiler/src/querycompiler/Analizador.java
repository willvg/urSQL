
package querycompiler;

import java.util.ArrayList;

/**
 *
 * @author Will varela
 */
public class Analizador {

    //Comandos DDL//
    
    /**
     * Método Frease_create_tabla lo que realiza es la validación de la 
     * estrucura del comando y si viene correcto
     * @param frase_entrada
     * @return boolean
     */
    public boolean Frase_create_tabla(ArrayList<String> frase_entrada) {
        int contador = 2;
        boolean caso = true;
        if (frase_entrada.get(contador).equals("AS") && frase_entrada.get(contador + 1).equals("(")) {
            //valida si viene las palabras claves AS y el parentesis
            contador += 2;
            caso = Verifica_columnas(frase_entrada, contador);
        }

        if (caso == true) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * el método Verifica_columnas lo que realiza es toda la validación de las 
     * columnas en el create table.
     * @param frase_columnas
     * @param contador
     * @return 
     */
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

    
    /**
     * El método Frase_set_dabase se encarga de la validación del comando 
     * SET_dabase
     * @param frase_entrada
     * @return boolean
     */
    public boolean Frase_set_dabase(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(1).equals("DATABASE")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * FRase_alter valida la estructura del comando ALTER
     * @param frase_entrada
     * @return boolean
     */
    public boolean Frase_alter(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(1).equals("TABLE")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Frase_add lo que realiza es la validación de la estructura del comando 
     * ADD CONSTRAINT
     * @param frase_entrada
     * @return boolean
     */
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

    /**
     * Frase_referencia valida la estructura del comando REFERENCES
     * @param frase_entrada
     * @return boolean
     */
    public boolean Frase_referencia(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(2).equals("(")
                && frase_entrada.get(4).equals(");")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Frase_drop validala estructura del comando DROP TABLE
     * @param frase_entrada
     * @return boolean
     */
    public boolean Frase_drop(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(1).equals("TABLE")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Frase_index valida la estructura del comando a ingresar que es 
     * CREATE INDEX
     * @param frase_entrada
     * @return boolean
     */
    public boolean Frase_index(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(2).equals("ON")
                && frase_entrada.get(4).equals("(")
                && frase_entrada.get(6).equals(");")) {
            return true;
        } else {
            return false;
        }
    }

    
    //Comando DML//
    
    /**
     * For_where lo que realiza es que valida si se encuentra un INNER JOIN 
     * en la sentencia del SELECT
     * @param frase_entrada
     * @return temporal
     */
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

    /**
     * Frase_select valida la estructura del comando SELECT
     * @param frase_entrada
     * @return boolean
     */
    public boolean Frase_select(ArrayList<String> frase_entrada) {
        ArrayList<Integer> arreglo_contador = new ArrayList<Integer>();
        boolean bandera_join = false;          //variable por si se encuentra un join
        boolean bandera_where = false;         //variable por si se encuentra un where
        boolean bandera_group_by = false;      //variable por si se encuentra un group_by
        boolean bandera_else = false;          //variable por si se encuentra no se piden todas las columnas
        if (frase_entrada.get(1).equals("*")   // si se piden todas las columnas de la tabla
                && frase_entrada.get(2).equals("FROM")) {
            return true;
        } else {                              //si se piden columnas especificas y valida si vienen palabras reservasdas 
                                              //o no vienen en el camando
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
        if (bandera_else == true) {    // valida si se pidieron columnas especificas pero que ya se haya validado el comando 
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
    
    /**
     * Frase_update realiza la validación de la estructura del comando UPDATE
     * @param frase_entrada
     * @return 
     */
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
    
    /**
     * Frase_delete realiza la validación de la estructura del comando DELETE
     * @param frase_entrada
     * @return 
     */
    public boolean Frase_delete(ArrayList<String> frase_entrada) {
        if (frase_entrada.get(1).equals("FROM")
                && frase_entrada.get(3).equals("WHERE")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Frase_insert realiza la validación de la estructura del comando INSERT
     * @param frase_entrada
     * @return 
     */
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
