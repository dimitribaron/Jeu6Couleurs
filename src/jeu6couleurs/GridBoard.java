package jeu6couleurs;

import java.util.ArrayList;
import java.util.Random;

abstract public class GridBoard extends Board {
	
	// constructor
	public GridBoard(int size) {
		if (size < 2) {
			throw new IllegalArgumentException("Le plateau doit faire au moins 2 cases de côté.");
		}
		this.size = size;
		this.nbCases = size * size;
		cases = new Case[size][size];
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cases[i][j] = new Case();
				cases[i][j].setCouleurFirstTime(couleurs[random.nextInt(6) * 2]);
			}
		}
	}

	// methods
	@Override
	protected void firstCases(int nbreJoueurs) {
		ArrayList<Integer> couleursRestantes = new ArrayList<>();	
		for (int i = 0, len = couleurs.length / 2; i < len; i += 1) {
			couleursRestantes.add(i);
		}
		Random random = new Random();
		int index = random.nextInt(couleursRestantes.size());
		cases[0][0].setCouleur(couleurs[2 * couleursRestantes.get(index) + 1]);
		couleurJoueur[0] = couleursRestantes.get(index);
		couleursRestantes.remove(index);
		cases[0][0].setOwner(1);
		cases[0][0].notRecentlyTaken();
		index = random.nextInt(couleursRestantes.size());
		cases[size - 1][size - 1].setCouleur(couleurs[2 * couleursRestantes.get(index) + 1]);
		if (nbreJoueurs > 2) {
			couleurJoueur[2] = couleursRestantes.get(index);
			couleursRestantes.remove(index);
			cases[size - 1][size - 1].setOwner(3);
			cases[size - 1][size - 1].notRecentlyTaken();
			index = random.nextInt(couleursRestantes.size());
			cases[0][size - 1].setCouleur(couleurs[2 * couleursRestantes.get(index) + 1]);
			couleurJoueur[1] = couleursRestantes.get(index);
			couleursRestantes.remove(index);
			cases[0][size - 1].setOwner(2);
			cases[0][size - 1].notRecentlyTaken();
			if (nbreJoueurs > 3) {
				index = random.nextInt(couleursRestantes.size());
				cases[size - 1][0].setCouleur(couleurs[2 * couleursRestantes.get(index) + 1]);
				couleurJoueur[3] = couleursRestantes.get(index);
				couleursRestantes.remove(index);
				cases[size - 1][0].setOwner(4);
				cases[size - 1][0].notRecentlyTaken();
			}
		}
		else {
			couleurJoueur[1] = couleursRestantes.get(index);
			couleursRestantes.remove(index);
			cases[size - 1][size - 1].setOwner(2);
			cases[size - 1][size - 1].notRecentlyTaken();
		}
	}
	
	@Override
	protected void capture(Couleur couleurUpper, Couleur couleurLower, int owner) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!cases[i][j].isRecentlyTaken() && cases[i][j].getOwner() == owner) {
					cases[i][j].setCouleur(couleurUpper);
					if (i > 0) {
						captureCase(couleurUpper, couleurLower, owner, i - 1, j);
					}
					if (j > 0) {
						captureCase(couleurUpper, couleurLower, owner, i, j - 1);
					}
					if (i < size - 1) {
						captureCase(couleurUpper, couleurLower, owner, i + 1, j);
					}
					if (j < size - 1) {
						captureCase(couleurUpper, couleurLower, owner, i, j + 1);
					}
				}
			}
		}
	}
	
	protected int fausseCapture(Couleur couleurUpper, Couleur couleurLower, int owner) {
		int a = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!cases[i][j].isRecentlyTaken() && cases[i][j].getOwner() == owner) {
					if (i > 0) {
						if (testCapturedCases(couleurUpper, couleurLower, i - 1, j)) {
							a++;
						}
					}
					if (j > 0) {
						if (testCapturedCases(couleurUpper, couleurLower, i, j - 1)) {
							a++;
						}
					}
					if (i < size - 1) {
						if (testCapturedCases(couleurUpper, couleurLower, i + 1, j)) {
							a++;
						}
					}
					if (j < size - 1) {
						if (testCapturedCases(couleurUpper, couleurLower, i, j + 1)) {
							a++;
						}
					}
				}
			}
		}
		return a;
	}
	
	protected void noCaseRecentlyTaken() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cases[i][j].notRecentlyTaken();
			}
		}
	}
}