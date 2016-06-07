package jeu6couleurs;

import edu.princeton.cs.introcs.StdDraw;

public class GUIBoard extends GridBoard {
	
	// constructors
	public GUIBoard(int size) {
		super(size);
		this.sizeBoard = size;
		this.tailleInterface = 0.45;
	}
	
	public GUIBoard(int size, double inclinaison) {
		super(size);
		if (inclinaison < 0) {
			throw new IllegalArgumentException("Rentrez une valeur positive.");
		}
		else if (inclinaison > 3) {
			throw new IllegalArgumentException("Inclinaison trop élevée.");
		}
		this.sizeBoard = size;
		this.tailleInterface = 0.45;
		this.inclinaison = inclinaison;
	}
	
	// methods
	@Override
	public void jeu(int nbreJoueursHumains, int nbreJoueursIA, int difficulty) {
		
		int nbreJoueurs = nbreJoueursHumains + nbreJoueursIA;
		
		if (nbreJoueurs < 2 || 4 < nbreJoueurs) {
			throw new IllegalArgumentException("Il doit y avoir entre 2 et 4 joueurs par partie.");
		}
		
		capturedCases = nbreJoueurs;
		for (int i = 0; i < nbreJoueurs; i++) {
			capturedCasesJoueurs[i] = 1;
		}
		
		couleurJoueur = new int[nbreJoueurs];
		
		firstCases(nbreJoueurs);
		
		int index;
		
		for (int couleur : couleurJoueur) {
			index = couleursRestantes.indexOf(couleur);
			couleursRestantes.remove(index);
		}
		
		displayBoard();
		displayGUIScores(nbreJoueurs, nbreJoueurs, size, tailleInterface);
		for (int i : couleurJoueur) {
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.filledCircle(i * size * 0.2 + size * (tailleInterface + inclinaison) * 0.5, size * 0.1, size * 0.1);
		}
		
		int selectedButton;
		Couleur couleurUpper, couleurLower;
		
		while (game) {
			
			for (int i = 1; i <= nbreJoueurs; i++) {
				if (i <= nbreJoueursHumains) {
					selectedButton = getMouseXY();
					selectedButton = validerCouleur(selectedButton, i);
				}
				else {
					switch (difficulty) {
					case 1:
						selectedButton = artificialIntelligence1(nbreJoueurs);
						break;
					case 2:
						selectedButton = artificialIntelligence2(nbreJoueurs, i)[0];
						break;
					default:
						selectedButton = artificialIntelligence1(nbreJoueurs);
						break;
					}
				}
				couleursRestantes.add(couleurJoueur[i - 1]);
				index = couleursRestantes.indexOf(selectedButton);
				couleursRestantes.remove(index);
				couleurJoueur[i - 1] = selectedButton;
				couleurUpper = couleurs[2 * selectedButton + 1];
				couleurLower = couleurs[2 * selectedButton];
				capture(couleurUpper, couleurLower, i);		
				displayChangedCases();
				for (int k = 0; k < 6; k++) {
					StdDraw.setPenColor(colors[k]);
					StdDraw.filledSquare(k * size * 0.2 + size * (tailleInterface + inclinaison) * 0.5, size * 0.1, size * 0.1);
				}
				for (int k : couleurJoueur) {
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.filledCircle(k * size * 0.2 + size * (tailleInterface + inclinaison) * 0.5, size * 0.1, size * 0.1);
				}
				noCaseRecentlyTaken();
				displayGUIScores(i, nbreJoueurs, size, tailleInterface);
				
				if (endGame(nbreJoueurs, nbCases)) {
					i = 5;
				}
			}
		}
	}
	
	protected void displayBoard() {
		StdDraw.setXscale(0, size * (1 + tailleInterface + inclinaison));
		StdDraw.setYscale(0, size * (1 + tailleInterface));
		
		for (int i = 0; i < 6; i++) {
			StdDraw.setPenColor(colors[i]);
			StdDraw.filledSquare(i * size * 0.2 + size * (tailleInterface + inclinaison) * 0.5, size * 0.1, size * 0.1);
		}
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				drawCases(i, j);
			}
		}
	}
	
	private void displayChangedCases() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (cases[i][j].isRecentlyChanged() || cases[i][j].isRecentlyTaken()) {
					cases[i][j].notRecentlyChanged();
					drawCases(i, j);
				}
			}
		}
	}
	
	private void drawCases(int i, int j) {
		double[] x = {0, inclinaison, 1 + inclinaison, 1};
		double[] y = {0, 1, 1, 0};
		for (int k = 0; k < 4; k++) {
			x[k] += size * tailleInterface * 0.5;
			y[k] += size * tailleInterface;
		}
		for (int k = 0; k < 4; k++) {
			x[k] += i + j * inclinaison;
			y[k] += j;
		}
		switch (cases[i][j].getCouleur()) {
		case r:
		case R:
			StdDraw.setPenColor(StdDraw.RED);
			break;
		case o:
		case O:
			StdDraw.setPenColor(StdDraw.ORANGE);
			break;
		case j:
		case J:
			StdDraw.setPenColor(StdDraw.YELLOW);
			break;
		case v:
		case V:
			StdDraw.setPenColor(StdDraw.GREEN);
			break;
		case b:
		case B:
			StdDraw.setPenColor(StdDraw.BLUE);
			break;
		case i:
		case I:
			StdDraw.setPenColor(StdDraw.MAGENTA);
			break;
		default:
			break;
		}
		StdDraw.filledPolygon(x, y);
		
		double m1 = (x[0] + x[2]) / 2;
		double m2 = (y[0] + y[2]) / 2;
		
		StdDraw.setFont();
		StdDraw.setPenColor(StdDraw.BLACK);
		switch (cases[i][j].getOwner()) {
		case 1:
			StdDraw.text(m1, m2, "1");
			break;
		case 2:
			StdDraw.text(m1, m2, "2");
			break;
		case 3:
			StdDraw.text(m1, m2, "3");
			break;
		case 4:
			StdDraw.text(m1, m2, "4");
			break;
		default:
			break;
		}
	}
}