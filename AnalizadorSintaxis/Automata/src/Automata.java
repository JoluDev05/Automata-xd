//Hola

public class Automata {
    public static void main(String[] args) {
        Compilador automata = new Compilador();
        if (!automata.errorFound) {
            System.out.println("\n El Análisis Léxico ha terminado.\n");
        }

        automata.Sintaxis();

        if(!automata.errorFound){
            System.out.println("\n El Análisis Sintáctico ha terminado.\n");
        }
    }
}