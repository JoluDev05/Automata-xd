

public class Nodo {
    // Atributos
    String lexema; //Se usa para los caracteres
    int token; //Token de la matriz que representa el lexema
    int linea; // linea de codigo
    Nodo next = null; // referencia al siguiente

    public Nodo(String lexema, int token, int linea) {
        this.lexema = lexema;
        this.token = token;
        this.linea = linea;
    }
}