package gui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;
import javax.swing.plaf.synth.SynthSpinnerUI;

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
	Fila comerI[] = new Fila[10];
	Fila comerJ[] = new Fila[10];
	
	String maioresJogadas[] = new String[10];
	int indJogada = 0;
	int maiorTamanho = 0;
	
	public Tabuleiro()
	{
		for(int i = 0; i < 10; i++)
		{
			comerI[i] = new Fila(10);
			comerJ[i] = new Fila(10);
		}
		
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
				
				if(peca[i].dama())
				{
					//Verificando o movimento para cima e esquerda
					for(int Ix = peca[i].getPosI()-1, Jx = peca[i].getPosJ()-1; Ix >= 0 && Jx >= 0; Ix--, Jx--)
					{
						if(!casa[Ix][Jx].vazia() && casa[Ix][Jx].pecaAtual().getTipo() == jogadorDaRodada)
							break;
						else if(casa[Ix][Jx].vazia())
							casa[Ix][Jx].setMarcada(true);
					}
					
					//Verificando o movimento para cima e direita
					for(int Ix = peca[i].getPosI()-1, Jx = peca[i].getPosJ()+1; Ix >= 0 && Jx <= 7; Ix--, Jx++)
					{
						if(!casa[Ix][Jx].vazia() && casa[Ix][Jx].pecaAtual().getTipo() == jogadorDaRodada)
							break;
						else if(casa[Ix][Jx].vazia())
							casa[Ix][Jx].setMarcada(true);
					}
					
					//Verificando o movimento para baixo e esquerda
					for(int Ix = peca[i].getPosI()+1, Jx = peca[i].getPosJ()-1; Ix <= 7 && Jx >= 0; Ix++, Jx--)
					{
						if(!casa[Ix][Jx].vazia() && casa[Ix][Jx].pecaAtual().getTipo() == jogadorDaRodada)
							break;
						else if(casa[Ix][Jx].vazia())
							casa[Ix][Jx].setMarcada(true);
					}
					
					//Verificando o movimento para baixo e direita
					for(int Ix = peca[i].getPosI()+1, Jx = peca[i].getPosJ()+1; Ix <= 7 && Jx <= 7; Ix++, Jx++)
					{
						if(!casa[Ix][Jx].vazia() && casa[Ix][Jx].pecaAtual().getTipo() == jogadorDaRodada)
							break;
						else if(casa[Ix][Jx].vazia())
							casa[Ix][Jx].setMarcada(true);
					}
				}
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
						comerPeca(casa[clickI][clickJ], casa[i][j]);		
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
	
	public void comerPeca(Casa comedor, Casa futura)
	{
		int indiceJogada = -1;
		for(int x = 0; x < indJogada; x++)
		{
			String indComer[] = maioresJogadas[x].split("/");
			if(Integer.parseInt(indComer[indComer.length-2]) == futura.getPosI() && Integer.parseInt(indComer[indComer.length-1]) == futura.getPosJ())
			{
				indiceJogada = x;
				break;
			}
		}
		
		if(indiceJogada != -1)
		{
			int i;
			String indComer[] = maioresJogadas[indiceJogada].split("/");
			System.out.println();
			for(i = 0; i < comerI[indiceJogada].size(); i++)
			{
				if(!casa[comerI[indiceJogada].obter(i)][comerJ[indiceJogada].obter(i)].vazia())
				{
					System.out.printf("Comer (%d,%d)\n", comerI[indiceJogada].obter(i), comerJ[indiceJogada].obter(i));
					casa[comerI[indiceJogada].obter(i)][comerJ[indiceJogada].obter(i)].pecaAtual().setEmJogo(false);
					casa[comerI[indiceJogada].obter(i)][comerJ[indiceJogada].obter(i)].pecaAtual().setJogavel(false);
					casa[comerI[indiceJogada].obter(i)][comerJ[indiceJogada].obter(i)].removerPeca();
				}
			}
			System.out.printf("Mover (%d,%d)\n\n", Integer.parseInt(indComer[indComer.length-2]), Integer.parseInt(indComer[indComer.length-1]));
			comedor.pecaAtual().setComer(false);
			moverPeca(comedor, casa[Integer.parseInt(indComer[indComer.length-2])][Integer.parseInt(indComer[indComer.length-1])]);
			comerI[indiceJogada].limpar();
			comerJ[indiceJogada].limpar();
			indJogada = 0;
			maiorTamanho = 0;
		}
	}
	
	public void iniciarJogo()
	{
		posicionarPecas();
		proximaRodada();
	}
	
	public void verificarJogada(int i, int j, boolean f1, boolean f2, boolean t2, boolean t1, int tamanho, String mov)
	{
		System.out.println("AHSDUAHDASU");
		//Comendo para frente
		if(i >= 2 && j >= 2 && f1 && !casa[i-1][j-1].vazia() && casa[i-1][j-1].pecaAtual().getTipo() != jogadorDaRodada && casa[i-2][j-2].vazia())
		{
			System.out.println("FRENTE 1");
			verificarJogada(i-2, j-2, true, true, false, true, tamanho+1, mov + String.format("%d/%d/%d/%d/", i-1, j-1, i-2, j-2));
		}

		if(i >= 2 && j <= 5 && f2 && j <= 8 && i<=8 && j <= 8 && !casa[i-1][j+1].vazia() && casa[i-1][j+1].pecaAtual().getTipo() != jogadorDaRodada && casa[i-2][j+2].vazia())
		{
			System.out.println("FRENTE 2");
			verificarJogada(i-2, j+2, true, true, true, false, tamanho+1, mov + String.format("%d/%d/%d/%d/", i-1, j+1, i-2, j+2));
		}
	
		//Comendo para tras

		if(i <= 5 && j >= 2 && t1 && i <= 8 && !casa[i+1][j-1].vazia() && casa[i+1][j-1].pecaAtual().getTipo() != jogadorDaRodada && casa[i+2][j-2].vazia())
		{
			System.out.println("TRAS 1");
			verificarJogada(i+2, j-2, true, false, true, true, tamanho+1, mov + String.format("%d/%d/%d/%d/", i+1, j-1, i+2, j-2));
		}

		if(i <= 5 && j <= 5 && i <= 8 && j <= 8 && t2 && !casa[i+1][j+1].vazia() && casa[i+1][j+1].pecaAtual().getTipo() != jogadorDaRodada && casa[i+2][j+2].vazia())
		{
			System.out.println("TRAS 2");
			verificarJogada(i+2, j+2, false, true, true ,true, tamanho+1, mov + String.format("%d/%d/%d/%d/", i+1, j+1, i+2, j+2));
		}
		
		if(tamanho > maiorTamanho)
		{
			maiorTamanho = tamanho;
			maioresJogadas[indJogada = 0] = mov;
			indJogada++;
		}
		else if(tamanho == maiorTamanho)
		{
			maioresJogadas[indJogada++] = mov;
		}
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
		{
			verificarJogada(i, j, true, true, true, true, 0, "");//Marcando as pecas envolvidas no movimento de comer
			System.out.println(indJogada);
			for(int x = 0; x < indJogada; x++)
			{
				maioresJogadas[x] = maioresJogadas[x].substring(0, maioresJogadas[x].length()-1);
				System.out.println(maioresJogadas[x]);
				int I = 0, J = 0, y;
				String indComer[] = maioresJogadas[x].split("/");
				for(y = 3; y < indComer.length; y += 4)
				{
					I = Integer.parseInt(indComer[y-3]);
					J = Integer.parseInt(indComer[y-2]);
					comerI[x].adicionar(I);
					comerJ[x].adicionar(J);
					System.out.printf("Comer (%d,%d)\n", I, J);
					casa[I][J].setBackground(Color.decode("#4d70ad"));
					I = Integer.parseInt(indComer[y-1]);
					J = Integer.parseInt(indComer[y]);
					System.out.printf("Mover (%d,%d)\n", I, J);
					casa[I][J].setBackground(Color.decode("#4d70ad"));
				}
				casa[I][J].setMarcada(true);
			}
		}
		else
		{
			if(!casaX.pecaAtual().dama())
			{
				if(jogadorDaRodada == 1)
				{
					if(i != 0 && j != 0 && casa[i-1][j-1].vazia())
						casa[i-1][j-1].setMarcada(true);
					if(i != 0 && j != 7 && casa[i-1][j+1].vazia())
						casa[i-1][j+1].setMarcada(true);
				}
				else
				{
					if(i != 0 && j != 0 && casa[i+1][j-1].vazia())
						casa[i-1][j-1].setMarcada(true);
					if(i != 0 && j != 7 && casa[i+1][j+1].vazia())
						casa[i-1][j+1].setMarcada(true);
				}
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
