package gui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;

import javax.swing.*;
import javax.swing.plaf.synth.SynthSpinnerUI;

public class Tabuleiro extends JFrame implements ActionListener
{
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	JPanel p4 = new JPanel();
	JPanel p5 = new JPanel();
	
	JLabel lbJogador = new JLabel("Jogador preto");
	JLabel lbTempo = new JLabel("00:00");
	
	Timer timer;
	
	Casa casa[][] = new Casa[8][8];
	Peca peca[] = new Peca[24];
	
	Casa pecaComer;
	
	int tempo = 0;
	
	int jogadorUm = 1;
	int jogadorDaRodada = 0;
	int pecaInd = 0;
	int clickI;
	int clickJ;
	int numPecas[] = new int[2];
	boolean doisJogadores = false;
	
	//Auxiliares para salvar dados em comida
	Fila comerI[] = new Fila[10];
	Fila comerJ[] = new Fila[10];
	
	String maioresJogadas[] = new String[10];
	int maioresId[] = new int[10];
	int indJogada = 0;
	int maiorTamanho = 0;
	
	Random rand = new Random();
	
	public Tabuleiro()
	{
		construtor();
	}
	
	public Tabuleiro(int inicia)
	{
		doisJogadores = true;
		jogadorDaRodada = inicia;
		construtor();
	}
	
	public void construtor()
	{
		for(int i = 0; i < 10; i++)
		{
			comerI[i] = new Fila(10);
			comerJ[i] = new Fila(10);
		}
		
		//Organizando o layout do tabuleiro
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		p3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		p4.setLayout(new GridLayout(8,8));
		p5.setLayout(new GridLayout(1, 2));
		
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				casa[i][j] = new Casa(i, j);
				p4.add(casa[i][j]);
				
				//Eventos de clique
				casa[i][j].addActionListener(this);
			}
		
		p5.add(p2);
		p2.add(lbJogador);
		
		lbJogador.setForeground(Color.WHITE);
		lbJogador.setFont(new Font("Arial", Font.BOLD, 22));
		
		p5.add(p3);
		p3.add(lbTempo);
		
		lbTempo.setForeground(Color.WHITE);
		lbTempo.setFont(new Font("Arial", Font.BOLD, 22));
		
		p1.add(p5);
		p1.add(p4);
		
		add(p1);
		p2.setBackground(Color.BLACK);
		p3.setBackground(Color.BLACK);
		p4.setBackground(Color.BLACK);
		
		timer = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				tempo++;
				lbTempo.setText(String.format("%02d:%02d", (int)(tempo/60), tempo%60));
			}
		});
		timer.setRepeats(true);
		timer.start();
		
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
						numPecas[tipo]++;
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
		int maior = 0;
		for(int i = inicio; i < inicio+12; i++)
		{
			if(peca[i].emJogo())
			{
				int I = peca[i].getPosI(), J = peca[i].getPosJ();	
				limparJogadas();
				System.out.println("Pedra: " + I + " " + J + "\n");
				if(simularJogada(I, J, true, true, true, true, true, 0, "", peca[i].dama()))
				{
					if(maiorTamanho >= maior)
					{
						if(maiorTamanho > maior)
						{
							System.out.println("Maior jogada");
							maior = maiorTamanho;
							System.out.println(maioresJogadas[indJogada-1] + "\n\n");
							for(int j = inicio; j < i; j++)
							{
								peca[j].setJogavel(false);
								peca[j].setComer(false);
								casa[peca[j].getPosI()][peca[j].getPosJ()].setMarcada(false);
							}
						}
						System.out.println("Tamanho da Jogada: " + maiorTamanho);
						System.out.println("CACETE DE AGULHA");
						marcarPeca(casa[I][J]);
						peca[i].setComer(true);
						Comer = true;
					}
				}
				else
					peca[i].setComer(false);
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
		if(numPecas[0] != 0 && numPecas[1] != 0)
		{
			if(jogadorDaRodada == 1)
			{
				jogadorDaRodada = 0;
				lbJogador.setText("Jogador preto");
			}
			else
			{
				jogadorDaRodada = 1;
				lbJogador.setText("Jogador branco");
			}
			
			if(!doisJogadores && jogadorDaRodada != jogadorUm)
				jogadaPC();
			else
				marcarPecas();
		}
		else if(numPecas[0] == 0)
		{
			timer.stop();
			JOptionPane.showMessageDialog(null, String.format("Vit�ria do jogador Branco\nTempo: %02d:%02d", (int)(tempo/60), tempo%60));
		}
		else
		{
			timer.stop();
			JOptionPane.showMessageDialog(null, String.format("Vit�ria do jogador Preto\nTempo: %02d:%02d", (int)(tempo/60), tempo%60));
		}
		
	}
	
	public void jogadaPC()
	{
		boolean Comer = false;
		for(int i = 0; i < 12 && !Comer; i++)
		{
			if(peca[i].emJogo())
			{
				int I = peca[i].getPosI(), J = peca[i].getPosJ();
				
				limparJogadas();
				if(simularJogada(peca[i].getPosI(), peca[i].getPosJ(), true, true, true, true, true, 0, "", peca[i].dama()) && indJogada > 0)//Marcando as pecas envolvidas no movimento de comer
				{
					int x = rand.nextInt(indJogada);
					//System.out.println(maioresJogadas[x]);
					if(maioresJogadas[x].charAt(maioresJogadas[x].length()-1) == '/')
						maioresJogadas[x] = maioresJogadas[x].substring(0, maioresJogadas[x].length()-1);
					
					//System.out.println(maioresJogadas[x]);
					I = 0;
					J = 0;
					int y;
					String indComer[] = maioresJogadas[x].split("/");
					for(y = 1; y < indComer.length; y += 2)
					{
						I = Integer.parseInt(indComer[y-1]);
						J = Integer.parseInt(indComer[y]);
						if(!casa[I][J].vazia())
						{
							comerI[x].adicionar(I);
							comerJ[x].adicionar(J);
							System.out.printf("Comer (%d,%d)\n", I, J);
						}
					}
					Comer = true;
					for(y = indComer.length-1; y > 0; y -= 2)
					{
						I = Integer.parseInt(indComer[y-1]);
						J = Integer.parseInt(indComer[y]);
						if(casa[I][J].vazia())
						{
							comerPeca(casa[peca[i].getPosI()][peca[i].getPosJ()], casa[I][J]);
							break;
						}
					}
				}
			}
		}
		
		if(!Comer)
		{
			System.out.println("Pora meu, n comeu");
			for(int i = 0; i < 12; i++)
			{
				if(peca[i].emJogo())
				{
					if(!peca[i].dama())
					{
						int I = peca[i].getPosI(), J = peca[i].getPosJ();
						if(I <= 6 && J <= 6 && casa[I+1][J+1].vazia())
						{
							moverPeca(casa[I][J], casa[I+1][J+1]);
							break;
						}
						
						if(I <= 6 && J >= 1 && casa[I+1][J-1].vazia())
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
		}
		proximaRodada();
	}
	
	public void adicionarComer(int I, int J, int Is, int Js)
	{
		maioresJogadas[indJogada] = String.format("%d/%d/%d/%d/", I+Is, J+Js, I+Is*2, J+Js*2);
		comerI[indJogada].adicionar(I+Is);
		comerJ[indJogada].adicionar(J+Js);
		indJogada++;
		comerPeca(casa[I][J], casa[I+Is*2][J+Js*2]);
	}
	
	
	public void comerPeca(Casa comedor, Casa futura)
	{
		int indiceJogada = -1;
		//System.out.println("\n\nJOGADAS DE CLIQUE");
		for(int x = 0; x < indJogada; x++)
		{
			String indComer[] = maioresJogadas[x].split("/");
			if(maioresJogadas[x].length() > 0 && maioresJogadas[x].charAt(maioresJogadas[x].length()-1) == '/')
				indComer = maioresJogadas[x].substring(0, maioresJogadas[x].length()-1).split("/");
			
			//System.out.println(maioresJogadas[x]);
			if(Integer.parseInt(indComer[indComer.length-2]) == futura.getPosI() && Integer.parseInt(indComer[indComer.length-1]) == futura.getPosJ())
			{
				indiceJogada = x;
				break;
			}
		}
		//System.out.println("Ind jogada = " + indiceJogada);
		if(indiceJogada != -1)
		{
			int i;
			String indComer[];
			if(maioresJogadas[indiceJogada].charAt(maioresJogadas[indiceJogada].length()-1) == '/')
				indComer = maioresJogadas[indiceJogada].substring(0, maioresJogadas[indiceJogada].length()-1).split("/");
			else
				indComer = maioresJogadas[indiceJogada].split("/");
			
			//System.out.println("Tamanho: " + comerI[indiceJogada].size());
			for(i = 0; i < comerI[indiceJogada].size(); i++)
			{
				//System.out.println("Fila: " + i + " " + comerI[indiceJogada].obter(i) + " " + comerJ[indiceJogada].obter(i));
				if(!casa[comerI[indiceJogada].obter(i)][comerJ[indiceJogada].obter(i)].vazia())
				{
					//System.out.printf("Comer (%d,%d)\n", comerI[indiceJogada].obter(i), comerJ[indiceJogada].obter(i));
					numPecas[casa[comerI[indiceJogada].obter(i)][comerJ[indiceJogada].obter(i)].pecaAtual().getTipo()]--;
					casa[comerI[indiceJogada].obter(i)][comerJ[indiceJogada].obter(i)].pecaAtual().setEmJogo(false);
					casa[comerI[indiceJogada].obter(i)][comerJ[indiceJogada].obter(i)].pecaAtual().setJogavel(false);
					casa[comerI[indiceJogada].obter(i)][comerJ[indiceJogada].obter(i)].removerPeca();
				}
			}
			//System.out.printf("Mover (%d,%d)\n\n", futura.getPosI(), futura.getPosJ());
			if(!comedor.vazia())
			{
				comedor.pecaAtual().setComer(false);
				moverPeca(comedor, futura);
			}
		}
		
		limparJogadas();
	}
	
	public void iniciarJogo()
	{
		posicionarPecas();
		proximaRodada();
	}
	
	public boolean simularJogada(int i, int j, boolean f1, boolean f2, boolean t2, boolean t1, boolean mover, int tamanho, String mov, boolean dama)
	{
		boolean comer = false;
		//System.out.println("SIMULACAO");
		if(mov.length() > 0)
			System.out.println("Movimento: " + mov);
		//Comendo para frente
		if(i >= 2 && j >= 2 && f1 && !casa[i-1][j-1].vazia() && casa[i-1][j-1].pecaAtual().getTipo() != jogadorDaRodada && casa[i-2][j-2].vazia())
		{
			System.out.println("Cima esquerda");
			simularJogada(i-2, j-2, true, true, false, true, true, tamanho+1, mov + String.format("%d/%d/%d/%d/", i-1, j-1, i-2, j-2), dama);
			comer = true;
		}

		if(i >= 2 && j <= 5 && f2 && !casa[i-1][j+1].vazia() && casa[i-1][j+1].pecaAtual().getTipo() != jogadorDaRodada && casa[i-2][j+2].vazia())
		{
			System.out.println("Cima direita");
			simularJogada(i-2, j+2, true, true, true, false, true, tamanho+1, mov + String.format("%d/%d/%d/%d/", i-1, j+1, i-2, j+2), dama);
			comer = true;
		}
	
		//Comendo para tras
		if(i <= 5 && j >= 2 && t1 && !casa[i+1][j-1].vazia() && casa[i+1][j-1].pecaAtual().getTipo() != jogadorDaRodada && casa[i+2][j-2].vazia())
		{
			System.out.println("Baixo esquerda");
			simularJogada(i+2, j-2, true, false, true, true, true, tamanho+1, mov + String.format("%d/%d/%d/%d/", i+1, j-1, i+2, j-2), dama);
			comer = true;
		}

		if(i <= 5 && j <= 5 && t2 && !casa[i+1][j+1].vazia() && casa[i+1][j+1].pecaAtual().getTipo() != jogadorDaRodada && casa[i+2][j+2].vazia())
		{
			System.out.println("Baixo direita");
			simularJogada(i+2, j+2, false, true, true, true, true, tamanho+1, mov + String.format("%d/%d/%d/%d/", i+1, j+1, i+2, j+2), dama);
			comer = true;
		}
		
		
		if(!comer && mover && dama)
		{
			if(i >= 1 && j >= 1 && f1 && casa[i-1][j-1].vazia())
			{
				String movAux = mov;
				//Verificando o movimento para cima e esquerda
				for(int I = i-1, J = j-1; I >= 0 && J >= 0; I--, J--)
				{
					if(!casa[I][J].vazia())
						break;
					else
					{
						mov += String.format("%d/%d/%d/%d/", I, J, I, J);
						System.out.println("Mov cima esquerda " + I + " " + J);
						comer = simularJogada(I, J, true, true, false, true, false, tamanho, mov, dama);
						System.out.println("RETORNO");
					}
				}
			}
			
			if(i >= 1 && j <= 6 && f2 && casa[i-1][j+1].vazia())
			{
				String movAux = mov;
				for(int I = i-1, J = j+1; I >= 0 && J <= 7; I--, J++)
				{
					if(!casa[I][J].vazia())
						break;
					else
					{
						mov += String.format("%d/%d/%d/%d/", I, J, I, J);
						System.out.println("Mov cima direita " + I + " " + J);
						comer = simularJogada(I, J, true, true, true, false, false, tamanho, mov, dama);
						System.out.println("RETORNO");
					}
				}
			}
			
			if(i <= 6 && j >= 1 && t1 && casa[i+1][j-1].vazia())
			{
				String movAux = mov;
				for(int I = i+1, J = j-1; I <= 7 && J >= 0; I++, J--)
				{
					if(!casa[I][J].vazia())
						break;
					else
					{
						mov += String.format("%d/%d/%d/%d/", I, J, I, J);
						System.out.println("Mov baixo esquerda " + I + " " + J);
						comer = simularJogada(I, J, true, false, true, true, false, tamanho, mov, dama);
						System.out.println("RETORNO");
					}
				}
			}
			
			if(i <= 6 && j <= 6 && t2 && casa[i+1][j+1].vazia())
			{
				String movAux = mov;
				for(int I = i+1, J = j+1; I <= 7 && J <= 7; I++, J++)
				{
					if(!casa[I][J].vazia())
						break;
					else
					{
						mov += String.format("%d/%d/%d/%d/", I, J, I, J);
						System.out.println("Mov baixo direita " + I + " " + J);
						comer = simularJogada(I, J, false, true, true, true, false, tamanho, mov, dama);
						System.out.println("RETORNO");
					}
				}
			}
		}
		
		if(tamanho > maiorTamanho)
		{
			maiorTamanho = tamanho;
			maioresJogadas[indJogada = 0] = mov;
			indJogada++;
		}
		else if(tamanho == maiorTamanho && indJogada < 10)
		{
			maioresJogadas[indJogada++] = mov;
		}
		return comer;
	}
	
	public void marcarCasas(Casa casaComer, Casa casaFutura)
	{
		casaComer.setBackground(Color.decode("#4d70ad"));//Marcando a peca a ser comida
		casaFutura.setMarcada(true);//Deixando laranja as casas
	}
	
	public void limparJogadas()
	{
		indJogada = 0;
		maiorTamanho = 0;
		
		//Limpando todas as listas
		for(int x = 0; x < 10; x++)
		{
			comerI[x].limpar();
			comerJ[x].limpar();
		}
	}
	
	public void marcarMovimento(Casa casaX)
	{
		int i = casaX.getPosI(), j = casaX.getPosJ();
		casaX.setMarcada(true);//Tornando verde a casa clicada
		if(casaX.pecaAtual().comer())
		{
			/*if(!casaX.pecaAtual().dama())
			{*/
				limparJogadas();
				simularJogada(i, j, true, true, true, true, true, 0, "", casaX.pecaAtual().dama());//Marcando as pecas envolvidas no movimento de comer
			//}
			System.out.println(indJogada);
			for(int x = 0; x < indJogada; x++)
			{
				if(maioresJogadas[x].length() > 0 && maioresJogadas[x].charAt(maioresJogadas[x].length()-1) == '/')
					maioresJogadas[x] = maioresJogadas[x].substring(0, maioresJogadas[x].length()-1);
				
				System.out.println(maioresJogadas[x]);
				int I = 0, J = 0, y;
				String indComer[] = maioresJogadas[x].split("/");
				System.out.println("Movimento marcado");
				for(y = 1; y < indComer.length; y += 2)
				{
					I = Integer.parseInt(indComer[y-1]);
					J = Integer.parseInt(indComer[y]);
					casa[I][J].setBackground(Color.decode("#4d70ad"));
					if(!casa[I][J].vazia())
					{
						comerI[x].adicionar(I);
						comerJ[x].adicionar(J);
						System.out.printf("Comer (%d,%d)\n", I, J);
					}
					else
					{
						System.out.printf("Mover (%d,%d)\n", I, J);
					}
				}
				for(y = indComer.length-1; y > 0; y -= 2)
				{
					I = Integer.parseInt(indComer[y-1]);
					J = Integer.parseInt(indComer[y]);
					if(casa[I][J].vazia())
						casa[I][J].setMarcada(true);
					else
						break;
				}
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
					if(i != 7 && j != 0 && casa[i+1][j-1].vazia())
						casa[i+1][j-1].setMarcada(true);
					if(i != 7 && j != 7 && casa[i+1][j+1].vazia())
						casa[i+1][j+1].setMarcada(true);
				}
			}
			else
			{
				Peca pecaMov = casaX.pecaAtual();
				//Verificando o movimento para cima e esquerda
				for(int I = pecaMov.getPosI()-1, J = pecaMov.getPosJ()-1; I >= 0 && J >= 0; I--, J--)
				{
					if(!casa[I][J].vazia())
						break;
					else
						casa[I][J].setMarcada(true);
				}
				
				//Verificando o movimento para cima e direita
				for(int I = pecaMov.getPosI()-1, J = pecaMov.getPosJ()+1; I >= 0 && J <= 7; I--, J++)
				{
					if(!casa[I][J].vazia())
						break;
					else
						casa[I][J].setMarcada(true);
				}
				
				//Verificando o movimento para baixo e esquerda
				for(int I = pecaMov.getPosI()+1, J = pecaMov.getPosJ()-1; I <= 7 && J >= 0; I++, J--)
				{
					if(!casa[I][J].vazia())
						break;
					else
						casa[I][J].setMarcada(true);
				}
				
				//Verificando o movimento para baixo e direita
				for(int I = pecaMov.getPosI()+1, J = pecaMov.getPosJ()+1; I <= 7 && J <= 7; I++, J++)
				{
					if(!casa[I][J].vazia())
						break;
					else
						casa[I][J].setMarcada(true);
				}
			}
		}
	}
}
