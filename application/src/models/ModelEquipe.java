package models;

import infos.InfosModelEquipe;
import infos.InfosModelParticipation;
import infos.InfosModelProprieteEquipe;
import interfaces.ILinker;
import interfaces.IUpdater;

import java.util.ArrayList;

import log.Log;

import common.TrackableList;
import common.Pair;
import common.Quintuple;

/**
 * Equipe
 */
public class ModelEquipe extends ModelMatchable
{
	
	// Attributs objets
	private ModelPoule poule;
	private ArrayList<ModelParticipation> participations = new ArrayList<ModelParticipation>();
	private ArrayList<ModelPrix> prix = new ArrayList<ModelPrix>();
	private ArrayList<ModelProprietePossedee> proprietesEquipes = new ArrayList<ModelProprietePossedee>();
	
	// Attributs scalaires
	private String stand;
	private String nom;
	private String ville;
	private InfosModelEquipe.Statut statut;
	private String membres;
	private String details;
	
	// Constructeur
	public ModelEquipe(ModelPoule poule, InfosModelEquipe infos) {
		// Retenir la poule
		this.poule = poule;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelEquipe(ModelPoule poule, ModelEquipe equipe) {
		// Appeller le constructeur principal
		this(poule, equipe.toInformation());
		
		// Récupérer l'id
		this.setId(equipe.getId());
	}
	
	// Getters
	public String getStand () {
		return this.stand;
	}
	public String getNom () {
		return this.nom;
	}
	public String getVille () {
		return this.ville;
	}
	public InfosModelEquipe.Statut getStatut () {
		return this.statut;
	}
	public String getMembres () {
		return this.membres;
	}
	public String getDetails () {
		return this.details;
	}
	public ModelPoule getPoule () {
		return this.poule;
	}
	public ArrayList<ModelParticipation> getParticipations () {
		return new ArrayList<ModelParticipation>(this.participations);
	}
	public ArrayList<ModelPrix> getPrix () {
		return new ArrayList<ModelPrix>(this.prix);
	}
	public ArrayList<ModelProprietePossedee> getProprietesEquipe () {
		return new ArrayList<ModelProprietePossedee>(this.proprietesEquipes);
	}
	public int getRangPhasesQualifs () {
		return this.poule.getRang(this);
	}
	public int getRangPhasesQualifs (int phaseQualifMax) {
		return this.poule.getRang(this,phaseQualifMax);
	}
	public int getRangPhasesElims () {
		return this.poule.getCategorie().getPhasesEliminatoires() == null ? 1 : this.poule.getCategorie().getPhasesEliminatoires().getRang(this);
	}
	public int getNbVictoires () {
		return this.getNbVictoires(false, true, -1);
	}
	public int getNbVictoires (int phaseQualifMax) {
		return this.getNbVictoires(false, true, phaseQualifMax);
	}
	public int getNbVictoires (boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser le nombre de victoires
		int nbVictoires = 0;
		
		// Comptabiliser les victoires
		for (ModelParticipation participation : this.participations) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre de victoires
			if (comptabiliser) {
				if (participation.getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE) {
					nbVictoires++;
				}
			}
		}
		
		// Retourner le nombre de victoires
		return nbVictoires;
	}
	public int getQuantiteObjectif (ModelObjectif objectif) {
		return this.getQuantiteObjectifs(objectif, false, true, -1);
	}
	public int getQuantiteObjectif (ModelObjectif objectif, int phaseQualifMax) {
		return this.getQuantiteObjectifs(objectif, false, true, phaseQualifMax);
	}
	public int getQuantiteObjectifs (ModelObjectif objectif, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser la quantite
		int quantite = 0;
		
		// Comptabiliser les quantites
		for (ModelParticipation participation : this.getParticipations()) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre d'objectifs remportés
			if (comptabiliser) {
				ModelObjectifRemporte objectifRemporte = participation.getObjectifRemporte(objectif);
				if (objectifRemporte != null) {
					quantite += objectifRemporte.getQuantite();
				}
			}
		}
		
		// Retourner la quantite
		return quantite;
	}
	public double getPoints () {
		return this.getPoints(false, true, -1);
	}
	public double getPoints (int phaseQualifMax) {
		return this.getPoints(false, true, phaseQualifMax);
	}
	public double getPoints (boolean phasesEliminatoires, boolean phasesQualificatives) {
		return this.getPoints(phasesEliminatoires, phasesQualificatives, -1);
	}	
	public double getPoints (boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser le compteur de points
		double points = 0;
		
		// Comptabiliser les points
		for (ModelParticipation participation : this.participations) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre de points
			if (comptabiliser) {
				points += participation.getPoints();
			}
		}
		
		// Retourner le nombre de points
		return points;
	}
	public int getNbRencontres (ModelEquipe equipe) {
		return this.getNbRencontres(equipe, false, true);
	}
	public int getNbRencontres (ModelEquipe equipe, boolean phasesEliminatoires, boolean phasesQualificatives) {
		// Initialiser le nombre de rencontres
		int nbRencontres = 0;
		
		// Comptabiliser le nombre de rencontres
		for (ModelParticipation participation : this.participations) {
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				// Récupérer le match
				ModelMatchAbstract match = participation.getMatch();
				
				// Récupérer l'équipe adverse
				ModelEquipe adversaire = participation.equals(match.getParticipationA()) ? match.getParticipationB().getEquipe() : match.getParticipationA().getEquipe();
				
				// Vérifier si l'équipe passé en paramètre est l'équipe adverse
				if (equipe == null && adversaire == null || equipe != null && equipe.equals(adversaire)) {
					// Incrémenter le nombre de rencontres
					nbRencontres++;
				}
			}
		}
		
		// Retourner le nombre de rencontres
		return nbRencontres;
	}
	
	// Setters
	protected void setInfos (InfosModelEquipe infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.stand = infos.getStand();
		this.nom = infos.getNom();
		this.ville = infos.getVille();
		this.statut = infos.getStatut();
		this.membres = infos.getMembres();
		this.details = infos.getDetails();
		
		// Fire update
		this.fireUpdate();
	}
	protected void setStatut (InfosModelEquipe.Statut statut) {
		// Enregistrer le statut
		this.statut = statut;
		
		// Fire update
		this.fireUpdate();
	}
	protected void setPoule (ModelPoule poule) throws ContestOrgModelException {
		// Modifier la poule
		this.poule.removeEquipe(this);
		this.poule = poule;
		this.poule.addEquipe(this);
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	public void addParticipation (ModelParticipation participation) throws ContestOrgModelException {
		if (!this.participations.contains(participation)) {
			// Ajouter la participation
			this.participations.add(participation);
			
			// Fire add
			this.fireAdd(participation, this.participations.size() - 1);
		} else {
			throw new ContestOrgModelException("La participation existe déjà dans l'équipe");
		}
	}
	public void addPrix (ModelPrix prix) throws ContestOrgModelException {
		if (!this.prix.contains(prix)) {
			// Ajouter le prix
			this.prix.add(prix);
			
			// Fire add
			this.fireAdd(prix, this.prix.size() - 1);
		} else {
			throw new ContestOrgModelException("La participation existe déjà dans l'équipe");
		}
	}
	public void addProprieteEquipe (ModelProprietePossedee proprieteEquipe) throws ContestOrgModelException {
		if (!this.proprietesEquipes.contains(proprieteEquipe)) {
			// Ajouter la propriété d'équipe
			this.proprietesEquipes.add(proprieteEquipe);
			
			// Fire add
			this.fireAdd(proprieteEquipe, this.proprietesEquipes.size() - 1);
		} else {
			throw new ContestOrgModelException("La participation existe déjà dans l'équipe");
		}
	}
	
	// Removers
	protected void removeParticipation (ModelParticipation participation) throws ContestOrgModelException {
		// Retirer la participation
		int index;
		if ((index = this.participations.indexOf(participation)) != -1) {
			// Remove
			this.participations.remove(participation);
			
			// Fire remove
			this.fireRemove(participation, index);
		} else {
			throw new ContestOrgModelException("La participation n'existe pas dans l'équipe");
		}
	}
	protected void removePrix (ModelPrix prix) throws ContestOrgModelException {
		// Retirer le prix
		int index;
		if ((index = this.prix.indexOf(prix)) != -1) {
			// Remove
			this.prix.remove(prix);
			
			// Fire remove
			this.fireRemove(prix, index);
		} else {
			throw new ContestOrgModelException("La participation n'existe pas dans l'équipe");
		}
	}
	protected void removeProprieteEquipe (ModelProprietePossedee proprieteEquipe) throws ContestOrgModelException {
		// Retirer la propriété d'équipe
		int index;
		if ((index = this.proprietesEquipes.indexOf(proprieteEquipe)) != -1) {
			// Remove
			this.proprietesEquipes.remove(proprieteEquipe);
			
			// Fire remove
			this.fireRemove(proprieteEquipe, index);
		} else {
			throw new ContestOrgModelException("La participation n'existe pas dans l'équipe");
		}
	}
	
	// Updaters
	protected void updatePrix (TrackableList<String> list) throws ContestOrgModelException {
		// Mettre à jour les prix remportés
		this.links(new ModelEquipe.LinkerForPrix(this), list);
	}
	protected void updateProprietesEquipe (TrackableList<Pair<String, InfosModelProprieteEquipe>> list) throws ContestOrgModelException {
		// Mettre à jour les propriétés d'équipe
		this.updates(new ModelProprietePossedee.UpdaterForEquipe(this), this.proprietesEquipes, list, true, null);
	}
	
	// Implémentation de toStrings
	protected String[] toStrings () {
		String[] strings = { this.nom, this.stand, this.ville, this.membres, this.details };
		return strings;
	}
	
	// Clone
	protected ModelEquipe clone (ModelPoule poule) {
		return new ModelEquipe(poule, this);
	}
	
	// ToInformation
	public InfosModelEquipe toInformation () {
		InfosModelEquipe infos = new InfosModelEquipe(this.stand, this.nom, this.ville, this.statut, this.membres, this.details);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter l'équipe à la liste des removers
			removers.add(this);
			
			// "Supprimer" les participations de l'équipe
			for (ModelParticipation participation : this.participations) {
				if (!removers.contains(participation)) {
					participation.setEquipe(null);
				}
			}
			this.participations.clear();
			this.fireClear(ModelParticipation.class);
			
			// Retirer l'équipe des prix
			for (ModelPrix prix : this.prix) {
				if (!removers.contains(prix)) {
					prix.removeEquipe(this);
				}
			}
			this.prix.clear();
			this.fireClear(ModelPrix.class);
			
			// Retirer l'équipe de la poule
			if (this.poule != null) {
				if (!removers.contains(this.poule)) {
					this.poule.removeEquipe(this);
				}
				this.poule = null;
				this.fireClear(ModelPoule.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour une liste d'équipes indépendament de son conteneur
	protected static class Updater implements IUpdater<Quintuple<String, String, InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>>, ModelEquipe>
	{
		// Concours
		private ModelConcours concours;
		
		// Constructeur
		public Updater(ModelConcours concours) {
			// Retenir le concours
			this.concours = concours;
		}
		
		// Implémentation de create
		@Override
		public ModelEquipe create (Quintuple<String, String, InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>> infos) {
			try {
				// Récupérer la poule
				ModelPoule poule = this.concours.getCategorieByNom(infos.getFirst()).getPouleByNom(infos.getSecond());
				
				// Créer, configurer et retourner l'équipe
				ModelEquipe equipe = new ModelEquipe(poule, infos.getThird());
				poule.addEquipe(equipe);
				equipe.updateProprietesEquipe(infos.getFourth());
				equipe.updatePrix(infos.getFifth());
				return equipe;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'une équipe.",e);
				return null;
			}
		}
		
		@Override
		public void update (ModelEquipe equipe, Quintuple<String, String, InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>> infos) {
			try {
				// Récupérer la poule
				ModelPoule poule = this.concours.getCategorieByNom(infos.getFirst()).getPouleByNom(infos.getSecond());
				
				// Configurer l'équipe
				if (!equipe.getPoule().equals(poule)) {
					equipe.setPoule(poule);
				}
				equipe.setInfos(infos.getThird());
				equipe.updateProprietesEquipe(infos.getFourth());
				equipe.updatePrix(infos.getFifth());
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal(e.getMessage());
			}
		}
	}
	
	// Classe pour lié une liste de prix à une équipe
	protected static class LinkerForPrix implements ILinker<String>
	{
		// Equipe
		private ModelEquipe equipe;
		
		// Constructeur
		public LinkerForPrix(ModelEquipe equipe) {
			this.equipe = equipe;
		}
		
		// Implémentation de link
		@Override
		public void link (String nomPrix) {
			// Récupérer le prix
			ModelPrix prix = this.equipe.getPoule().getCategorie().getConcours().getPrixByNom(nomPrix);
			
			// Créer le lien entre le prix et l'équipe
			if (prix.getEquipes().indexOf(this.equipe) == -1 && this.equipe.getPrix().indexOf(prix) == -1) {
				try {
					prix.addEquipe(this.equipe);
					this.equipe.addPrix(prix);
				} catch (ContestOrgModelException e) {
					Log.getLogger().fatal("Erreur lors de la création du lien entre un prix et une équipe.");
				}
			}
		}
		
		// Implémentation de unlink
		@Override
		public void unlink (String nomPrix) {
			// Récupérer le prix
			ModelPrix prix = this.equipe.getPoule().getCategorie().getConcours().getPrixByNom(nomPrix);
			
			// Supprimer le lien entre le prix et l'équipe
			if (prix.getEquipes().indexOf(this.equipe) != -1 && this.equipe.getPrix().indexOf(prix) != -1) {
				try {
					prix.removeEquipe(this.equipe);
					this.equipe.removePrix(prix);
				} catch (ContestOrgModelException e) {
					Log.getLogger().fatal("Erreur lors de la suppression du lien entre un prix et une équipe.");
				}
			}
		}
	}
	
}
