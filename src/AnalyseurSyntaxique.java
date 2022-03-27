import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Stack;

public class AnalyseurSyntaxique {
    Analyseur analyseur;
    FileWriter fw;
    static T_UNILEX UNILEX ;
    static int NB_CONST_CHAINE;
    static String[] VAL_DE_CONST_CHAINE ;
    static int DERNIERE_ADRESSE_VAR_GLOB;
    static ArrayList<Object> P_CODE = new ArrayList();
    static int CO ;
    Stack<Object> PILOP = new Stack();

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
    void GENCORE_ECRITURE(){
        P_CODE.set(CO,Interpreteur.T_Interpreteur.ECRL);
        CO+=1;
    }
    private boolean ECRITURE() throws IOException, InterruptedException {
        System.out.println("ECRITURE");
        boolean erreur;
        boolean fin;
        if(UNILEX.equals(T_UNILEX.motcle)&& Analyseur.CHAINE.equals("ECRIRE")){
            UNILEX = analyseur.ANALEX();
            if(UNILEX.equals(T_UNILEX.parouv)){
                UNILEX = analyseur.ANALEX();
                if (UNILEX.equals(T_UNILEX.parfer)) {
                    GENCORE_ECRITURE();
                    UNILEX = analyseur.ANALEX();
                    return true;
                }
                erreur = false;
                if(ECR_EXP()){
                    System.out.println(Analyseur.CARLU);
                    UNILEX = analyseur.ANALEX();

                    fin = false;
                    do{
                        System.out.println("rentrer au moins une fois ");

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
                    System.out.println("erreur1");
                    return false;
                }else if (UNILEX.equals(T_UNILEX.parfer)){
                    UNILEX = analyseur.ANALEX();
                    return true;
                }else{
                    System.out.println("erreur2");
                    return false;
                }
            }else{
                System.out.println("erreur3");
                return false;
            }
        }else{
            System.out.println("erreur3");
            return false;
        }
    }
    void GENCODE_ECR_EXP_EXP(){
        P_CODE.set(CO,Interpreteur.T_Interpreteur.ECRE);
        CO+=1;
    }
    void GENCODE_ECR_EXP_CHAINE(String ch){
        P_CODE.set(CO,Interpreteur.T_Interpreteur.ECRC);
        CO++;
        for (int i = 0; i <ch.length() ; i++) {
            P_CODE.set(CO+i,ch.charAt(i));
        }
        P_CODE.set(CO +ch.length(),Interpreteur.T_Interpreteur.FINC);
        CO = CO + ch.length()+1;
    }
    private boolean ECR_EXP() throws IOException, InterruptedException {
        System.out.println("ECR_EXP");
        if(UNILEX.equals(T_UNILEX.ch)){
            GENCODE_ECR_EXP_CHAINE(Analyseur.CHAINE);

            return true;
        }else {
            if(EXP()){
                GENCODE_ECR_EXP_EXP();
                return true;
            }else {
                return false;
            }
        }
    }
    void GENCODE_LECTURE(String ch){
        P_CODE.set(CO,Interpreteur.T_Interpreteur.EMPI);
        P_CODE.set(CO +1, Analyseur.TABLE_IDENT.get(analyseur.CHERCHER(ch)).getMapInformationSupp().get("adresse"));
        P_CODE.set(CO +2 ,Interpreteur.T_Interpreteur.LIRE );
        CO +=3;
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
                    GENCODE_LECTURE(nom_ident);
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
                                GENCODE_LECTURE(nom_ident);
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
    void GENCODE_AFFECTATION(String ch){
        P_CODE.set(CO,Interpreteur.T_Interpreteur.EMPI);
        P_CODE.set(CO +1, Analyseur.TABLE_IDENT.get(analyseur.CHERCHER(ch)).getMapInformationSupp().get("adresse"));
        CO +=2;
    }
    void GENCORE_FIN_AFFECTATION(){
        P_CODE.set(CO,Interpreteur.T_Interpreteur.AFFE);
        CO+=1;
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
            GENCODE_AFFECTATION(nom_ident);
            UNILEX = analyseur.ANALEX();
            if(UNILEX.equals(T_UNILEX.aff)){
                UNILEX = analyseur.ANALEX();
                if(EXP()){
                    GENCORE_FIN_AFFECTATION();
                    return true;
                }else return false;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    void GENCODE_EXP(){
        P_CODE.set(CO,PILOP.pop());
        CO+=1;
    }
    private boolean EXP() throws IOException, InterruptedException {
        System.out.println("EXP");
        return TERME() && SUITE_TERME();
    }

    private boolean SUITE_TERME() throws IOException, InterruptedException {
        System.out.println("SUITE_TERME");
        if(OP_BIN() ){
            if(EXP()){
                GENCODE_EXP();
                return true;
            }else return false;
        }else return true;

    }
    void GENCODE_OP_BIN(String ch){
        switch(ch){
            case "+" -> PILOP.push(Interpreteur.T_Interpreteur.ADDI);
            case "-" -> PILOP.push(Interpreteur.T_Interpreteur.MOIN);
            case "*" -> PILOP.push(Interpreteur.T_Interpreteur.MULT);
            case "/" -> PILOP.push(Interpreteur.T_Interpreteur.DIVI);
        }
    }
    private boolean OP_BIN() {
        System.out.println("OP_BIN");
        if (UNILEX.equals(T_UNILEX.plus)){
            GENCODE_OP_BIN("+");
            return true;
        }
        else if (UNILEX.equals(T_UNILEX.moins)){
            GENCODE_OP_BIN("-");
            return true;
        }
        else if (UNILEX.equals(T_UNILEX.mult)){
            GENCODE_OP_BIN("*");
            return true;
        }
        else if (UNILEX.equals(T_UNILEX.div)){
            GENCODE_OP_BIN("/");
            return true;
        }
        else return false;


    }

    void GENCODE_TERME_ENTIER(int nombre){
        P_CODE.set(CO,Interpreteur.T_Interpreteur.EMPI);
        P_CODE.set(CO +1 , nombre);
        CO+=2;
    }
    void GENCORE_TERME_IDENTIFICATEUR(String ch){
        P_CODE.set(CO,Interpreteur.T_Interpreteur.EMPI);
        P_CODE.set(CO +1 , Analyseur.TABLE_IDENT.get(analyseur.CHERCHER(ch)).getMapInformationSupp().get("adresse"));
        P_CODE.set(CO +2, Interpreteur.T_Interpreteur.CONT);
        CO +=3;
    }
    void GENCODE_TERME_MOIN(){
        P_CODE.set(CO,Interpreteur.T_Interpreteur.MOIN);
        CO+=1;
    }
    private boolean TERME() throws IOException, InterruptedException {
        System.out.println("TERME");
        String nom_ident;
        if(UNILEX.equals(T_UNILEX.ent)){
            GENCODE_TERME_ENTIER(Analyseur.NOMBRE);
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
            GENCORE_TERME_IDENTIFICATEUR(nom_ident);
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
            if(TERME()){
                GENCODE_TERME_MOIN();
                return true;
            }else return false;

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

    void CREER_FICHIER_CODE() throws IOException {
        String path = analyseur.pathname+".COD";
        this.fw = new FileWriter(path, false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);

        out.println(Analyseur.TABLE_IDENT.size()+" mot(s) réservé(s) pour les variables globales");
            for (int i = 0;i < P_CODE.size();i++) {
                if (!P_CODE.get(i).equals(Interpreteur.T_Interpreteur.STOP)) {
                    if(P_CODE.get(i).equals(Interpreteur.T_Interpreteur.ECRC)){
                        out.print(P_CODE.get(i).toString()+" ");
                        i++;
                        while(!P_CODE.get(i).equals(Interpreteur.T_Interpreteur.FINC)){

                            out.print( P_CODE.get(i));
                            i++;
                        }out.println(" "+P_CODE.get(i).toString());
                    }
                    else if(P_CODE.get(i).equals(Interpreteur.T_Interpreteur.EMPI)){
                        out.print(P_CODE.get(i).toString()+" ");
                        i++;
                        out.println(P_CODE.get(i));
                    }
                    else{
                        out.println(P_CODE.get(i).toString()+" ");
                    }


                } else {
                    out.println(P_CODE.get(i).toString());
                    out.close();
                    break;
                }


            }


    }
    public static void main(String[] args) throws IOException,InterruptedException {
        Analyseur analyseur = new Analyseur("C:\\Users\\stekf\\Desktop\\TPL3\\TP1COMP\\src\\test4");
        AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique(analyseur);
        Interpreteur interpreteur = new Interpreteur();
        analyseur.INITIALISER();
        analyseur.LIRE_CAR();
        analyseurSyntaxique.ANASYNT();
        analyseur.TERMINER();
        analyseurSyntaxique.CREER_FICHIER_CODE();
        interpreteur.INTERPRETER();
    }
}
