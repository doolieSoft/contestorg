package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Objectif remporté
 */
public class ModelObjectifRemporte extends ModelAbstract
{
	
	// Attributs objets
	
	/** Participation */
	private ModelParticipation participation;
	
	/** Objectif */
	private ModelObjectif objectif;
	
	// Attributs scalaires
	
	/** Quantité */
	private int quantite;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param participation participation
	 * @param objectif objectif
	 * @param infos informations de l'objectif remporté
	 */
	public ModelObjectifRemporte(ModelParticipation participation, ModelObjectif objectif, InfosModelObjectifRemporte infos) {
		// Retenir la participation et l'objectif
		this.participation = participation;
		this.objectif = objectif;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param participation participation
	 * @param objectif objectif
	 * @param objectifRemporte objectif remporté
	 */
	protected ModelObjectifRemporte(ModelParticipation participation, ModelObjectif objectif, ModelObjectifRemporte objectifRemporte) {
		// Appeller le constructeur parent
		this(participation, objectif, objectifRemporte.getInfos());
		
		// Récupérer l'id
		this.setId(objectifRemporte.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer la quantité
	 * @return quantité
	 */
	public int getQuantite () {
		return this.quantite;
	}
	
	/**
	 * Récupérer la participation
	 * @return participation
	 */
	public ModelParticipation getParticipation () {
		return this.participation;
	}
	
	/**
	 * Récupérer l'objectif
	 * @return objectif
	 */
	public ModelObjectif getObjectif () {
		return this.objectif;
	}
	
	// Setters
	
	/**
	 * Définir les informations de l'objectif remporté
	 * @param infos informations de l'objectif remporté
	 */
	protected void setInfos (InfosModelObjectifRemporte infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.quantite = infos.getQuantite();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir l'objectif
	 * @param objectif objectif
	 * @throws ContestOrgModelException
	 */
	protected void setObjectif (ModelObjectif objectif) throws ContestOrgModelException {
		this.objectif.removeObjectifRemporte(this);
		this.objectif = objectif;
		this.objectif.addObjectifRemporte(this);
		this.fireUpdate();
	}
	
	/**
	 * Cloner l'objectif remporté
	 * @param participation participation
	 * @param objectif objectif
	 * @return clone de l'objectif remporté
	 */
	protected ModelObjectifRemporte clone (ModelParticipation participation, ModelObjectif objectif) {
		return new ModelObjectifRemporte(participation, objectif, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelObjectifRemporte getInfos () {
		InfosModelObjectifRemporte infos = new InfosModelObjectifRemporte(this.quantite);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter l'objectif remporte à la liste des removers
			removers.add(this);
			
			// Retirer l'objectif remporte de l'objectif
			if (this.objectif != null) {
				if (!removers.contains(this.objectif)) {
					this.objectif.removeObjectifRemporte(this);
				}
				this.objectif = null;
				this.fireClear(ModelObjectif.class);
			}
			
			// Retirer l'objectif remporte de la participation
			if (this.participation != null) {
				if (!removers.contains(this.participation)) {
					this.participation.removeObjectifRemporte(this);
				}
				this.participation = null;
				this.fireClear(ModelParticipation.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des objectif remportés d'une participation
	 */
	protected static class UpdaterForParticipation implements IUpdater<Pair<String, InfosModelObjectifRemporte>,ModelObjectifRemporte>
	{
		/** Concours */
		private ModelConcours concours;
		
		/** Participation */
		private ModelParticipation participation;
		
		/**
		 * Constructeur
		 * @param concours concours participation
		 * @param participation
		 */
		public UpdaterForParticipation(ModelConcours concours,ModelParticipation participation) {
			this.concours = concours;
			this.participation = participation;
		}

		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelObjectifRemporte create (Pair<String, InfosModelObjectifRemporte> infos) {
			try {
				// Récupérer l'objectif
				ModelObjectif objectif = this.concours.getObjectifByNom(infos.getFirst());
				
				// Créer et retourner l'objectif remporté
				ModelObjectifRemporte objectifRemporte = new ModelObjectifRemporte(this.participation, objectif, infos.getSecond());
				objectif.addObjectifRemporte(objectifRemporte);
				return objectifRemporte;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un objectif remporté.",e);
				return null;
			}
		}

		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelObjectifRemporte objectifRemporte, Pair<String, InfosModelObjectifRemporte> infos) {
			try {
				// Changer l'objectif si nécéssaire
				if(!objectifRemporte.getObjectif().getNom().equals(infos.getFirst())) {
					objectifRemporte.setObjectif(this.concours.getObjectifByNom(infos.getFirst()));
				}
				
				// Modifier les informations
				objectifRemporte.setInfos(infos.getSecond());
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'un objectif remporté.",e);
			}
		}
		
	}
	
}
