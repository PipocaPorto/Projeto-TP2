package br.uniriotec.projeto.tp2;

import java.awt.*;

public class Asteroide {
	double x, y, xVel, yVel, raio;
	int vida, numSplit;

	public Asteroide(double x, double y, double raio, double velMin, double velMax, int vida, int numSplit) {
		this.x = x;
		this.y = y;
		this.raio = raio;
		this.vida = vida; // number of shots left to destroy it
		this.numSplit = numSplit; // number of smaller asteroids it
		// breaks up into when shot
		// calculates a random direction and a random
		// velocity between minVelocity and maxVelocity
		double vel = velMin + Math.random() * (velMax - velMin), dir = 2 * Math.PI * Math.random(); // random
																									// direction
		xVel = vel * Math.cos(dir);
		yVel = vel * Math.sin(dir);
	}

	public void move(int scrnWidth, int scrnHeight) {
		x += xVel; // move the asteroid
		y += yVel;
		// wrap around code allowing the asteroid to go off the screen
		// to a distance equal to its raio before entering on the
		// other side. Otherwise, it would go halfway off the sceen,
		// then disappear and reappear halfway on the other side
		// of the screen.
		if (x < 0 - raio) {
			x += scrnWidth + 2 * raio;

		} else if (x > scrnWidth + raio) {
			x -= scrnWidth + 2 * raio;

		}

		if (y < 0 - raio) {
			y += scrnHeight + 2 * raio;
		} else if (y > scrnHeight + raio) {
			y -= scrnHeight + 2 * raio;

		}
	}

	public void draw(Graphics g) {
		g.setColor(Color.GRAY); // set color for the asteroid
		// draw the asteroid centered at (x,y)
		g.fillOval((int) (x - raio + .5), (int) (y - raio + .5), (int) (2 * raio), (int) (2 * raio));
	}

	public boolean naveCollision(Nave nave) {
		// Use the distance formula to check if the nave is touching this
		// asteroid: Distance^2 = (x1-x2)^2 + (y1-y2)^2 ("^" denotes
		// exponents). If the sum of the radii is greater than the
		// distance between the center of the nave and asteroid, they are
		// touching.
		// if (naveRadius + asteroidRadius)^2 > (x1-x2)^2 + (y1-y2)^2,
		// then they have collided.
		// It does not check for collisions if the nave is not active
		// (the player is waiting to start a new life or the game is paused).
		if (Math.pow(raio + nave.getRadius(), 2) > Math.pow(nave.getX() - x, 2) + Math.pow(nave.getY() - y, 2)
				&& nave.isAtivo()) {
			return true;

		}

		return false;

	}

	public boolean shotCollision(Tiro tiro) {
		// Same idea as naveCollision, but using shotRadius = 0
		if (Math.pow(raio, 2) > Math.pow(tiro.getX() - x, 2) + Math.pow(tiro.getY() - y, 2)) {
			return true;

		}

		return false;

	}

	public Asteroide createSplitAsteroid(double velMin, double velMax) {
		// when this asteroid gets hit by a shot, this method is called
		// numSplit times by AsteroidsGame to create numSplit smaller
		// asteroids. Dividing the radius by sqrt(numSplit) makes the
		// sum of the areas taken up by the smaller asteroids equal to
		// the area of this asteroid. Each smaller asteroid has one
		// less hit left before being completely destroyed.
		return new Asteroide(x, y, raio / Math.sqrt(numSplit), velMin, velMax, vida - 1, numSplit);
		
	}

	public int getHitsLeft() {
		// used by AsteroidsGame to determine whether the asteroid should
		// be split up into smaller asteroids or destroyed completely.
		return vida;

	}

	public int getNumSplit() {
		return numSplit;

	}

}
