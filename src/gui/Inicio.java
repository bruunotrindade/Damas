package gui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Inicio extends JFrame implements ActionListener
{
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	
	JLabel fundo = new JLabel(new ImageIcon(getClass().getResource("fundo.png")));
	
	JButton btUmJogador = new JButton("Um jogador");
	JButton btDoisJogadores = new JButton("Dois jogadores");
	
	JMenuBar menuBar = new JMenuBar();
	JMenu mnOpcoes = new JMenu("Opções");
	JMenuItem mitmCadastro = new JMenuItem("Cadastro");
	JMenuItem mitmRanking = new JMenuItem("Ranking");
	
	public Inicio() 
	{
		setLayout(new BorderLayout());
		add(fundo);
		fundo.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 225));
		fundo.add(p1);

		p1.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));
		p1.add(btUmJogador);
		p1.add(btDoisJogadores);
		p1.setOpaque(false);
		
		btUmJogador.setFocusPainted(false);
		btUmJogador.addActionListener(this);
		btUmJogador.setBackground(Color.WHITE);
		
		btDoisJogadores.setFocusPainted(false);
		btDoisJogadores.addActionListener(this);
		btDoisJogadores.setBackground(Color.WHITE);
		
		setJMenuBar(menuBar);
		menuBar.add(mnOpcoes);
		
		mitmCadastro.addActionListener(this);
		mnOpcoes.add(mitmCadastro);
		
		mitmRanking.addActionListener(this);
		mnOpcoes.add(mitmRanking);

		setVisible(true);
		setSize(490, 399);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Damas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args)
	{
		Cadastro.jogador[0] = new Jogador("Branco");
		Cadastro.jogador[1] = new Jogador("Preto");
		new Inicio();
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btUmJogador)
		{
			dispose();
			new Tabuleiro();
		}
		else if(e.getSource() == btDoisJogadores)
		{
			dispose();
			new Tabuleiro(0);
		}
		else if(e.getSource() == mitmCadastro)
		{
			new Cadastro();
		}
		else if(e.getSource() == mitmRanking)
		{
			new Ranking();
		}
	}
}
