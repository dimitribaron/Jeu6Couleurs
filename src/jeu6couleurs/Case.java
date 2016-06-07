package jeu6couleurs;

public class Case {
	
	private Couleur couleur;
	private int owner = 0;
	private boolean recentlyTaken = false;
	private boolean recentlyChanged = false;
	
	public Couleur getCouleur() {
		return couleur;
	}
	public void setCouleur(Couleur couleur) {
		this.couleur = couleur;
		recentlyChanged = true;
	}
	public void setCouleurFirstTime(Couleur couleur) {
		this.couleur = couleur;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
		recentlyTaken = true;
	}
	public boolean isRecentlyTaken() {
		return recentlyTaken;
	}
	public void notRecentlyTaken() {
		this.recentlyTaken = false;
	}
	public boolean isRecentlyChanged() {
		return recentlyChanged;
	}
	public void notRecentlyChanged() {
		this.recentlyChanged = false;
	}
}
