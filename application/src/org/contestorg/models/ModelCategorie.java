package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelPhaseEliminatoires;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.interfaces.IListValidator;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;



/**
 * Conteneur de poules
 */
public class ModelCategorie extends ModelAbstract
{
	
	// Attributs objets
	private ModelConcours concours;
	private ModelPhasesEliminatoires phasesEliminatoires;
	private ArrayList<ModelPoule> poules = new ArrayList<ModelPoule>();
	
	// Attributs scalaires
	private String nom;
	
	// Constructeurs
	public ModelCategorie(ModelConcours concours, InfosModelCategorie infos) {
		// Retenir le concours
		this.concours = concours;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelCategorie(ModelConcours concours, ModelCategorie categorie) {
		// Appeller le constructeur principal
		this(concours, categorie.toInfos());
		
		// Récupérer l'id
		this.setId(categorie.getId());
	}
	
	// Getters
	public String getNom () {
		return this.nom;
	}
	public ModelConcours getConcours () {
		return this.concours;
	}
	public ArrayList<ModelPoule> getPoules () {
		return new ArrayList<ModelPoule>(this.poules);
	}
	public ModelPoule getPouleByNom (String nom) {
		for (ModelPoule poule : this.poules) {
			if (poule.getNom().equals(nom)) {
				return poule;
			}
		}
		return null;
	}
	public ArrayList<ModelParticipant> getParticipants () {
		// Initialiser la liste des participants
		ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		
		// Ajouter les participants
		for (ModelPoule poule : this.poules) {
			participants.addAll(poule.getParticipants());
		}
		
		// Retourner la liste des participants
		return participants;
	}
	public int getNbParticipants() {
		int nbParticipants = 0;
		for(ModelPoule poule : this.poules) {
			nbParticipants += poule.getNbParticipants();
		}
		return nbParticipants;
	}
	public ModelParticipant getParticipantByNom (String nom) {
		for (ModelParticipant participant : this.getParticipants()) {
			if (participant.getNom().equals(nom)) {
				return participant;
			}
		}
		return null;
	}
	public ArrayList<ModelMatchPhasesQualifs> getMatchsPhasesQualifs () {
		ArrayList<ModelMatchPhasesQualifs> matchs = new ArrayList<ModelMatchPhasesQualifs>();
		for (ModelPoule poule : this.poules) {
			matchs.addAll(poule.getMatchsPhasesQualifs());
		}
		return matchs;
	}
	public ModelPhasesEliminatoires getPhasesEliminatoires () {
		return this.phasesEliminatoires;
	}
	public ArrayList<ModelParticipant> getParticipantsParticipants () {
		// Créer et retourner la liste des participants qui peuvent participer
		ArrayList<ModelParticipant> participantes = new ArrayList<ModelParticipant>();
		for (ModelPoule poule : this.poules) {
			participantes.addAll(poule.getParticipantsParticipants());
		}
		return participantes;
	}
	public int getNumero() {
		return this.concours.getCategories().indexOf(this);
	}
	
	// Setters
	protected void setInfos (InfosModelCategorie infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		
		// Fire update
		this.fireUpdate();
	}
	public void setPhasesEliminatoires (ModelPhasesEliminatoires phasesEliminatoires) {
		// Enregistrer la phase éliminatoire
		this.phasesEliminatoires = phasesEliminatoires;
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	public void addPoule (ModelPoule poule) throws ContestOrgModelException {
		if (!this.poules.contains(poule)) {
			// Ajouter la poule
			this.poules.add(poule);
			
			// Fire add
			this.fireAdd(poule, this.poules.size() - 1);
		} else {
			throw new ContestOrgModelException("La poule existe déjà dans la catégorie");
		}
	}
	
	// Removers
	protected void removePoule (ModelPoule poule) throws ContestOrgModelException {
		// Retirer la poule
		int index;
		if ((index = this.poules.indexOf(poule)) != -1) {
			// Remove
			this.poules.remove(poule);
			
			// Fire remove
			this.fireRemove(poule, index);
		} else {
			throw new ContestOrgModelException("La poule n'existe pas dans la catégorie");
		}
	}
	
	// Updaters
	protected void updatePoules(TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list) throws ContestOrgModelException {
		// Mettre à jour la liste des poules
		this.updates(new ModelPoule.UpdaterForCategorie(this), this.poules, list, true, null);
	}
	
	// Actions
	protected void genererPhasesEliminatoires (int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhaseEliminatoires infosPhaseEliminatoire) throws ContestOrgModelException {
		// Supprimer les phases éliminatoires actuelles si nécéssaire
		if (this.phasesEliminatoires != null) {
			// Supprimer les phases éliminatoires
			this.phasesEliminatoires.delete(this);
			
			// Perdre la référence des phases éliminatoires
			this.phasesEliminatoires = null;
		}
		
		// Générer les phases éliminatoires
		this.phasesEliminatoires = ModelPhasesEliminatoires.genererPhasesEliminatoires(this, nbPhases, infosMatchs, infosPhaseEliminatoire);
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelCategorie clone (ModelConcours concours) {
		return new ModelCategorie(concours, this);
	}
	
	// ToInformation
	public InfosModelCategorie toInfos () {
		InfosModelCategorie infos = new InfosModelCategorie(this.nom);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter la catégorie a la liste des removers
			removers.add(this);
			
			// Retirer la catégorie du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removeCategorie(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Supprimer les phases éliminatoires
			if (this.phasesEliminatoires != null) {
				if (!removers.contains(this.phasesEliminatoires)) {
					this.phasesEliminatoires.delete(removers);
				}
				this.phasesEliminatoires = null;
				this.fireClear(ModelPhasesEliminatoires.class);
			}
			
			// Supprimer les poules
			for (ModelPoule poule : this.poules) {
				if (!removers.contains(poule))
					poule.delete(removers);
			}
			this.poules.clear();
			this.fireClear(ModelPoule.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des catégories d'un concours
	protected static class UpdaterForConcours implements IUpdater<InfosModelCategorie, ModelCategorie>
	{
		// Concours
		private ModelConcours concours;
		
		// Constructeur
		protected UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		// Implémentation de create
		@Override
		public ModelCategorie create (InfosModelCategorie infos) {
			try {
				// Créer, configurer et retourner la catégorie
				ModelCategorie categorie = new ModelCategorie(this.concours, infos);
				categorie.addPoule(new ModelPoule(categorie, new InfosModelPoule("Défaut")));
				return categorie;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'une catégorie.",e);
				return null;
			}
		}
		
		// Implémentation de update
		@Override
		public void update (ModelCategorie categorie, InfosModelCategorie infos) {
			categorie.setInfos(infos);
		}
	}
	
	// Classe pour valider les opérations sur la liste des catégories d'un concours
	protected static class ValidatorForConcours implements IListValidator<InfosModelCategorie>
	{
		// Implémentation de IListValidator
		@Override
		public String validateAdd (InfosModelCategorie infos, TrackableList<InfosModelCategorie> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNom().equals(infos.getNom())) {
					return "Une catégorie existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelCategorie infos, TrackableList<InfosModelCategorie> list) {
			if(row == 0) {
				return "La catégorie \""+list.get(row).getNom()+"\" ne peut pas être modifiée.";
			}
			if(infos.getNom().isEmpty()) {
				return "Le nom d'une catégorie ne peut pas être vide.";
			}
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getNom().equals(infos.getNom())) {
					return "Une catégorie existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelCategorie> list) {
			return row == 0 ? "La catégorie \""+list.get(row).getNom()+"\" ne peut pas être supprimée." : null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelCategorie> list) {
			return row == 0 ? "La catégorie \""+list.get(row).getNom()+"\" ne peut pas être déplacée." : null;
		}
	}
	
}
