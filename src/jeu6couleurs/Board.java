package jeu6couleurs;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import edu.princeton.cs.introcs.StdDraw;

public abstract class Board {
	
	// attributes
	protected Case[][] cases;
	protected int size;
	protected double sizeBoard;
	protected double tailleInterface = 0;
	protected int nbCases;
	protected int capturedCases;
	protected int[] capturedCasesJoueurs = new int[4];
	protected boolean game = true;
	protected Couleur[] couleurs = Couleur.values();
	protected double inclinaison = 0;
	protected int[] couleurJoueur;
	protected ArrayList<Integer> couleursRestantes;
	protected Color[] colors = {StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE, StdDraw.MAGENTA};

	// methods
	abstract public void jeu(int nbreJoueursHumains, int nbreJoueurs, int difficulty);
	abstract protected void displayBoard();
	abstract protected void firstCases(int nbreJoueurs);
	abstract protected void capture(Couleur couleurUpper, Couleur couleurLower, int owner);
	abstract protected int fausseCapture(Couleur couleurUpper, Couleur couleurLower, int owner);
	abstract protected void noCaseRecentlyTaken();
	
	public Board() {
		couleursRestantes = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			couleursRestantes.add(i);
		}
	}
	
	public void jeu(int nbreJoueursHumains) {
		jeu(nbreJoueursHumains, 0, 0);
	}
	
	protected int validerCouleur(int numeroCouleur, int numeroJoueur) {	
		for (int a : couleurJoueur) {
			if (a == numeroCouleur) {
				JOptionPane.showMessageDialog(null, "Cette couleur est déjà utilisée par un autre joueur!");
				numeroCouleur = validerCouleur(getMouseXY(), numeroJoueur);
			}
		}
		return numeroCouleur;
	}
	
	protected int getMouseXY() {
		double x = 0;
		double y = sizeBoard * (1 + tailleInterface);
		double nombre = (sizeBoard * (1 + tailleInterface + inclinaison) - sizeBoard * 1.2) / 2;
		while (y > sizeBoard * 0.2 || x < nombre || x > sizeBoard * 1.2 + nombre) {
			
			while (!StdDraw.mousePressed()) {}
			x = StdDraw.mouseX();
			y = StdDraw.mouseY();
			while (StdDraw.mousePressed()) {}
		}
		int c = (int) ((x - nombre) / (sizeBoard * 0.2));
		return c;
	}
	
	protected void captureCase(Couleur couleurUpper, Couleur couleurLower, int owner, int i, int j) {
		if (cases[i][j].getOwner() == 0) {
			if (cases[i][j].getCouleur() == couleurLower) {
				capturedCasesJoueurs[owner - 1]++;
				cases[i][j].setCouleur(couleurUpper);
				capturedCases++;
				cases[i][j].setOwner(owner);
			}
			else if (cases[i][j].getCouleur() == couleurUpper) {
				capturedCasesJoueurs[owner - 1]++;
				capturedCases++;
				cases[i][j].setOwner(owner);
			}
		}
	}
	
	protected boolean testCapturedCases(Couleur couleurUpper, Couleur couleurLower, int i, int j) {
		Couleur couleur = cases[i][j].getCouleur();
		if (cases[i][j].getOwner() == 0) {
			if (couleur == couleurLower || couleur == couleurUpper) {
				return true;
			}
		}
		return false;
	}
	
	private int scoreLeader(int nbreJoueurs) {
		int[] capturedCasesJoueurs2 = new int[4];
		
		for (int j = 0; j < 4; j++) {
			capturedCasesJoueurs2[j] = capturedCasesJoueurs[j];
		}
		int a;
		for (int j = 0; j < nbreJoueurs - 1; j++) {
			for (int k = j + 1; k < nbreJoueurs; k++) {
				if (capturedCasesJoueurs2[j] < capturedCasesJoueurs2[k]) {
					a = capturedCasesJoueurs2[j];
					capturedCasesJoueurs2[j] = capturedCasesJoueurs2[k];
					capturedCasesJoueurs2[k] = a;
				}
			}	
		}
		return capturedCasesJoueurs2[0];
	}
	
	protected boolean endGame(int nbreJoueurs, int nbCases) {
		if (capturedCases == nbCases || scoreLeader(nbreJoueurs) > (nbCases / 2)) {
			afficherMessage("FIN DU JEU !!!");
			int[][] scores = new int[2][nbreJoueurs];
			for (int j = 0; j < nbreJoueurs; j++) {
				scores[0][j] = j + 1;
				scores[1][j] = capturedCasesJoueurs[j];
			}
			int var2, var3;
			for (int j = 0; j < nbreJoueurs - 1; j++) {
				for (int k = j + 1; k < nbreJoueurs; k++) {
					if (scores[1][j] < scores[1][k]) {
						var2 = scores[0][j];
						var3 = scores[1][j];
						scores[0][j] = scores[0][k];
						scores[1][j] = scores[1][k];
						scores[0][k] = var2;
						scores[1][k] = var3;
					}
				}	
			}
			String[] positions = {"1er","2ème","3ème","4ème"};
			String str = positions[0] + " : Joueur " + scores[0][0] + " : " + scores[1][0];
			int ex = 0;
			for (int j = 1; j < nbreJoueurs; j++) {
				if (scores[1][j - 1] == scores[1][j]) {
					ex++;
				}
				str += "\n" + positions[j - ex] + " : Joueur " + scores[0][j] + " : " + scores[1][j];
			}
			afficherMessage(str);
			
			game = false;
			return true;
		}
		return false;
	}
	
	protected void afficherMessage(String str) {
		JOptionPane.showMessageDialog(null, str);
	}
	
	protected void displayGUIScores(int numeroJoueur, int nbreJoueurs, double sizeBoard, double tailleInterface) {
		Font fontJoueur = new Font("Arial", Font.PLAIN, 22);
		Font fontScore = new Font("Arial", Font.PLAIN, 20);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledCircle(sizeBoard * (1 + tailleInterface) * 0.1, sizeBoard * (3 + tailleInterface) * 0.1, sizeBoard * 0.07);
		StdDraw.filledCircle(sizeBoard * (1 + tailleInterface) * 0.1, sizeBoard * (1 + tailleInterface) * 0.9, sizeBoard * 0.07);
		StdDraw.filledCircle(sizeBoard * (1 + tailleInterface) * 0.9 + sizeBoard * inclinaison, sizeBoard * (1 + tailleInterface) * 0.9, sizeBoard * 0.07);
		StdDraw.filledCircle(sizeBoard * (1 + tailleInterface) * 0.9 + sizeBoard * inclinaison, sizeBoard * (3 + tailleInterface) * 0.1, sizeBoard * 0.07);
		if (numeroJoueur == nbreJoueurs) {
			StdDraw.setPenColor(StdDraw.RED);
		}
		else {
			StdDraw.setPenColor(StdDraw.BLACK);
		}
		StdDraw.setFont(fontJoueur);
		StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.1, sizeBoard * (3.3 + tailleInterface) * 0.1, "J1:");
		StdDraw.setFont(fontScore);
		StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.1, sizeBoard * (2.8 + tailleInterface) * 0.1, Integer.toString(capturedCasesJoueurs[0]));
		if (numeroJoueur == 1) {
			StdDraw.setPenColor(StdDraw.RED);
		}
		else {
			StdDraw.setPenColor(StdDraw.BLACK);
		}
		if (nbreJoueurs > 2) {
			StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.1, sizeBoard * (0.87 + 0.9 * tailleInterface), Integer.toString(capturedCasesJoueurs[1]));
			StdDraw.setFont(fontJoueur);
			StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.1, sizeBoard * (0.92 + 0.9 * tailleInterface), "J2:");
			if (numeroJoueur == 2) {
				StdDraw.setPenColor(StdDraw.RED);
			}
			else {
				StdDraw.setPenColor(StdDraw.BLACK);
			}
			StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.9 + inclinaison * size, sizeBoard * (0.92 + 0.9 * tailleInterface), "J3:");
			StdDraw.setFont(fontScore);
			StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.9 + inclinaison * size, sizeBoard * (0.87 + 0.9 * tailleInterface), Integer.toString(capturedCasesJoueurs[2]));
			if (nbreJoueurs > 3) {
				if (numeroJoueur == 3) {
					StdDraw.setPenColor(StdDraw.RED);
				}
				else {
					StdDraw.setPenColor(StdDraw.BLACK);
				}
				StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.9 + inclinaison * size, sizeBoard * (2.8 + tailleInterface) * 0.1, Integer.toString(capturedCasesJoueurs[3]));
				StdDraw.setFont(fontJoueur);
				StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.9 + inclinaison * size, sizeBoard * (3.3 + tailleInterface) * 0.1, "J4:");
			}
		}
		else {
			StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.9 + inclinaison * size, sizeBoard * (0.87 + 0.9 * tailleInterface), Integer.toString(capturedCasesJoueurs[1]));
			StdDraw.setFont(fontJoueur);
			StdDraw.text(sizeBoard * (1 + tailleInterface) * 0.9 + inclinaison * size, sizeBoard * (0.92 + 0.9 * tailleInterface), "J2:");
		}
	}
	
	// IAs
	protected int artificialIntelligence1(int nbreJoueurs) {
		Random random = new Random();
		int index = random.nextInt(6 - nbreJoueurs);
		int choix = couleursRestantes.get(index);
		return choix;
	}
	
	protected int[] artificialIntelligence2(int nbreJoueurs, int owner) {
		int i = 0;
		int[][] tab = new int[6 - nbreJoueurs][2];
		Couleur couleurUpper, couleurLower;
		for (int selectedButton : couleursRestantes) {
			couleurUpper = couleurs[2 * selectedButton + 1];
			couleurLower = couleurs[2 * selectedButton];
			tab[i][0] = selectedButton;
			tab[i][1] = fausseCapture(couleurUpper, couleurLower, owner);
			i++;
		}
		int a, b;
		for (int j = 0, len = 6 - nbreJoueurs; j < len - 1; j++) {
			for (int k = j; k < len; k++) {
				if (tab[j][1] < tab[k][1]) {
					a = tab[j][0];
					b = tab[j][1];
					tab[j][0] = tab[k][0];
					tab[j][1] = tab[k][1];
					tab[k][0] = a;
					tab[k][1] = b;
				}
			}
		}
		int[] tab2 = new int[2];
		tab2[0] = tab[0][0];
		tab2[1] = tab[0][1];
		return tab2;
	}
}
