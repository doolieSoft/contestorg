package org.contestorg.models;


import java.util.ArrayList;
import java.util.Collections;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.comparators.CompPhasesQualifs;
import org.contestorg.comparators.CompPhasesQualifsNbObjectifs;
import org.contestorg.comparators.CompPhasesQualifsNbPoints;
import org.contestorg.comparators.CompPhasesQualifsNbVictoires;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelTheme;


/**
 * Concours
 */
public class ModelConcours extends ModelAbstract
{
	
	// Attributs objets
	
	/** Publication */
	private ModelExportation publication;
	
	/** Liste des lieux */
	private ArrayList<ModelLieu> lieux = new ArrayList<ModelLieu>();
	
	/** Liste des objectifs */
	private ArrayList<ModelObjectif> objectifs = new ArrayList<ModelObjectif>();
	
	/** Liste de prix */
	private ArrayList<ModelPrix> prix = new ArrayList<ModelPrix>();
	
	/** Liste des catégories */
	private ArrayList<ModelCategorie> categories = new ArrayList<ModelCategorie>();
	
	/** Liste des propriétés d'équipe */
	private ArrayList<ModelPropriete> proprietes = new ArrayList<ModelPropriete>();
	
	/** Liste des exportations */
	private ArrayList<ModelExportation> exportations = new ArrayList<ModelExportation>();
	
	/** Liste des diffusions */
	private ArrayList<ModelDiffusion> diffusions = new ArrayList<ModelDiffusion>();
	
	/** Liste des critères de classement des phases qualificatives */
	private ArrayList<ModelCompPhasesQualifsAbstract> compsPhasesQualifs = new ArrayList<ModelCompPhasesQualifsAbstract>();
	
	// Attributs scalaires
	
	// Informations sur le concours
	
	/** Nom du concours */
	private String concoursNom;
	/** Site du concours */
	private String concoursSite;
	/** Lieu du concours */
	private String concoursLieu;
	/** Email du concours */
	private String concoursEmail;
	/** Téléphone du concours */
	private String concoursTelephone;
	/** Description du concours */
	private String concoursDescription;
	
	// Informations sur l'organisateur
	
	/** Nom de l'organisateur */
	private String organisateurNom;
	/** Site de l'organisateur */
	private String organisateurSite;
	/** Lieu de l'organisateur */
	private String organisateurLieu;
	/** Email de l'organisateur */
	private String organisateurEmail;
	/** Téléphone de l'organisateur */
	private String organisateurTelephone;
	/** Description de l'organisateur */
	private String organisateurDescription;
	
	// Informations diverses
	
	/** Type de qualifications */
	private int typePhasesQualificatives;
	/** Type de participants */
	private int typeParticipants;
	
	// Informations sur les points
	
	/** Points de victoire */
	private double pointsVictoire;
	/** Points d'égalité */
	private double pointsEgalite;
	/** Points de défaite */
	private double pointsDefaite;
	
	// Informations de programmation
	
	/** Durée d'un match (en minutes) */
	private Double programmationDuree;
	/** Interval minimal entre deux matchs (en minutes) */
	private Double programmationInterval;
	/** Pause minimal d'un participant entre deux matchs (en minutes) */
	private Double programmationPause;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param infos informations du concours
	 */
	public ModelConcours(InfosModelConcours infos) {
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param concours concours
	 */
	protected ModelConcours(ModelConcours concours) {
		// Appeller le constructeur principal
		this(concours.getInfos());
		
		// Récupérer l'id
		this.setId(concours.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer le nom du concours
	 * @return nom du concours
	 */
	public String getConcoursNom () {
		return this.concoursNom;
	}
	
	/**
	 * Récupérer le site du concours
	 * @return site du concours
	 */
	public String getConcoursSite () {
		return this.concoursSite;
	}
	
	/**
	 * Récupérer le lieu du concours
	 * @return lieu du concours
	 */
	public String getConcoursLieu () {
		return this.concoursLieu;
	}
	
	/**
	 * Récupérer l'email du concours
	 * @return email du concours
	 */
	public String getConcoursEmail () {
		return this.concoursEmail;
	}
	
	/**
	 * Récupérer le téléphone du concours
	 * @return téléphone du concours
	 */
	public String getConcoursTelephone () {
		return this.concoursTelephone;
	}
	
	/**
	 * Récupérer la description du concours
	 * @return description du concours
	 */
	public String getConcoursDescription () {
		return this.concoursDescription;
	}
	
	/**
	 * Récupérer le nom de l'organisateur
	 * @return nom de l'organisateur 
	 */
	public String getOrganisateurNom () {
		return this.organisateurNom;
	}
	
	/**
	 * Récupérer le site de l'organisateur
	 * @return site de l'organisateur
	 */
	public String getOrganisateurSite () {
		return this.organisateurSite;
	}
	
	/**
	 * Récupérer le lieu de l'organisateur
	 * @return lieu de l'organisateur
	 */
	public String getOrganisateurLieu () {
		return this.organisateurLieu;
	}
	
	/**
	 * Récupérer l'email de l'organisateur
	 * @return email de l'organisateur
	 */
	public String getOrganisateurEmail () {
		return this.organisateurEmail;
	}
	
	/**
	 * Récupérer le téléphone de l'organisateur
	 * @return téléphone de l'organisateur
	 */
	public String getOrganisateurTelephone () {
		return this.organisateurTelephone;
	}
	
	/**
	 * Récupérer la description de l'organisateur
	 * @return description de l'organisateur
	 */
	public String getOrganisateurDescription () {
		return this.organisateurDescription;
	}
	
	/**
	 * Récupérer le type des phases qualificatives
	 * @return type des phases qualificatives
	 */
	public int getTypePhasesQualificatives () {
		return this.typePhasesQualificatives;
	}
	
	/**
	 * Récupérer le type des participants
	 * @return type des participants
	 */
	public int getTypeParticipants () {
		return this.typeParticipants;
	}
	
	/**
	 * Récupérer les points de victoire
	 * @return points de victoire
	 */
	public double getPointsVictoire () {
		return this.pointsVictoire;
	}
	
	/**
	 * Récupérer les points d'égalité
	 * @return points d'égalité
	 */
	public double getPointsEgalite () {
		return this.pointsEgalite;
	}
	
	/**
	 * Récupérer les points de défaite
	 * @return points de défaite
	 */
	public double getPointsDefaite () {
		return this.pointsDefaite;
	}
	
	/**
	 * Récupérer la durée d'un match (en minutes)
	 * @return durée d'un match (en minutes)
	 */
	public Double getProgrammationDuree () {
		return this.programmationDuree;
	}
	
	/**
	 * Récupérer l'interval minimal entre deux matchs (en minutes)
	 * @return interval minimal entre deux matchs (en minutes)
	 */
	public Double getProgrammationInterval () {
		return this.programmationInterval;
	}
	
	/**
	 * Récupérer la pause minimal d'un participant entre deux matchs (en minutes)
	 * @return pause minimal d'un participant entre deux matchs (en minutes)
	 */
	public Double getProgrammationPause () {
		return this.programmationPause;
	}
	
	/**
	 * Récupérer la publication
	 * @return publication
	 */
	public ModelExportation getPublication () {
		return this.publication;
	}
	
	/**
	 * Récupérer la liste des lieux
	 * @return liste des lieux
	 */
	public ArrayList<ModelLieu> getLieux () {
		return this.lieux;
	}
	
	/**
	 * Récupérer les critères de classement des phases qualificatives
	 * @return critères de classement des phases qualificatives
	 */
	public CompPhasesQualifs getComparateurPhasesQualificatives () {
		return this.getComparateurPhasesQualificatives(-1);
	}
	
	/**
	 * Récupérer les critères de classement des phases qualificatives
	 * @param phaseQualifMax numéro de la phase qualificative maximale (-1 pour toutes les phases qualificatives)
	 * @return critères de classement des phases qualificatives
	 */
	public CompPhasesQualifs getComparateurPhasesQualificatives (int phaseQualifMax) {
		// Récupérer une copie des comparateurs
		ArrayList<ModelCompPhasesQualifsAbstract> compsPhasesQualifs = new ArrayList<ModelCompPhasesQualifsAbstract>(this.compsPhasesQualifs);
		
		// Renverser l'ordre des comparateurs
		Collections.reverse(compsPhasesQualifs);
		
		// Construire le comparateur
		CompPhasesQualifs result = null;
		for (ModelCompPhasesQualifsAbstract comparateur : compsPhasesQualifs) {
			if (comparateur instanceof ModelCompPhasesQualifsPoints) {
				result = new CompPhasesQualifsNbPoints(result, phaseQualifMax);
			} else if (comparateur instanceof ModelCompPhasesQualifsVictoires) {
				result = new CompPhasesQualifsNbVictoires(result, phaseQualifMax);
			} else if (comparateur instanceof ModelCompPhasesQualifsObjectif) {
				result = new CompPhasesQualifsNbObjectifs(result, phaseQualifMax, ((ModelCompPhasesQualifsObjectif)comparateur).getObjectif());
			}
		}
		
		// Retourner le comparacteur
		return result;
	}
	
	/**
	 * Récupérer la liste des objectifs
	 * @return liste des objectifs
	 */
	public ArrayList<ModelObjectif> getObjectifs () {
		return new ArrayList<ModelObjectif>(this.objectifs);
	}
	
	/**
	 * Récupérer un objectif d'après son nom
	 * @param nom nom de l'objectif
	 * @return objectif trouvé
	 */
	public ModelObjectif getObjectifByNom (String nom) {
		for (ModelObjectif objectif : this.objectifs) {
			if (objectif.getNom().equals(nom)) {
				return objectif;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer la liste des prix
	 * @return liste des prix
	 */
	public ArrayList<ModelPrix> getPrix () {
		return new ArrayList<ModelPrix>(this.prix);
	}
	
	/**
	 * Récupérer un prix d'après son nom
	 * @param nom nom du prix
	 * @return prix trouvé
	 */
	public ModelPrix getPrixByNom (String nom) {
		for (ModelPrix prix : this.prix) {
			if (prix.getNom().equals(nom)) {
				return prix;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer la liste des catégories
	 * @return liste des catégories
	 */
	public ArrayList<ModelCategorie> getCategories () {
		return new ArrayList<ModelCategorie>(this.categories);
	}
	
	/**
	 * Récupérer une catégorie d'après son nom
	 * @param nom nom de la catégorie
	 * @return catégorie trouvée
	 */
	public ModelCategorie getCategorieByNom (String nom) {
		for (ModelCategorie categorie : this.categories) {
			if (categorie.getNom().equals(nom)) {
				return categorie;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer la liste des matchs des phases qualificatives
	 * @return liste des matchs des phases qualificatives
	 */
	public ArrayList<ModelMatchPhasesQualifs> getMatchsPhasesQualifs () {
		ArrayList<ModelMatchPhasesQualifs> matchs = new ArrayList<ModelMatchPhasesQualifs>();
		for (ModelCategorie categorie : this.categories) {
			matchs.addAll(categorie.getMatchsPhasesQualifs());
		}
		return matchs;
	}
	
	/**
	 * Récupérer la liste des exportations
	 * @return liste des exportations
	 */
	public ArrayList<ModelExportation> getExportations () {
		return new ArrayList<ModelExportation>(this.exportations);
	}
	
	/**
	 * Récupérer une exportation d'après son nom
	 * @param nom nom de l'exportation
	 * @return exportation trouvée
	 */
	public ModelExportation getExportationByNom (String nom) {
		for (ModelExportation exportation : this.exportations) {
			if (exportation.getNom().equals(nom)) {
				return exportation;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer la liste des propriétés
	 * @return liste des propriétés
	 */
	public ArrayList<ModelPropriete> getProprietes () {
		return new ArrayList<ModelPropriete>(this.proprietes);
	}
	
	/**
	 * Récupérer une propriété d'après son nom
	 * @param nom nom de la propriété
	 * @return propriété trouvée
	 */
	public ModelPropriete getProprieteByNom (String nom) {
		for (ModelPropriete propriete : this.proprietes) {
			if (propriete.getNom().equals(nom)) {
				return propriete;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer la liste des diffusions
	 * @return liste des diffusions
	 */
	public ArrayList<ModelDiffusion> getDiffusions () {
		return new ArrayList<ModelDiffusion>(this.diffusions);
	}
	
	/**
	 * Récupérer une diffusion d'après son port
	 * @param port port
	 * @return diffusion trouvée
	 */
	public ModelDiffusion getDiffusionByPort (int port) {
		for (ModelDiffusion diffusion : this.diffusions) {
			if (diffusion.getPort() == port) {
				return diffusion;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer la liste des critères de classement des phases qualificatives
	 * @return liste des critères de classement des phases qualificatives
	 */
	public ArrayList<ModelCompPhasesQualifsAbstract> getCompsPhasesQualifs () {
		return new ArrayList<ModelCompPhasesQualifsAbstract>(this.compsPhasesQualifs);
	}
	
	/**
	 * Récupérer la liste des participants
	 * @return liste des participants
	 */
	public ArrayList<ModelParticipant> getParticipants () {
		ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		for (ModelCategorie categorie : this.categories) {
			participants.addAll(categorie.getParticipants());
		}
		return participants;
	}
	
	/**
	 * Récupérer le nombre de participants
	 * @return nombre de participants
	 */
	public int getNbParticipants() {
		int nbParticipants = 0;
		for(ModelCategorie categorie : this.categories) {
			nbParticipants += categorie.getNbParticipants();
		}
		return nbParticipants;
	}
	
	/**
	 * Récupérer un participant d'après son nom
	 * @param nom nom du participant
	 * @return participant trouvé
	 */
	public ModelParticipant getParticipantByNom (String nom) {
		for (ModelParticipant participant : this.getParticipants()) {
			if (participant.getNom().equals(nom)) {
				return participant;
			}
		}
		return null;
	}
	
	// Setters
	
	/**
	 * Définir les informations du concours
	 * @param infos informations du concours
	 */
	public void setInfos (InfosModelConcours infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistre les informations
		this.concoursNom = infos.getConcoursNom();
		this.concoursSite = infos.getConcoursSite();
		this.concoursLieu = infos.getConcoursLieu();
		this.concoursEmail = infos.getConcoursEmail();
		this.concoursTelephone = infos.getConcoursTelephone();
		this.concoursDescription = infos.getConcoursDescription();
		
		this.organisateurNom = infos.getOrganisateurNom();
		this.organisateurSite = infos.getOrganisateurSite();
		this.organisateurLieu = infos.getOrganisateurLieu();
		this.organisateurEmail = infos.getOrganisateurEmail();
		this.organisateurTelephone = infos.getOrganisateurTelephone();
		this.organisateurDescription = infos.getOrganisateurDescription();
		
		this.typePhasesQualificatives = infos.getTypeQualifications();
		this.typeParticipants = infos.getTypeParticipants();
		
		this.pointsVictoire = infos.getPointsVictoire();
		this.pointsEgalite = infos.getPointsEgalite();
		this.pointsDefaite = infos.getPointsDefaite();
		
		this.programmationDuree = infos.getProgrammationDuree();
		this.programmationInterval = infos.getProgrammationInterval();
		this.programmationPause = infos.getProgrammationPause();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir la publication
	 * @param publication publication
	 * @throws ContestOrgModelException
	 */
	public void setPublication (ModelExportation publication) throws ContestOrgModelException {
		// Vérifier si l'exportation de publication fait bien partie des exportations
		if (publication != null && !this.exportations.contains(publication)) {
			throw new ContestOrgModelException("L'exportation de publication ne fait pas partie de la liste des exportations.");
		}
		
		// Enregistrer l'exportation
		this.publication = publication;
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	
	/**
	 * Ajouter un objectif
	 * @param objectif objectif
	 * @throws ContestOrgModelException
	 */
	public void addObjectif (ModelObjectif objectif) throws ContestOrgModelException {
		if (!this.objectifs.contains(objectif)) {
			// Enregistrer l'objectif
			this.objectifs.add(objectif);
			
			// Fire add
			this.fireAdd(objectif, this.objectifs.size() - 1);
		} else {
			throw new ContestOrgModelException("L'objectif existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter un prix
	 * @param prix prix
	 * @throws ContestOrgModelException
	 */
	public void addPrix (ModelPrix prix) throws ContestOrgModelException {
		if (!this.prix.contains(prix)) {
			// Enregistrer le prix
			this.prix.add(prix);
			
			// Fire add
			this.fireAdd(prix, this.prix.size() - 1);
		} else {
			throw new ContestOrgModelException("Le prix existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter une catégorie
	 * @param categorie catégorie
	 * @throws ContestOrgModelException
	 */
	public void addCategorie (ModelCategorie categorie) throws ContestOrgModelException {
		if (!this.categories.contains(categorie)) {
			// Enregistrer la categorie
			this.categories.add(categorie);
			
			// Fire add
			this.fireAdd(categorie, this.categories.size() - 1);
		} else {
			throw new ContestOrgModelException("La catégorie existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter un lieu
	 * @param lieu lieu
	 * @throws ContestOrgModelException
	 */
	public void addLieu (ModelLieu lieu) throws ContestOrgModelException {
		if (!this.lieux.contains(lieu)) {
			// Enregistrer le lieu
			this.lieux.add(lieu);
			
			// Fire add
			this.fireAdd(lieu, this.lieux.size() - 1);
		} else {
			throw new ContestOrgModelException("Le lieu existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter une propriété
	 * @param propriete propriété
	 * @throws ContestOrgModelException
	 */
	public void addPropriete (ModelPropriete propriete) throws ContestOrgModelException {
		if (!this.proprietes.contains(propriete)) {
			// Enregistrer la propriete
			this.proprietes.add(propriete);
			
			// Fire add
			this.fireAdd(propriete, this.proprietes.size() - 1);
		} else {
			throw new ContestOrgModelException("La propriété existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter une exportation
	 * @param exportation exportation
	 * @throws ContestOrgModelException
	 */
	public void addExportation (ModelExportation exportation) throws ContestOrgModelException {
		if (!this.exportations.contains(exportation)) {
			// Enregistrer l'exportation
			this.exportations.add(exportation);
			
			// Fire add
			this.fireAdd(exportation, this.exportations.size() - 1);
		} else {
			throw new ContestOrgModelException("L'exportation existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter une diffusion
	 * @param diffusion diffusion
	 * @throws ContestOrgModelException
	 */
	public void addDiffusion (ModelDiffusion diffusion) throws ContestOrgModelException {
		if (!this.diffusions.contains(diffusion)) {
			// Enregistrer l'exportation
			this.diffusions.add(diffusion);
			
			// Fire add
			this.fireAdd(diffusion, this.diffusions.size() - 1);
		} else {
			throw new ContestOrgModelException("La diffusion existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter un critère de classement des phases qualificatives
	 * @param compPhasesQualifs critère de classement des phases qualificatives
	 * @throws ContestOrgModelException
	 */
	public void addCompPhasesQualifs (ModelCompPhasesQualifsAbstract compPhasesQualifs) throws ContestOrgModelException {
		if (!this.compsPhasesQualifs.contains(compPhasesQualifs)) {
			// Ajouter l'objectif remporte
			this.compsPhasesQualifs.add(compPhasesQualifs);
			
			// Fire add
			this.fireAdd(compPhasesQualifs, this.compsPhasesQualifs.size() - 1);
		} else {
			throw new ContestOrgModelException("Le comparateur pour phases qualificatives existe déjà dans le concours");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer l'objectif
	 * @param objectif objectif
	 * @throws ContestOrgModelException
	 */
	protected void removeObjectif (ModelObjectif objectif) throws ContestOrgModelException {
		// Supprimer l'objectif
		int index;
		if ((index = this.objectifs.indexOf(objectif)) != -1) {
			// Remove
			this.objectifs.remove(objectif);
			
			// Fire remove
			this.fireRemove(objectif, index);
		} else {
			throw new ContestOrgModelException("L'objectif n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer un prix
	 * @param prix prix
	 * @throws ContestOrgModelException
	 */
	protected void removePrix (ModelPrix prix) throws ContestOrgModelException {
		// Supprimer le prix
		int index;
		if ((index = this.prix.indexOf(prix)) != -1) {
			// Remove
			this.prix.remove(prix);
			
			// Fire remove
			this.fireRemove(prix, index);
		} else {
			throw new ContestOrgModelException("Le prix n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer une catégorie
	 * @param categorie catégorie
	 * @throws ContestOrgModelException
	 */
	protected void removeCategorie (ModelCategorie categorie) throws ContestOrgModelException {
		// Supprimer la categorie
		int index;
		if ((index = this.categories.indexOf(categorie)) != -1) {
			// Remove
			this.categories.remove(categorie);
			
			// Fire remove
			this.fireRemove(categorie, index);
		} else {
			throw new ContestOrgModelException("La catégorie n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer un lieu
	 * @param lieu lieu
	 * @throws ContestOrgModelException
	 */
	protected void removeLieu (ModelLieu lieu) throws ContestOrgModelException {
		// Supprimer le lieu
		int index;
		if ((index = this.lieux.indexOf(lieu)) != -1) {
			// Remove
			this.lieux.remove(lieu);
			
			// Fire remove
			this.fireRemove(lieu, index);
		} else {
			throw new ContestOrgModelException("Le lieu n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer une propriété
	 * @param propriete propriété
	 * @throws ContestOrgModelException
	 */
	protected void removePropriete (ModelPropriete propriete) throws ContestOrgModelException {
		// Supprimer la propriete
		int index;
		if ((index = this.proprietes.indexOf(propriete)) != -1) {
			// Remove
			this.proprietes.remove(propriete);
			
			// Fire remove
			this.fireRemove(propriete, index);
		} else {
			throw new ContestOrgModelException("La propriété n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer une exportation
	 * @param exportation exportation
	 * @throws ContestOrgModelException
	 */
	protected void removeExportation (ModelExportation exportation) throws ContestOrgModelException {
		// Supprimer l'exportation
		int index;
		if ((index = this.exportations.indexOf(exportation)) != -1) {
			// Remove
			this.exportations.remove(exportation);
			
			// Retirer l'exportation de la publication si nécéssaire
			if (exportation.equals(this.publication)) {
				this.setPublication(null);
			}
			
			// Fire remove
			this.fireRemove(exportation, index);
		} else {
			throw new ContestOrgModelException("L'exportation n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer une diffusion
	 * @param diffusion diffusion
	 * @throws ContestOrgModelException
	 */
	protected void removeDiffusion (ModelDiffusion diffusion) throws ContestOrgModelException {
		// Supprimer l'exportation
		int index;
		if ((index = this.diffusions.indexOf(diffusion)) != -1) {
			// Remove
			this.diffusions.remove(diffusion);
			
			// Fire remove
			this.fireRemove(diffusion, index);
		} else {
			throw new ContestOrgModelException("L'exportation n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer un critère de classement des phases qualificatives
	 * @param compPhasesQualifs critère de classement des phases qualificatives
	 * @throws ContestOrgModelException
	 */
	protected void removeCompPhasesQualifs (ModelCompPhasesQualifsAbstract compPhasesQualifs) throws ContestOrgModelException {
		// Retirer l'objectif remporte
		int index;
		if ((index = this.compsPhasesQualifs.indexOf(compPhasesQualifs)) != -1) {
			// Remove
			this.compsPhasesQualifs.remove(compPhasesQualifs);
			
			// Fire remove
			this.fireRemove(compPhasesQualifs, index);
		} else {
			throw new ContestOrgModelException("Le comparateur pour phases qualificatives n'existe pas dans le concours");
		}
	}
	
	// Updaters
	
	/**
	 * Mettre à jour la liste des objectifs
	 * @param list liste des objectifs source
	 * @throws ContestOrgModelException
	 */
	protected void updateObjectifs (TrackableList<InfosModelObjectif> list) throws ContestOrgModelException {
		// Mettre à jour la liste des objectifs
		this.updates(new ModelObjectif.UpdaterForConcours(this), this.objectifs, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des lieux
	 * @param list liste des lieux source
	 * @throws ContestOrgModelException
	 */
	protected void updateLieux (TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> list) throws ContestOrgModelException {
		// Mettre à jour la liste des lieux
		this.updates(new ModelLieu.UpdaterForConcours(this), this.lieux, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des prix
	 * @param list liste des prix source
	 * @throws ContestOrgModelException
	 */
	protected void updatePrix (TrackableList<InfosModelPrix> list) throws ContestOrgModelException {
		// Mettre à jour la liste des prix
		this.updates(new ModelPrix.UpdaterForConcours(this), this.prix, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des catégories
	 * @param list liste des catégories source
	 * @throws ContestOrgModelException
	 */
	protected void updateCategories (TrackableList<InfosModelCategorie> list) throws ContestOrgModelException {
		// Mettre à jour la liste des catégories
		this.updates(new ModelCategorie.UpdaterForConcours(this), this.categories, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des propiétés
	 * @param list liste des propriétés source
	 * @throws ContestOrgModelException
	 */
	protected void updateProprietes (TrackableList<InfosModelPropriete> list) throws ContestOrgModelException {
		// Mettre à jour la liste des propriétés
		this.updates(new ModelPropriete.UpdaterForConcours(this), this.proprietes, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des exportations
	 * @param list liste des exportations source
	 * @throws ContestOrgModelException
	 */
	protected void updateExportations (TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> list) throws ContestOrgModelException {
		// Mettre à jour la liste des exportations
		this.updates(new ModelExportation.UpdaterForConcours(this), this.exportations, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des diffusions
	 * @param list liste des diffusions source
	 * @throws ContestOrgModelException
	 */
	protected void updateDiffusions (TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> list) throws ContestOrgModelException {
		// Mettre à jour la liste des diffusions
		this.updates(new ModelDiffusion.UpdaterForConcours(this), this.diffusions, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des critères de classement des phases qualificatives
	 * @param list liste des critères de classement des phases qualificatives source
	 * @throws ContestOrgModelException
	 */
	protected void updateCompPhasesQualifs (TrackableList<InfosModelCompPhasesQualifsAbstract> list) throws ContestOrgModelException {
		// Mettre à jour la liste des comparateurs
		this.updates(new ModelCompPhasesQualifsAbstract.UpdaterForConcours(this), this.compsPhasesQualifs, list, true, null);
	}
	
	/**
	 * Cloner le concours
	 * @return clone du concours
	 */
	protected ModelConcours clone () {
		return new ModelConcours(this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelConcours getInfos () {
		InfosModelConcours infos = new InfosModelConcours(this.concoursNom, this.concoursSite, this.concoursLieu, this.concoursEmail, this.concoursTelephone, this.concoursDescription, this.organisateurNom, this.organisateurSite, this.organisateurLieu, this.organisateurEmail, this.organisateurTelephone, this.organisateurDescription, this.typePhasesQualificatives, this.typeParticipants, this.pointsVictoire, this.pointsEgalite, this.pointsDefaite, this.programmationDuree, this.programmationInterval, this.programmationPause);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter l'emplacement à la liste des removers
			removers.add(this);
			
			// Supprimer les objectifs
			for (ModelPropriete propriete : this.proprietes) {
				if (!removers.contains(propriete)) {
					propriete.delete(removers);
				}
			}
			this.proprietes.clear();
			this.fireClear(ModelPropriete.class);
			
			// Supprimer les prix
			for (ModelPrix prix : this.prix) {
				if (!removers.contains(prix)) {
					prix.delete(removers);
				}
			}
			this.prix.clear();
			this.fireClear(ModelPrix.class);
			
			// Supprimer les categories
			for (ModelCategorie categorie : this.categories) {
				if (!removers.contains(categorie)) {
					categorie.delete(removers);
				}
			}
			this.categories.clear();
			this.fireClear(ModelCategorie.class);
			
			// Supprimer les lieux
			for (ModelLieu lieu : this.lieux) {
				if (!removers.contains(lieu)) {
					lieu.delete(removers);
				}
			}
			this.lieux.clear();
			this.fireClear(ModelLieu.class);
			
			// Supprimer les proprietes
			for (ModelPropriete propriete : this.proprietes) {
				if (!removers.contains(propriete)) {
					propriete.delete(removers);
				}
			}
			this.proprietes.clear();
			this.fireClear(ModelPropriete.class);
			
			// Supprimer les diffusions
			for (ModelDiffusion diffusion : this.diffusions) {
				if (!removers.contains(diffusion)) {
					diffusion.delete(removers);
				}
			}
			this.diffusions.clear();
			this.fireClear(ModelDiffusion.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
