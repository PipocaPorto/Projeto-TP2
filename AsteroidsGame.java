package br.uniriotec.projeto.tp2;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class AsteroidsGame extends Applet implements Runnable, KeyListener {

	// int x, y, xVel, yVel;
	Thread thread;
	Dimension dim;
	Image img;
	Graphics g;

	long iniciaTimer, fechaTimer, fps;

	Nave nave;
	boolean paused;
	Tiro[] tiros; // Variable that stores the new array of Shots
	int numTiros; // Stores the number of shots in the array
	boolean atira; // true if the ship is currently shooting

	Asteroide[] asteroides; // the array of asteroids
	int numAsteroides; // the number of asteroids currently in the array
	double astRaio, velMinAst, velMaxAst; // values used to create asteroids

	int astNumHits, astNumSplit;

	int level; // the current level number

	public void init() {
		resize(500, 500); // define o tamanho da janela
		// x = 225; // cria coordenadas iniciais para o retangulo
		// y = 225;
		// xVel = 0;
		// yVel = 0;
		// addKeyListener(this);
		// iniciaTimer = 0;
		// fechaTimer = 0;
		// fps = 25; // 25 milisegundos
		// dim = getSize(); // define a dimensão de acordo com a janela
		// img = createImage(dim.width, dim.height);// cria o buffer
		// g = img.getGraphics(); // entrega os gráficos para o buffer
		// thread = new Thread(this); // cria um thread
		// thread.start(); // inicia o thread
		// nave = new Nave(250, 250, 0, .35, .98, .1, 12); // creates the nave
		// paused = false;
		tiros = new Tiro[41]; // Allocate the space for the array.
		// We allocate enough space to store the maximum number of
		// shots that can possibly be on the screen at one time.
		// 41 is the max because no more than one shot can be fired per
		// frame and shots only last for 40 frames (40 is the value passed
		// in for lifeLeft when shots are created)
		numAsteroides = 0;
		level = 0; // will be incremented to 1 when first level is set up
		astRaio = 60; // values used to create the asteroids
		velMinAst = .5;
		velMaxAst = 5;
		astNumHits = 3;
		astNumSplit = 2;

		fechaTimer = 0;
		iniciaTimer = 0;
		fps = 25;
		addKeyListener(this); // tell it to listen for KeyEvents
		dim = getSize();
		img = createImage(dim.width, dim.height);
		g = img.getGraphics();
		thread = new Thread(this);
		thread.start();
		// numTiros = 0; // no shots on the screen to start with.
		// atira = false; // the ship is not shooting

	}

	public void setUpNextLevel() { // starts a new level with one more asteroid
		level++;
		// create a new, inactive ship centered on the screen
		// I like .35 for acceleration, .98 for velocityDecay, and
		// .1 for rotationalSpeed. They give the controls a nice feel.
		nave = new Nave(250, 250, 0, .35, .98, .1, 12);
		numTiros = 0; // no shots on the screen at beginning of level
		paused = false;
		atira = false;
		// create an array large enough to hold the biggest number
		// of asteroids possible on this level (plus one because
		// the split asteroids are created first, then the original
		// one is deleted). The level number is equal to the
		// number of asteroids at it's start.
		asteroides = new Asteroide[level * (int) Math.pow(astNumSplit, astNumHits - 1) + 1];
		numAsteroides = level;
		// create asteroids in random spots on the screen
		for (int i = 0; i < numAsteroides; i++)
			asteroides[i] = new Asteroide(Math.random() * dim.width, Math.random() * dim.height, astRaio, velMinAst,
					velMaxAst, astNumHits, astNumSplit);
	}

	public void paint(Graphics gfx) {
		g.setColor(Color.BLACK); // deixa a tela preta
		g.fillRect(0, 0, 500, 500);

		for (int i = 0; i < numTiros; i++) {// loop that calls draw() for each
			tiros[i].draw(g); // shot

		}

		for (int i = 0; i < numAsteroides; i++) {
			asteroides[i].draw(g);

		}

		nave.draw(g); // draw the nave

		g.setColor(Color.GREEN); // Display the level number in top left corner
		g.drawString("Level " + level, 20, 20);

		gfx.drawImage(img, 0, 0, this);
		// gfx.setColor(Color.RED);

		// gfx.fillOval(x, y, 50, 50); // desenha a forma inicial
		// gfx.drawImage(img, 0, 0, this); // imrpime o buffer na tela

	}

	public void update(Graphics gfx) {
		paint(gfx); // Chama a função paint sem alterar a tela

	}

	@Override
	public void run() {
		// for (y = 0; y < 450; y += 50) { // x e y posicionam o circulo
		for (;;) {
			// marca o nício do timer
			iniciaTimer = System.currentTimeMillis();

			if (numAsteroides <= 0) {
				setUpNextLevel();

			}

			if (!paused) {
				nave.move(dim.width, dim.height); // move the nave

				for (int i = 0; i < numTiros; i++) {
					tiros[i].move(dim.width, dim.height);
					// removes shot if it has gone for too long
					// without hitting anything
					if (tiros[i].getDeletaTiro() <= 0) {
						// shifts all the next shots up one
						// space in the array
						deletaTiro(i); // SEE NEW METHOD BELOW
						i--; // move the outer loop back one so
						// the shot shifted up is not skipped
					}
				}

				updateAsteroids();// SEE NEW METHOD BELOW

				if (atira && nave.canShoot()) {
					// add a shot on to the array if the ship is shooting
					tiros[numTiros] = nave.shoot();
					numTiros++;
				}

			}
			// repaint();
			// x += xVel;
			// y += yVel;
			repaint();

			try {
				// marca o final do timer
				fechaTimer = System.currentTimeMillis();

				if (fps - (fechaTimer - iniciaTimer) > 0) {
					Thread.sleep(fps - (fechaTimer - iniciaTimer));

				}
			} catch (InterruptedException e) {
			}
		}
		// }
	}

	private void deletaTiro(int index) {
		// delete shot and move all shots after it up in the array
		numTiros--;
		for (int i = index; i < numTiros; i++)
			tiros[i] = tiros[i + 1];
		tiros[numTiros] = null;
	}

	private void deleteAsteroid(int index) {
		// delete asteroid and shift ones after it up in the array
		numAsteroides--;
		for (int i = index; i < numAsteroides; i++)
			asteroides[i] = asteroides[i + 1];
		asteroides[numAsteroides] = null;
	}

	private void addAsteroid(Asteroide ast) {
		// adds the asteroid passed in to the end of the array
		asteroides[numAsteroides] = ast;
		numAsteroides++;
	}

	private void updateAsteroids() {
		for (int i = 0; i < numAsteroides; i++) {
			// move each asteroid
			asteroides[i].move(dim.width, dim.height);
			// check for collisions with the ship, restart the
			// level if the ship gets hit
			if (asteroides[i].naveCollision(nave)) {
				level--; // restart this level
				numAsteroides = 0;
				return;
			}
			// check for collisions with any of the shots
			for (int j = 0; j < numTiros; j++) {
				if (asteroides[i].shotCollision(tiros[j])) {
					// if the shot hit an asteroid, delete the shot
					deletaTiro(j);
					// split the asteroid up if needed
					if (asteroides[i].getHitsLeft() > 1) {
						for (int k = 0; k < asteroides[i].getNumSplit(); k++)
							addAsteroid(asteroides[i].createSplitAsteroid(velMinAst, velMaxAst));
					}
					// delete the original asteroid
					deleteAsteroid(i);
					j = numTiros; // break out of inner loop - it has
					// already been hit, so don’t need to check
					// for collision with other shots
					i--; // don’t skip asteroid shifted back into
					// the deleted asteroid's position
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// These first two lines allow the asteroids to move
			// while the player chooses when to enter the game.
			// This happens when the player is starting a new life.
			if (!nave.isAtivo() && !paused) {
				nave.setAtivo(true);

			} else {
				paused = !paused; // enter is the pause button

			}
			if (paused) {// grays out the nave if paused
				nave.setAtivo(false);

			} else {
				nave.setAtivo(true);

			}
		} else if (paused || !nave.isAtivo()) {// if the game is
			return; // paused or nave is inactive, do not respond
			// to the controls except for enter to unpause

		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			nave.setAcelerando(true);

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			nave.setOlharEsquerda(true);

		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			nave.setOlharDireita(true);

		} else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			atira = true; // Start shooting when ctrl is pushed

		}

		/*
		 * if (e.getKeyCode() == KeyEvent.VK_UP) {// VK_UP é a seta para cima
		 * yVel = -1; // subtracting from y moves it upward
		 * 
		 * } else if (e.getKeyCode() == KeyEvent.VK_DOWN) { yVel = 1; // adding
		 * to y moves it down
		 * 
		 * } else if (e.getKeyCode() == KeyEvent.VK_LEFT) { xVel = -1; //
		 * subtracting from x moves it left
		 * 
		 * } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { xVel = 1; // adding
		 * to x moves it right
		 * 
		 * }
		 */

	}

	@Override
	public void keyReleased(KeyEvent e) { // stops moving the circle when the
		// arrow key is released
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			nave.setAcelerando(false);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			nave.setOlharEsquerda(false);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			nave.setOlharDireita(false);
		} else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			atira = false;
		}
		/*
		 * if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() ==
		 * KeyEvent.VK_DOWN) { yVel = 0; // stops vertical motion when either up
		 * or down // is released
		 * 
		 * } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() ==
		 * KeyEvent.VK_RIGHT) { xVel = 0; // stops horizontal motion when either
		 * right or // left is released
		 * 
		 * } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) { atira = false; //
		 * Stop shooting when ctrl is released
		 * 
		 * }
		 */

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
