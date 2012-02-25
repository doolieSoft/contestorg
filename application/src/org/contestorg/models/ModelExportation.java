package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Exportation
 */
public class ModelExportation extends ModelAbstract
{
	
	// Attributs objets
	
	/** Concours */
	private ModelConcours concours;
	
	/** Chemin */
	private ModelCheminAbstract chemin;
	
	/** Thème */
	private ModelTheme theme;
	
	// Attributs scalaires
	
	/** Nom */
	private String nom;
	
	/** Exportation automatique ? */
	private boolean auto;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param chemin chemin
	 * @param theme thème
	 * @param infos informations de l'exportation
	 */
	public ModelExportation(ModelConcours concours, ModelCheminAbstract chemin, ModelTheme theme, InfosModelExportation infos) {
		// Retenir le concours, le chemin et le theme
		this.concours = concours;
		this.chemin = chemin;
		this.theme = theme;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param concours concours
	 * @param chemin chemin
	 * @param theme thème
	 * @param exportation exportation
	 */
	protected ModelExportation(ModelConcours concours,ModelCheminAbstract chemin, ModelTheme theme, ModelExportation exportation) {
		// Appeller le constructeur principal
		this(concours, chemin, theme, exportation.getInfos());
		
		// Récupérer l'id
		this.setId(exportation.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}
	
	/**
	 * Vérifier s'il s'agit d'une exportation automatique
	 * @return exportation automatique ?
	 */
	public boolean isAuto () {
		return this.auto;
	}
	
	/**
	 * Récupérer le thème
	 * @return thème
	 */
	public ModelTheme getTheme () {
		return this.theme;
	}
	
	/**
	 * Récupérer le chemin
	 * @return chemin
	 */
	public ModelCheminAbstract getChemin () {
		return this.chemin;
	}
	
	// Setters
	
	/**
	 * Définir les informations de l'exportation
	 * @param infos informations de l'exportation
	 */
	protected void setInfos (InfosModelExportation infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		this.auto = infos.isAuto();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir le thème
	 * @param theme thème
	 * @throws ContestOrgModelException
	 */
	protected void setTheme (ModelTheme theme) throws ContestOrgModelException {
		// Modifier le thème
		ModelTheme before = this.theme;
		this.theme = theme;
		before.delete(this);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir le chemin
	 * @param chemin chemin
	 * @throws ContestOrgModelException
	 */
	protected void setChemin (ModelCheminAbstract chemin) throws ContestOrgModelException {
		// Modifier le chemin
		ModelCheminAbstract before = this.chemin;
		this.chemin = chemin;
		before.delete(this);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Cloner l'exportation
	 * @param concours concours
	 * @param chemin chemin
	 * @param theme thème
	 * @return clone de l'exportation
	 */
	protected ModelExportation clone (ModelConcours concours, ModelCheminAbstract chemin, ModelTheme theme) {
		return new ModelExportation(concours, chemin, theme, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelExportation getInfos () {
		InfosModelExportation infos = new InfosModelExportation(this.nom, this.auto);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
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
	
	/**
	 * Classe pour mettre à jour la liste des exportations d'un concours
	 */
	protected static class UpdaterForConcours implements IUpdater<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>, ModelExportation>
	{
		/** Concours */
		private ModelConcours concours;
		
		/**
		 * Constructeur
		 * @param concours concours
		 */
		public UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelExportation create (Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> infos) {
			return new ModelExportation(this.concours,ModelCheminAbstract.create(infos.getSecond()), new ModelTheme(infos.getThird()), infos.getFirst());
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
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
	
	/**
	 * Classe pour valider les opérations sur la liste des exportations d'un concours
	 */
	protected static class ValidatorForConcours implements ITrackableListValidator<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>>
	{
		// Implémentation de ITrackableListValidator
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
