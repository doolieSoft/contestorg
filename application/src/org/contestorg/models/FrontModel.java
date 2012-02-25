package org.contestorg.models;


import org.contestorg.events.History;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelConcours;

/**
 * Point d'entrée principal aux modèles
 */
public class FrontModel
{
	/** Instance unique du point d'entrée principal aux modèles */
	private static FrontModel front;
	
	/** Concours */
	private ModelConcours concours;
	
	/** Historique */
	private History history;
	
	// Points d'entrée aux modèles
	
	/** Point d'entrée aux modèles concernant la configuration */
	private FrontModelConfiguration frontModelConfiguration;

	/** Point d'entrée aux modèles concernant les participants */
	private FrontModelParticipants frontModelParticipants;

	/** Point d'entrée aux modèles concernant les phases qualificatives */
	private FrontModelPhasesQualificatives frontModelPhasesQualificatives;

	/** Point d'entrée aux modèles concernant les phases éliminatoires */
	private FrontModelPhasesEliminatoires frontModelPhasesEliminatoires;
	
	/**
	 * Récupérer l'instance du point d'entrée principal aux modèles
	 * @return instance du point d'entrée principal aux modèles
	 */
	public static FrontModel get () {
		// Créer l'instance du FrontModel si nécéssaire
		if (FrontModel.front == null) {
			// Créer l'instance du FrontModel
			FrontModel.front = new FrontModel();
		}
		
		// Retourner l'instance de Front
		return FrontModel.front;
	}
	
	/**
	 * Constructeur
	 */
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
	
	/**
	 * Récupérer l'historique
	 * @return historique
	 */
	public History getHistory () {
		return this.history;
	}
	
	/**
	 * Récupérer le concours
	 * @return concours
	 */
	public ModelConcours getConcours () {
		return this.concours;
	}

	/**
	 * Nettoyer les modèles
	 */
	public void clean() {
		// Réinitialiser l'historique
		this.history.init();
		
		// Nettoyer les propriétés statiques de ModelAbstract
		ModelAbstract.clean();
	}
	
	// Récupérer les points d'entrée aux modèles
	
	/**
	 * Récupérer le point d'entrée aux modèles concernant la configuration
	 * @return point d'entrée aux modèles concernant la configuration
	 */
	public FrontModelConfiguration getFrontModelConfiguration () {
		return frontModelConfiguration;
	}
	
	/**
	 * Récupérer le point d'entrée aux modèles concernant les participants
	 * @return point d'entrée aux modèles concernant les participants
	 */
	public FrontModelParticipants getFrontModelParticipants () {
		return frontModelParticipants;
	}
	
	/**
	 * Récupérer le point d'entrée aux modèles concernant les phases qualificatives
	 * @return point d'entrée aux modèles concernant les phases qualificatives
	 */
	public FrontModelPhasesQualificatives getFrontModelPhasesQualificatives () {
		return frontModelPhasesQualificatives;
	}
	
	/**
	 * Récupérer le point d'entrée aux modèles concernant les phases éliminatoires
	 * @return point d'entrée aux modèles concernant les phases éliminatoires
	 */
	public FrontModelPhasesEliminatoires getFrontModelPhasesEliminatoires () {
		return frontModelPhasesEliminatoires;
	}
		
	// Créer/Fermer un concours
	
	/**
	 * Créer un concours
	 * @param concours concours
	 * @throws ContestOrgModelException
	 */
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
	
	/**
	 * Créer un concours
	 * @param infos informations du concours
	 * @throws ContestOrgModelException
	 */
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
	
	/**
	 * Fermer le concours
	 * @throws ContestOrgModelException
	 */
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
