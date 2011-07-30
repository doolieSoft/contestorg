package models;

import infos.InfosModelChemin;
import infos.InfosModelExportation;
import infos.InfosModelTheme;
import interfaces.IListValidator;
import interfaces.IUpdater;

import java.util.ArrayList;

import log.Log;

import common.TrackableList;
import common.Triple;

public class ModelExportation extends ModelAbstract
{
	
	// Attributs objets
	private ModelConcours concours;
	private ModelCheminAbstract chemin;
	private ModelTheme theme;
	
	// Attributs scalaires
	private String nom;
	private boolean auto;
	
	// Constructeur
	public ModelExportation(ModelConcours concours, ModelCheminAbstract chemin, ModelTheme theme, InfosModelExportation infos) {
		// Retenir le concours, le chemin et le theme
		this.concours = concours;
		this.chemin = chemin;
		this.theme = theme;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelExportation(ModelConcours concours,ModelCheminAbstract chemin, ModelTheme theme, ModelExportation exportation) {
		// Appeller le constructeur principal
		this(concours, chemin, theme, exportation.toInformation());
		
		// Récupérer l'id
		this.setId(exportation.getId());
	}
	
	// Getters
	public String getNom () {
		return this.nom;
	}
	public boolean isAutomatique () {
		return this.auto;
	}
	public ModelTheme getTheme () {
		return this.theme;
	}
	public ModelCheminAbstract getChemin () {
		return this.chemin;
	}
	
	// Setters
	protected void setInfos (InfosModelExportation infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		this.auto = infos.isAuto();
		
		// Fire update
		this.fireUpdate();
	}
	protected void setTheme (ModelTheme after) throws ContestOrgModelException {
		// Modifier le thème
		ModelTheme before = this.theme;
		this.theme = after;
		before.delete(this);
		
		// Fire update
		this.fireUpdate();
	}
	protected void setChemin (ModelCheminAbstract after) throws ContestOrgModelException {
		// Modifier le chemin
		ModelCheminAbstract before = this.chemin;
		this.chemin = after;
		before.delete(this);
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelExportation clone (ModelConcours concours, ModelCheminAbstract chemin, ModelTheme ressources) {
		return new ModelExportation(concours, chemin, ressources, this);
	}
	
	// ToInformation
	public InfosModelExportation toInformation () {
		InfosModelExportation infos = new InfosModelExportation(this.nom, this.auto);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter l'horaire aux removers
			removers.add(this);
			
			// Retirer l'exportation du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removeExportation(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Supprimer le chemin
			if (this.chemin != null) {
				if (!removers.contains(this.chemin)) {
					this.chemin.delete(removers);
				}
				this.chemin = null;
				this.fireClear(ModelCheminAbstract.class);
			}
			
			// Supprimer le ressources
			if (this.theme != null) {
				if (!removers.contains(this.theme)) {
					this.theme.delete(removers);
				}
				this.theme = null;
				this.fireClear(ModelTheme.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des exportations d'un concours
	protected static class UpdaterForConcours implements IUpdater<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>, ModelExportation>
	{
		// Concours
		private ModelConcours concours;
		
		// Constructeur
		public UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		// Implémentation de create
		@Override
		public ModelExportation create (Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> infos) {
			return new ModelExportation(this.concours,ModelCheminAbstract.create(infos.getSecond()), new ModelTheme(infos.getThird()), infos.getFirst());
		}
		
		// Implémentation de update
		@Override
		public void update (ModelExportation exportation, Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> infos) {
			try {
				exportation.setInfos(infos.getFirst());
				exportation.setChemin(ModelCheminAbstract.create(infos.getSecond()));
				exportation.getTheme().setInfos(infos.getThird());
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'une exportation.",e);
			}
		}
		
	}
	
	// Classe pour valider les opérations sur la liste des exportations d'un concours
	protected static class ValidatorForConcours implements IListValidator<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>>
	{
		// Implémentation de IListValidator
		@Override
		public String validateAdd (Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> infos, TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Une exportation existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> infos, TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Une exportation existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> list) {
			return null;
		}
	}
	
}
