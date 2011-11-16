package views;

import infos.Parametre;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class JCParametreAbstract extends JPanel
{
	// Constructeur statique
	public static JCParametreAbstract create (Parametre parametre) {
		switch(parametre.getType()) {
			case Parametre.TYPE_ENTIER: return new JCParametreEntier(parametre);
			case Parametre.TYPE_REEL: return new JCParametreReel(parametre);
			case Parametre.TYPE_TEXTE: return new JCParametreTexte(parametre);
			case Parametre.TYPE_CATEGORIE: return new JCParametreCategorie(parametre);
			case Parametre.TYPE_POULE: return new JCParametrePoule(parametre);
			case Parametre.TYPE_PHASE: return new JCParametrePhase(parametre);
			case Parametre.TYPE_MOTDEPASSE: return new JCParametreMotDePasse(parametre);
		}
		return null;
	}
	
	// Parametre associé au composant
	protected Parametre parametre;
	
	// Constructeur
	public JCParametreAbstract(Parametre parametre) {
		// Retenir le parametre
		this.parametre = parametre;
	}
	
	// Récupérer la valeur du paramètre
	public abstract String getValeur();
	
	// Définir la valeur du paramètre
	public abstract void setValeur(String valeur);
	
	// Récupérer une erreur éventuelle
	public abstract String getError();
	
	// Informer de la liste des autres composants
	public abstract void link(JCParametreAbstract[] composants);
	
	// Savoir si le composant est dépendant d'un autre
	public boolean isDependant(JCParametreAbstract composant) {
		return this.parametre.getDependance().equals(composant.parametre.getId());
	}
}
