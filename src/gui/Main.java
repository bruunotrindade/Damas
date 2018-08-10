package gui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Main extends JFrame implements ActionListener
{
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	
	JLabel fundo = new JLabel(new ImageIcon(getClass().getResource("fundo.png")));
	
	JButton btUmJogador = new JButton("Um jogador");
	JButton btDoisJogadores = new JButton("Dois jogadores");
	
	public Main() 
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
		//play();

		setVisible(true);
		setSize(490, 399);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Damas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void play() {
		/*URL url = this.getClass().getResource("tema.wav");
		AudioClip audio = Applet.newAudioClip(url);
		audio.play();*/
	}
	
	public static void main(String[] args)
	{
		new Main();
		//new Tabuleiro();
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
	}
}
