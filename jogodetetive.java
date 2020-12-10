import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.Date;
import java.util.ArrayList; 
import java.util.Arrays;
import java.util.List; 
import java.util.Random;
import java.util.Scanner;

public class servidorDetetive {
private static final ArrayList<String> suspeitos = new ArrayList<String>();
private static final ArrayList<String> armas = new ArrayList<String>();
private static final ArrayList<String> lugares = new ArrayList<String>();
private static final String[] envelope = new String[3];
private static final String[] palpite = new String[3];
private static String respostaServidor;
private static boolean jogoRolando = true;
private static Scanner entrada = new Scanner(System.in);
    
private static void iniciarCartas(){
    //adicionando as cartas de suspeitos
    suspeitos.add("Senhorita Scarlett");
    suspeitos.add("Coronel Mostarda");
    suspeitos.add("Dona Branca");
    suspeitos.add("Senhora Pavão");
    suspeitos.add("Professor Plum");
    suspeitos.add("Reverendo Sr.Green");
    //adicionando as cartas de armas
    armas.add("Castiçal");
    armas.add("Cano");
    armas.add("Chave Inglesa");
    armas.add("Corda");
    armas.add("Revólver");
    armas.add("Faca");
    //adicionando as cartas de lugares
    lugares.add("Biblioteca");
    lugares.add("Cozinha");
    lugares.add("Hall");
    lugares.add("Escritório");
    lugares.add("Sala de Jantar");
    lugares.add("Sala de Música");
    lugares.add("Salão de Festas");
    lugares.add("Salão de Jogos");
}

protected static void randomizarCrime(){
    iniciarCartas();
    Random rand = new Random();
    String suspeitoAleatorio = suspeitos.get(rand.nextInt(suspeitos.size()));
    String armaAleatorio = armas.get(rand.nextInt(suspeitos.size()));
    String lugarAleatorio = lugares.get(rand.nextInt(suspeitos.size()));
    envelope[0] = suspeitoAleatorio;
    envelope[1] = armaAleatorio;
    envelope[2] = lugarAleatorio;
    System.out.println(Arrays.toString(envelope));
}

protected static String processarPalpite(String[] palpite){
    if(envelope[0].equals(palpite[0]) || envelope[1].equals(palpite[1]) || envelope[2].equals(palpite[2])){
        respostaServidor = "SIM";
        return respostaServidor;
    }
    respostaServidor = "NÃO";
    return respostaServidor;
}

protected static String processarAcusacao(String[] palpite){
    if(envelope[0].equals(palpite[0]) && envelope[1].equals(palpite[1]) && envelope[2].equals(palpite[2])){
        jogoRolando = false;
        return "Jogador X venceu";
    }
    else{
        return "Jogador X eliminado";
    }
}
}
