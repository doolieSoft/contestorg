﻿package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.interfaces.IListValidator;
import org.contestorg.interfaces.IUpdater;


/**
 * Propriété qu'un participant peut avoir
 */
public class ModelPropriete extends ModelAbstract
{
	
	// Attributs objets
	private ModelConcours concours;
	private ArrayList<ModelProprietePossedee> proprietesParticipants = new ArrayList<ModelProprietePossedee>();
	
	// Attributs scalaires
	private String nom;
	private int type;
	private boolean obligatoire;
	
	// Constructeur
	public ModelPropriete(ModelConcours concours, InfosModelPropriete infos) {
		// Retenir le concours
		this.concours = concours;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelPropriete(ModelConcours concours, ModelPropriete propriete) {
		// Appeller le constructeur principal
		this(concours, propriete.toInfos());
		
		// Récupérer l'id
		this.setId(propriete.getId());
	}
	
	// Getters
	public String getNom () {
		return this.nom;
	}
	public int getType () {
		return this.type;
	}
	public boolean isObligatoire () {
		return this.obligatoire;
	}
	public ArrayList<ModelProprietePossedee> getProprieteParticipants () {
		return new ArrayList<ModelProprietePossedee>(this.proprietesParticipants);
	}
	
	// Setters
	protected void setInfos (InfosModelPropriete infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		this.type = infos.getType();
		this.obligatoire = infos.isObligatoire();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	public void addProprieteParticipant (ModelProprietePossedee proprieteParticipant) throws ContestOrgModelException {
		if (!this.proprietesParticipants.contains(proprieteParticipant)) {
			// Ajouter la propriété de participant
			this.proprietesParticipants.add(proprieteParticipant);
			
			// Fire add
			this.fireAdd(proprieteParticipant, this.proprietesParticipants.size() - 1);
		} else {
			throw new ContestOrgModelException("La propriété de participant existe déjà dans la propriété");
		}
	}
	
	// Removers
	protected void removeProprieteParticipant (ModelProprietePossedee proprieteParticipant) throws ContestOrgModelException {
		// Retirer la propriété de participant
		if (this.proprietesParticipants.remove(proprieteParticipant)) {
			// Fire remove
			this.fireRemove(proprieteParticipant, this.proprietesParticipants.size() - 1);
		} else {
			throw new ContestOrgModelException("La propriété de participant n'existe pas dans la propriété");
		}
	}
	
	// Clone
	protected ModelPropriete clone (ModelConcours concours) {
		return new ModelPropriete(concours, this);
	}
	
	// ToInformation
	public InfosModelPropriete toInfos () {
		InfosModelPropriete infos = new InfosModelPropriete(this.nom, this.type, this.obligatoire);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter la propriété à la liste des removers
			removers.add(this);
			
			// Retirer l'objectif du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removePropriete(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Supprimer les propriétés de participant
			for (ModelProprietePossedee propriete : this.proprietesParticipants) {
				if (!removers.contains(propriete)) {
					propriete.delete(removers);
				}
			}
			this.proprietesParticipants.clear();
			this.fireClear(ModelProprietePossedee.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des propriétés d'un concours
	protected static class UpdaterForConcours implements IUpdater<InfosModelPropriete, ModelPropriete>
	{
		// Concours
		private ModelConcours concours;
		
		// Constructeur
		protected UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		// Implémentation de create
		@Override
		public ModelPropriete create (InfosModelPropriete infos) {
			return new ModelPropriete(this.concours, infos);
		}
		
		// Implémentation de update
		@Override
		public void update (ModelPropriete propriete, InfosModelPropriete infos) {
			propriete.setInfos(infos);
		}
	}
	
	// Classe pour valider les opérations sur la liste des propriétés d'un concours
	protected static class ValidatorForConcours implements IListValidator<InfosModelPropriete>
	{
		// Implémentation de IListValidator
		@Override
		public String validateAdd (InfosModelPropriete infos, TrackableList<InfosModelPropriete> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNom().equals(infos.getNom())) {
					return "Une propriété existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelPropriete infos, TrackableList<InfosModelPropriete> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getNom().equals(infos.getNom())) {
					return "Une propriété existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelPropriete> list) {
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelPropriete> list) {
			return null;
		}
	}
}