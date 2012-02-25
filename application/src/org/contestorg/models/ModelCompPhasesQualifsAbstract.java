package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelCompPhasesQualifsObjectif;
import org.contestorg.infos.InfosModelCompPhasesQualifsPoints;
import org.contestorg.infos.InfosModelCompPhasesQualifsVictoires;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Critère de classement des phases qualificatives
 */
public abstract class ModelCompPhasesQualifsAbstract extends ModelAbstract
{
	// Attributs objets
	
	/** Concours */
	private ModelConcours concours;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param concours concours
	 */
	public ModelCompPhasesQualifsAbstract(ModelConcours concours) {
		// Retenir le concours
		this.concours = concours;
	}
	
	/**
	 * Constructeur statique
	 * @param concours concours
	 * @param infos informations du critère de classement
	 * @return critère de classement
	 * @throws ContestOrgModelException
	 */
	protected static ModelCompPhasesQualifsAbstract create (ModelConcours concours, InfosModelCompPhasesQualifsAbstract infos) throws ContestOrgModelException {
		// Creer et retourner le critere correspondant aux informations
		if (infos instanceof InfosModelCompPhasesQualifsPoints) {
			return new ModelCompPhasesQualifsPoints(concours, (InfosModelCompPhasesQualifsPoints)infos);
		} else if (infos instanceof InfosModelCompPhasesQualifsObjectif) {
			ModelObjectif objectif = concours.getObjectifByNom(((InfosModelCompPhasesQualifsObjectif)infos).getObjectif().getNom());
			if (objectif != null) {
				ModelCompPhasesQualifsObjectif critere = new ModelCompPhasesQualifsObjectif(concours, objectif, (InfosModelCompPhasesQualifsObjectif)infos);
				objectif.addCompPhasesQualifs(critere);
				return critere;
			} else {
				throw new ContestOrgModelException("L'objectif rattaché au critere de classement n'a pas été trouvé dans le concours");
			}
		} else if (infos instanceof InfosModelCompPhasesQualifsVictoires) {
			return new ModelCompPhasesQualifsVictoires(concours, (InfosModelCompPhasesQualifsVictoires)infos);
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
	 * @see ModelAbstract#getInfos()
	 */
	public abstract InfosModelCompPhasesQualifsAbstract getInfos ();
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter le critere à la liste des removers
			removers.add(this);
			
			// Retirer le critere du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removeCompPhasesQualifs(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
		}
	}
	
	/**
	 * Classe pour mettre à jour une liste de critères de classement d'un concours
	 */
	protected static class UpdaterForConcours implements IUpdater<InfosModelCompPhasesQualifsAbstract, ModelCompPhasesQualifsAbstract>
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
		public ModelCompPhasesQualifsAbstract create (InfosModelCompPhasesQualifsAbstract infos) {
			try {
				return ModelCompPhasesQualifsAbstract.create(this.concours, infos);
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un critère de classement.",e);
				return null;
			}
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelCompPhasesQualifsAbstract critere, InfosModelCompPhasesQualifsAbstract infos) {
			// Un critère ne doit pas etre modifié
			Log.getLogger().fatal("Un critère de classement ne peut pas etre modifié.");
		}
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des critères de classement d'un concours
	 */
	protected static class ValidatorForConcours implements ITrackableListValidator<InfosModelCompPhasesQualifsAbstract>
	{
		// Implémentation de ITrackableListValidator
		@Override
		public String validateAdd (InfosModelCompPhasesQualifsAbstract infos, TrackableList<InfosModelCompPhasesQualifsAbstract> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getClass().equals(infos.getClass())) {
					boolean same = true;
					if (list.get(i) instanceof InfosModelCompPhasesQualifsObjectif && infos instanceof InfosModelCompPhasesQualifsObjectif) {
						same = ((InfosModelCompPhasesQualifsObjectif)list.get(i)).getObjectif().getNom().equals(((InfosModelCompPhasesQualifsObjectif)infos).getObjectif().getNom());
					}
					if (same) {
						return "Un critère de classement similaire existe déjà.";
					}
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelCompPhasesQualifsAbstract infos, TrackableList<InfosModelCompPhasesQualifsAbstract> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getClass().equals(infos.getClass())) {
					boolean same = true;
					if (list.get(i) instanceof InfosModelCompPhasesQualifsObjectif && infos instanceof InfosModelCompPhasesQualifsObjectif) {
						same = ((InfosModelCompPhasesQualifsObjectif)list.get(i)).getObjectif().getNom().equals((InfosModelCompPhasesQualifsObjectif)infos);
					}
					if (same) {
						return "Un critère de classement similaire existe déjà.";
					}
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelCompPhasesQualifsAbstract> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelCompPhasesQualifsAbstract> list) {
			return null;
		}
	}
}
