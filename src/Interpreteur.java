

import java.awt.*;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Interpreteur {

    Stack<Object> PILEX = new Stack();

    static ArrayList<Integer> MEMVAR = new ArrayList();
    static int CO ;
    enum T_Interpreteur{
        ADDI,SOUS,MULT,DIVI,MOIN,AFFE,LIRE,ECRL,ECRE,ECRC,EMPI,CONT,STOP,FINC

    }



    void ADDI(){
        int x = (int) PILEX.pop();
        int y = (int) PILEX.pop();
        int z = y + x;
        PILEX.push(z);
        CO+=1;
    }
    void SOUS(){
        int x = (int) PILEX.pop();
        int y = (int) PILEX.pop();
        int z = y - x;
        PILEX.push(z);
        CO+=1;
    }
    void MULT(){
        int x = (int) PILEX.pop();
        int y = (int) PILEX.pop();
        int z = y * x;
        PILEX.push(z);
        CO+=1;
    }
    void DIVI(){
        int x = (int) PILEX.pop();
        int y = (int) PILEX.pop();
        if (x == 0){
            System.out.println("Erreur division par zero");
        }
        int z = y / x;
        PILEX.push(z);
        CO+=1;
    }
    void MOIN(){
        int x = (int) PILEX.pop();
        PILEX.push(-x);
        CO+=1;
    }
    void AFFE(){
        int x = (int) PILEX.pop();
        int y = (int) PILEX.pop();
        MEMVAR.set(y,x);
        CO+=1;
    }
    void LIRE(){
        Scanner sc = new Scanner(System.in);
        int x = (int) PILEX.pop();
        MEMVAR.set(x,sc.nextInt());
        CO+=1;
    }
    void ECRL(){
        System.out.println("Saut de ligne");
        CO+=1;
    }
    void ECRE(){
        int x = (int) PILEX.pop();
        CO+=1;
        System.out.println(x);
    }
    void ECRC(){
        int i = 1;


        while ((!AnalyseurSyntaxique.P_CODE.get(CO+i).equals(T_Interpreteur.FINC))){
            System.out.print(AnalyseurSyntaxique.P_CODE.get(CO +i));
            i+=1;

        }
        CO = CO + i + 1;
    }
    void EMPI(){
        PILEX.push(AnalyseurSyntaxique.P_CODE.get(CO +1));
        CO +=2;
    }
    void CONT(){
        int x = (int) PILEX.pop();
        PILEX.push(MEMVAR.get(x));
        CO += 1;
    }


     void  INTERPRETER() throws InterruptedException {
        CO = 0;
        while (!AnalyseurSyntaxique.P_CODE.get(CO).equals( T_Interpreteur.STOP)){
            Thread.sleep(10);

            if (T_Interpreteur.ADDI.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                ADDI();
            } else if (T_Interpreteur.SOUS.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                SOUS();
            } else if (T_Interpreteur.MULT.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                MULT();
            } else if (T_Interpreteur.DIVI.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                DIVI();
            } else if (T_Interpreteur.MOIN.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                MOIN();
            } else if (T_Interpreteur.AFFE.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                AFFE();
            } else if (T_Interpreteur.LIRE.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                LIRE();
            } else if (T_Interpreteur.ECRL.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                ECRL();
            } else if (T_Interpreteur.ECRE.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                ECRE();
            } else if (T_Interpreteur.ECRC.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                ECRC();
            } else if (T_Interpreteur.EMPI.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                EMPI();
            } else if (T_Interpreteur.CONT.equals(AnalyseurSyntaxique.P_CODE.get(CO))) {
                CONT();
            }


        }
    }
}
