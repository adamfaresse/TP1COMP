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
        table_erreur.put(4,"erreur syntaxique ou semantique");
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
            SAUTER_SEPARATEURS();
        }
        if((int)CARLU == 13  || (int)CARLU == 32  || (int)CARLU == 10){
            while((int)CARLU == 13  || (int)CARLU == 32  || (int)CARLU == 10){
                LIRE_CAR();
            }
            SAUTER_SEPARATEURS();
        }


    }


    T_UNILEX RECO_ENTIER() throws IOException {
        NOMBRE =0;

        while((int)CARLU >47 && (int)CARLU <58){
            NOMBRE = (NOMBRE * 10) + Character.getNumericValue(CARLU);
            LIRE_CAR();
            if(NOMBRE >Integer.MAX_VALUE/10){
                System.out.println(ERREUR(2));
                break;
            }

        }
        System.out.println("ent : "+NOMBRE);
        return T_UNILEX.ent;
    }

    T_UNILEX RECO_CHAINE() throws IOException {
        LIRE_CAR();
        CHAINE =String.valueOf(CARLU);
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

        System.out.println("ch : "+CHAINE);
        return T_UNILEX.ch;
    }

    T_UNILEX RECO_IDENT_OU_MOT_RESERVE() throws IOException {
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
            System.out.println("motcle : "+CHAINE);
            return T_UNILEX.motcle;
        }
        tableau.add(CHAINE);
        System.out.println("ident : "+CHAINE);
        return T_UNILEX.ident;

    }
    /*boolean EST_UN_MOT_RESERVE(){
        for(String chaine :TABLE_MOTS_RESERVES){
            if (Objects.equals(chaine, CHAINE)){
                return true;
            }
        }
        return false;
    }*/

    T_UNILEX RECO_SYMB() throws IOException{
        if(CARLU == ','){
            LIRE_CAR();
            return T_UNILEX.virg;
        }
        if(CARLU == ';'){
            LIRE_CAR();
            return T_UNILEX.ptvirg;
        }
        if(CARLU == '.'){
            LIRE_CAR();
            return T_UNILEX.point;
        }
        if(CARLU == '='){
            LIRE_CAR();
            return T_UNILEX.eg;
        }
        if(CARLU == '+'){
            LIRE_CAR();
            return T_UNILEX.plus;
        }
        if(CARLU == '-'){
            LIRE_CAR();
            return T_UNILEX.moins;
        }
        if(CARLU == '*'){
            LIRE_CAR();
            return T_UNILEX.mult;
        }
        if(CARLU == '/'){
            LIRE_CAR();
            return T_UNILEX.div;
        }
        if(CARLU == '('){
            LIRE_CAR();
            return T_UNILEX.parouv;
        }
        if(CARLU == ')'){
            LIRE_CAR();
            return T_UNILEX.parfer;
        }
        if(CARLU == '<'){
            LIRE_CAR();
            if(CARLU == '>'){
                LIRE_CAR();
                return T_UNILEX.diff;
            }
            if(CARLU == '='){
                LIRE_CAR();
                return T_UNILEX.infe;
            }
            return T_UNILEX.inf;
        }
        if(CARLU == '>'){
            LIRE_CAR();
            if(CARLU== '='){
                LIRE_CAR();
                return T_UNILEX.supe;
            }
            return T_UNILEX.sup;
        }
        if(CARLU == ':'){
            LIRE_CAR();
            if(CARLU == '='){
                LIRE_CAR();
                return T_UNILEX.aff;
            }
            return T_UNILEX.deuxpts;
        }
        return T_UNILEX.Erreur;
    }

    T_UNILEX ANALEX() throws IOException, InterruptedException {


            Thread.sleep(10);
            SAUTER_SEPARATEURS();

            if((int)CARLU >47 && (int)CARLU <58){
                T_UNILEX valeur = RECO_ENTIER();

                return valeur;

            }
            else if((int)CARLU == 39){
                T_UNILEX valeur = RECO_CHAINE();

                return valeur;

            }
            else if(((int)CARLU >96 && (int)CARLU < 123)  || ((int)CARLU >64 && (int)CARLU<91) ){
                T_UNILEX valeur = RECO_IDENT_OU_MOT_RESERVE();

                return valeur;
            }
            else if(SYMBOLE_SIMPLE_LISTE.contains(CARLU)){
                T_UNILEX valeur = RECO_SYMB();
                System.out.println(valeur);
                return valeur;
            }

        return T_UNILEX.Erreur;
    }


    void INITIALISER() throws IOException {
        NUM_LIGNE = 0;
        TABLE_MOTS_RESERVES = new String[]{"PROGRAMME", "DEBUT", "FIN", "CONST", "VAR", "ECRIRE", "LIRE"};
        TABLE_MOTS_RESERVES_LISTE = new ArrayList<>(List.of(TABLE_MOTS_RESERVES));
        this.doc = new File(pathname);
        this.fileReader = new FileReader(doc);
        this.scanner = new BufferedReader(fileReader);
        AnalyseurSyntaxique.NB_CONST_CHAINE = 0;
        AnalyseurSyntaxique.DERNIERE_ADRESSE_VAR_GLOB = -1;
        AnalyseurSyntaxique.VAL_DE_CONST_CHAINE = new String[10];
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

    void INSERER(Identificateur identificateur ){
        TABLE_IDENT.add(identificateur);
    }
    void AFFICHE_TABLE_IDENT(){
        for (Identificateur identificateur :TABLE_IDENT){
            System.out.print("[ "+identificateur.getNom() +" : "+ identificateur.getTyp()+" ]");
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Analyseur analyseur = new Analyseur("C:\\Users\\stekf\\Desktop\\TPL3\\TP1COMP\\src\\test3");
        analyseur.INITIALISER();
        analyseur.LIRE_CAR();
        while(analyseur.ANALEX() != T_UNILEX.Erreur){
            analyseur.ANALEX();
        }

        for(String nomIdentificateur :tableau){

            if(analyseur.CHERCHER(nomIdentificateur)==-1){
                Identificateur identificateur = new Identificateur(nomIdentificateur);
                analyseur.INSERER(identificateur);
            }
        }
        analyseur.TERMINER();
        analyseur.AFFICHE_TABLE_IDENT();
    }


}
