package org.contestorg.views;

import javax.swing.JPanel;

import org.contestorg.infos.InfosThemeParametre;

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
	public static JPParametreAbstract create (InfosThemeParametre parametre) {
		switch(parametre.getType()) {
			case InfosThemeParametre.TYPE_ENTIER: return new JPParametreEntier(parametre);
			case InfosThemeParametre.TYPE_REEL: return new JPParametreReel(parametre);
			case InfosThemeParametre.TYPE_TEXTE: return new JPParametreTexte(parametre);
			case InfosThemeParametre.TYPE_CATEGORIE: return new JPParametreCategorie(parametre);
			case InfosThemeParametre.TYPE_POULE: return new JPParametrePoule(parametre);
			case InfosThemeParametre.TYPE_PHASE: return new JPParametrePhase(parametre);
			case InfosThemeParametre.TYPE_MOTDEPASSE: return new JPParametreMotDePasse(parametre);
			case InfosThemeParametre.TYPE_BOOLEEN: return new JPParametreBooleen(parametre);
		}
		return null;
	}
	
	/** Parametre associé au panel */
	protected InfosThemeParametre parametre;
	
	/**
	 * Constructeur
	 * @param parametre paramètre
	 */
	public JPParametreAbstract(InfosThemeParametre parametre) {
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
