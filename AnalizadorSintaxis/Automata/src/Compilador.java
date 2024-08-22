
import java.io.RandomAccessFile;

public class Compilador {

    //Declaramos nuestras variables 
    Nodo head = null, puntero;
    int estado = 0, columna, valorMatriz, numLinea = 1, caracter = 0;
    String Lexema = "";
    boolean errorFound = false, endOfFile = false;

    int pila[] = new int[50];
    int tope = 0;
    int dato;

    // Archivo a leer
    String archivo = "Automata\\src\\Prueba.txt";

    //Matriz de Transiciones de Estado
    int Matriz[][] = {
        //       0    1   2   3   4   5   6   7   8   9  10   11  12  13  14  15  16  17  18  19  20
        //       L    d   .   +   -   *   /   <   >   =   :   (    )  ,   ;   "  EB  Tab  EOL EOF oc 
        /*0*/   {  1,  2,115,103,104,105,  5,  8,  9, 10, 11,113,114,116,117, 12,  0,  0,  0,  0,505},
        /*1*/   {  1,  1,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100},
        /*2*/   {101,  2,  3,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101},
        /*3*/   {500,  4,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500},
        /*4*/   {102,  4,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102},
        /*5*/   {106,106,106,106,106,  6,106,106,106,106,106,106,106,106,106,106,106,106,106,106,106},
        /*6*/   {  6,  6,  6,  6,  6,  7,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,503,  6},
        /*7*/   {  6,  6,  6,  6,  6,  7,  0,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,503,  6},
        /*8*/   {107,107,107,107,107,107,107,107,112,108,107,107,107,107,107,107,107,107,107,107,107},
        /*9*/   {109,109,109,109,109,109,109,109,109,110,109,109,109,109,109,109,109,109,109,109,109},
        /*10*/  {502,502,502,502,502,502,502,502,110,111,502,502,502,502,502,502,502,502,502,502,502},
        /*11*/  {120,120,120,120,120,120,120,120,120,118,120,120,120,120,120,120,120,120,120,120,120},
        /*12*/  { 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,119, 12, 12,504,504, 12},
    };

    //Palabras Reservadas
    String PalabrasReservadas[][] = {
        // 0       1
        {"200","program"},
        {"201","var"},
        {"202","string"},
        {"203","integer"},
        {"204","real"},
        {"205","boolean"},
        {"206","begin"},
        {"207","end"},
        {"208","read"},
        {"209","write"},
        {"210","if"},
        {"211","then"},
        {"212","else"},
        {"213","while"},
        {"214","do"},
        {"215","or"},
        {"216","and"},
        {"217","not"}
    };

    //Tabla de Errores
    String ErroresLexicos[][] = {
        {"500","Se espera un digito "},
        {"501","Se espera un = "},
        {"502","Se espera otro = "},
        {"503","Se espera cerrar el comentario "},
        {"504","Se espera cerrar la cadena "},
        {"505","Simbolo Invalido "}
    };

    RandomAccessFile file = null;
    
    public Compilador(){
        try{
            file = new RandomAccessFile(archivo,"r");
            
            //Lee el archivo hasta el final
            while (!endOfFile){
                
                caracter = file.read();
                
                //Final del archivo
                if(caracter == -1){
                    columna = 19;
                    endOfFile = true;
                }else if(Character.isLetter((char)caracter)){ //Caracter
                    columna = 0;
                    
                } else if(Character.isDigit((char)caracter)){ //Digito
                    columna = 1;
                }else{      
                    switch ((char) caracter){
                        case '.': columna = 2;
                        break;
                        
                        case '+': columna = 3;
                        break;
                        
                        case '-': columna = 4;
                        break;
                        
                        case '*': columna = 5;
                        break;
                        
                        case '/': columna = 6;
                        break;
                        
                        case '<': columna = 7;
                        break;
                        
                        case '>': columna = 8;
                        break;
                        
                        case '=': columna = 9;
                        break;
                        
                        case ':': columna = 10;
                        break;
                        
                        case '(': columna = 11;
                        break;
                        
                        case ')': columna = 12;
                        break;
                        
                        case ',': columna = 13;
                        break;
                        
                        case ';': columna = 14;
                        break;
                        
                        case '"': columna = 15;
                        break;

                        //EB
                        case ' ': columna = 16; 
                        break;
                        
                        //Tab
                        case 9: columna = 17;
                        break;

                        //EOL
                        case 10: 
                        columna = 18;
                        numLinea++;
                        break;

                        //EOL
                        case 13: columna = 18; 
                        break;

                        //Oc
                        default: 
                        columna = 20;
                        System.out.println((char) caracter);
                        break;
                    }
                }

                //Valor dado de la matriz 
                valorMatriz = Matriz[estado][columna];

                //Si valorMatriz < 100, el valor de estado se convierte en valorMatriz
                if (valorMatriz < 100) { //Valida si es un estado
                    estado = valorMatriz;

                    if (estado == 0) {
                        Lexema = "";
                    }else{
                        Lexema = Lexema + (char) caracter;
                    }
                }
                else if (valorMatriz >= 100 && valorMatriz < 500){ //Valida si es palabra reservada
                    if (valorMatriz == 100) {
                        PalabraReservada();
                    }

                    if (valorMatriz == 100 || valorMatriz == 101 || valorMatriz == 102 || valorMatriz == 106 ||
                        valorMatriz == 107 || valorMatriz == 109 || valorMatriz == 120 ||valorMatriz >= 200) { //
                        file.seek(file.getFilePointer()-1);
                    }else{
                        Lexema = Lexema + (char) caracter;
                    }

                    //Se termina todo, se reinicia el estado y el lexema y se vuelve a leer el archivo
                    //Esto pasa hasta que llegue al final del archivo o salga algun error
                    InsertarNodo();
                    estado = 0;
                    Lexema = "";
                }else{
                    ImprimirErrorLexico(); //Imprime un error de la tabla y reinicia todo
                    estado=0; 
                    Lexema = ""; 
                }
            }
            //Una vez, acabado el ciclo, se imprimen todos los nodos añadidos, y se agregan en la estructura que es impresa
            //lexema, token, y numero de linea
            ImprimirNodo();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //*Metodos del Analizador Lexico================================================================================================================
    //*Metodo para validar si es palabra reservada
    private void PalabraReservada(){
        for(String [] PalRes : PalabrasReservadas){
            if(Lexema.equals(PalRes[1])){
                valorMatriz = Integer.valueOf(PalRes[0]);
            }
        }
    }

    //Metodo para imprimir los errores lexicos
    private void ImprimirErrorLexico(){
        if ((caracter != -1 && valorMatriz >= 500) || (caracter == -1 && valorMatriz >= 500))  {
            for(String[] Errores : ErroresLexicos){
                if (valorMatriz == Integer.valueOf(Errores[0])){
                    System.out.println("El error encontrado es: " + "Error " + valorMatriz + ", " + Errores[1]  + "en la linea: " + (numLinea));
                }
            }
        }
        errorFound = true;
    }

    //Metodo para meter un nuevo nodo
    private void InsertarNodo(){
        Nodo nodo = new Nodo(Lexema, valorMatriz, numLinea);
        
        if(head == null){
            head = nodo;
            puntero = head;
        }
        else{
            puntero.next = nodo;
            puntero = nodo;
        }
    }

    //Metodo para imprimr nodo
    private void ImprimirNodo(){
        puntero = head;
        while (puntero != null){
            if(!puntero.lexema.trim().isEmpty()){
                System.out.println(puntero.lexema + " " + puntero.token + " " + puntero.linea);
            }
            puntero = puntero.next;
        }
    }

    //!===================================================================================================================================================
    //!Analizador sintáctico

    public void Sintaxis(){
        System.out.println("Analizador Sintáctico\n");
        puntero = head;

        while (puntero != null) {
            switch (puntero.token) {
                case 200: // program
                    puntero = puntero.next;
                    if (puntero.token != 100) { // identificador
                        System.out.println("Error 507: Se esperaba identificador en línea: " + puntero.linea);
                        System.exit(0);
                    }
                    puntero = puntero.next;
                    if (puntero.token != 113) { // (
                        System.out.println("Error 508: Se esperaba ( en línea: " + puntero.linea);
                        System.exit(0);
                    }
                    Declarar_ID();
                    if (puntero.next == null) break;
                    puntero = puntero.next;
                    while (puntero.token == 201) { // var
                        Declarar_Var();
                        if (puntero.next == null) break;
                        puntero = puntero.next;
                    }
                    if (puntero.token != 206) { // begin
                        System.out.println("Error 515: Se esperaba begin en línea: " + puntero.linea);
                        System.exit(0);
                    }
                    while (puntero.token != 115) { // end
                        BloqueEnunciados();
                        if (puntero.next == null) break;
                        puntero = puntero.next;
                    }
                    if (puntero.token == 115) { // .
                        System.out.println("Se Termina de Leer el Programa");
                        System.exit(0);
                        break;
                    } else {
                        System.out.println("Error 511: Se esperaba . en línea: " + puntero.linea);
                        System.exit(0);
                    }
                    break;
                    
                default:
                    System.out.println("Error 506: Se esperaba program en línea: " + puntero.linea);
                    System.exit(0);
            }
        }
            puntero = puntero.next;
        }
    

    //?==============================================================================================================================================================
    //?Metodos del analizador sintáctico

    //Metodo para declarar IDs en el Encabezado
    private void Declarar_ID() {
        puntero = puntero.next;

        while (puntero.token != 114) {
            switch (puntero.token) {
                case 100: // Identificador
                    puntero = puntero.next;
                    if (puntero.token == 116) { // Coma
                        puntero = puntero.next;
                    } else {
                        break;
                    }
                    break;
                default:
                    System.out.println("Error 507: Se espera identificador en línea: " + puntero.linea);
                    System.exit(0);
            }
        }
    
        if (puntero.token == 114) { // Paréntesis de cierre
            puntero = puntero.next;
            if (puntero.token == 117) { // Punto y coma
                System.out.println("Se declararon IDs en línea " + puntero.linea);
            } else {
                System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                System.exit(0);
            }
        } else {
            System.out.println("Error 509: Se esperaba ) en línea: " + puntero.linea);
            System.exit(0);
        }
    }
    

    //Metodo para declarar las variables
    private void Declarar_Var() {
        if (puntero.token == 201) { // var
            puntero = puntero.next;
            while (puntero.token != 120) { // :
                switch (puntero.token) {
                    case 100: // Identificador
                        puntero = puntero.next;
                        if (puntero.token == 116) { // Coma
                            puntero = puntero.next;
                        } else {
                            break;
                        }
                        break;
                    default:
                        System.out.println("Error 507: Se espera identificador en línea: " + puntero.linea);
                        System.exit(0);
                        return;
                }
            }
    
            if (puntero.token == 120) { // :
                Type();
                if (puntero.token == 117) { // Punto y coma
                    System.out.println("Se declararon variables en linea " + puntero.linea);
                } else {
                    System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                    System.exit(0);
                }
            } else {
                System.out.println("Error 512: Se esperaba : en línea " + puntero.linea);
                System.exit(0);
            }
        } else {
            System.out.println("Error 513: Se esperaba var en línea: " + puntero.linea);
            System.exit(0);
        }
    }

    //Metodo que hace la creacion de los bloques de enunciados
    public void BloqueEnunciados(){
        if(puntero.token == 206){ //begin
            dato = 206;
            Push(dato);
            if(puntero.next == null){
            }else{ 
                puntero = puntero.next;
            }
            if(puntero.token == 206){
                System.out.println("Error 527: No se puede abrir otro bloque de enunciados. Línea: " + puntero.linea);
                System.exit(0);
            }
            while(puntero.token != 207){
                BloqueSec();
                if(puntero.next == null){
                    break;
                } 
            }
            if(tope == 1){
                if(puntero.token == 207){ //end
                    dato = 207;
                    Push(dato);
                }
                else{
                    System.out.println("Error 516: Se esperaba end en línea " + puntero.linea);
                    System.exit(0);
                }//fin end
            }
        }else{
            System.out.println("Error 515: Se esperaba begin en línea " + puntero.linea);
            System.exit(0);
        }
    }
    
    //Metodo que hace que todo lo que este adentro del bloque de enunciados funcione
    public void BloqueSec(){
        if(puntero.token == 100){ // ID
            if(puntero.next == null){
            }else{ 
                puntero = puntero.next;
            }
            if(puntero.token == 118){
                puntero = puntero.next;
                Expresion_Sencila();
                if(puntero.token == 107 || puntero.token == 108 || puntero.token == 109 || puntero.token == 110 ||
                   puntero.token == 111 || puntero.token == 112){
                    Expresion_Condicional();
                    if(puntero.next.token == 100){
                        Expresion_Condicional();
                    }
                    if(puntero.token == 117){
                        System.out.println("Se ha asignado una variable en línea " + puntero.linea);
                        if(puntero.next == null){
                        }else{ 
                            puntero = puntero.next;
                        }
                    }else{
                        System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                        System.exit(0);
                    }
                }else{
                    if(puntero.token == 117){
                        System.out.println("Se ha asignado una variable en línea " + puntero.linea);
                        if(puntero.next == null){
                        }else{ 
                            puntero = puntero.next;
                        }
                    }else{
                        System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                        System.exit(0);
                    }
                }
            }else{
                if(puntero.token == 107 || puntero.token == 108 || puntero.token == 109 || puntero.token == 110 ||
                   puntero.token == 111 || puntero.token == 112){
                    Expresion_Condicional();
                    if(puntero.next.token == 100){
                        Expresion_Condicional();
                    }
                    if(puntero.token == 117){
                        System.out.println("Se ha realizado operación relación en línea " + puntero.linea);
                        if(puntero.next == null){
                        }else{ 
                            puntero = puntero.next;
                        }
                    }else{
                        System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                        System.exit(0);
                    }
                }else{
                    System.out.println("Error 517: Se esperaba := u operador condicional en línea " + puntero.linea);
                    System.exit(0);
                }
            }    
        }//fin ID

        if(puntero.token == 208){ //read
            if(puntero.next == null){
            }else{ 
                puntero = puntero.next;
            }
            if(puntero.token == 113){
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                if(puntero.token == 100){
                    if(puntero.next == null){
                    }else{ 
                        puntero = puntero.next;
                    }
                    if(puntero.token == 114){
                        if(puntero.next == null){
                        }else{ 
                            puntero = puntero.next;
                        }
                        if(puntero.token == 117){
                            System.out.println("Se ha leído en línea " + puntero.linea);
                            if(puntero.next == null){
                            }else{ 
                                puntero = puntero.next;
                            }
                        }else{
                            System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                            System.exit(0);
                        }
                    }else{
                        System.out.println("Error 509: Se esperaba ) en línea " + puntero.linea);
                        System.exit(0);
                    }
                }else{
                    System.out.println("Error 507: Se esperaba identificador en línea " + puntero.linea);
                    System.exit(0);
                }
                
            }else{
                System.out.println("Error 508: Se esperaba ( en línea " + puntero.linea);
                System.exit(0);
            }
        }//fin read

        if(puntero.token == 209){ //write
            if(puntero.next == null){
            }else{ 
                puntero = puntero.next;
            }
            if(puntero.token == 113){
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                while(puntero.token != 114){
                    if (puntero.token == 100 || puntero.token == 119) {
                        if(puntero.next == null){
                        }else{ 
                            puntero = puntero.next;
                        }
                        if (puntero.token == 116) {
                            puntero = puntero.next;
                        }else{
                            break;
                        }
                    } else {
                        System.out.println("Error 507: Se esperaba identificador en línea " + puntero.linea);
                        System.exit(0);
                    }
                }
        
                if (puntero.token == 114) {
                    if(puntero.next == null){
                    }else{ 
                        puntero = puntero.next;
                    }
                    if (puntero.token == 117){
                        System.out.println("Se está imprimiendo en línea " + puntero.linea);
                        if(puntero.next == null){
                        }else{ 
                            puntero = puntero.next;
                        }
                    } else{
                        System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                        System.exit(0);
                    }
                } else {
                    System.out.println("Error 509: Se esperaba ) en línea " + puntero.linea);
                    System.exit(0);
                }
            }else{
                System.out.println("Error 508: Se esperaba ( en línea " + puntero.linea);
                System.exit(0); 
            }
        }//fin write

        //todo/ Aqui van los condicionales
        if(puntero.token == 213){ //while
            if(puntero.next == null){
            }else{ 
                puntero = puntero.next;
            }
            if(puntero.token == 100){ //ID
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                Expresion_Condicional();
                if(puntero.next.token == 100 || puntero.next.token == 101 || puntero.next.token == 102){
                    Expresion_Condicional();
                }
                if(puntero.token == 214){ //do
                    if(puntero.next == null){
                    }else{ 
                        puntero = puntero.next;
                    }
                    if(puntero.token == 206){ //begin
                        dato = 206;
                        Push(dato);
                        if(puntero.next == null){
                        }else{ 
                            puntero = puntero.next;
                        }
                        while(puntero.token != 207){ 
                            BloqueSec(); 
                            if(puntero.next == null){
                                break;
                            }
                        }
                        if(puntero.next.token != 115){
                            if(puntero.token == 207){ //end
                                dato = 207;
                                Push(dato);
                                if(puntero.next == null){
                                }else{ 
                                    puntero = puntero.next;
                                }
                                if (puntero.token == 117){
                                    System.out.println("Ciclo while terminado en línea " + puntero.linea);
                                    if(puntero.next == null){
                                    }else{ 
                                        puntero = puntero.next;
                                    }
                                } else{puntero = puntero.next;
                                    System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                                    System.exit(0);
                                }
                            }
                        }else{
                            System.out.println("Error 516: Se esperaba end en línea " + puntero.linea);
                            System.exit(0);
                        } 
                    }else{
                        System.out.println("Error 515: Se esperaba begin en línea " + puntero.linea);
                        System.exit(0);
                    }    
                }else{
                    System.out.println("Error 525: Se esperaba do en línea " + puntero.linea);
                    System.exit(0);
                }
            }else{
                System.out.println("Error 507: Se esperaba identificador en línea " + puntero.linea);
                System.exit(0);
            }
        }//fin while

        if(puntero.token == 210){ //if
            if(puntero.next == null){
            }else{ 
                puntero = puntero.next;
            }
            if(puntero.token == 100){ //ID
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                Expresion_Condicional();
                if(puntero.next.token == 100 || puntero.next.token == 101 || puntero.next.token == 102){
                    Expresion_Condicional();
                }
                if(puntero.token == 211){ //then
                    if(puntero.next == null){
                    }else{ 
                        puntero = puntero.next;
                    }
                    if(puntero.token == 206){ //begin
                        dato = 206;
                        Push(dato);
                        if(puntero.next == null){
                        }else{ 
                            puntero = puntero.next;
                        }
                        while(puntero.token != 207){
                            BloqueSec();
                            if(puntero.next == null || puntero.token != 207){
                                System.out.println("Error 516: Se esperaba end en línea " + puntero.linea);
                                System.exit(0);
                                break;
                            }
                        }
                        if(puntero.next.token != 115 || puntero.next.token == 212){
                            if (puntero.token == 207){ //end
                                dato = 207;
                                Push(dato);
                                if(puntero.next == null){
                                }else{ 
                                    puntero = puntero.next;
                                }

                                if(puntero.token != 212){
                                    if (puntero.token == 117){
                                        if(puntero.next.token == 212){
                                            System.out.println("Error 526: Se esperaba quitar el ; en línea " + puntero.linea);
                                            System.exit(0);  
                                        }else{
                                            System.out.println("if terminado en linea " + puntero.linea);
                                            if(puntero.next == null){
                                            }else{ 
                                                puntero = puntero.next;
                                            }
                                        }

                                        if(puntero.token == 206){
                                            System.out.println("Error 527: No se puede abrir otro bloque de enunciados. Línea: " + puntero.linea);
                                            System.exit(0);
                                        }

                                    } else{
                                        System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                                        System.exit(0);
                                    }       
                                }    

                                if(puntero.token == 212){// else
                                    if(puntero.next == null){
                                    }else{ 
                                        puntero = puntero.next;
                                    }
                                    if(puntero.token == 206){ //begin-else
                                        dato = 206;
                                        Push(dato);
                                        if(puntero.next == null){
                                        }else{ 
                                            puntero = puntero.next;
                                        }
                                        if(puntero.token != 207){
                                            BloqueSec();
                                            if(puntero.next == null){
                                                System.exit(0);
                                            }else{
                                                if(puntero.next.token != 115){
                                                    if(puntero.token == 207){ //end-else
                                                        dato = 207;
                                                        Push(dato);
                                                        if(puntero.next == null){
                                                        }else{ 
                                                            puntero = puntero.next;
                                                        }
                                                        if (puntero.token == 117){
                                                            System.out.println("if-else terminado en línea " + puntero.linea);
                                                            if(puntero.next == null){
                                                            }else{ 
                                                                puntero = puntero.next;
                                                            }
                                                        } else{
                                                            System.out.println("Error 510: Se esperaba ; en línea " + puntero.linea);
                                                            System.exit(0);
                                                        }                                                        
                                                    }
                                                }else{
                                                    System.out.println("Error 516: Se esperaba end en línea " + puntero.linea);
                                                    System.exit(0);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else{
                            System.out.println("Error 516: Se esperaba end en línea " + puntero.linea);
                            System.exit(0); 
                        }    
                    }else{
                        System.out.println("Error 515: Se esperaba begin en línea " + puntero.linea);
                        System.exit(0);
                    }
                }else{
                    System.out.println("Error 523: Se esperaba then en línea " + puntero.linea);
                    System.exit(0);
                }
            }else{
                System.out.println("Error 507: Se esperaba identificador en línea " + puntero.linea);
                System.exit(0);
            }
        }//fin if
    }


    //Metodo que asigna los tipos de las variables
    private void Type() {
        if (puntero.next != null) {
            puntero = puntero.next;
        }
    
        switch (puntero.token) {
            case 202: 
            case 203:
            case 204:
            case 205:
                if (puntero.next != null) {
                    puntero = puntero.next;
                }
                break;
            default:
                System.out.println("Error 514: Se esperaba un tipo en línea " + puntero.linea);
                System.exit(0);
                break;
        }
    }

    //Metodo que verifica las expresiones matematicas o relacionales
    private void Expresion_Sencila() {
        if (puntero.token == 103 || puntero.token == 104 || (puntero.next != null && puntero.next.token == 100)) {
            Signo();
            Termino();
        } else if (puntero.next != null && (puntero.next.token == 103 || puntero.next.token == 104 || puntero.next.token == 215)) {
            Factor();
            Operador_Add();
            Expresion_Sencila();
        } else if (puntero.token == 113) {
            avanzarPuntero();
            Expresion_Sencila();
            if (puntero.token == 114) {
                avanzarPuntero();
            } else {
                System.out.println("Error 509: Se esperaba ) en línea " + puntero.linea);
                System.exit(0);
            }
        } else {
            Termino();
        }
    }
    
    private void avanzarPuntero() {
        if (puntero.next != null) {
            puntero = puntero.next;
        }
    }

    //Metodo que solo verifica las expresiones condicionales
    private void Expresion_Condicional() {
        Operador_Relacional();
        Expresion_Sencila();
    }

    //Metodo que verifica los terminos para operadores multiplicativos
    private void Termino() {
        if(puntero.next.token == 105 || puntero.next.token == 106 || puntero.next.token == 216){
            Factor();
            Operador_Mult();
            Factor();
        }
        else{
            Factor();
        }
    }

    //Metodo que verifica si hay signos u operadores logicos
    private void Signo() {
        if(puntero.token == 103 || puntero.token == 104){
            if(puntero.next == null){
            }else{ 
                puntero = puntero.next;
            }
        }
        else{ 
            System.out.println("Error 518: Se esperaba un signo u operador lógico en línea " + puntero.linea);
            System.exit(0);
        }
    }

    //Metodo que verifica que factores hay en la expresion
    private void Factor() {
        switch(puntero.token){
            case 100:
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            case 101: 
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            case 102:
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            case 119:
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            default:
                System.out.println("Error 519: Se esperaba un dato en línea " + puntero.linea);
                System.exit(0);
                break;
        }
    }

    //Metodo que verifica si hay signos u operadores aditivos
    private void Operador_Add() {
        if(puntero.token == 103 || puntero.token == 104 || puntero.token == 215){
            if(puntero.next == null){
            }else{ 
                puntero = puntero.next;
            }
        }
        else{
            System.out.println("Error 520: Se esperaba un operador aditivo en línea " + puntero.linea);
            System.exit(0);
        }
    }

    //Metodo que verifica si hay operadores relacionales
    private void Operador_Relacional() {
        switch(puntero.token){
            case 107:
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            case 108: 
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            case 109:
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            case 110:
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            case 111: 
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            case 112:
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            case 217:
                if(puntero.next == null){
                }else{ 
                    puntero = puntero.next;
                }
                break;
            default:
                System.out.println("Error 521: Se esperaba un operador relacional en línea " + puntero.linea);
                System.exit(0);
                break;
        }
    }

    //Metodo que verifica si hay signos u operadores multiplicativos
    private void Operador_Mult() {
        if(puntero.token == 105 || puntero.token == 106 || puntero.token == 216){
            puntero = puntero.next;
        }
        else{
            System.out.println("Error 522: Se esperaba un operador multiplicativo en línea " + puntero.linea);
            System.exit(0);
        }
    }

    //todo/ Metodos de la Pila================================================================================================================   
    public  boolean PilaFull(){
        int x = pila.length;
        if(x == tope){
            return true;
        }else{
            return false;
        }
    }

    public boolean PilaVacia(){
        if(tope == 0){
            return true;
        }else{
            return false;
        }
    }

    public void Push(int dato) {
        if (dato == 206) {
            if (PilaFull()) {
                System.out.println("Pila Llena");
            } else {
                pila[tope] = dato;
                ++tope;
            }
        } else {
            if (dato == 207) {
                if (PilaVacia()) {
                    System.out.println("Pila Vacia");
                } else {
                    Pop();
                }
            }
        }
    }

    public void Pop() {
        pila[tope - 1] = 0;
        tope--;
    }

    public void Mostrar() {
        for (int i = 0; i < pila.length ; i++) {
            System.out.println(pila[i]);
        }
    }
}