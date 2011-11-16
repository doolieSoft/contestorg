package models;

import infos.InfosModelPrix;
import interfaces.IListValidator;
import interfaces.IUpdater;

import java.util.ArrayList;

import common.TrackableList;

/**
 * Prix pouvant être decerné aux équipes
 */
public class ModelPrix extends ModelAbstract
{
	
	// Attributs objets
	private ModelConcours concours;
	private ArrayList<ModelEquipe> equipes = new ArrayList<ModelEquipe>();
	
	// Attributs scalaires
	private String nom;
	
	// Constructeur
	public ModelPrix(ModelConcours concours, InfosModelPrix infos) {
		// Retenir le concours
		this.concours = concours;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelPrix(ModelConcours concours, ModelPrix prix) {
		// Appeller le constructeur principal
		this(concours, prix.toInformation());
		
		// Récupérer l'id
		this.setId(prix.getId());
	}
	
	// Getters
	public String getNom () {
		return this.nom;
	}
	public ModelConcours getConcours () {
		return this.concours;
	}
	public ArrayList<ModelEquipe> getEquipes () {
		return new ArrayList<ModelEquipe>(this.equipes);
	}
	
	// Setters
	protected void setInfos (InfosModelPrix infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	public void addEquipe (ModelEquipe equipe) throws ContestOrgModelException {
		if (!this.equipes.contains(equipe)) {
			// Enregistrer l'équipe
			this.equipes.add(equipe);
			
			// Fire add
			this.fireAdd(equipe, this.equipes.size() - 1);
		} else {
			throw new ContestOrgModelException("L'équipe existe déjà dans le prix");
		}
	}
	
	// Removers
	protected void removeEquipe (ModelEquipe equipe) throws ContestOrgModelException {
		// Supprimer l'équipe
		int index;
		if ((index = this.equipes.indexOf(equipe)) != -1) {
			// Remove
			this.equipes.remove(equipe);
			
			// Fire remove
			this.fireRemove(equipe, index);
		} else {
			throw new ContestOrgModelException("L'équipe n'existe pas dans le prix");
		}
	}
	
	// Clone
	protected ModelPrix clone (ModelConcours concours) {
		return new ModelPrix(concours, this);
	}
	
	// ToInformation
	public InfosModelPrix toInformation () {
		InfosModelPrix infos = new InfosModelPrix(this.nom);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter la poule à la liste des removers
			removers.add(this);
			
			// Retirer le prix du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removePrix(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Retirer le prix des équipes
			for (ModelEquipe equipe : this.equipes) {
				if (!removers.contains(equipe)) {
					equipe.removePrix(this);
				}
			}
			this.equipes.clear();
			this.fireClear(ModelEquipe.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des prix d'un concours
	protected static class UpdaterForConcours implements IUpdater<InfosModelPrix, ModelPrix>
	{
		// Concours
		private ModelConcours concours;
		
		// Constructeur
		protected UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		// Implémentation de create
		@Override
		public ModelPrix create (InfosModelPrix infos) {
			return new ModelPrix(this.concours, infos);
		}
		
		// Implémentation de update
		@Override
		public void update (ModelPrix prix, InfosModelPrix infos) {
			prix.setInfos(infos);
		}
		
	}
	
	// Classe pour valider les opérations sur la liste des prix d'un concours
	protected static class ValidatorForConcours implements IListValidator<InfosModelPrix>
	{
		// Implémentation de IListValidator
		@Override
		public String validateAdd (InfosModelPrix infos, TrackableList<InfosModelPrix> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNom().equals(infos.getNom())) {
					return "Un prix existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelPrix infos, TrackableList<InfosModelPrix> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getNom().equals(infos.getNom())) {
					return "Un prix existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelPrix> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelPrix> list) {
			return null;
		}
	}
	
}
