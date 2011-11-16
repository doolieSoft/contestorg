package org.contestorg.infos;

public class InfosModelCompPhasesQualifsObjectif extends InfosModelCompPhasesQualifsAbstract
{
	
	// Note : l'utilisation des informations n'a d'interet que pour faciliter l'information entre la fenetre de configuration du concours et le frontmodel
	
	// Objectif associé
	private InfosModelObjectif objectif;
	
	// Constructeur
	public InfosModelCompPhasesQualifsObjectif(InfosModelObjectif objectif) {
		this.objectif = objectif;
	}
	
	// Récupérer les informations de l'objectif associé
	public InfosModelObjectif getObjectif() {
		return this.objectif;
	}
	
}
