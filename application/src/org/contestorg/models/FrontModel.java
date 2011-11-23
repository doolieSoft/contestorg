package org.contestorg.models;


import org.contestorg.events.History;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelConcours;

/**
 * Point d'entrée principal aux modèles
 */
public class FrontModel
{
	// Instance unique du point d'entrée principal aux modèles
	private static FrontModel front;
	
	// Concours
	private ModelConcours concours;
	
	// Historique
	private History history;
	
	// Points d'entrée aux modèles
	private FrontModelConfiguration frontModelConfiguration;
	private FrontModelParticipants frontModelParticipants;
	private FrontModelPhasesQualificatives frontModelPhasesQualificatives;
	private FrontModelPhasesEliminatoires frontModelPhasesEliminatoires;
	
	// Récupérer l'instance du point d'entrée principal aux modèles
	public static FrontModel get () {
		// Créer l'instance du FrontModel si nécéssaire
		if (FrontModel.front == null) {
			// Créer l'instance du FrontModel
			FrontModel.front = new FrontModel();
		}
		
		// Retourner l'instance de Front
		return FrontModel.front;
	}
	
	// Constructeur
	private FrontModel() {
		// Créer l'historique et l'abonner aux évenements du concours
		this.history = new History();
		ModelAbstract.addStaticListener(this.history);
		
		// Créer les points d'entrée aux modèles
		this.frontModelConfiguration = new FrontModelConfiguration(this);
		this.frontModelParticipants = new FrontModelParticipants(this);
		this.frontModelPhasesQualificatives = new FrontModelPhasesQualificatives(this);
		this.frontModelPhasesEliminatoires = new FrontModelPhasesEliminatoires(this);
	}
	
	// Récupérer l'historique
	public History getHistory () {
		return this.history;
	}
	
	// Récupérer le concours
	public ModelConcours getConcours () {
		return this.concours;
	}
	
	// Récupérer les points d'entrée aux modèles
	public FrontModelConfiguration getFrontModelConfiguration () {
		return frontModelConfiguration;
	}
	public FrontModelParticipants getFrontModelParticipants () {
		return frontModelParticipants;
	}
	public FrontModelPhasesQualificatives getFrontModelPhasesQualificatives () {
		return frontModelPhasesQualificatives;
	}
	public FrontModelPhasesEliminatoires getFrontModelPhasesEliminatoires () {
		return frontModelPhasesEliminatoires;
	}

	// Nettoyer les modèles
	public void clean() {
		// Réinitialiser l'historique
		this.history.init();
		
		// Nettoyer les propriétés statiques de ModelAbstract
		ModelAbstract.clean();
	}
		
	// Créer/Fermer un concours
	public void nouveauConcours (ModelConcours concours) throws ContestOrgModelException {
		// Démarrer l'action de création
		this.history.start("Chargement du concours");
		
		// Retenir le nouveau concours
		this.concours = concours;
		
		// Fermer l'action de création
		this.history.close();
		
		// Initialiser l'historique
		this.history.init();
	}
	public void nouveauConcours (InfosModelConcours infos) throws ContestOrgModelException {
		// Démarrer l'action de création
		this.history.start("Création du concours");
		
		// Créer et retenir le nouveau concours
		this.concours = new ModelConcours(infos);
		
		// Créer la catégorie et la poule par défaut
		this.concours.addCategorie(new ModelCategorie.UpdaterForConcours(this.concours).create(new InfosModelCategorie("Défaut")));
		
		// Fermer l'action de création
		this.history.close();
		
		// Initialiser l'historique
		this.history.init();
	}
	public void closeConcours () throws ContestOrgModelException {
		if (this.concours != null) {
			// Vider le concours et en perdre la référence
			this.history.start("Fermeture du concours");
			this.concours.delete();
			this.concours = null;
			this.history.close();
		}
		
		// Nettoyer les modèles
		this.clean();
	}
	
}
