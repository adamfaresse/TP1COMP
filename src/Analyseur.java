import javax.swing.text.TabableView;
import java.io.*;
import java.util.*;

public class Analyseur {
    int LONG_MAX_IDENT = 20;
    int LONG_MAX_CHAINE = 50;
    int NB_MOTS_RESERVES = 7;
    static File Source;
    static  char CARLU;
    static int NOMBRE;
    static String CHAINE = "";
    static int NUM_LIGNE;
    static String[] TABLE_MOTS_RESERVES;
    static ArrayList<String> TABLE_MOTS_RESERVES_LISTE ;
    static Character[]SYMBOLE_SIMPLE = {',', ';', '.', ':', '(', ')', '<', '>', '=', '+', '-', '*', '/'};
    static ArrayList<Character> SYMBOLE_SIMPLE_LISTE = new ArrayList<>(List.of(SYMBOLE_SIMPLE));
    static ArrayList<Identificateur> TABLE_IDENT = new ArrayList<Identificateur>();
    static ArrayList<String> tableau = new ArrayList<String>();



    static Map<Integer,String> table_erreur = new HashMap<Integer,String>();
    File doc;
    FileReader fileReader;
    BufferedReader scanner;
    String pathname;

    Analyseur(String pathname) throws FileNotFoundException {
        table_erreur.put(1,"fin de fichier atteinte");
        table_erreur.put(2,"Taille de Nombre trop grand");
        table_erreur.put(3,"Taille de chaine trop longue");
        this.pathname = pathname;

    }

    String ERREUR(int num_erreur){
        return table_erreur.get(num_erreur)+NUM_LIGNE;
    }


    void LIRE_CAR() throws IOException {
        CARLU = '$';
        int c = 0;

        if((c = scanner.read())==-1){

            System.out.println(ERREUR(1));


        }else {


            CARLU = (char) c;
            if ((int) CARLU == 13) {
                NUM_LIGNE = NUM_LIGNE + 1;
            }
        }


    }

    void SAUTER_SEPARATEURS() throws IOException {
        if(CARLU == '{'){
            LIRE_CAR();
            while((CARLU !='}')){
                LIRE_CAR();
                if(CARLU == '{'){
                    SAUTER_SEPARATEURS();
                }
            }
            LIRE_CAR();
        }
        if((int)CARLU == 13  || (int)CARLU == 32  || (int)CARLU == 10){
            while((int)CARLU == 13  || (int)CARLU == 32  || (int)CARLU == 10){
                LIRE_CAR();
            }
        }

    }


    String RECO_ENTIER() throws IOException {
        NOMBRE =0;

        int i = 10;
        while((int)CARLU >47 && (int)CARLU <58){
            NOMBRE = (NOMBRE * 10) + Character.getNumericValue(CARLU);
            LIRE_CAR();
            if(NOMBRE >Integer.MAX_VALUE/10){
                System.out.println(ERREUR(2));
                break;
            }

        }
        return "ent : "+NOMBRE;
    }

    String RECO_CHAINE() throws IOException {
        LIRE_CAR();
        CHAINE =String.valueOf(CARLU);
        boolean loop = true;
        while(true){
            LIRE_CAR();
            if ((int)CARLU == 39){
                LIRE_CAR();
                if((int)CARLU != 39){
                    break;
                }
            }
            CHAINE = CHAINE + CARLU;
            if(CHAINE.length()>LONG_MAX_CHAINE-1){
                System.out.println(ERREUR(3));
                break;
            }

        }
        LIRE_CAR();


        return "ch : "+CHAINE;
    }

    String RECO_IDENT_OU_MOT_RESERVE() throws IOException {
        CHAINE = String.valueOf(Character.toUpperCase(CARLU));
        LIRE_CAR();

        while((int)CARLU == 95 || ((int)CARLU >96 && (int)CARLU < 123)  || ((int)CARLU >64 && (int)CARLU<91) || ((int)CARLU >47 && (int)CARLU<58)){
            if((int)CARLU >96){
                CARLU = Character.toUpperCase(CARLU);
            }
            if(CHAINE.length()<LONG_MAX_IDENT){
                CHAINE = CHAINE + CARLU;
            }
            LIRE_CAR();

        }

        if( TABLE_MOTS_RESERVES_LISTE.contains(CHAINE)){
            return "motcle : "+CHAINE;
        }
        tableau.add(CHAINE);
        return "ident : "+CHAINE;

    }
    boolean EST_UN_MOT_RESERVE(){
        for(String chaine :TABLE_MOTS_RESERVES){
            if (Objects.equals(chaine, CHAINE)){
                return true;
            }
        }
        return false;
    }

    String RECO_SYMB() throws IOException{
        if(CARLU == ','){
            LIRE_CAR();
            return "virg";
        }
        if(CARLU == ';'){
            LIRE_CAR();
            return "ptvirg";
        }
        if(CARLU == '.'){
            LIRE_CAR();
            return "point";
        }
        if(CARLU == '='){
            LIRE_CAR();
            return "eg";
        }
        if(CARLU == '+'){
            LIRE_CAR();
            return "plus";
        }
        if(CARLU == '-'){
            LIRE_CAR();
            return "moins";
        }
        if(CARLU == '*'){
            LIRE_CAR();
            return "mult";
        }
        if(CARLU == '/'){
            LIRE_CAR();
            return "div";
        }
        if(CARLU == '('){
            LIRE_CAR();
            return "parouv";
        }
        if(CARLU == ')'){
            LIRE_CAR();
            return "parfer";
        }
        if(CARLU == '<'){
            LIRE_CAR();
            if(CARLU == '>'){
                LIRE_CAR();
                return "diff";
            }
            if(CARLU == '='){
                LIRE_CAR();
                return "infe";
            }
            return "inf";
        }
        if(CARLU == '>'){
            LIRE_CAR();
            if(CARLU== '='){
                LIRE_CAR();
                return "suppe";
            }
            return "supp";
        }
        if(CARLU == ':'){
            LIRE_CAR();
            if(CARLU == '='){
                LIRE_CAR();
                return "aff";
            }
            return "deuxpts";
        }
        return "erreur";
    }

    void ANALEX() throws IOException, InterruptedException {
        while(CARLU != '$'){

            Thread.sleep(10);
            SAUTER_SEPARATEURS();

            if((int)CARLU >47 && (int)CARLU <58){
                System.out.println(RECO_ENTIER());

            }
            else if((int)CARLU == 39){
                System.out.println(RECO_CHAINE());

            }
            else if(((int)CARLU >96 && (int)CARLU < 123)  || ((int)CARLU >64 && (int)CARLU<91) ){
                System.out.println(RECO_IDENT_OU_MOT_RESERVE());
            }
            else if(SYMBOLE_SIMPLE_LISTE.contains(CARLU)){
                System.out.println(RECO_SYMB());
            }

        }
    }


    void INITIALISER() throws IOException {
        NUM_LIGNE = 0;
        TABLE_MOTS_RESERVES = new String[]{"PROGRAMME", "DEBUT", "FIN", "CONST", "VAR", "ECRIRE", "LIRE"};
        TABLE_MOTS_RESERVES_LISTE = new ArrayList<>(List.of(TABLE_MOTS_RESERVES));
        this.doc = new File(pathname);
        this.fileReader = new FileReader(doc);
        this.scanner = new BufferedReader(fileReader);

    }
    void TERMINER() throws IOException {
        scanner.close();
    }
    int CHERCHER(String nomIdentificateur){
        for(Identificateur identificateur:TABLE_IDENT){

            if (identificateur.getNom().equals(nomIdentificateur)){
                return TABLE_IDENT.indexOf(identificateur);
            }
        }

        return -1;
    }

    void INSERER(String nomIdentifiacteur , String genreIdentificateur){
        Identificateur identificateur = new Identificateur(nomIdentifiacteur,genreIdentificateur);
        TABLE_IDENT.add(identificateur);
    }
    void AFFICHE_TABLE_IDENT(){
        for (Identificateur identificateur :TABLE_IDENT){
            System.out.print("[ "+identificateur.getNom() +" : "+ identificateur.getGenre()+" ]");
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Analyseur analyseur = new Analyseur("C:\\Users\\stekf\\Desktop\\TPL3\\TP1COMP\\src\\test3");
        analyseur.INITIALISER();
        analyseur.LIRE_CAR();
        analyseur.ANALEX();
        for(String nomIdentificateur :tableau){

            if(analyseur.CHERCHER(nomIdentificateur)==-1){
                analyseur.INSERER(nomIdentificateur,null);
            }
        }
        analyseur.TERMINER();
        analyseur.AFFICHE_TABLE_IDENT();
    }


}
