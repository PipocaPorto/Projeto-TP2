package br.uniriotec.projeto.tp2;

import java.awt.*;

public class Tiro {

	final double velTiro = 12; // the speed at which the shots move, in
	// pixels per frame
	double x, y, xVel, yVel; // variables for movement
	int deletaTiro; // causes the shot to eventually disappear if it doesn’t
	// hit anything

	public void move(int larguraTela, int alturaTela) {
		deletaTiro--; // used to make shot disappear if it goes too long
		// without hitting anything
		x += xVel; // move the shot
		y += yVel;

		if (x < 0) { // wrap the shot around to the opposite side of the
			x += larguraTela; // screen if needed

		} else if (x > larguraTela) {
			x -= larguraTela;

		}

		if (y < 0) {
			y += alturaTela;

		} else if (y > alturaTela) {
			y -= alturaTela;

		}
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLUE); // set shot color
		// draw circle of radius 3 centered at the closest point
		// with integer coordinates (.5 added to x-1 and y-1 for rounding)
		g.fillOval((int) (x - .5), (int) (y - .5), 3, 3);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getDeletaTiro() {
		return deletaTiro;
	}

	public Tiro(double x, double y, double angle, double naveXVel, double naveYVel, int deletaTiro) {
		this.x = x;
		this.y = y;
		// add the velocity of the ship to the shot velocity
		// (so the shot's velocity is relative to the ship's velocity)
		xVel = velTiro * Math.cos(angle) + naveXVel;
		yVel = velTiro * Math.sin(angle) + naveYVel;
		// the number of frames the shot will last for before
		// disappearing if it doesn't hit anything
		this.deletaTiro = deletaTiro;
	
	}

}
