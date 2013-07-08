package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelCritereClassementAbstract;
import org.contestorg.infos.InfosModelCritereClassementGoalAverage;
import org.contestorg.infos.InfosModelCritereClassementNbDefaites;
import org.contestorg.infos.InfosModelCritereClassementNbEgalites;
import org.contestorg.infos.InfosModelCritereClassementNbForfaits;
import org.contestorg.infos.InfosModelCritereClassementNbPoints;
import org.contestorg.infos.InfosModelCritereClassementNbVictoires;
import org.contestorg.infos.InfosModelCritereClassementQuantiteObjectif;
import org.contestorg.infos.InfosModelCritereClassementRencontresDirectes;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Critère de classement
 */
public abstract class ModelCritereClassementAbstract extends ModelAbstract
{
	// Attributs objets
	
	/** Concours */
	private ModelConcours concours;
	
	// Attributs scalaires
	
	/** Est-ce que le critère est inversé ? */
	protected boolean isInverse;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param concours concours
	 */
	public ModelCritereClassementAbstract(ModelConcours concours) {
		// Retenir le concours
		this.concours = concours;
	}
	
	/**
	 * Constructeur statique
	 * @param concours concours
	 * @param infos informations du critère de classement
	 * @return critère de classement
	 * @throws ContestOrgErrorException
	 */
	protected static ModelCritereClassementAbstract create (ModelConcours concours, InfosModelCritereClassementAbstract infos) throws ContestOrgErrorException {
		// Creer et retourner le critère de classement correspondant aux informations
		if (infos instanceof InfosModelCritereClassementNbPoints) {
			return new ModelCritereClassementNbPoints(concours, (InfosModelCritereClassementNbPoints)infos);
		} else if (infos instanceof InfosModelCritereClassementNbVictoires) {
			return new ModelCritereClassementNbVictoires(concours, (InfosModelCritereClassementNbVictoires)infos);
		} else if (infos instanceof InfosModelCritereClassementNbEgalites) {
			return new ModelCritereClassementNbEgalites(concours, (InfosModelCritereClassementNbEgalites)infos);
		} else if (infos instanceof InfosModelCritereClassementNbDefaites) {
			return new ModelCritereClassementNbDefaites(concours, (InfosModelCritereClassementNbDefaites)infos);
		} else if (infos instanceof InfosModelCritereClassementNbForfaits) {
			return new ModelCritereClassementNbForfaits(concours, (InfosModelCritereClassementNbForfaits)infos);
		} else if (infos instanceof InfosModelCritereClassementRencontresDirectes) {
			return new ModelCritereClassementRencontresDirectes(concours, (InfosModelCritereClassementRencontresDirectes)infos);
		} else if (infos instanceof InfosModelCritereClassementQuantiteObjectif) {
			ModelObjectif objectif = concours.getObjectifByNom(((InfosModelCritereClassementQuantiteObjectif)infos).getObjectif().getNom());
			if (objectif != null) {
				ModelCritereClassementQuantiteObjectif critereClassement = new ModelCritereClassementQuantiteObjectif(concours, objectif, (InfosModelCritereClassementQuantiteObjectif)infos);
				objectif.addCompPhasesQualifs(critereClassement);
				return critereClassement;
			} else {
				throw new ContestOrgErrorException("L'objectif rattaché au critère de classement n'a pas été trouvé dans le concours");
			}
		} else if (infos instanceof InfosModelCritereClassementGoalAverage) {
			ModelObjectif objectif = null;
			if(((InfosModelCritereClassementGoalAverage)infos).getDonnee() == InfosModelCritereClassementGoalAverage.DONNEE_QUANTITE_OBJECTIF) {
				objectif = concours.getObjectifByNom(((InfosModelCritereClassementGoalAverage)infos).getObjectif().getNom());
				if (objectif == null) {
					throw new ContestOrgErrorException("L'objectif rattaché au critère de classement n'a pas été trouvé dans le concours");
				}
			}
			ModelCritereClassementGoalAverage critereClassement = new ModelCritereClassementGoalAverage(concours, objectif, (InfosModelCritereClassementGoalAverage)infos);
			if(objectif != null) {
				objectif.addCompPhasesQualifs(critereClassement);
			}
			return critereClassement;
			
		}
		
		// Retourner null si les informations ne correspondent pas à un critère connu
		return null;
	}
	
	// Getters
	
	/**
	 * Récupérer le concours
	 * @return concours
	 */
	public ModelConcours getConcours () {
		return this.concours;
	}
	
	/**
	 * Est-ce que le critère est inversé ?
	 * @return est-ce que le critère est inversé ?
	 */
	public boolean isInverse() {
		return this.isInverse;
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public abstract InfosModelCritereClassementAbstract getInfos ();
	
	// Setters
	
	/**
	 * Définir les informations du critère de classement
	 * @param infos informations du critère de classement
	 */
	protected void setInfos (InfosModelCritereClassementAbstract infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Retenir les attributs
		this.isInverse = infos.isInverse();
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter le critère de classement à la liste des removers
			removers.add(this);
			
			// Retirer le critère de classement du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removeCritereClassement(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
		}
	}
	
	/**
	 * Classe pour mettre à jour une liste de critères de classement d'un concours
	 */
	protected static class UpdaterForConcours implements IUpdater<InfosModelCritereClassementAbstract, ModelCritereClassementAbstract>
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
		public ModelCritereClassementAbstract create (InfosModelCritereClassementAbstract infos) {
			try {
				return ModelCritereClassementAbstract.create(this.concours, infos);
			} catch (ContestOrgErrorException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un critère de classement.",e);
				return null;
			}
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public ModelCritereClassementAbstract update (ModelCritereClassementAbstract critereClassement, InfosModelCritereClassementAbstract infos) {
			try {
				return ModelCritereClassementAbstract.create(this.concours, infos);
			} catch (ContestOrgErrorException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'un critère de classement.",e);
			}
			return null;
		}
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des critères de classement d'un concours
	 */
	protected static class ValidatorForConcours implements ITrackableListValidator<InfosModelCritereClassementAbstract>
	{
		// Implémentation de ITrackableListValidator
		@Override
		public String validateAdd (InfosModelCritereClassementAbstract infos, TrackableList<InfosModelCritereClassementAbstract> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(infos) && !list.get(i).isUtilisablePlusieursFois()) {
					return "Un critère de classement similaire existe déjà.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelCritereClassementAbstract infos, TrackableList<InfosModelCritereClassementAbstract> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).equals(infos) && !list.get(i).isUtilisablePlusieursFois()) {
					return "Un critère de classement similaire existe déjà.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelCritereClassementAbstract> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelCritereClassementAbstract> list) {
			return null;
		}
	}
}
