package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Casa extends JButton
{
	private int casaI = 0;
	private int casaJ = 0;
	private String cor;
	private Peca pecaAtual;
	private boolean casaVazia = true;
	private boolean casaMarcada = false;
	
	public Casa(int i, int j)
	{
		casaI = i;
		casaJ = j;
		if(i % 2 == 0)
			if(j % 2 == 0)
				cor = "#AD6A42";
			else
				cor = "#F4ECBE";
		else
			if(j % 2 == 0)
				cor = "#F4ECBE";
			else
				cor = "#AD6A42";
		
		setBackground(Color.decode(cor));
		setBorder(new LineBorder(Color.black, 1));
		setFocusPainted(false);
	}
	
	public void atualizarPeca(Peca peca)
	{
		pecaAtual = peca;
		setIcon(peca.getPeca());
	}
	
	public void removerPeca()
	{
		casaVazia = true;
		pecaAtual = null;
		setIcon(null);
	}
	
	public void setVazia(boolean res)
	{
		casaVazia = res;
	}
	
	public int getPosI()
	{
		return casaI;
	}
	
	public int getPosJ()
	{
		return casaJ;
	}
	
	public boolean vazia()
	{
		return casaVazia;
	}
	
	public void setMarcada(boolean res)
	{
		casaMarcada = res;
		
		if(casaMarcada)
			setBackground(Color.decode("#74A13C"));
		else if(!vazia() && pecaAtual.jogavel())
			setBackground(Color.decode("#e4912c"));
		else
			setBackground(Color.decode(cor));
	}
	
	public boolean marcada()
	{
		return casaMarcada;
	}
	
	public Peca pecaAtual()
	{
		return pecaAtual;
	}
}
