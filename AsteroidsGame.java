package br.uniriotec.projeto.tp2;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AsteroidsGame extends Applet implements Runnable, KeyListener {

	int x, y, xVel, yVel;
	Thread thread;
	long iniciaTimer, fechaTimer, fps;
	Dimension dim;
	Image img;
	Graphics g;

	public void init() {
		resize(500, 500); // define o tamanho da janela
		x = 225; // cria coordenadas iniciais para o retangulo
		y = 225;
		xVel = 0;
		yVel = 0;
		addKeyListener(this);
		iniciaTimer = 0;
		fechaTimer = 0;
		fps = 25; // 25 milisegundos
		dim = getSize(); // define a dimensão de acordo com a janela
		img = createImage(dim.width, dim.height);// cria o buffer
		g = img.getGraphics(); // entrega os gráficos para o buffer
		thread = new Thread(this); // cria um thread
		thread.start(); // inicia o thread

	}

	public void paint(Graphics gfx) {
		gfx.setColor(Color.BLACK); // deixa a tela preta
		gfx.fillRect(0, 0, 500, 500);
		gfx.setColor(Color.RED);
		gfx.fillOval(x, y, 50, 50); // desenha a forma inicial
		gfx.drawImage(img, 0, 0, this); // imrpime o buffer na tela

	}

	public void update(Graphics gfx) {
		paint(gfx); // Chama a função paint sem alterar a tela

	}

	@Override
	public void run() {
		for (y = 0; y < 450; y += 50) { // x e y posicionam o circulo
			for (x = 0; x < 450; x += 2) {
				// marca o nício do timer
				iniciaTimer = System.currentTimeMillis();
				x += xVel;
				y += yVel;
				repaint();

				try {
					// marca o final do timer
					fechaTimer = System.currentTimeMillis();

					if (fps - (fechaTimer - iniciaTimer) > 0)
						Thread.sleep(fps - (fechaTimer - iniciaTimer));
				} catch (InterruptedException e) {
				}
			}
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) // VK_UP é a seta para cima
			yVel = -1; // subtracting from y moves it upward
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			yVel = 1; // adding to y moves it down
		else if (e.getKeyCode() == KeyEvent.VK_LEFT)
			xVel = -1; // subtracting from x moves it left
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			xVel = 1; // adding to x moves it right

	}

	@Override
	public void keyReleased(KeyEvent e) { //stops moving the circle when the
		 //arrow key is released
		 if(e.getKeyCode()==KeyEvent.VK_UP ||
		 e.getKeyCode()==KeyEvent.VK_DOWN)
		 yVel=0; //stops vertical motion when either up or down
		 //is released
		 else if(e.getKeyCode()==KeyEvent.VK_LEFT ||
		 e.getKeyCode()==KeyEvent.VK_RIGHT)
		 xVel=0; //stops horizontal motion when either right or
		 //left is released

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
