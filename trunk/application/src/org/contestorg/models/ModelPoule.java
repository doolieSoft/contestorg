package org.contestorg.models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.interfaces.IListValidator;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;



/**
 * Conteneur de participants
 */
public class ModelPoule extends ModelAbstract
{
	
	// Méthodes de génération
	protected final static int MODE_BASIQUE = 1;
	protected final static int MODE_AVANCE = 2;
	
	// Attributs scalaires
	private String nom;
	
	// Attributs objets
	private ModelCategorie categorie;
	private ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
	private ArrayList<ModelPhaseQualificative> phasesQualificatives = new ArrayList<ModelPhaseQualificative>();
	
	// Constructeur
	public ModelPoule(ModelCategorie categorie, InfosModelPoule infos) {
		// Retenir la catégorie
		this.categorie = categorie;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelPoule(ModelCategorie categorie, ModelPoule poule) {
		// Appeller le constructeur principal
		this(categorie, poule.toInfos());
		
		// Récupérer l'id
		this.setId(poule.getId());
	}
	
	// Getters
	public String getNom () {
		return this.nom;
	}
	public ModelCategorie getCategorie () {
		return this.categorie;
	}
	public ArrayList<ModelParticipant> getParticipants () {
		return new ArrayList<ModelParticipant>(this.participants);
	}
	public int getNbParticipants() {
		return this.participants.size();
	}
	public ModelParticipant getParticipantByNom (String nom) {
		for (ModelParticipant participant : this.participants) {
			if (participant.getNom().equals(nom)) {
				return participant;
			}
		}
		return null;
	}
	public ArrayList<ModelPhaseQualificative> getPhasesQualificatives () {
		return this.phasesQualificatives;
	}
	public ArrayList<ModelMatchPhasesQualifs> getMatchsPhasesQualifs () {
		ArrayList<ModelMatchPhasesQualifs> matchs = new ArrayList<ModelMatchPhasesQualifs>();
		for (ModelPhaseQualificative phase : this.phasesQualificatives) {
			matchs.addAll(phase.getMatchs());
		}
		return matchs;
	}
	@SuppressWarnings("unchecked")
	public ArrayList<ModelParticipant> getClassement () {
		// Trier et retourner la liste des participants
		Collections.sort(this.participants, this.categorie.getConcours().getComparateurPhasesQualificatives());
		return this.participants;
	}
	public int getRang (ModelParticipant participant) {
		return this.getRang(participant, -1);
	}
	@SuppressWarnings("unchecked")
	public int getRang (ModelParticipant participantA, int phaseQualifMax) {
		// Retourner -1 si le participant ne fait pas partie de la poule
		if (!this.participants.contains(participantA)) {
			return -1;
		}
		
		// Initialiser le rang
		int rang = 1;
		
		// Récupérer le comparateur des phases qualificatives
		Comparator<ModelParticipant> comparateur = this.categorie.getConcours().getComparateurPhasesQualificatives(phaseQualifMax);
		
		// Comptabiliser le nombre de participants ayant un rang supérieur au participant spécifié
		for (ModelParticipant participantB : this.participants) {
			if (comparateur.compare(participantA, participantB) < 0) {
				rang++;
			}
		}
		
		// Retourner le rang
		return rang;
	}
	public ArrayList<ModelParticipant> getParticipantsParticipants () {
		// Créer la liste des participants qui peuvent participer
		ArrayList<ModelParticipant> participantes = new ArrayList<ModelParticipant>();
		for (ModelParticipant participant : this.participants) {
			if (participant.getStatut().isParticipante()) {
				participantes.add(participant);
			}
		}
		
		// Retourner la liste des participants
		return participantes;
	}
	public int getNumero () {
		return this.categorie.getPoules().indexOf(this);
	}
	
	// Setters
	protected void setInfos (InfosModelPoule infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Retenir les informations
		this.nom = infos.getNom();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	public void addParticipant (ModelParticipant participant) throws ContestOrgModelException {
		if (!this.participants.contains(participant)) {
			// Ajouter le participant
			this.participants.add(participant);
			
			// Fire add
			this.fireAdd(participant, this.participants.size() - 1);
		} else {
			throw new ContestOrgModelException("Le participant existe déjà dans la poule");
		}
	}
	public void addPhaseQualificative (ModelPhaseQualificative phaseQualificative) throws ContestOrgModelException {
		if (!this.phasesQualificatives.contains(phaseQualificative)) {
			// Ajouter la phase qualificative
			this.phasesQualificatives.add(phaseQualificative);
			
			// Fire add
			this.fireAdd(phaseQualificative, this.phasesQualificatives.size() - 1);
		} else {
			throw new ContestOrgModelException("La phase qualificative existe déjà dans la poule");
		}
	}
	
	// Removers
	protected void removeParticipant (ModelParticipant participant) throws ContestOrgModelException {
		// Retirer le participant
		int index;
		if ((index = this.participants.indexOf(participant)) != -1) {
			// Remove
			this.participants.remove(participant);
			
			// Fire remove
			this.fireRemove(participant, index);
		} else {
			throw new ContestOrgModelException("Le participant n'existe pas dans la poule");
		}
	}
	protected void removePhaseQualificative (ModelPhaseQualificative phaseQualificative) throws ContestOrgModelException {
		// Retirer la phase qualificative
		int index;
		if ((index = this.phasesQualificatives.indexOf(phaseQualificative)) != -1) {
			// Remove
			this.phasesQualificatives.remove(phaseQualificative);
			
			// Fire remove
			this.fireRemove(phaseQualificative, index);
		} else {
			throw new ContestOrgModelException("La phase qualificative n'existe pas dans la poule");
		}
	}
	
	// Clone
	protected ModelPoule clone (ModelCategorie categorie) {
		return new ModelPoule(categorie, this);
	}
	
	// ToInformation
	public InfosModelPoule toInfos () {
		InfosModelPoule infos = new InfosModelPoule(this.nom);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter la poule à la liste des removers
			removers.add(this);
			
			// Supprimer les participants de la poule ou les déplacer dans la première poule
			for (ModelParticipant participant : new ArrayList<ModelParticipant>(this.participants)) {
				if (!removers.contains(participant)) {
					ModelPoule poule = this.categorie.getPoules().get(0);
					if(poule.equals(this)) {
						participant.delete(removers);
					} else {
						participant.setPoule(poule);
					}
				}
			}
			this.participants.clear();
			this.fireClear(ModelParticipant.class);
			
			// Retirer la poule de la categorie
			if (this.categorie != null) {
				if (!removers.contains(this.categorie)) {
					this.categorie.removePoule(this);
				}
				this.categorie = null;
				this.fireClear(ModelCategorie.class);
			}
			
			
			// Supprimer les phases qualificatives de la poule
			for (ModelPhaseQualificative phaseQualificative : this.phasesQualificatives) {
				if (!removers.contains(phaseQualificative)) {
					phaseQualificative.delete(removers);
				}
			}
			this.phasesQualificatives.clear();
			this.fireClear(ModelPhaseQualificative.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des poules d'une catégorie et les affectations de participant
	protected static class UpdaterForCategorie implements IUpdater<Pair<InfosModelPoule, ArrayList<String>>, ModelPoule>
	{
		// Catégorie
		private ModelCategorie categorie;
		
		// Constructeur
		public UpdaterForCategorie(ModelCategorie categorie) {
			this.categorie = categorie;
		}
		
		// Implémentation de create
		@Override
		public ModelPoule create (Pair<InfosModelPoule, ArrayList<String>> infos) {
			try {
				// Créer la poule
				ModelPoule poule = new ModelPoule(this.categorie, infos.getFirst());
				
				// Déplacer les participants dans la poule
				for (String participant : infos.getSecond()) {
					this.categorie.getParticipantByNom(participant).setPoule(poule);
				}
				
				// Retourner la poule
				return poule;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'une poule.",e);
				return null;
			}
		}
		
		// Implémentation de update
		@Override
		public void update (ModelPoule poule, Pair<InfosModelPoule, ArrayList<String>> infos) {
			try {
				// Modifier la poule
				poule.setInfos(infos.getFirst());
				
				// Déplacer les participants dans la poule
				for (String participant : infos.getSecond()) {
					if (poule.getParticipantByNom(participant) == null) {
						this.categorie.getParticipantByNom(participant).setPoule(poule);
					}
				}
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'une poule.",e);
			}
		}
		
	}
	
	// Classe pour valider les opérations sur la liste des poules d'une catégorie et les affectations de participant
	protected static class ValidatorForCategorie implements IListValidator<Pair<InfosModelPoule, ArrayList<String>>> {

		// Implémentation de IListValidator
		@Override
		public String validateAdd (Pair<InfosModelPoule, ArrayList<String>> infos, TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Une poule existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, Pair<InfosModelPoule, ArrayList<String>> infos, TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list) {
			if(row == 0) {
				return "La poule \""+list.get(row).getFirst().getNom()+"\" ne peut pas être modifiée.";
			}
			if(infos.getFirst().getNom().isEmpty()) {
				return "Le nom d'une poule ne peut pas être vide.";
			}
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Une poule existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list) {
			return row == 0 ? "La poule \""+list.get(row).getFirst().getNom()+"\" ne peut pas être supprimée." : null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list) {
			return row == 0 ? "La poule \""+list.get(row).getFirst().getNom()+"\" ne peut pas être déplacée." : null;
		}
	}
}
