package br.uniriotec.projeto.tp2;

import java.awt.*;

public class Nave {
	// define the shape of the ship and its flame
	final double[] origXPts = { 14, -10, -6, -10 }, origYPts = { 0, -8, 0, 8 }, origFlameXPts = { -6, -23, -6 },
			origFlameYPts = { -3, 0, 3 };
	final int radius = 6; // radius of circle used to approximate the ship

	double x, y, angle, xVel, yVel, acceleration, freio, velGiro; // variables
																	// used
																	// in
																	// movement
	boolean olharEsquerda, olharDireita, acelerando, ativo;
	int[] xPts, yPts, flameXPts, flameYPts; // store the current locations
	// of the points used to draw the ship and its flame
	int shotDelay, shotDelayLeft; // used to determine the rate of firing

	public void draw(Graphics g) {
		// rotate the points, translate them to the ship's location (by
		// adding x and y), then round them by adding .5 and casting them
		// as integers (which truncates any decimal place)
		if (acelerando && ativo) { // draw flame if acelerando
			for (int i = 0; i < 3; i++) {
				flameXPts[i] = (int) (origFlameXPts[i] * Math.cos(angle) - origFlameYPts[i] * Math.sin(angle) + x + .5);
				flameYPts[i] = (int) (origFlameXPts[i] * Math.sin(angle) + origFlameYPts[i] * Math.cos(angle) + y + .5);
			}
			g.setColor(Color.RED); // set color of flame
			g.fillPolygon(flameXPts, flameYPts, 3); // 3 is # of points
		}
		// calculate the polygon for the ship, then draw it
		for (int i = 0; i < 4; i++) {
			xPts[i] = (int) (origXPts[i] * Math.cos(angle) - // rotate
					origYPts[i] * Math.sin(angle) + x + .5); // translate and
																// round
			yPts[i] = (int) (origXPts[i] * Math.sin(angle) + // rotate
					origYPts[i] * Math.cos(angle) + y + .5); // translate and
																// round
		}
		if (ativo) {// ativo means game is running (not paused)
			g.setColor(Color.WHITE);

		} else {// draw the ship dark gray if the game is paused
			g.setColor(Color.DARK_GRAY);

		}
		g.fillPolygon(xPts, yPts, 4); // 4 is the number of points
	}

	public void move(int larguraTela, int alturaTela) {
		if (shotDelayLeft > 0) {// move() is called every frame that the game
			shotDelayLeft--;// is run, so this ticks down the shot delay

		}

		if (olharEsquerda) {// this is backwards from typical polar coordinates
			angle -= velGiro; // because positive y is downward.

		}

		if (olharDireita) {// Because of that, adding to the angle is
			angle += velGiro; // rotating clockwise (to the right)

		}

		if (angle > (2 * Math.PI)) {// Keep angle within bounds of 0 to 2*PI
			angle -= (2 * Math.PI);

		} else if (angle < 0) {
			angle += (2 * Math.PI);

		}

		if (acelerando) { // adds accel to velocity in direction pointed
			// calculates components of accel and adds them to velocity
			xVel += acceleration * Math.cos(angle);
			yVel += acceleration * Math.sin(angle);
		}

		x += xVel; // move the ship by adding velocity to position
		y += yVel;
		xVel *= freio; // slows ship down by percentages (velDecay
		yVel *= freio; // should be a decimal between 0 and 1
		
		if (x < 0) {// wrap the ship around to the opposite side of the screen
			x += larguraTela; // when it goes out of the screen's bounds

		} else if (x > larguraTela) {
			x -= larguraTela;

		}
		if (y < 0) {
			y += alturaTela;

		} else if (y > alturaTela) {
			y -= alturaTela;

		}
	}

	public Tiro shoot() {
		shotDelayLeft = shotDelay; // set delay till next shot can be fired
		// a life of 40 makes the shot travel about the width of the
		// screen before disappearing
		return new Tiro(x, y, angle, xVel, yVel, 40);
		
	}

	public void setAcelerando(boolean acelerando) {
		this.acelerando = acelerando; // start or stop acelerando the ship
		
	}

	public void setOlharEsquerda(boolean olharEsquerda) {
		this.olharEsquerda = olharEsquerda; // start or stop turning the ship
		
	}

	public void setOlharDireita(boolean olharDireita) {
		this.olharDireita = olharDireita;
		
	}

	public double getX() {
		return x; // returns the ship’s x location
		
	}

	public double getY() {
		return y;
		
	}

	public double getRadius() {
		return radius; // returns radius of circle that approximates the ship
		
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo; // used when the game is paused or unpaused
		
	}

	public boolean isAtivo() {
		return ativo;
		
	}

	public boolean canShoot() {
		if (shotDelayLeft > 0) { // checks to see if the ship is ready to
			return false; // shoot again yet or if it needs to wait longer
			
		} else {
			return true;
			
		}
		
	}

	public Nave(double x, double y, double angle, double acceleration, double freio, double velGiro, int shotDelay) {
		// this.x refers to the Ship's x, x refers to the x parameter
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.acceleration = acceleration;
		this.freio = freio;
		this.velGiro = velGiro;
		xVel = 0; // not moving
		yVel = 0;
		olharEsquerda = false; // not turning
		olharDireita = false;
		acelerando = false; // not acelerando
		ativo = false; // start off paused
		xPts = new int[4]; // allocate space for the arrays
		yPts = new int[4];
		flameXPts = new int[3];
		flameYPts = new int[3];
		this.shotDelay = shotDelay; // # of frames between shots
		shotDelayLeft = 0; // ready to shoot

	}
}
