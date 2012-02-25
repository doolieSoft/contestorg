package org.contestorg.views;

import javax.swing.JPanel;

import org.contestorg.infos.Parametre;

/**
 * Panel d'un paramètre de thème d'exportation/diffusion
 */
@SuppressWarnings("serial")
public abstract class JPParametreAbstract extends JPanel
{
	/**
	 * Constructeur statique
	 * @param parametre paramètre
	 * @return panel du paramètre
	 */
	public static JPParametreAbstract create (Parametre parametre) {
		switch(parametre.getType()) {
			case Parametre.TYPE_ENTIER: return new JPParametreEntier(parametre);
			case Parametre.TYPE_REEL: return new JPParametreReel(parametre);
			case Parametre.TYPE_TEXTE: return new JPParametreTexte(parametre);
			case Parametre.TYPE_CATEGORIE: return new JPParametreCategorie(parametre);
			case Parametre.TYPE_POULE: return new JPParametrePoule(parametre);
			case Parametre.TYPE_PHASE: return new JPParametrePhase(parametre);
			case Parametre.TYPE_MOTDEPASSE: return new JPParametreMotDePasse(parametre);
		}
		return null;
	}
	
	/** Parametre associé au panel */
	protected Parametre parametre;
	
	/**
	 * Constructeur
	 * @param parametre paramètre
	 */
	public JPParametreAbstract(Parametre parametre) {
		// Retenir le parametre
		this.parametre = parametre;
	}
	
	/**
	 * Récupérer la valeur du paramètre
	 * @return valeur du paramètre
	 */
	public abstract String getValeur();
	
	/**
	 * Définir la valeur du paramètre
	 * @param valeur valeur du paramètre
	 */
	public abstract void setValeur(String valeur);
	
	/**
	 * Récupérer une erreur éventuelle
	 * @return erreur éventuelle
	 */
	public abstract String getError();
	
	/**
	 * Informer de la liste des autres panels
	 * @param panels informer la liste des autres panels
	 */
	public abstract void link(JPParametreAbstract[] panels);
	
	/**
	 * Savoir si le panel est dépendant d'un autre panel
	 * @param panel panel
	 * @return panel dépendant du panel ?
	 */
	public boolean isDependant(JPParametreAbstract panel) {
		return this.parametre.getDependance().equals(panel.parametre.getId());
	}
}
