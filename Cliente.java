import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;


import java.awt.*;

public class Cliente extends JFrame implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnSair;
    private JLabel lblHistorico;
    private JLabel lblCartada;
    private JLabel lblAcao;
    private JLabel lblEscolha;
    private JLabel lblEliminado;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream ou;
    private Writer ouw;
    private BufferedWriter bfw;
    private JTextField txtIP;
    private JTextField txtPorta;
    private JTextField txtNome;
    private JComboBox<String> Armas;
    private JComboBox<String> Lugares;
    private JComboBox<String> Suspeitos;
    private JRadioButton Palpite;
    private JRadioButton Acusacao;
    private ButtonGroup g;
    private GridBagConstraints gbc;

    public Cliente() throws IOException{
        JLabel lblMessage = new JLabel("Dados para login no server");
        txtIP = new JTextField("127.0.0.1");
        
        txtPorta = new JTextField("Portaa");
        txtNome = new JTextField("Digite seu nome");
        Object[] texts = {lblMessage, txtIP, txtPorta, txtNome };
        JOptionPane.showMessageDialog(null, texts);
       
         pnlContent = new JPanel();
         pnlContent.setLayout(new GridBagLayout());
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridwidth = GridBagConstraints.REMAINDER;
         gbc.insets = new Insets(10, 0, 0, 0);
         texto = new JTextArea(10,30);
         texto.setEditable(false);
         texto.setBackground(new Color(240,240,240));
         txtMsg = new JTextField(20);
         lblHistorico = new JLabel("Jogados:");
         btnSend = new JButton("Enviar");
         btnSend.setToolTipText("Enviar");
         btnSair = new JButton("Sair");
         btnSair.setToolTipText("Sair");
         btnSend.addActionListener(this);
         btnSair.addActionListener(this);
         btnSend.addKeyListener(this);
         txtMsg.addKeyListener(this);
         //Radio Button para selecionar Palpite ou Acusacao
         g = new ButtonGroup();
         lblCartada = new JLabel("Jogada: ");
         lblAcao = new JLabel("Acao: ");
         lblEscolha = new JLabel("Escolha: ");
         Palpite = new JRadioButton("Palpite");
         Acusacao = new JRadioButton("Acusacao");
         Palpite.addKeyListener(this);
         Palpite.setSelected(true);
         Acusacao.addKeyListener(this);
         g.add(Palpite);
         g.add(Acusacao);
         gbc.anchor = GridBagConstraints.FIRST_LINE_START;        
         gbc.gridx = 0;
         gbc.gridy = 0;
         pnlContent.add(lblCartada, gbc);
         gbc.gridx = 1;
         gbc.gridy = 0;
         pnlContent.add(Palpite, gbc);
         gbc.gridx = 2;
         gbc.gridy = 0;
         pnlContent.add(Acusacao, gbc);

         JScrollPane scroll = new JScrollPane(texto);
         texto.setLineWrap(true);
         gbc.anchor = GridBagConstraints.LINE_START;    
         gbc.gridx = 0;
         gbc.gridy = 2;
         pnlContent.add(lblHistorico,gbc);
         gbc.anchor = GridBagConstraints.CENTER;  
         gbc.gridx = 1;
         gbc.gridy = 2;
         pnlContent.add(scroll, gbc);
         pnlContent.add(txtMsg, gbc);         
         gbc.gridx = 0;
         gbc.gridy = 6;
         pnlContent.add(lblAcao, gbc);
         gbc.gridx = 1;
         gbc.gridy = 6;
         pnlContent.add(btnSair, gbc);
         gbc.gridx = 2;
         gbc.gridy = 6;
         pnlContent.add(btnSend, gbc);
         gbc.gridx = 0;
         gbc.gridy = 8;
         pnlContent.add(lblEscolha, gbc);
         //Inicializando ComboBox Armas
         Armas = new JComboBox<>();
         Armas.addItem("Castiçal");
         Armas.addItem("Cano");
         Armas.addItem("Chave Inglesa");
         Armas.addItem("Corda");
         Armas.addItem("Revólver");
         Armas.addItem("Faca");
         pnlContent.add(Armas);
         Armas.addActionListener(this);
         //Inicializando ComboBox Suspeitos
         Suspeitos = new JComboBox<>();
         Suspeitos.addItem("Senhorita Scarlett");
         Suspeitos.addItem("Coronel Mostarda");
         Suspeitos.addItem("Dona Branca");
         Suspeitos.addItem("Senhora Pavão");
         Suspeitos.addItem("Professor Plum");
         Suspeitos.addItem("Reverendo Sr.Green");
         pnlContent.add(Suspeitos);
         Suspeitos.addActionListener(this);
         //Inicializando ComboBox Lugares
         Lugares = new JComboBox<>();
         Lugares.addItem("Biblioteca");
         Lugares.addItem("Cozinha");
         Lugares.addItem("Hall");
         Lugares.addItem("Escritório");
         Lugares.addItem("Sala de Jantar");
         Lugares.addItem("Sala de Música");
         Lugares.addItem("Salão de Festas");
         Lugares.addItem("Salão de Jogos");
         pnlContent.add(Lugares);
         Lugares.addActionListener(this);
         pnlContent.setBackground(Color.LIGHT_GRAY);
         texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
         txtMsg.setVisible(false);
         setTitle(txtNome.getText());
         setContentPane(pnlContent);
         setLocationRelativeTo(null);
         setResizable(false);
         setSize(800,800);
         setVisible(true);
         setDefaultCloseOperation(EXIT_ON_CLOSE);         
    }

    //Método de conexão com o socket
    //faz a conexão cliente-servidor
    public void conectar()
    {
        try 
        {
            socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
            ou = socket.getOutputStream();
            ouw = new OutputStreamWriter(ou);
            bfw = new BufferedWriter(ouw);
            bfw.write(txtNome.getText()+"\r\n");
            bfw.flush();
        } 
        catch (IOException e) 
        {
            System.out.println(e.getMessage());
        }
    }

    //Método de comunicação com o servidor
    //envia palpite/acusação do detective
     public void enviarMensagem(String msg)
     {
        try 
        {
            if(msg.equals("Sair")){
                bfw.write("Desistiu \r\n");
                texto.append("Desistiu \r\n");
            }else{
                String jogada = Palpite.isSelected() ? "palpite" : "acusacao";
                msg = Armas.getSelectedItem()+","+Suspeitos.getSelectedItem()+","+Lugares.getSelectedItem()+","+jogada+"";
                bfw.write(msg+"\r\n");
            }
            bfw.flush();
            txtMsg.setText("");
        }
        catch (IOException e) 
        {
            System.out.println(e.getMessage());
        }
    }  


    //Método escutar, responsavel por receber e mostrar as mensagens recebidas do servidor.
    //por ele, o detective recebe decisões do jogo tomadas pelo servidor
    public void escutar()
    {
        try 
        {
            InputStream in = socket.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
            BufferedReader bfr = new BufferedReader(inr);
            String msg = "";
    
            while(!"Sair".equalsIgnoreCase(msg))    
                if(bfr.ready()){    
                    msg = bfr.readLine();
                    if(msg.equals("Sair"))
                        texto.append("Servidor caiu! \r\n");
                    else
                        texto.append(msg+"\r\n");
                }

        } 
        catch (IOException e) 
        {
            System.out.println(e.getMessage());
        }
    }

    //Método para fechar a conn com o servidor
    public void sair()
    {
        try
        {
            enviarMensagem("Sair");
            bfw.close();
            ouw.close();
            ou.close();
        }
        catch (IOException e) 
        {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String []args)
    {
        try 
        {
            Cliente detective = new Cliente();
            detective.conectar();
            detective.escutar();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getActionCommand().equals(btnSend.getActionCommand()))
            enviarMensagem(txtMsg.getText());
        else if (e.getActionCommand().equals(btnSair.getActionCommand()))
            sair();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }
}
