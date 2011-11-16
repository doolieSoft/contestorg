package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.infos.InfosModelProprieteEquipe;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;



public class ModelProprietePossedee extends ModelAbstract
{
	
	// Attributs objets
	private ModelPropriete propriete;
	private ModelEquipe equipe;
	
	// Attributs scalaires
	private String value;
	
	// Constructeur
	public ModelProprietePossedee(ModelPropriete propriete, ModelEquipe equipe, InfosModelProprieteEquipe infos) {
		// Retenir la propriété et l'équipe
		this.propriete = propriete;
		this.equipe = equipe;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelProprietePossedee(ModelPropriete propriete, ModelEquipe equipe, ModelProprietePossedee proprieteEquipe) {
		// Appeller le constructeur principal
		this(propriete, equipe, proprieteEquipe.toInformation());
		
		// Récupérer l'id
		this.setId(proprieteEquipe.getId());
	}
	
	// Getters
	public String getValue () {
		return this.value;
	}
	public ModelPropriete getPropriete () {
		return this.propriete;
	}
	public ModelEquipe getEquipe () {
		return this.equipe;
	}
	
	// Setters
	protected void setInfos (InfosModelProprieteEquipe infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.value = infos.getValue();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelProprietePossedee clone (ModelPropriete propriete, ModelEquipe equipe) {
		return new ModelProprietePossedee(propriete, equipe, this);
	}
	
	// ToInformation
	public InfosModelProprieteEquipe toInformation () {
		InfosModelProprieteEquipe infos = new InfosModelProprieteEquipe(this.value);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter la propriété d'équipe à la liste des removers
			removers.add(this);
			
			// Retirer la propriété d'équipe de la propriété
			if (this.propriete != null) {
				if (!removers.contains(this.propriete)) {
					this.propriete.removeProprieteEquipe(this);
				}
				this.propriete = null;
				this.fireClear(ModelPropriete.class);
			}
			
			// Retirer la propriété d'équipe de l'équipe
			if (this.equipe != null) {
				if (!removers.contains(this.equipe)) {
					this.equipe.removeProprieteEquipe(this);
				}
				this.equipe = null;
				this.fireClear(ModelEquipe.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des propriétés d'équipe d'une équipe
	protected static class UpdaterForEquipe implements IUpdater<Pair<String, InfosModelProprieteEquipe>, ModelProprietePossedee>
	{
		
		// Equipe
		private ModelEquipe equipe;
		
		// Constructeur
		public UpdaterForEquipe(ModelEquipe equipe) {
			// Retenir l'équipe
			this.equipe = equipe;
		}
		
		// Implémentation de create
		@Override
		public ModelProprietePossedee create (Pair<String, InfosModelProprieteEquipe> infos) {
			try {
				// Récupérer la proprieté
				ModelPropriete propriete = this.equipe.getPoule().getCategorie().getConcours().getProprieteByNom(infos.getFirst());
				
				// Créer la propriété d'équipe
				ModelProprietePossedee proprieteEquipe = new ModelProprietePossedee(propriete, this.equipe, infos.getSecond());
				propriete.addProprieteEquipe(proprieteEquipe);
				
				// Retourner la propriété d'équipe
				return proprieteEquipe;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'une propriété possédée.",e);
				return null;
			}
		}
		
		// Implémentation de update
		@Override
		public void update (ModelProprietePossedee proprieteEquipe, Pair<String, InfosModelProprieteEquipe> infos) {
			// Mettre à jour les information de la propriété d'équipe
			proprieteEquipe.setInfos(infos.getSecond());
		}
	}
	
}
