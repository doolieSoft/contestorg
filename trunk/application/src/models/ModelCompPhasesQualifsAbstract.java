package models;

import infos.InfosModelCompPhasesQualifsAbstract;
import infos.InfosModelCompPhasesQualifsObjectif;
import infos.InfosModelCompPhasesQualifsPoints;
import infos.InfosModelCompPhasesQualifsVictoires;
import interfaces.IListValidator;
import interfaces.IUpdater;

import java.util.ArrayList;

import log.Log;

import common.TrackableList;

public abstract class ModelCompPhasesQualifsAbstract extends ModelAbstract
{
	// Attributs objets
	private ModelConcours concours;
	
	// Constructeur
	public ModelCompPhasesQualifsAbstract(ModelConcours concours) {
		// Retenir le concours
		this.concours = concours;
	}
	
	// Constructeur statique
	protected static ModelCompPhasesQualifsAbstract create (ModelConcours concours, InfosModelCompPhasesQualifsAbstract infos) throws ContestOrgModelException {
		// Creer et retourner le comparateur correspondant aux informations
		if (infos instanceof InfosModelCompPhasesQualifsPoints) {
			return new ModelCompPhasesQualifsPoints(concours, (InfosModelCompPhasesQualifsPoints)infos);
		} else if (infos instanceof InfosModelCompPhasesQualifsObjectif) {
			ModelObjectif objectif = concours.getObjectifByNom(((InfosModelCompPhasesQualifsObjectif)infos).getObjectif().getNom());
			if (objectif != null) {
				ModelCompPhasesQualifsObjectif comparateur = new ModelCompPhasesQualifsObjectif(concours, objectif, (InfosModelCompPhasesQualifsObjectif)infos);
				objectif.addCompPhasesQualifs(comparateur);
				return comparateur;
			} else {
				throw new ContestOrgModelException("L'objectif rattaché au comparateur n'a pas été trouvé dans le concours");
			}
		} else if (infos instanceof InfosModelCompPhasesQualifsVictoires) {
			return new ModelCompPhasesQualifsVictoires(concours, (InfosModelCompPhasesQualifsVictoires)infos);
		}
		
		// Retourner null si les informations ne correspondent pas à un comparateur connu
		return null;
	}
	
	// Getters
	public ModelConcours getConcours () {
		return this.concours;
	}
	
	// "Implémentation" de toInformation
	public abstract InfosModelCompPhasesQualifsAbstract toInformation ();
	
	// Remove
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter le comparateur à la liste des removers
			removers.add(this);
			
			// Retirer le comparateur du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removeCompPhasesQualifs(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
		}
	}
	
	// Classe pour mettre à jour une liste des comparateurs d'un concours
	protected static class UpdaterForConcours implements IUpdater<InfosModelCompPhasesQualifsAbstract, ModelCompPhasesQualifsAbstract>
	{
		// Concours
		private ModelConcours concours;
		
		// Constructeur
		protected UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		// Implémentation de create
		@Override
		public ModelCompPhasesQualifsAbstract create (InfosModelCompPhasesQualifsAbstract infos) {
			try {
				return ModelCompPhasesQualifsAbstract.create(this.concours, infos);
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un comparateur de phases qualificatives.",e);
				return null;
			}
		}
		
		// Implémentation de update
		@Override
		public void update (ModelCompPhasesQualifsAbstract comparateur, InfosModelCompPhasesQualifsAbstract infos) {
			// Un comparateur ne doit pas etre modifié
			Log.getLogger().fatal("Un comparateur ne peut pas etre modifié.");
		}
	}
	
	// Classe pour valider les opérations sur la liste des comparateurs d'un concours
	protected static class ValidatorForConcours implements IListValidator<InfosModelCompPhasesQualifsAbstract>
	{
		// Implémentation de IListValidator
		@Override
		public String validateAdd (InfosModelCompPhasesQualifsAbstract infos, TrackableList<InfosModelCompPhasesQualifsAbstract> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getClass().equals(infos.getClass())) {
					boolean same = true;
					if (list.get(i) instanceof InfosModelCompPhasesQualifsObjectif && infos instanceof InfosModelCompPhasesQualifsObjectif) {
						same = ((InfosModelCompPhasesQualifsObjectif)list.get(i)).getObjectif().getNom().equals(((InfosModelCompPhasesQualifsObjectif)infos).getObjectif().getNom());
					}
					if (same) {
						return "Un comparateur similaire existe déjà.";
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
						return "Un comparateur similaire existe déjà.";
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
