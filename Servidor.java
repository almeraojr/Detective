import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread{
    private static ArrayList<BufferedWriter>clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;  
    private static final ArrayList<String> suspeitos = new ArrayList<String>();
    private static final ArrayList<String> armas = new ArrayList<String>();
    private static final ArrayList<String> lugares = new ArrayList<String>();
    private static final String[] envelope = new String[3];
    private static boolean jogoRolando = true;

    //Método que inicia o servidor
    public Servidor(Socket con) 
    {
        this.con = con;
        try 
        {
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    //Método de tomada de decisões do jogo de detective
    //Ele tbm é resonsável pelo envio de mensagens aos detectives
    public void run() 
    {
        try 
        {
            String msg;
            Boolean acertou = false;
            OutputStream ou = this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            clientes.add(bfw);
            nome = msg = bfr.readLine();

            while (jogoRolando) {
                msg = bfr.readLine();
                String palpite[] = msg.split(",");
                if("palpite".equals(palpite[3])){
                    acertou = processarPalpite(msg);
                    if (acertou) {
                        String respServer = "Sim";
                        sendToAll(bfw, respServer);
                    } else {
                        String respServer = "Nao";
                        sendToAll(bfw, respServer);
                    }
                }else if("acusacao".equals(palpite[3])){
                    acertou = processarAcusacao(msg);
                    if (acertou) {
                        String respServer = "Jogador "+ nome + " ganhou!!!";
                        sendToAll(bfw, respServer);
                    } else {
                        String respServer = "Jogador "+ nome + " eliminado!!!";
                        sendToAll(bfw, respServer);
                    }
                }
                System.out.println(msg);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    //Método de envio de Mensagens para os detectives
    //usado no RUN
    public void sendToAll(BufferedWriter bwSaida, String msg)
    {
        try 
        {
            BufferedWriter bwS;
            for (BufferedWriter bw : clientes) {
                // var nome esta o nome do player que fez o palpite ou
                bw.write("Servidor fala: " + msg + "\r\n");
                bw.flush();
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    //Método principal
    public static void main(String[] args) 
    {
        try 
        {
            // Cria os objetos necessário para instânciar o servidor
            Scanner scan = new Scanner(System.in);
            Integer porta;
            System.out.println("Digite a porta do Servidor: ");
            porta = scan.nextInt();
            server = new ServerSocket(porta);
            clientes = new ArrayList<BufferedWriter>();
            System.out.println("Servidor ativo na porta: " + porta);
            System.out.println("Inicializando cartas... ");
            iniciarCartas();
            System.out.println("Randomizando crime... ");
            randomizarCrime();
            System.out.println("Pronto para conexões!!!\n \n");
            while (true) {
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new Servidor(con);
                t.start();
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    //Método que inicia os cards do game detective
    private static void iniciarCartas() 
    {
        // adicionando as cartas de suspeitos
        suspeitos.add("Senhorita Scarlett");
        suspeitos.add("Coronel Mostarda");
        suspeitos.add("Dona Branca");
        suspeitos.add("Senhora Pavão");
        suspeitos.add("Professor Plum");
        suspeitos.add("Reverendo Sr.Green");
        // adicionando as cartas de armas
        armas.add("Castiçal");
        armas.add("Cano");
        armas.add("Chave Inglesa");
        armas.add("Corda");
        armas.add("Revólver");
        armas.add("Faca");
        // adicionando as cartas de lugares
        lugares.add("Biblioteca");
        lugares.add("Cozinha");
        lugares.add("Hall");
        lugares.add("Escritório");
        lugares.add("Sala de Jantar");
        lugares.add("Sala de Música");
        lugares.add("Salão de Festas");
        lugares.add("Salão de Jogos");
    }

    //Método que sorteia a arma do crime, o lugar e o assassino
    protected static void randomizarCrime() 
    {
        iniciarCartas();
        Random rand = new Random();
        String suspeitoAleatorio = suspeitos.get(rand.nextInt(suspeitos.size()));
        String armaAleatorio = armas.get(rand.nextInt(suspeitos.size()));
        String lugarAleatorio = lugares.get(rand.nextInt(suspeitos.size()));
        envelope[0] = armaAleatorio;
        envelope[1] = suspeitoAleatorio;
        envelope[2] = lugarAleatorio;
        System.out.println(Arrays.toString(envelope));
    }

    //Método que processa um palpite feito por um dos detectives    
    protected static boolean processarPalpite(String msg) 
    {
        String palpite[] = msg.split(",");
        if (envelope[0].equals(palpite[0]) || envelope[1].equals(palpite[1]) || envelope[2].equals(palpite[2])) {
            return true;
        }
        return false;
    }

    //Método que processa uma acusação feita por um dos detectives
    protected static boolean processarAcusacao(String msg) 
    {
        String palpite[] = msg.split(",");
        if (envelope[0].equals(palpite[0]) && envelope[1].equals(palpite[1]) && envelope[2].equals(palpite[2])) {
            jogoRolando = false;
            return true;
        }
        else{
            return false;
        }
    }    

}


