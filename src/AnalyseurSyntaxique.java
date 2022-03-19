import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;

public class AnalyseurSyntaxique {
    Analyseur analyseur;
    static T_UNILEX UNILEX ;
    static int NB_CONST_CHAINE;
    static String[] VAL_DE_CONST_CHAINE ;
    static int DERNIERE_ADRESSE_VAR_GLOB;

    AnalyseurSyntaxique(Analyseur analyseur){
        this.analyseur = analyseur;
    }

    private boolean PROG() throws IOException, InterruptedException{
        System.out.println("PROG");
        if(UNILEX.equals(T_UNILEX.motcle) && Analyseur.CHAINE.equals("PROGRAMME")){
            UNILEX = analyseur.ANALEX();
            if (UNILEX.equals(T_UNILEX.ident)){
                UNILEX = analyseur.ANALEX();
                if(UNILEX.equals(T_UNILEX.ptvirg)){
                    UNILEX = analyseur.ANALEX();
                    DECL_CONST();
                    DECL_VAR();
                    BLOC();
                    return UNILEX.equals(T_UNILEX.point);
                }else return false;
            }else return false;
        }else return false;
    }

    private boolean DEFINIR_CONSTANTE(String nom,T_UNILEX ul){
        Identificateur identificateur = new Identificateur(nom);
        if(analyseur.CHERCHER(nom)!=-1){
            return false;
        }else{
            identificateur.setTyp("constante");
            if(ul.equals(T_UNILEX.ent)){
                identificateur.getMapInformationSupp().put("typc",0);
                identificateur.getMapInformationSupp().put("val",Analyseur.NOMBRE);
            }else{
                identificateur.getMapInformationSupp().put("typc",1);
                NB_CONST_CHAINE += 1;
                VAL_DE_CONST_CHAINE[NB_CONST_CHAINE] = Analyseur.CHAINE;
                identificateur.getMapInformationSupp().put("val",NB_CONST_CHAINE);
            }
            analyseur.INSERER(identificateur);
            return true;
        }
    }


    private boolean DECL_CONST() throws IOException, InterruptedException {
        System.out.println("DECL_CONST");
        boolean erreur;
        boolean fin;
        String nom_constante;
        if(UNILEX.equals(T_UNILEX.motcle)&& Analyseur.CHAINE.equals("CONST")){
            UNILEX = analyseur.ANALEX();
            if(UNILEX.equals(T_UNILEX.ident)){
                nom_constante = Analyseur.CHAINE;
                UNILEX = analyseur.ANALEX();
                if(UNILEX.equals(T_UNILEX.eg)){
                    UNILEX = analyseur.ANALEX();
                    if(UNILEX.equals(T_UNILEX.ent)|| UNILEX.equals(T_UNILEX.ch)){
                        if(DEFINIR_CONSTANTE(nom_constante,UNILEX)) {
                            UNILEX = analyseur.ANALEX();
                            fin = false;
                            erreur = false;
                            do {
                                if (UNILEX.equals(T_UNILEX.virg)) {
                                    UNILEX = analyseur.ANALEX();
                                    if (UNILEX.equals(T_UNILEX.ident)) {
                                        nom_constante = Analyseur.CHAINE;
                                        UNILEX = analyseur.ANALEX();
                                        if (UNILEX.equals(T_UNILEX.eg)) {
                                            UNILEX = analyseur.ANALEX();
                                            if (UNILEX.equals(T_UNILEX.ent) || UNILEX.equals(T_UNILEX.ch)) {
                                                if(DEFINIR_CONSTANTE(nom_constante,UNILEX)){
                                                    UNILEX = analyseur.ANALEX();
                                                }else{
                                                    fin = true;
                                                }

                                            } else {
                                                fin = true;
                                                erreur = true;
                                            }
                                        } else {
                                            fin = true;
                                            erreur = true;
                                        }
                                    } else {
                                        fin = true;
                                        erreur = true;
                                    }
                                } else {
                                    fin = true;
                                }
                            } while (!fin);
                            if (erreur) {
                                return false;
                            } else if (UNILEX.equals(T_UNILEX.ptvirg)) {
                                UNILEX = analyseur.ANALEX();
                                return true;
                            } else {
                                return false;
                            }
                        }else return false;
                    }else return false;
                }else return false;
            }else return false;
        }else return false;
    }

    private boolean DEFINIR_VARIABLE(String nom , T_UNILEX ul){
        Identificateur identificateur = new Identificateur(nom);
        if(analyseur.CHERCHER(nom)!= -1){
            return false;
        }else{
            identificateur.setTyp("variable");
            identificateur.getMapInformationSupp().put("typc",0);
            DERNIERE_ADRESSE_VAR_GLOB += 1;
            identificateur.getMapInformationSupp().put("adresse",DERNIERE_ADRESSE_VAR_GLOB);
            analyseur.INSERER(identificateur);
            return true;
        }
    }

    private boolean DECL_VAR() throws IOException, InterruptedException {
        System.out.println("DECL_VAR");
        boolean erreur;
        boolean fin;
        String nom_variable;
        if(UNILEX.equals(T_UNILEX.motcle) && Analyseur.CHAINE.equals("VAR")){
            UNILEX = analyseur.ANALEX();
            if(UNILEX.equals(T_UNILEX.ident)) {
                nom_variable = Analyseur.CHAINE;
                if (DEFINIR_VARIABLE(nom_variable, UNILEX)) {
                    UNILEX = analyseur.ANALEX();
                    fin = false;
                    erreur = false;

                    do {
                        if (UNILEX.equals(T_UNILEX.virg)) {
                            UNILEX = analyseur.ANALEX();
                            if (UNILEX.equals(T_UNILEX.ident)) {
                                nom_variable = Analyseur.CHAINE;
                                if(DEFINIR_VARIABLE(nom_variable,UNILEX)){
                                    UNILEX = analyseur.ANALEX();
                                }
                            } else {
                                fin = true;
                                erreur = true;
                            }
                        } else {

                            fin = true;
                        }

                    } while (!fin);

                    if (erreur) {
                        return false;
                    } else if (UNILEX.equals(T_UNILEX.ptvirg)) {
                        UNILEX = analyseur.ANALEX();
                        return true;
                    } else {
                        return false;
                    }
                }else return false;
            }else{
                return false;
            }

        } else{
        return false;
        }
    }


    boolean INSTRUCTION() throws IOException, InterruptedException {
        System.out.println("INSTRUCTION");

        return AFFECTATION() || LECTURE() || ECRITURE() || BLOC();
    }

    private boolean BLOC() throws IOException, InterruptedException {
        System.out.println("BLOC");
        boolean erreur;
        boolean fin;
        if(UNILEX.equals(T_UNILEX.motcle)&& Analyseur.CHAINE.equals("DEBUT")){
            UNILEX = analyseur.ANALEX();

            INSTRUCTION();

            erreur = false;
            fin = false;
            do{
                if(UNILEX.equals(T_UNILEX.ptvirg)){
                    UNILEX = analyseur.ANALEX();
                    System.out.println(UNILEX);
                    INSTRUCTION();

                }else{
                    fin = true;
                }
            }while(!fin);
            if(UNILEX.equals(T_UNILEX.motcle)&& Analyseur.CHAINE.equals("FIN")){
                UNILEX = analyseur.ANALEX();
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean ECRITURE() throws IOException, InterruptedException {
        System.out.println("ECRITURE");
        boolean erreur;
        boolean fin;
        if(UNILEX.equals(T_UNILEX.motcle)&& Analyseur.CHAINE.equals("ECRIRE")){
            UNILEX = analyseur.ANALEX();
            if(UNILEX.equals(T_UNILEX.parouv)){
                UNILEX = analyseur.ANALEX();
                erreur = false;
                if(ECR_EXP()){
                    UNILEX = analyseur.ANALEX();
                    fin = false;
                    do{
                        if(UNILEX.equals(T_UNILEX.virg)){
                            UNILEX = analyseur.ANALEX();
                            erreur = !ECR_EXP();
                            if(erreur){
                                fin = true;
                            }
                        }else{
                            fin = true;
                        }
                    }while(!fin);
                }
                if(erreur){
                    return false;
                }else if (UNILEX.equals(T_UNILEX.parfer)){
                    UNILEX = analyseur.ANALEX();
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean ECR_EXP() throws IOException, InterruptedException {
        System.out.println("ECR_EXP");
        if(UNILEX.equals(T_UNILEX.ch)){
            return true;
        }else {
            return EXP();
        }
    }

    private boolean LECTURE() throws IOException, InterruptedException {
        System.out.println("LECTURE");
        boolean erreur;
        boolean fin;
        String nom_ident;
        if(UNILEX.equals(T_UNILEX.motcle) && Analyseur.CHAINE.equals("LIRE")){
            UNILEX = analyseur.ANALEX();
            if(UNILEX.equals(T_UNILEX.parouv)){
                UNILEX = analyseur.ANALEX();
                if(UNILEX.equals(T_UNILEX.ident)){
                    nom_ident = Analyseur.CHAINE;
                    if(analyseur.CHERCHER(nom_ident)!=-1){
                        if(!Analyseur.TABLE_IDENT.get(analyseur.CHERCHER(nom_ident)).getTyp().equals("variable")){
                            return false;
                        }
                    }else return false;
                    UNILEX = analyseur.ANALEX();
                    fin = false;
                    erreur = false;
                    do{
                        if(UNILEX.equals(T_UNILEX.virg)){
                            UNILEX = analyseur.ANALEX();
                            if(UNILEX.equals(T_UNILEX.ident)){
                                nom_ident = Analyseur.CHAINE;
                                if(analyseur.CHERCHER(nom_ident)!=-1){
                                    if(!Analyseur.TABLE_IDENT.get(analyseur.CHERCHER(nom_ident)).getTyp().equals("variable")){
                                        return false;
                                    }
                                }else return false;
                                UNILEX = analyseur.ANALEX();
                            }else{
                                erreur = true;
                                fin = true;
                            }
                        }else {
                            fin = true;
                        }

                    }while(!fin);
                    if(erreur){
                        return false;
                    }else if (UNILEX.equals(T_UNILEX.parfer)){
                        UNILEX = analyseur.ANALEX();
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    boolean AFFECTATION() throws IOException, InterruptedException {
        System.out.println("AFFECTATION");
        String nom_ident;
        if (UNILEX.equals(T_UNILEX.ident)){
            nom_ident = Analyseur.CHAINE;
            if(analyseur.CHERCHER(nom_ident)!=-1){
                if(!Analyseur.TABLE_IDENT.get(analyseur.CHERCHER(nom_ident)).getTyp().equals("variable")){
                    return false;
                }
            }else return false;
            UNILEX = analyseur.ANALEX();
            if(UNILEX.equals(T_UNILEX.aff)){
                UNILEX = analyseur.ANALEX();
                return EXP();
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean EXP() throws IOException, InterruptedException {
        System.out.println("EXP");
        return TERME() && SUITE_TERME();
    }

    private boolean SUITE_TERME() throws IOException, InterruptedException {
        System.out.println("SUITE_TERME");
        if(UNILEX.equals(T_UNILEX.Erreur)){
            return true;
        }else{
            return OP_BIN() && EXP();
        }
    }

    private boolean OP_BIN() {
        System.out.println("OP_BIN");
        return switch (UNILEX) {
            case plus -> true;
            case moins -> true;
            case mult -> true;
            case div -> true;
            default -> false;
        };

    }

    private boolean TERME() throws IOException, InterruptedException {
        System.out.println("TERME");
        String nom_ident;
        if(UNILEX.equals(T_UNILEX.ent)){
            UNILEX = analyseur.ANALEX();
            return true;
        }
        if (UNILEX.equals(T_UNILEX.ident)){
            nom_ident = Analyseur.CHAINE;
            if(analyseur.CHERCHER(nom_ident)!=-1){
                if(Analyseur.TABLE_IDENT.get(analyseur.CHERCHER(nom_ident)).getTyp().equals("constante")){
                    if(!Analyseur.TABLE_IDENT.get(analyseur.CHERCHER(nom_ident)).getMapInformationSupp().get("typc").equals(0)){
                        return false;
                    }
                }
            }else return false;
            UNILEX = analyseur.ANALEX();
            return true;
        }
        if (UNILEX.equals(T_UNILEX.parouv)){
            UNILEX = analyseur.ANALEX();

            if(!EXP()){
                return false;
            }
            if(UNILEX.equals(T_UNILEX.parfer)){
                UNILEX = analyseur.ANALEX();
                return true;
            }
        }
        if (UNILEX.equals(T_UNILEX.moins)){
            UNILEX = analyseur.ANALEX();
            return true;
        }
        UNILEX = analyseur.ANALEX();
        return TERME();

    }

    void ANASYNT() throws IOException, InterruptedException {
        UNILEX = analyseur.ANALEX();

        if(PROG()){
            System.out.println("Le programme source est syntaxiquement correct");
        }else {
            System.out.println(analyseur.ERREUR(4));
        }
    }
    public static void main(String[] args) throws IOException,InterruptedException {
        Analyseur analyseur = new Analyseur("C:\\Users\\stekf\\Desktop\\TPL3\\TP1COMP\\src\\test3");
        AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique(analyseur);
        analyseur.INITIALISER();
        analyseur.LIRE_CAR();
        analyseurSyntaxique.ANASYNT();
        analyseur.TERMINER();
    }
}
