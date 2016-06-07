package jeu6couleurs;

import java.util.Scanner;

public class ConsoleBoard extends GridBoard {

	// constructor
	public ConsoleBoard(int size) {
		super(size);
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
		displayConsoleScores(nbreJoueurs);
		
		Scanner scan = new Scanner(System.in);
		
		char c;
		int selectedButton;
		Couleur couleurUpper, couleurLower;
		
		while (game) {
			
			for (int i = 1; i <= nbreJoueurs; i++) {
				if (i <= nbreJoueursHumains) {
					System.out.println("JOUEUR " + i + ":");
					c = scan.nextLine().charAt(0);
					c = verifCouleur(c, i, scan);
					couleurUpper = convertUpperColor(c);
					couleurLower = convertLowerColor(c);
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
					couleursRestantes.add(couleurJoueur[i - 1]);
					index = couleursRestantes.indexOf(selectedButton);
					couleursRestantes.remove(index);
					couleurJoueur[i - 1] = selectedButton;
					couleurUpper = couleurs[2 * selectedButton + 1];
					couleurLower = couleurs[2 * selectedButton];
				}
				capture(couleurUpper, couleurLower, i);
				noCaseRecentlyTaken();
				displayBoard();
				displayConsoleScores(nbreJoueurs);
				
				if (endGame(nbreJoueurs, nbCases)) {
					i = 5;
				}
			}
		}
		scan.close();
	}
	
	@Override
	protected void displayBoard() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(cases[i][j].getCouleur() + "" + cases[i][j].getOwner() + " ");
			}
			System.out.println();
		}
	}
	
	private char verifCouleur(char c, int numeroJoueur, Scanner scan) {
		if (c == 'r' || c == 'R' || c == 'o' || c == 'O' || c == 'j' || c == 'J' || c == 'v' || c == 'V' || c == 'b' || c == 'B' || c == 'i' || c == 'I') {
			int i = convertColorInt(c);
			for (int j : couleurJoueur) {
				if (i == j) {
					System.err.println("Cette couleur est déjà utilisée par un autre joueur!");
					c = scan.nextLine().charAt(0);
					return verifCouleur(c,numeroJoueur, scan);
				}
			}
			couleurJoueur[numeroJoueur - 1] = i;
			return c;
		} else {
			System.err.println("Les couleurs possibles sont: rouge ('r'), orange ('o'), jaune ('j'), vert ('v'), bleu ('b') et violet ('i').");
			c = scan.nextLine().charAt(0);
			return verifCouleur(c, numeroJoueur, scan);
		}
	}
	
	private Couleur convertUpperColor(char couleur) {
		switch (couleur) {
		case 'r':
		case 'R':
			return Couleur.R;
		case 'o':
		case 'O':
			return Couleur.O;
		case 'j':
		case 'J':
			return Couleur.J;
		case 'v':
		case 'V':
			return Couleur.V;
		case 'b':
		case 'B':
			return Couleur.B;
		case 'i':
		case 'I':
			return Couleur.I;
		default:
			return null;
		}
	}
	
	private Couleur convertLowerColor(char couleur) {
		switch (couleur) {
		case 'r':
		case 'R':
			return Couleur.r;
		case 'o':
		case 'O':
			return Couleur.o;
		case 'j':
		case 'J':
			return Couleur.j;
		case 'v':
		case 'V':
			return Couleur.v;
		case 'b':
		case 'B':
			return Couleur.b;
		case 'i':
		case 'I':
			return Couleur.i;
		default:
			return null;
		}
	}
	
	private int convertColorInt(char couleur) {
		switch (couleur) {
		case 'r':
		case 'R':
			return 0;
		case 'o':
		case 'O':
			return 1;
		case 'j':
		case 'J':
			return 2;
		case 'v':
		case 'V':
			return 3;
		case 'b':
		case 'B':
			return 4;
		case 'i':
		case 'I':
			return 5;
		default:
			return 6;
		}
	}
	
	private void displayConsoleScores(int nbreJoueurs) {
		System.out.println("Cases contrôlées: " + capturedCases + "/" + nbCases);
		for (int i = 1; i <= nbreJoueurs; i++) {
			System.out.println("Joueur" + i + " : " + capturedCasesJoueurs[i - 1]);
		}
	}
	
	@Override
	protected void afficherMessage(String str) {
		System.out.println(str);
	}
}
