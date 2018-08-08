package gui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Main
{
	public Main() {
		//play();
	}

	public void play() {
		URL url = this.getClass().getResource("tema.wav");
		AudioClip audio = Applet.newAudioClip(url);
		audio.play();
	}
	
	public static void main(String[] args)
	{
		new Main();
		new Tabuleiro();
		
	}
	
	

}
