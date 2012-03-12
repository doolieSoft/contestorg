package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelObjectifNul;
import org.contestorg.infos.InfosModelObjectifPoints;
import org.contestorg.infos.InfosModelObjectifPourcentage;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;


/**
 * Objectif
 */
public class ModelObjectif extends ModelAbstract
{
	
	// Attributs objets
	
	/** Concours */
	private ModelConcours concours;
	
	/** Liste des objectifs remportés */
	private ArrayList<ModelObjectifRemporte> objectifsRemportes = new ArrayList<ModelObjectifRemporte>();
	
	/** Liste des critères de classement */
	private ArrayList<ModelCompPhasesQualifsObjectif> compsPhasesQualifs = new ArrayList<ModelCompPhasesQualifsObjectif>();
	
	// Types d'objectifs possible
	
	/** Objectif à points */
	public static final int TYPE_POINTS = 1;
	
	/** Objectif à pourcentage */
	public static final int TYPE_POURCENTAGE = 2;
	
	/** Objectif nul */
	protected static final int TYPE_NUL = 3;
	
	// Attributs scalaires
	
	/** Nom */
	private String nom;
	
	/** Type */
	private int type;
	
	/** Points (s'il s'agit d'un objectif à points) */
	private double points_points;
	
	/** Borne de participation (s'il s'agit d'un objectif à points) */
	private Double points_borneParticipation;
	
	/** Pourcentage (s'il s'agit d'un objectif à pourcentage) */
	private double pourcentage_pourcentage;
	
	/** Borne de participation (s'il s'agit d'un objectif à pourcentage) */
	private Double pourcentage_borneParticipation;
	
	/** Borne d'augmentation (s'il s'agit d'un objectif à pourcentage) */
	private Double pourcentage_borneAugmentation;
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param infos informations de l'objectif
	 */
	public ModelObjectif(ModelConcours concours, InfosModelObjectif infos) {
		// Retenir le concours
		this.concours = concours;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param concours concours
	 * @param objectif objectif
	 */
	protected ModelObjectif(ModelConcours concours, ModelObjectif objectif) {
		// Appeller le constructeur principal
		this(concours, objectif.getInfos());
		
		// Récupérer l'id
		this.setId(objectif.getId());
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
	 * Récupérer le type
	 * @return type
	 */
	public int getType () {
		return this.type;
	}
	
	/**
	 * Récupérer le concours
	 * @return concours
	 */
	public ModelConcours getConcours () {
		return this.concours;
	}
	
	/**
	 * Récupérer la liste des objectifs remportés
	 * @return liste des objectifs remportés
	 */
	public ArrayList<ModelObjectifRemporte> getObjectifsRemportes () {
		return new ArrayList<ModelObjectifRemporte>(this.objectifsRemportes);
	}
	
	/**
	 * Récupérer la liste des critères de classement des phases qualificatives
	 * @return liste des critères de classement des phases qualificatives
	 */
	public ArrayList<ModelCompPhasesQualifsObjectif> getCompPhasesQualifs () {
		return new ArrayList<ModelCompPhasesQualifsObjectif>(this.compsPhasesQualifs);
	}
	
	// Setters
	
	/**
	 * Définir les informations de l'objectif
	 * @param infos informations de l'objectif
	 */
	protected void setInfos (InfosModelObjectif infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		if (infos instanceof InfosModelObjectifPoints) {
			this.type = TYPE_POINTS;
			this.points_points = ((InfosModelObjectifPoints)infos).getPoints();
			this.points_borneParticipation = ((InfosModelObjectifPoints)infos).getBorneParticipation();
		} else if (infos instanceof InfosModelObjectifPourcentage) {
			this.type = TYPE_POURCENTAGE;
			this.pourcentage_pourcentage = ((InfosModelObjectifPourcentage)infos).getPourcentage();
			this.pourcentage_borneParticipation = ((InfosModelObjectifPourcentage)infos).getBorneParticipation();
			this.pourcentage_borneAugmentation = ((InfosModelObjectifPourcentage)infos).getBorneAugmentation();
		} else if (infos instanceof InfosModelObjectifNul) {
			this.type = TYPE_NUL;
		}
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	
	/**
	 * Ajouter un objectif remporté
	 * @param objectifRemporte objectif remporté
	 * @throws ContestOrgErrorException
	 */
	public void addObjectifRemporte (ModelObjectifRemporte objectifRemporte) throws ContestOrgErrorException {
		if (!this.objectifsRemportes.contains(objectifRemporte)) {
			// Ajouter l'objectif remporte
			this.objectifsRemportes.add(objectifRemporte);
			
			// Fire add
			this.fireAdd(objectifRemporte, this.objectifsRemportes.size() - 1);
		} else {
			throw new ContestOrgErrorException("L'objectif remporté existe déjà dans l'objectif");
		}
	}
	
	/**
	 * Ajouter un critère de classement des phases qualificatives
	 * @param compPhasesQualifs critère de classement des phases qualificatives
	 * @throws ContestOrgErrorException
	 */
	public void addCompPhasesQualifs (ModelCompPhasesQualifsObjectif compPhasesQualifs) throws ContestOrgErrorException {
		if (!this.compsPhasesQualifs.contains(compPhasesQualifs)) {
			// Ajouter l'objectif remporte
			this.compsPhasesQualifs.add(compPhasesQualifs);
			
			// Fire add
			this.fireAdd(compPhasesQualifs, this.compsPhasesQualifs.size() - 1);
		} else {
			throw new ContestOrgErrorException("Le comparateur pour phases qualificatives existe déjà dans l'objectif");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer un objectif remporté
	 * @param objectifRemporte objectif remporté
	 * @throws ContestOrgErrorException
	 */
	protected void removeObjectifRemporte (ModelObjectifRemporte objectifRemporte) throws ContestOrgErrorException {
		// Retirer l'objectif remporte
		int index;
		if ((index = this.objectifsRemportes.indexOf(objectifRemporte)) != -1) {
			// Remove
			this.objectifsRemportes.remove(objectifRemporte);
			
			// Fire remove
			this.fireRemove(objectifRemporte, index);
		} else {
			throw new ContestOrgErrorException("L'objectif remporté n'existe pas dans l'objectif");
		}
	}
	
	/**
	 * Supprimer un critère de classement des phases qualificatives
	 * @param compPhasesQualifs critère de classement des phases qualificatives
	 * @throws ContestOrgErrorException
	 */
	protected void removeCompPhasesQualifs (ModelCompPhasesQualifsObjectif compPhasesQualifs) throws ContestOrgErrorException {
		// Retirer l'objectif remporte
		int index;
		if ((index = this.compsPhasesQualifs.indexOf(compPhasesQualifs)) != -1) {
			// Remove
			this.compsPhasesQualifs.remove(compPhasesQualifs);
			
			// Fire remove
			this.fireRemove(compPhasesQualifs, index);
		} else {
			throw new ContestOrgErrorException("Le comparateur pour phases qualificatives n'existe pas dans l'objectif");
		}
	}
	
	/**
	 * Cloner l'objectif
	 * @param concours concours
	 * @return clone de l'objectif
	 */
	protected ModelObjectif clone (ModelConcours concours) {
		return new ModelObjectif(concours, this.getInfos());
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelObjectif getInfos () {
		InfosModelObjectif infos = null;
		switch (this.type) {
			case TYPE_POINTS:
				infos = new InfosModelObjectifPoints(this.nom, this.points_points, this.points_borneParticipation);
				break;
			case TYPE_POURCENTAGE:
				infos = new InfosModelObjectifPourcentage(this.nom, this.pourcentage_pourcentage, this.pourcentage_borneParticipation, this.pourcentage_borneAugmentation);
				break;
			case TYPE_NUL:
				infos = new InfosModelObjectifNul(this.nom);
				break;
		}
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * Incrémenter le nombre de points d'une participation
	 * @param points nombre de points d'une participation
	 * @return nombre de points de la participation incrémenté
	 */
	protected double operation (double points) {
		switch (this.type) {
			case TYPE_POINTS:
				// Calculer le nombre de points majorée de l'augmentation
				points += this.points_points;
				
				// Vérifier si le nombre de point n'a pas dépassé la borne
				if (this.points_borneParticipation != null && (this.points_points < 0 && points < this.points_borneParticipation || this.points_points > 0 && points > this.points_borneParticipation)) {
					points = this.points_borneParticipation;
				}
				
				// Retourner le nombre de points
				return points;
			case TYPE_POURCENTAGE:
				// Calculer l'augmentation du nombre de points
				double augmentation = this.pourcentage_pourcentage/100 * points;
				
				// Vérifier si l'augmentation ne fait pas dépassé la borne
				if (this.pourcentage_borneAugmentation != null && (this.pourcentage_pourcentage < 0 && augmentation < this.pourcentage_borneAugmentation || this.pourcentage_pourcentage > 0 && augmentation > this.pourcentage_borneAugmentation)) {
					augmentation = this.pourcentage_borneAugmentation;
				}
				
				// Calculer le nombre de points majorée de l'augmentation
				points += augmentation;
				
				// Vérifier si le nombre de point n'a pas dépassé la borne
				if (this.pourcentage_borneParticipation != null && (this.pourcentage_pourcentage < 0 && points < this.pourcentage_borneParticipation || this.pourcentage_pourcentage > 0 && points > this.pourcentage_borneParticipation)) {
					points = this.pourcentage_borneParticipation;
				}
				
				// Retourner le nombre de points
				return points;
			case TYPE_NUL:
				// Retourner le nombre de points du participant sans aucune modification
				return points;
		}
		return points;
	}
	
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter l'objectif à la liste des removers
			removers.add(this);
			
			// Retirer l'objectif du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removeObjectif(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Supprimer les objectifs remportés
			for (ModelObjectifRemporte objectifRemporte : this.objectifsRemportes) {
				if (!removers.contains(objectifRemporte)) {
					objectifRemporte.delete(removers);
				}
			}
			this.objectifsRemportes.clear();
			this.fireClear(ModelParticipation.class);
			
			// Supprimer les comparateurs
			for (ModelCompPhasesQualifsObjectif compPhasesQualifs : this.compsPhasesQualifs) {
				if (!removers.contains(compPhasesQualifs)) {
					compPhasesQualifs.delete(removers);
				}
			}
			this.compsPhasesQualifs.clear();
			this.fireClear(ModelCompPhasesQualifsObjectif.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des objectifs d'un concours
	 */
	protected static class UpdaterForConcours implements IUpdater<InfosModelObjectif, ModelObjectif>
	{
		/** Concours */
		private ModelConcours concours;
		
		/**
		 * Constructeur
		 * @param concours concours
		 */
		protected UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelObjectif create (InfosModelObjectif infos) {
			return new ModelObjectif(this.concours, infos);
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelObjectif objectif, InfosModelObjectif infos) {
			objectif.setInfos(infos);
		}
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des objectifs d'un concours
	 */
	protected static class ValidatorForConcours implements ITrackableListValidator<InfosModelObjectif>
	{
		// Implémentation de ITrackableListValidator
		@Override
		public String validateAdd (InfosModelObjectif infos, TrackableList<InfosModelObjectif> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNom().equals(infos.getNom())) {
					return "Un objectif existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelObjectif infos, TrackableList<InfosModelObjectif> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getNom().equals(infos.getNom())) {
					return "Un objectif existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelObjectif> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelObjectif> list) {
			return null;
		}
	}
	
}
