package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'une participation
 */
public class InfosModelParticipation extends InfosModelAbstract
{

	/** Résultat */
	private int resultat;

	// Résultats
	
	/** Attente */
	public static final int RESULTAT_ATTENTE = 0;
	
	/** Victoire */
	public static final int RESULTAT_VICTOIRE = 1;
	
	/** Egalité */
	public static final int RESULTAT_EGALITE = 2;
	
	/** Défaite */
	public static final int RESULTAT_DEFAITE = 3;
	
	/** Forfait */
	public static final int RESULTAT_FORFAIT = 4;

	/**
	 * Constructeur
	 * @param resultat résultat
	 */
	public InfosModelParticipation(int resultat) {
		this.resultat = resultat;
	}

	/**
	 * Récupérer le résultat
	 * @return résultat
	 */
	public int getResultat () {
		return resultat;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelParticipation defaut () {
		return new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE);
	}

}
