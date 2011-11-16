package org.contestorg.models;


import java.util.ArrayList;
import java.util.Collections;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.comparators.CompPhasesQualifs;
import org.contestorg.comparators.CompPhasesQualifsObjectif;
import org.contestorg.comparators.CompPhasesQualifsPoints;
import org.contestorg.comparators.CompPhasesQualifsVictoires;
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
	private ModelExportation publication;
	private ArrayList<ModelLieu> lieux = new ArrayList<ModelLieu>();
	private ArrayList<ModelObjectif> objectifs = new ArrayList<ModelObjectif>();
	private ArrayList<ModelPrix> prix = new ArrayList<ModelPrix>();
	private ArrayList<ModelCategorie> categories = new ArrayList<ModelCategorie>();
	private ArrayList<ModelPropriete> proprietes = new ArrayList<ModelPropriete>();
	private ArrayList<ModelExportation> exportations = new ArrayList<ModelExportation>();
	private ArrayList<ModelDiffusion> diffusions = new ArrayList<ModelDiffusion>();
	private ArrayList<ModelCompPhasesQualifsAbstract> compsPhasesQualifs = new ArrayList<ModelCompPhasesQualifsAbstract>();
	
	// Attributs scalaires
	
	// Informations sur le concours
	private String concoursNom;
	private String concoursSite;
	private String concoursLieu;
	private String concoursEmail;
	private String concoursTelephone;
	private String concoursDescription;
	
	// Informations sur l'organisateur
	private String organisateurNom;
	private String organisateurSite;
	private String organisateurLieu;
	private String organisateurEmail;
	private String organisateurTelephone;
	private String organisateurDescription;
	
	// Informations diverses
	private int typeQualifications; // Type de qualifications
	private int typeParticipants; // Type de participants
	
	// Informations sur les points
	private double pointsVictoire;
	private double pointsEgalite;
	private double pointsDefaite;
	
	// Informations de programmation
	private Double programmationDuree; // En minutes
	private Double programmationInterval; // En minutes
	private Double programmationPause; // En minutes
	
	// Constructeur
	public ModelConcours(InfosModelConcours infos) {
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelConcours(ModelConcours concours) {
		// Appeller le constructeur principal
		this(concours.toInformation());
		
		// Récupérer l'id
		this.setId(concours.getId());
	}
	
	// Getters
	public String getConcoursNom () {
		return this.concoursNom;
	}
	public String getConcoursSite () {
		return this.concoursSite;
	}
	public String getConcoursLieu () {
		return this.concoursLieu;
	}
	public String getConcoursEmail () {
		return this.concoursEmail;
	}
	public String getConcoursTelephone () {
		return this.concoursTelephone;
	}
	public String getConcoursDescription () {
		return this.concoursDescription;
	}
	public String getOrganisateurNom () {
		return this.organisateurNom;
	}
	public String getOrganisateurSite () {
		return this.organisateurSite;
	}
	public String getOrganisateurLieu () {
		return this.organisateurLieu;
	}
	public String getOrganisateurEmail () {
		return this.organisateurEmail;
	}
	public String getOrganisateurTelephone () {
		return this.organisateurTelephone;
	}
	public String getOrganisateurDescription () {
		return this.organisateurDescription;
	}
	public int getTypeQualifications () {
		return this.typeQualifications;
	}
	public int getTypeParticipants () {
		return this.typeParticipants;
	}
	public double getPointsVictoire () {
		return this.pointsVictoire;
	}
	public double getPointsEgalite () {
		return this.pointsEgalite;
	}
	public double getPointsDefaite () {
		return this.pointsDefaite;
	}
	public Double getProgrammationDuree () {
		return this.programmationDuree;
	}
	public Double getProgrammationInterval () {
		return this.programmationInterval;
	}
	public Double getProgrammationPause () {
		return this.programmationPause;
	}
	public ModelExportation getPublication () {
		return this.publication;
	}
	public ArrayList<ModelLieu> getLieux () {
		return this.lieux;
	}
	public CompPhasesQualifs getComparateurPhasesQualificatives () {
		return this.getComparateurPhasesQualificatives(-1);
	}
	public CompPhasesQualifs getComparateurPhasesQualificatives (int phaseQualifMax) {
		// Récupérer une copie des comparateurs
		ArrayList<ModelCompPhasesQualifsAbstract> compsPhasesQualifs = new ArrayList<ModelCompPhasesQualifsAbstract>(this.compsPhasesQualifs);
		
		// Renverser l'ordre des comparateurs
		Collections.reverse(compsPhasesQualifs);
		
		// Construire le comparateur
		CompPhasesQualifs result = null;
		for (ModelCompPhasesQualifsAbstract comparateur : compsPhasesQualifs) {
			if (comparateur instanceof ModelCompPhasesQualifsPoints) {
				result = new CompPhasesQualifsPoints(result, phaseQualifMax);
			} else if (comparateur instanceof ModelCompPhasesQualifsVictoires) {
				result = new CompPhasesQualifsVictoires(result, phaseQualifMax);
			} else if (comparateur instanceof ModelCompPhasesQualifsObjectif) {
				result = new CompPhasesQualifsObjectif(result, phaseQualifMax, ((ModelCompPhasesQualifsObjectif)comparateur).getObjectif());
			}
		}
		
		// Retourner le comparacteur
		return result;
	}
	public ArrayList<ModelObjectif> getObjectifs () {
		return new ArrayList<ModelObjectif>(this.objectifs);
	}
	public ModelObjectif getObjectifByNom (String nom) {
		for (ModelObjectif objectif : this.objectifs) {
			if (objectif.getNom().equals(nom)) {
				return objectif;
			}
		}
		return null;
	}
	public ArrayList<ModelPrix> getPrix () {
		return new ArrayList<ModelPrix>(this.prix);
	}
	public ModelPrix getPrixByNom (String nom) {
		for (ModelPrix prix : this.prix) {
			if (prix.getNom().equals(nom)) {
				return prix;
			}
		}
		return null;
	}
	public ArrayList<ModelCategorie> getCategories () {
		return new ArrayList<ModelCategorie>(this.categories);
	}
	public ModelCategorie getCategorieByNom (String nom) {
		for (ModelCategorie categorie : this.categories) {
			if (categorie.getNom().equals(nom)) {
				return categorie;
			}
		}
		return null;
	}
	public ArrayList<ModelMatchPhasesQualifs> getMatchsPhasesQualifs () {
		ArrayList<ModelMatchPhasesQualifs> matchs = new ArrayList<ModelMatchPhasesQualifs>();
		for (ModelCategorie categorie : this.categories) {
			matchs.addAll(categorie.getMatchsPhasesQualifs());
		}
		return matchs;
	}
	public ArrayList<ModelExportation> getExportations () {
		return new ArrayList<ModelExportation>(this.exportations);
	}
	public ModelExportation getExportationByNom (String nom) {
		for (ModelExportation exportation : this.exportations) {
			if (exportation.getNom().equals(nom)) {
				return exportation;
			}
		}
		return null;
	}
	public ArrayList<ModelPropriete> getProprietes () {
		return new ArrayList<ModelPropriete>(this.proprietes);
	}
	public ModelPropriete getProprieteByNom (String nom) {
		for (ModelPropriete propriete : this.proprietes) {
			if (propriete.getNom().equals(nom)) {
				return propriete;
			}
		}
		return null;
	}
	public ArrayList<ModelDiffusion> getDiffusions () {
		return new ArrayList<ModelDiffusion>(this.diffusions);
	}
	public ModelDiffusion getDiffusionByPort (int port) {
		for (ModelDiffusion diffusion : this.diffusions) {
			if (diffusion.getPort() == port) {
				return diffusion;
			}
		}
		return null;
	}
	public ArrayList<ModelCompPhasesQualifsAbstract> getCompsPhasesQualifs () {
		return new ArrayList<ModelCompPhasesQualifsAbstract>(this.compsPhasesQualifs);
	}
	public ArrayList<ModelEquipe> getEquipes () {
		ArrayList<ModelEquipe> equipes = new ArrayList<ModelEquipe>();
		for (ModelCategorie categorie : this.categories) {
			equipes.addAll(categorie.getEquipes());
		}
		return equipes;
	}
	public int getNbEquipes() {
		int nbEquipes = 0;
		for(ModelCategorie categorie : this.categories) {
			nbEquipes += categorie.getNbEquipes();
		}
		return nbEquipes;
	}
	public ModelEquipe getEquipeByNom (String nom) {
		for (ModelEquipe equipe : this.getEquipes()) {
			if (equipe.getNom().equals(nom)) {
				return equipe;
			}
		}
		return null;
	}
	
	// Setters
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
		
		this.typeQualifications = infos.getTypeQualifications();
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
	protected void updateObjectifs (TrackableList<InfosModelObjectif> list) throws ContestOrgModelException {
		// Mettre à jour la liste des objectifs
		this.updates(new ModelObjectif.UpdaterForConcours(this), this.objectifs, list, true, null);
	}
	protected void updateLieux (TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> list) throws ContestOrgModelException {
		// Mettre à jour la liste des lieux
		this.updates(new ModelLieu.UpdaterForConcours(this), this.lieux, list, true, null);
	}
	protected void updatePrix (TrackableList<InfosModelPrix> list) throws ContestOrgModelException {
		// Mettre à jour la liste des prix
		this.updates(new ModelPrix.UpdaterForConcours(this), this.prix, list, true, null);
	}
	protected void updateCategories (TrackableList<InfosModelCategorie> list) throws ContestOrgModelException {
		// Mettre à jour la liste des catégories
		this.updates(new ModelCategorie.UpdaterForConcours(this), this.categories, list, true, null);
	}
	protected void updateProprietes (TrackableList<InfosModelPropriete> list) throws ContestOrgModelException {
		// Mettre à jour la liste des propriétés
		this.updates(new ModelPropriete.UpdaterForConcours(this), this.proprietes, list, true, null);
	}
	protected void updateExportations (TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> list) throws ContestOrgModelException {
		// Mettre à jour la liste des exportations
		this.updates(new ModelExportation.UpdaterForConcours(this), this.exportations, list, true, null);
	}
	protected void updateDiffusions (TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> list) throws ContestOrgModelException {
		// Mettre à jour la liste des diffusions
		this.updates(new ModelDiffusion.UpdaterForConcours(this), this.diffusions, list, true, null);
	}
	protected void updateCompPhasesQualifs (TrackableList<InfosModelCompPhasesQualifsAbstract> list) throws ContestOrgModelException {
		// Mettre à jour la liste des comparateurs
		this.updates(new ModelCompPhasesQualifsAbstract.UpdaterForConcours(this), this.compsPhasesQualifs, list, true, null);
	}
	
	// Clone
	protected ModelConcours clone () {
		return new ModelConcours(this);
	}
	
	// ToInformation
	public InfosModelConcours toInformation () {
		InfosModelConcours infos = new InfosModelConcours(this.concoursNom, this.concoursSite, this.concoursLieu, this.concoursEmail, this.concoursTelephone, this.concoursDescription, this.organisateurNom, this.organisateurSite, this.organisateurLieu, this.organisateurEmail, this.organisateurTelephone, this.organisateurDescription, this.typeQualifications, this.typeParticipants, this.pointsVictoire, this.pointsEgalite, this.pointsDefaite, this.programmationDuree, this.programmationInterval, this.programmationPause);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
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
