package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelProprieteParticipant;
import org.contestorg.interfaces.ILinker;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;



/**
 * Participant
 */
public class ModelParticipant extends ModelMatchable
{
	
	// Attributs objets
	private ModelPoule poule;
	private ArrayList<ModelParticipation> participations = new ArrayList<ModelParticipation>();
	private ArrayList<ModelPrix> prix = new ArrayList<ModelPrix>();
	private ArrayList<ModelProprietePossedee> proprietesParticipants = new ArrayList<ModelProprietePossedee>();
	
	// Attributs scalaires
	private String stand;
	private String nom;
	private String ville;
	private InfosModelParticipant.Statut statut;
	private String membres;
	private String details;
	
	// Constructeur
	public ModelParticipant(ModelPoule poule, InfosModelParticipant infos) {
		// Retenir la poule
		this.poule = poule;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelParticipant(ModelPoule poule, ModelParticipant participant) {
		// Appeller le constructeur principal
		this(poule, participant.toInfos());
		
		// Récupérer l'id
		this.setId(participant.getId());
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
	public InfosModelParticipant.Statut getStatut () {
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
	public ArrayList<ModelProprietePossedee> getProprietesParticipant () {
		return new ArrayList<ModelProprietePossedee>(this.proprietesParticipants);
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
	public int getNbRencontres (ModelParticipant participant) {
		return this.getNbRencontres(participant, false, true);
	}
	public int getNbRencontres (ModelParticipant participant, boolean phasesEliminatoires, boolean phasesQualificatives) {
		// Initialiser le nombre de rencontres
		int nbRencontres = 0;
		
		// Comptabiliser le nombre de rencontres
		for (ModelParticipation participation : this.participations) {
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				// Récupérer le match
				ModelMatchAbstract match = participation.getMatch();
				
				// Récupérer le participant adverse
				ModelParticipant adversaire = participation.equals(match.getParticipationA()) ? match.getParticipationB().getParticipant() : match.getParticipationA().getParticipant();
				
				// Vérifier si le participant passé en paramètre est le participant adverse
				if (participant == null && adversaire == null || participant != null && participant.equals(adversaire)) {
					// Incrémenter le nombre de rencontres
					nbRencontres++;
				}
			}
		}
		
		// Retourner le nombre de rencontres
		return nbRencontres;
	}
	
	// Setters
	protected void setInfos (InfosModelParticipant infos) {
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
	protected void setStatut (InfosModelParticipant.Statut statut) {
		// Enregistrer le statut
		this.statut = statut;
		
		// Fire update
		this.fireUpdate();
	}
	protected void setPoule (ModelPoule poule) throws ContestOrgModelException {
		// Modifier la poule
		this.poule.removeParticipant(this);
		this.poule = poule;
		this.poule.addParticipant(this);
		
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
			throw new ContestOrgModelException("La participation existe déjà dans le participant");
		}
	}
	public void addPrix (ModelPrix prix) throws ContestOrgModelException {
		if (!this.prix.contains(prix)) {
			// Ajouter le prix
			this.prix.add(prix);
			
			// Fire add
			this.fireAdd(prix, this.prix.size() - 1);
		} else {
			throw new ContestOrgModelException("La participation existe déjà dans le participant");
		}
	}
	public void addProprieteParticipant (ModelProprietePossedee proprieteParticipant) throws ContestOrgModelException {
		if (!this.proprietesParticipants.contains(proprieteParticipant)) {
			// Ajouter la propriété de participant
			this.proprietesParticipants.add(proprieteParticipant);
			
			// Fire add
			this.fireAdd(proprieteParticipant, this.proprietesParticipants.size() - 1);
		} else {
			throw new ContestOrgModelException("La participation existe déjà dans le participant");
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
			throw new ContestOrgModelException("La participation n'existe pas dans le participant");
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
			throw new ContestOrgModelException("La participation n'existe pas dans le participant");
		}
	}
	protected void removeProprieteParticipant (ModelProprietePossedee proprieteParticipant) throws ContestOrgModelException {
		// Retirer la propriété de participant
		int index;
		if ((index = this.proprietesParticipants.indexOf(proprieteParticipant)) != -1) {
			// Remove
			this.proprietesParticipants.remove(proprieteParticipant);
			
			// Fire remove
			this.fireRemove(proprieteParticipant, index);
		} else {
			throw new ContestOrgModelException("La participation n'existe pas dans le paricipant");
		}
	}
	
	// Updaters
	protected void updatePrix (TrackableList<String> list) throws ContestOrgModelException {
		// Mettre à jour les prix remportés
		this.links(new ModelParticipant.LinkerForPrix(this), list);
	}
	protected void updateProprietesParticipant (TrackableList<Pair<String, InfosModelProprieteParticipant>> list) throws ContestOrgModelException {
		// Mettre à jour les propriétés de participant
		this.updates(new ModelProprietePossedee.UpdaterForParticipant(this), this.proprietesParticipants, list, true, null);
	}
	
	// Implémentation de toStrings
	protected String[] toStrings () {
		String[] strings = { this.nom, this.stand, this.ville, this.membres, this.details };
		return strings;
	}
	
	// Clone
	protected ModelParticipant clone (ModelPoule poule) {
		return new ModelParticipant(poule, this);
	}
	
	// ToInformation
	public InfosModelParticipant toInfos () {
		InfosModelParticipant infos = new InfosModelParticipant(this.stand, this.nom, this.ville, this.statut, this.membres, this.details);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter le participant à la liste des removers
			removers.add(this);
			
			// "Supprimer" les participations du participant
			for (ModelParticipation participation : this.participations) {
				if (!removers.contains(participation)) {
					participation.setParticipant(null);
				}
			}
			this.participations.clear();
			this.fireClear(ModelParticipation.class);
			
			// Retirer le participant des prix
			for (ModelPrix prix : this.prix) {
				if (!removers.contains(prix)) {
					prix.removeParticipant(this);
				}
			}
			this.prix.clear();
			this.fireClear(ModelPrix.class);
			
			// Retirer le participant de la poule
			if (this.poule != null) {
				if (!removers.contains(this.poule)) {
					this.poule.removeParticipant(this);
				}
				this.poule = null;
				this.fireClear(ModelPoule.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour une liste de participants indépendament de son conteneur
	protected static class Updater implements IUpdater<Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>>, ModelParticipant>
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
		public ModelParticipant create (Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) {
			try {
				// Récupérer la poule
				ModelPoule poule = this.concours.getCategorieByNom(infos.getFirst()).getPouleByNom(infos.getSecond());
				
				// Créer, configurer et retourner le participant
				ModelParticipant participant = new ModelParticipant(poule, infos.getThird());
				poule.addParticipant(participant);
				participant.updateProprietesParticipant(infos.getFourth());
				participant.updatePrix(infos.getFifth());
				return participant;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un participant.",e);
				return null;
			}
		}
		
		@Override
		public void update (ModelParticipant participant, Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) {
			try {
				// Récupérer la poule
				ModelPoule poule = this.concours.getCategorieByNom(infos.getFirst()).getPouleByNom(infos.getSecond());
				
				// Configurer le participant
				if (!participant.getPoule().equals(poule)) {
					participant.setPoule(poule);
				}
				participant.setInfos(infos.getThird());
				participant.updateProprietesParticipant(infos.getFourth());
				participant.updatePrix(infos.getFifth());
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal(e.getMessage());
			}
		}
	}
	
	// Classe pour lier une liste de prix à un participant
	protected static class LinkerForPrix implements ILinker<String>
	{
		// Participant
		private ModelParticipant participant;
		
		// Constructeur
		public LinkerForPrix(ModelParticipant participant) {
			this.participant = participant;
		}
		
		// Implémentation de link
		@Override
		public void link (String nomPrix) {
			// Récupérer le prix
			ModelPrix prix = this.participant.getPoule().getCategorie().getConcours().getPrixByNom(nomPrix);
			
			// Créer le lien entre le prix et le participant
			if (prix.getParticipants().indexOf(this.participant) == -1 && this.participant.getPrix().indexOf(prix) == -1) {
				try {
					prix.addParticipant(this.participant);
					this.participant.addPrix(prix);
				} catch (ContestOrgModelException e) {
					Log.getLogger().fatal("Erreur lors de la création du lien entre un prix et un participant.");
				}
			}
		}
		
		// Implémentation de unlink
		@Override
		public void unlink (String nomPrix) {
			// Récupérer le prix
			ModelPrix prix = this.participant.getPoule().getCategorie().getConcours().getPrixByNom(nomPrix);
			
			// Supprimer le lien entre le prix et le participant
			if (prix.getParticipants().indexOf(this.participant) != -1 && this.participant.getPrix().indexOf(prix) != -1) {
				try {
					prix.removeParticipant(this.participant);
					this.participant.removePrix(prix);
				} catch (ContestOrgModelException e) {
					Log.getLogger().fatal("Erreur lors de la suppression du lien entre un prix et un participant.");
				}
			}
		}
	}
	
}
