package jeu6couleurs;

public class Runner {

	public static void main(String[] args) {
//		Board board = new ConsoleBoard(13);
//		Board board = new GUIBoard(13);
//		Board board = new GUIBoard(13, 0.4);
		Board board = new HexagonBoard(10);
		board.jeu(0, 3, 2);
//		board.jeu(3);
	}
}
