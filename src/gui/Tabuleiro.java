package gui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;

public class Tabuleiro extends JFrame implements ActionListener
{
	JPanel p1 = new JPanel();
	Casa casa[][] = new Casa[8][8];
	Peca peca[] = new Peca[24];
	
	Casa pecaComer;
	
	int jogadorUm = 1;
	int jogadorDaRodada = 0;
	int pecaInd = 0;
	int clickI;
	int clickJ;
	boolean doisJogadores = false;
	
	//Auxiliares para salvar dados em comida
	Fila comerI = new Fila(10);
	Fila comerJ = new Fila(10);
	
	
	public Tabuleiro()
	{
		
		//Organizando o layout do tabuleiro
		p1.setLayout(new GridLayout(8,8));
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				casa[i][j] = new Casa(i, j);
				p1.add(casa[i][j]);
				
				//Eventos de clique
				casa[i][j].addActionListener(this);
			}
		
		add(p1);
		
		iniciarJogo();
		
		setVisible(true);
		setSize(600, 600);
		setResizable(false);
		setTitle("Damas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public void posicionarPecas()
	{
		int pecasInd = 0;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				if((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0))
					if(i < 3 || i > 4)
					{
						int tipo = 0;
						if(i > 4)
							tipo = 1;
						
						peca[pecasInd++] = new Peca(tipo, i, j);
						casa[i][j].atualizarPeca(peca[pecasInd-1]);
						casa[i][j].setVazia(false);
					}
			}
	}
	
	public void marcarPecas()
	{
		int inicio;
		if(jogadorDaRodada == 0)
			inicio = 0;
		else
			inicio = 12;
		
		//Verificando se tem pecas para comer
		boolean Comer = false;
		for(int i = inicio; i < inicio+12; i++)
		{
			if(peca[i].emJogo())
			{
				int I = peca[i].getPosI(), J = peca[i].getPosJ();
				
				//Comendo para frente
				if((I >= 2 && ((J >= 2 && !casa[I-1][J-1].vazia() && casa[I-1][J-1].pecaAtual().getTipo() != peca[i].getTipo() && casa[I-2][J-2].vazia()) || (J <= 5 && !casa[I-1][J+1].vazia() && casa[I-1][J+1].pecaAtual().getTipo() != peca[i].getTipo() && casa[I-2][J+2].vazia()))) ||
				//Comendo para tras
				I <= 5 && ((J >= 2 && !casa[I+1][J-1].vazia() && casa[I+1][J-1].pecaAtual().getTipo() != peca[i].getTipo() && casa[I+2][J-2].vazia()) || (J <= 5 && !casa[I+1][J+1].vazia() && casa[I+1][J+1].pecaAtual().getTipo() != peca[i].getTipo() && casa[I+2][J+2].vazia())))
				{
					marcarPeca(casa[I][J]);
					casa[I][J].pecaAtual().setComer(true);
					Comer = true;
				}
				else
					casa[I][J].pecaAtual().setComer(false);	
			}
		}
		
		if(!Comer)
		{
			for(int i = inicio; i < inicio+12; i++)
			{
				if(peca[i].emJogo())
				{
					int I = peca[i].getPosI(), J = peca[i].getPosJ();
					boolean Valida = false;
					//Verificando se nao esta nas laterais
					if(!peca[i].dama())
					{
						if(jogadorDaRodada == 1 && I != 0 && ((J != 7 && casa[I-1][J+1].vazia()) || (J != 0 && casa[I-1][J-1].vazia())))
							Valida = true;//Pecas brancas
						else if(jogadorDaRodada == 0 && I != 7 && ((J != 7 && casa[I+1][J+1].vazia()) || (J != 0 && casa[I+1][J-1].vazia())))
							Valida = true;//Pecas pretas
					}
					else
					{
						if(I != 0 && ((J != 0 && casa[I-1][J-1].vazia()) || (J != 7 && casa[I-1][J+1].vazia())))
							Valida = true;
						else if(I != 7 && ((J != 0 && casa[I+1][J-1].vazia()) || (J != 7 && casa[I+1][J+1].vazia())))
							Valida = true;
					}
					
					
					if(Valida)
						marcarPeca(casa[I][J]);
				}
			}
		}
	}

	public void moverPeca(Casa origem, Casa destino)
	{
		destino.atualizarPeca(origem.pecaAtual());
		destino.pecaAtual().setPosI(destino.getPosI());
		destino.pecaAtual().setPosJ(destino.getPosJ());
		destino.setVazia(false);
		if(!destino.pecaAtual().dama() && ((destino.getPosI() == 0 && destino.pecaAtual().getTipo() == 1) || (destino.getPosI() == 7 && destino.pecaAtual().getTipo() == 0)))
		{
			destino.pecaAtual().tornarDama();
			destino.atualizarPeca(destino.pecaAtual());
		}
		
		origem.removerPeca();
		origem.setVazia(true);
	}
	
	public void marcarPeca(Casa casa)
	{
		casa.setBackground(Color.decode("#e4912c"));
		casa.pecaAtual().setJogavel(true);
	}
	
	public void desmarcarCasas(int modo)
	{
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				if(modo == 1 && !casa[i][j].vazia() && casa[i][j].pecaAtual().jogavel())
					casa[i][j].pecaAtual().setJogavel(false);
				
				casa[i][j].setMarcada(false);
			}
	}
	
	public void actionPerformed(ActionEvent evento)
	{	
		//Marcando as possiveis jogadas
		boolean Origem = true;
		for(int i = 0; i < 8 && Origem; i++)
			for(int j = 0; j < 8 && Origem; j++)
			{
				if(casa[i][j] == evento.getSource())
				{
					//Jogadas de movimento
					if(!casa[i][j].vazia() && !casa[i][j].marcada())
					{
						desmarcarCasas(0);
						if(casa[i][j].pecaAtual().jogavel())
						{	
							clickI = i;
							clickJ = j;
							
							marcarMovimento(casa[i][j]);
						}
					}
					//Clique na casa futura para comer
					else if(casa[i][j] != casa[clickI][clickJ] && casa[i][j].marcada() && casa[clickI][clickJ].pecaAtual().comer() && casa[clickI][clickJ].marcada())
					{
						desmarcarCasas(1);
						comerPeca(casa[clickI][clickJ]);		
						proximaRodada();
					}
					//Clique na casa futura do movimento
					else if(casa[i][j] != casa[clickI][clickJ] && casa[i][j].marcada() && casa[clickI][clickJ].marcada())
					{
						desmarcarCasas(1);
						moverPeca(casa[clickI][clickJ], casa[i][j]);
						proximaRodada();
					}
					
					Origem = false;
				}
			}
	}
	
	public void proximaRodada()
	{
		if(jogadorDaRodada == 1)
			jogadorDaRodada = 0;
		else
			jogadorDaRodada = 1;
		
		if(!doisJogadores && jogadorDaRodada != jogadorUm)
			jogadaPC();
		else if(jogadorDaRodada == jogadorUm)
		{
			marcarPecas();
		}
	}
	
	public void jogadaPC()
	{
		for(int i = 0; i < 12; i++)
		{
			if(peca[i].emJogo())
			{
				if(!peca[i].dama())
				{
					int I = peca[i].getPosI(), J = peca[i].getPosJ();
					if(I != 7 && J != 7 && casa[I+1][J+1].vazia())
					{
						moverPeca(casa[I][J], casa[I+1][J+1]);
						break;
					}
					else if(I != 7 && J != 0 && casa[I+1][J-1].vazia())
					{
						moverPeca(casa[I][J], casa[I+1][J-1]);
						break;
					}
				}
				else//Dama
				{
					
				}
			}
		}
		proximaRodada();
	}
	
	public void comerPeca(Casa comedor)
	{
		Casa comido = null;
		int i;
		for(i = 0; i < comerI.size()-1; i++)
		{
			if(!casa[comerI.obter(i)][comerJ.obter(i)].vazia())
			{
				casa[comerI.obter(i)][comerJ.obter(i)].pecaAtual().setEmJogo(false);
				casa[comerI.obter(i)][comerJ.obter(i)].pecaAtual().setJogavel(false);
				casa[comerI.obter(i)][comerJ.obter(i)].removerPeca();
			}
		}
		comedor.pecaAtual().setComer(false);
		moverPeca(comedor, casa[comerI.obter(i-1)][comerJ.obter(i-1)]);
		comerI.limpar();
		comerJ.limpar();
	}
	
	public void iniciarJogo()
	{
		posicionarPecas();
		proximaRodada();
	}
	
	public void verificarJogada(int i, int j)
	{
		boolean frente1 = false, frente2 = false, tras1 = false, tras2 = false;
		int I = i, J = j;
		
		//Comendo para frente
		while(i >= 2 && j >= 2 && !casa[i-1][j-1].vazia() && casa[i-1][j-1].pecaAtual().getTipo() != jogadorDaRodada && casa[i-2][j-2].vazia())
		{
			frente1 = true;
			casa[i-1][j-1].setBackground(Color.decode("#4d70ad"));
			casa[i-2][j-2].setBackground(Color.decode("#4d70ad"));
			comerI.adicionar(i-1);
			comerJ.adicionar(j-1);
			i -= 2;
			j -= 2;
		}

		while(i >= 2 && j <= 5 && !casa[i-1][j+1].vazia() && casa[i-1][j+1].pecaAtual().getTipo() != jogadorDaRodada && casa[i-2][j+2].vazia())
		{
			frente1 = false;
			frente2 = true;
			casa[i-1][j+1].setBackground(Color.decode("#4d70ad"));
			casa[i-2][j+2].setBackground(Color.decode("#4d70ad"));
			comerI.adicionar(i-1);
			comerJ.adicionar(j+1);
			i -= 2;
			j += 2;
		}
	
		//Comendo para tras
		while(i <= 5 && j >= 2 && !casa[i+1][j-1].vazia() && casa[i+1][j-1].pecaAtual().getTipo() != jogadorDaRodada && casa[i+2][j-2].vazia())
		{
			frente1 = false;
			frente2 = false;
			tras1 = true;
			casa[i+1][j-1].setBackground(Color.decode("#4d70ad"));
			casa[i+2][j-2].setBackground(Color.decode("#4d70ad"));
			comerI.adicionar(i+1);
			comerJ.adicionar(j-1);
			i += 2;
			j -= 2;
		}

		while(i <= 5 && j <= 5 && !casa[i+1][j+1].vazia() && casa[i+1][j+1].pecaAtual().getTipo() != jogadorDaRodada && casa[i+2][j+2].vazia())
		{
			frente1 = false;
			frente2 = false;
			tras1 = false;
			tras2 = true;
			casa[i+1][j+1].setBackground(Color.decode("#4d70ad"));
			casa[i+2][j+2].setBackground(Color.decode("#4d70ad"));
			comerI.adicionar(i+1);
			comerJ.adicionar(j+1);
			i += 2;
			j += 2;
		}
		comerI.adicionar(i);
		comerJ.adicionar(j);
		casa[i][j].setMarcada(true);
	}
	
	
	public void marcarCasas(Casa casaComer, Casa casaFutura)
	{
		casaComer.setBackground(Color.decode("#4d70ad"));//Marcando a peca a ser comida
		casaFutura.setMarcada(true);//Deixando laranja as casas
	}
	
	public void marcarMovimento(Casa casaX)
	{
		int i = casaX.getPosI(), j = casaX.getPosJ();
		casaX.setMarcada(true);//Tornando verde a casa clicada
		if(casaX.pecaAtual().comer())
			verificarJogada(i, j);//Marcando as pecas envolvidas no movimento de comer
		else
		{
			//Tornando verde as casas de movimentos possiveis
			if(!casaX.pecaAtual().dama())
			{
				if(i != 0 && j != 0 && casaX.vazia())
					casaX.setMarcada(true);
				if(i != 0 && j != 7 && casaX.vazia())
					casaX.setMarcada(true);
			}
			else
			{
				Peca pecaMov = casaX.pecaAtual();
				//Verificando o movimento para cima e esquerda
				for(int I = pecaMov.getPosI()-1, J = pecaMov.getPosJ()-1; I >= 0 && J >= 0; I--, J--)
				{
					if(!casa[I][J].vazia() && casa[I][J].pecaAtual().getTipo() == jogadorDaRodada)
						break;
					else if(casa[I][J].vazia())
						casa[I][J].setMarcada(true);
				}
				
				//Verificando o movimento para cima e direita
				for(int I = pecaMov.getPosI()-1, J = pecaMov.getPosJ()+1; I >= 0 && J <= 7; I--, J++)
				{
					if(!casa[I][J].vazia() && casa[I][J].pecaAtual().getTipo() == jogadorDaRodada)
						break;
					else if(casa[I][J].vazia())
						casa[I][J].setMarcada(true);
				}
				
				//Verificando o movimento para baixo e esquerda
				for(int I = pecaMov.getPosI()+1, J = pecaMov.getPosJ()-1; I <= 7 && J >= 0; I++, J--)
				{
					if(!casa[I][J].vazia() && casa[I][J].pecaAtual().getTipo() == jogadorDaRodada)
						break;
					else if(casa[I][J].vazia())
						casa[I][J].setMarcada(true);
				}
				
				//Verificando o movimento para baixo e direita
				for(int I = pecaMov.getPosI()+1, J = pecaMov.getPosJ()+1; I <= 7 && J <= 7; I++, J++)
				{
					if(!casa[I][J].vazia() && casa[I][J].pecaAtual().getTipo() == jogadorDaRodada)
						break;
					else if(casa[I][J].vazia())
						casa[I][J].setMarcada(true);
				}
			}
		}
	}
}
