package org.contestorg.models;


import java.util.ArrayList;
import java.util.Collections;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.comparators.CompPhasesQualifs;
import org.contestorg.comparators.CompPhasesQualifsGoalAverage;
import org.contestorg.comparators.CompPhasesQualifsNbDefaites;
import org.contestorg.comparators.CompPhasesQualifsNbEgalites;
import org.contestorg.comparators.CompPhasesQualifsNbForfaits;
import org.contestorg.comparators.CompPhasesQualifsNbPoints;
import org.contestorg.comparators.CompPhasesQualifsNbVictoires;
import org.contestorg.comparators.CompPhasesQualifsQuantiteObjectif;
import org.contestorg.comparators.CompPhasesQualifsRencontresDirectes;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelCritereClassementAbstract;
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
	
	/** Liste des critères de classement */
	private ArrayList<ModelCritereClassementAbstract> criteresClassement = new ArrayList<ModelCritereClassementAbstract>();
	
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
	/** Statut "homologué" activé ? */
	private boolean statutHomologueActive;
	
	// Informations sur les points
	
	/** Points de victoire */
	private double pointsVictoire;
	/** Points d'égalité */
	private double pointsEgalite;
	/** Egalité activée ? */
	private boolean egaliteActivee;
	/** Points de défaite */
	private double pointsDefaite;
	/** Points de forfait */
	private double pointsForfait;
	
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
	 * Savoir si le statut "homologué" est activé
	 * @return statut "homologué" est activé ?
	 */
	public boolean isStatutHomologueActive() {
		return this.statutHomologueActive;
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
	 * Savoir si l'égalité est activée
	 * @return égalité activée ?
	 */
	public boolean isEgaliteActivee() {
		return this.egaliteActivee;
	}
	
	/**
	 * Récupérer les points de défaite
	 * @return points de défaite
	 */
	public double getPointsDefaite () {
		return this.pointsDefaite;
	}
	
	/**
	 * Récupérer les points de forfait
	 * @return points de forfait
	 */
	public double getPointsForfait () {
		return this.pointsForfait;
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
	 * Récupérer un lieu d'après son nom
	 * @param nom nom du lieu
	 * @return lieu trouvé
	 */
	public ModelLieu getLieuByNom(String nom) {
		for(ModelLieu lieu : this.lieux) {
			if(lieu.getNom().equals(nom)) {
				return lieu;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer le comparateur pour établir le classement des phases qualificatives
	 * @return comparateur pour établir le classement des phases qualificatives
	 */
	public CompPhasesQualifs getComparateurPhasesQualificatives () {
		return this.getComparateurPhasesQualificatives(-1);
	}
	
	/**
	 * Récupérer le comparateur pour établir le classement des phases qualificatives
	 * @param phaseQualifMax numéro de la phase qualificative maximale (-1 pour toutes les phases qualificatives)
	 * @return comparateur pour établir le classement des phases qualificatives
	 */
	public CompPhasesQualifs getComparateurPhasesQualificatives (int phaseQualifMax) {
		// Récupérer une copie des critères
		ArrayList<ModelCritereClassementAbstract> criteresClassement = new ArrayList<ModelCritereClassementAbstract>(this.criteresClassement);
		
		// Renverser l'ordre des critères
		Collections.reverse(criteresClassement);
		
		// Construire le comparateur
		CompPhasesQualifs comparateur = null;
		for (ModelCritereClassementAbstract critereClassement : criteresClassement) {
			if (critereClassement instanceof ModelCritereClassementNbPoints) {
				comparateur = new CompPhasesQualifsNbPoints(comparateur, critereClassement.isInverse ? CompPhasesQualifs.SENS_ASCENDANT : CompPhasesQualifs.SENS_DESCENDANT, phaseQualifMax);
			} else if (critereClassement instanceof ModelCritereClassementNbVictoires) {
				comparateur = new CompPhasesQualifsNbVictoires(comparateur, critereClassement.isInverse ? CompPhasesQualifs.SENS_ASCENDANT : CompPhasesQualifs.SENS_DESCENDANT, phaseQualifMax);
			} else if (critereClassement instanceof ModelCritereClassementNbEgalites) {
				comparateur = new CompPhasesQualifsNbEgalites(comparateur, critereClassement.isInverse ? CompPhasesQualifs.SENS_ASCENDANT : CompPhasesQualifs.SENS_DESCENDANT, phaseQualifMax);
			} else if (critereClassement instanceof ModelCritereClassementNbDefaites) {
				comparateur = new CompPhasesQualifsNbDefaites(comparateur, critereClassement.isInverse ? CompPhasesQualifs.SENS_ASCENDANT : CompPhasesQualifs.SENS_DESCENDANT, phaseQualifMax);
			} else if (critereClassement instanceof ModelCritereClassementNbForfaits) {
				comparateur = new CompPhasesQualifsNbForfaits(comparateur, critereClassement.isInverse ? CompPhasesQualifs.SENS_ASCENDANT : CompPhasesQualifs.SENS_DESCENDANT, phaseQualifMax);
			} else if (critereClassement instanceof ModelCritereClassementRencontresDirectes) {
				comparateur = new CompPhasesQualifsRencontresDirectes(comparateur, critereClassement.isInverse ? CompPhasesQualifs.SENS_ASCENDANT : CompPhasesQualifs.SENS_DESCENDANT, phaseQualifMax);
			} else if (critereClassement instanceof ModelCritereClassementQuantiteObjectif) {
				comparateur = new CompPhasesQualifsQuantiteObjectif(comparateur, critereClassement.isInverse ? CompPhasesQualifs.SENS_ASCENDANT : CompPhasesQualifs.SENS_DESCENDANT, phaseQualifMax, ((ModelCritereClassementQuantiteObjectif)critereClassement).getObjectif());
			} else if (critereClassement instanceof ModelCritereClassementGoalAverage) {
				comparateur = new CompPhasesQualifsGoalAverage(comparateur, critereClassement.isInverse ? CompPhasesQualifs.SENS_ASCENDANT : CompPhasesQualifs.SENS_DESCENDANT, phaseQualifMax, ((ModelCritereClassementGoalAverage)critereClassement).getType(), ((ModelCritereClassementGoalAverage)critereClassement).getMethode(), ((ModelCritereClassementGoalAverage)critereClassement).getDonnee(), ((ModelCritereClassementGoalAverage)critereClassement).getObjectif());
			}
		}
		
		// Retourner le comparacteur
		return comparateur;
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
	 * Récupérer la liste des critères de classement
	 * @return liste des critères de classement
	 */
	public ArrayList<ModelCritereClassementAbstract> getCriteresClassement () {
		return new ArrayList<ModelCritereClassementAbstract>(this.criteresClassement);
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
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelConcours getInfos () {
		InfosModelConcours infos = new InfosModelConcours(this.concoursNom, this.concoursSite, this.concoursLieu, this.concoursEmail, this.concoursTelephone, this.concoursDescription, this.organisateurNom, this.organisateurSite, this.organisateurLieu, this.organisateurEmail, this.organisateurTelephone, this.organisateurDescription, this.typePhasesQualificatives, this.typeParticipants, this.statutHomologueActive, this.pointsVictoire, this.pointsEgalite, this.egaliteActivee, this.pointsDefaite, this.pointsForfait, this.programmationDuree, this.programmationInterval, this.programmationPause);
		infos.setId(this.getId());
		return infos;
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
		this.statutHomologueActive = infos.isStatutHomologueActive();
		
		this.pointsVictoire = infos.getPointsVictoire();
		this.pointsEgalite = infos.getPointsEgalite();
		this.egaliteActivee = infos.isEgaliteActivee();
		this.pointsDefaite = infos.getPointsDefaite();
		this.pointsForfait = infos.getPointsForfait();
		
		this.programmationDuree = infos.getProgrammationDuree();
		this.programmationInterval = infos.getProgrammationInterval();
		this.programmationPause = infos.getProgrammationPause();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir la publication
	 * @param publication publication
	 * @throws ContestOrgErrorException
	 */
	public void setPublication (ModelExportation publication) throws ContestOrgErrorException {
		// Vérifier si l'exportation de publication fait bien partie des exportations
		if (publication != null && !this.exportations.contains(publication)) {
			throw new ContestOrgErrorException("L'exportation de publication ne fait pas partie de la liste des exportations.");
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
	 * @throws ContestOrgErrorException
	 */
	public void addObjectif (ModelObjectif objectif) throws ContestOrgErrorException {
		if (!this.objectifs.contains(objectif)) {
			// Enregistrer l'objectif
			this.objectifs.add(objectif);
			
			// Fire add
			this.fireAdd(objectif, this.objectifs.size() - 1);
		} else {
			throw new ContestOrgErrorException("L'objectif existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter un prix
	 * @param prix prix
	 * @throws ContestOrgErrorException
	 */
	public void addPrix (ModelPrix prix) throws ContestOrgErrorException {
		if (!this.prix.contains(prix)) {
			// Enregistrer le prix
			this.prix.add(prix);
			
			// Fire add
			this.fireAdd(prix, this.prix.size() - 1);
		} else {
			throw new ContestOrgErrorException("Le prix existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter une catégorie
	 * @param categorie catégorie
	 * @throws ContestOrgErrorException
	 */
	public void addCategorie (ModelCategorie categorie) throws ContestOrgErrorException {
		if (!this.categories.contains(categorie)) {
			// Enregistrer la categorie
			this.categories.add(categorie);
			
			// Fire add
			this.fireAdd(categorie, this.categories.size() - 1);
		} else {
			throw new ContestOrgErrorException("La catégorie existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter un lieu
	 * @param lieu lieu
	 * @throws ContestOrgErrorException
	 */
	public void addLieu (ModelLieu lieu) throws ContestOrgErrorException {
		if (!this.lieux.contains(lieu)) {
			// Enregistrer le lieu
			this.lieux.add(lieu);
			
			// Fire add
			this.fireAdd(lieu, this.lieux.size() - 1);
		} else {
			throw new ContestOrgErrorException("Le lieu existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter une propriété
	 * @param propriete propriété
	 * @throws ContestOrgErrorException
	 */
	public void addPropriete (ModelPropriete propriete) throws ContestOrgErrorException {
		if (!this.proprietes.contains(propriete)) {
			// Enregistrer la propriete
			this.proprietes.add(propriete);
			
			// Fire add
			this.fireAdd(propriete, this.proprietes.size() - 1);
		} else {
			throw new ContestOrgErrorException("La propriété existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter une exportation
	 * @param exportation exportation
	 * @throws ContestOrgErrorException
	 */
	public void addExportation (ModelExportation exportation) throws ContestOrgErrorException {
		if (!this.exportations.contains(exportation)) {
			// Enregistrer l'exportation
			this.exportations.add(exportation);
			
			// Fire add
			this.fireAdd(exportation, this.exportations.size() - 1);
		} else {
			throw new ContestOrgErrorException("L'exportation existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter une diffusion
	 * @param diffusion diffusion
	 * @throws ContestOrgErrorException
	 */
	public void addDiffusion (ModelDiffusion diffusion) throws ContestOrgErrorException {
		if (!this.diffusions.contains(diffusion)) {
			// Enregistrer l'exportation
			this.diffusions.add(diffusion);
			
			// Fire add
			this.fireAdd(diffusion, this.diffusions.size() - 1);
		} else {
			throw new ContestOrgErrorException("La diffusion existe déjà dans le concours");
		}
	}
	
	/**
	 * Ajouter un critère de classement
	 * @param critereClassement critère de classement
	 * @throws ContestOrgErrorException
	 */
	public void addCritereClassement (ModelCritereClassementAbstract critereClassement) throws ContestOrgErrorException {
		if (!this.criteresClassement.contains(critereClassement)) {
			// Ajouter le critère de classement
			this.criteresClassement.add(critereClassement);
			
			// Fire add
			this.fireAdd(critereClassement, this.criteresClassement.size() - 1);
		} else {
			throw new ContestOrgErrorException("Le critère de classement existe déjà dans le concours");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer l'objectif
	 * @param objectif objectif
	 * @throws ContestOrgErrorException
	 */
	protected void removeObjectif (ModelObjectif objectif) throws ContestOrgErrorException {
		// Supprimer l'objectif
		int index;
		if ((index = this.objectifs.indexOf(objectif)) != -1) {
			// Remove
			this.objectifs.remove(objectif);
			
			// Fire remove
			this.fireRemove(objectif, index);
		} else {
			throw new ContestOrgErrorException("L'objectif n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer un prix
	 * @param prix prix
	 * @throws ContestOrgErrorException
	 */
	protected void removePrix (ModelPrix prix) throws ContestOrgErrorException {
		// Supprimer le prix
		int index;
		if ((index = this.prix.indexOf(prix)) != -1) {
			// Remove
			this.prix.remove(prix);
			
			// Fire remove
			this.fireRemove(prix, index);
		} else {
			throw new ContestOrgErrorException("Le prix n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer une catégorie
	 * @param categorie catégorie
	 * @throws ContestOrgErrorException
	 */
	protected void removeCategorie (ModelCategorie categorie) throws ContestOrgErrorException {
		// Supprimer la categorie
		int index;
		if ((index = this.categories.indexOf(categorie)) != -1) {
			// Remove
			this.categories.remove(categorie);
			
			// Fire remove
			this.fireRemove(categorie, index);
		} else {
			throw new ContestOrgErrorException("La catégorie n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer un lieu
	 * @param lieu lieu
	 * @throws ContestOrgErrorException
	 */
	protected void removeLieu (ModelLieu lieu) throws ContestOrgErrorException {
		// Supprimer le lieu
		int index;
		if ((index = this.lieux.indexOf(lieu)) != -1) {
			// Remove
			this.lieux.remove(lieu);
			
			// Fire remove
			this.fireRemove(lieu, index);
		} else {
			throw new ContestOrgErrorException("Le lieu n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer une propriété
	 * @param propriete propriété
	 * @throws ContestOrgErrorException
	 */
	protected void removePropriete (ModelPropriete propriete) throws ContestOrgErrorException {
		// Supprimer la propriete
		int index;
		if ((index = this.proprietes.indexOf(propriete)) != -1) {
			// Remove
			this.proprietes.remove(propriete);
			
			// Fire remove
			this.fireRemove(propriete, index);
		} else {
			throw new ContestOrgErrorException("La propriété n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer une exportation
	 * @param exportation exportation
	 * @throws ContestOrgErrorException
	 */
	protected void removeExportation (ModelExportation exportation) throws ContestOrgErrorException {
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
			throw new ContestOrgErrorException("L'exportation n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer une diffusion
	 * @param diffusion diffusion
	 * @throws ContestOrgErrorException
	 */
	protected void removeDiffusion (ModelDiffusion diffusion) throws ContestOrgErrorException {
		// Supprimer l'exportation
		int index;
		if ((index = this.diffusions.indexOf(diffusion)) != -1) {
			// Remove
			this.diffusions.remove(diffusion);
			
			// Fire remove
			this.fireRemove(diffusion, index);
		} else {
			throw new ContestOrgErrorException("L'exportation n'existe pas dans le concours");
		}
	}
	
	/**
	 * Supprimer un critère de classement
	 * @param critereClassement critère de classement
	 * @throws ContestOrgErrorException
	 */
	protected void removeCritereClassement (ModelCritereClassementAbstract critereClassement) throws ContestOrgErrorException {
		// Retirer le critère de classement
		int index;
		if ((index = this.criteresClassement.indexOf(critereClassement)) != -1) {
			// Remove
			this.criteresClassement.remove(critereClassement);
			
			// Fire remove
			this.fireRemove(critereClassement, index);
		} else {
			throw new ContestOrgErrorException("Le critère de classement n'existe pas dans le concours");
		}
	}
	
	// Updaters
	
	/**
	 * Mettre à jour la liste des objectifs
	 * @param list liste des objectifs source
	 * @throws ContestOrgErrorException
	 */
	protected void updateObjectifs (TrackableList<InfosModelObjectif> list) throws ContestOrgErrorException {
		// Mettre à jour la liste des objectifs
		this.updates(new ModelObjectif.UpdaterForConcours(this), this.objectifs, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des lieux
	 * @param list liste des lieux source
	 * @throws ContestOrgErrorException
	 */
	protected void updateLieux (TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> list) throws ContestOrgErrorException {
		// Mettre à jour la liste des lieux
		this.updates(new ModelLieu.UpdaterForConcours(this), this.lieux, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des prix
	 * @param list liste des prix source
	 * @throws ContestOrgErrorException
	 */
	protected void updatePrix (TrackableList<InfosModelPrix> list) throws ContestOrgErrorException {
		// Mettre à jour la liste des prix
		this.updates(new ModelPrix.UpdaterForConcours(this), this.prix, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des catégories
	 * @param list liste des catégories source
	 * @throws ContestOrgErrorException
	 */
	protected void updateCategories (TrackableList<InfosModelCategorie> list) throws ContestOrgErrorException {
		// Mettre à jour la liste des catégories
		this.updates(new ModelCategorie.UpdaterForConcours(this), this.categories, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des propiétés
	 * @param list liste des propriétés source
	 * @throws ContestOrgErrorException
	 */
	protected void updateProprietes (TrackableList<InfosModelPropriete> list) throws ContestOrgErrorException {
		// Mettre à jour la liste des propriétés
		this.updates(new ModelPropriete.UpdaterForConcours(this), this.proprietes, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des exportations
	 * @param list liste des exportations source
	 * @throws ContestOrgErrorException
	 */
	protected void updateExportations (TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> list) throws ContestOrgErrorException {
		// Mettre à jour la liste des exportations
		this.updates(new ModelExportation.UpdaterForConcours(this), this.exportations, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des diffusions
	 * @param list liste des diffusions source
	 * @throws ContestOrgErrorException
	 */
	protected void updateDiffusions (TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> list) throws ContestOrgErrorException {
		// Mettre à jour la liste des diffusions
		this.updates(new ModelDiffusion.UpdaterForConcours(this), this.diffusions, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des critères de classement
	 * @param list liste des critères de classement
	 * @throws ContestOrgErrorException
	 */
	protected void updateCriteresClassement (TrackableList<InfosModelCritereClassementAbstract> list) throws ContestOrgErrorException {
		// Mettre à jour la liste des comparateurs
		this.updates(new ModelCritereClassementAbstract.UpdaterForConcours(this), this.criteresClassement, list, true, null);
	}
	
	/**
	 * Cloner le concours
	 * @return clone du concours
	 */
	protected ModelConcours clone () {
		return new ModelConcours(this);
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
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
