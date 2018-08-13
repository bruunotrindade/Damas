package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.sun.prism.paint.Color;

public class Ranking extends JDialog implements ActionListener
{
	static String nomes[] = {"", "", "", "", ""};
	static int tempo[] = {0, 0, 0, 0, 0};
	
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	
	JButton btFechar = new JButton("Fechar");
	JLabel lbNomeJogador1 = new JLabel("Nome do jogador um:");
	JLabel lbNomeJogador2 = new JLabel("Nome do jogador dois:");
		
	JLabel lbRanking[][] = new JLabel[5][2];
	
	public Ranking()
	{
		p1.setLayout(new GridLayout(5, 2, 15, 5));
		p1.setBorder(new TitledBorder("Ranking"));
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 2; j++)
			{
				lbRanking[i][j] = new JLabel();
				lbRanking[i][j].setFont(new Font("Arial", Font.BOLD, 18));
				p1.add(lbRanking[i][j]);
			}
			if(nomes[i].length() == 0)
			{
				lbRanking[i][0].setText("Nenhum"); 
				lbRanking[i][1].setText("--:--");
			}
			else
			{
				lbRanking[i][0].setText(nomes[i]); 
				lbRanking[i][1].setText(String.format("%d:%d", tempo[i]/60, tempo[i]%60));
			}
		}
		p2.setLayout(new BorderLayout());
		p2.add(p1, BorderLayout.CENTER);
		p2.add(btFechar, BorderLayout.SOUTH);
		
		add(p2);
		btFechar.addActionListener(this);
		
		setUndecorated(true);
		setModal(true);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setTitle("Cadastro");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}


	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btFechar)
		{
			dispose();
		}
	}
}
