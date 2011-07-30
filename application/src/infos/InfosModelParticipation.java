package infos;

/**
 * Cette classe est un conteneur d'information pour la création ou la modification d'un objet participation
 */
public class InfosModelParticipation extends InfosModelAbstract
{

	// Attributs
	private int resultat;

	// Constantes
	public static final int RESULTAT_ATTENTE = 0;
	public static final int RESULTAT_VICTOIRE = 1;
	public static final int RESULTAT_EGALITE = 2;
	public static final int RESULTAT_DEFAITE = 3;
	public static final int RESULTAT_FORFAIT = 4;

	// Constructeur
	public InfosModelParticipation(int resultat) {
		this.resultat = resultat;
	}

	// Getters
	public int getResultat () {
		return resultat;
	}

	// Informations par défaut
	public static InfosModelParticipation defaut () {
		return new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE);
	}

}
