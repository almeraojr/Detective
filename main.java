import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.Date;
import java.util.ArrayList; 
import java.util.List; 
import java.util.Random;
import java.util.Scanner;

public class main {
private static final String[] palpite = new String[3];
private static Scanner entrada = new Scanner(System.in);

protected static void receberRespostas(){
    System.out.println("Suspeito: ");
    palpite[0] = entrada.nextLine();
    entrada.nextLine();
    System.out.println("Arma: ");
    palpite[1] = entrada.nextLine();
    entrada.nextLine();
    System.out.println("Lugar: ");
    palpite[2] = entrada.nextLine();
}
    
    public static void main(String[] args) {
        boolean jogo = true;
        String respostaPalpite = new String();
        String respostaAcusacao = new String();
        servidorDetetive servidor = new servidorDetetive();
        Scanner entrada = new Scanner(System.in);
        servidor.randomizarCrime();
        char acaoJogo;
        while(jogo){
            System.out.println("P para Palpite e A para Acusação");
            acaoJogo = entrada.next().charAt(0);
            entrada.nextLine();
            if(acaoJogo == 'P' || acaoJogo == 'p'){
                receberRespostas();
                respostaPalpite = servidor.processarPalpite(palpite);
                System.out.println(respostaPalpite);
                respostaPalpite = new String();
            }
            if(acaoJogo == 'A' || acaoJogo == 'a'){
                receberRespostas();
                respostaAcusacao = servidor.processarAcusacao(palpite);
                jogo = (respostaAcusacao.contains("venceu")) ? false : true;               
                System.out.println(respostaAcusacao);
                respostaAcusacao = new String();
            }
        }
    }   
}
