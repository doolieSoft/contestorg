package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;

/**
 * Participation
 */
public class ModelParticipation extends ModelAbstract
{
	
	// Attributs objets
	
	/** Participant */
	private ModelParticipant participant;
	
	/** Match */
	private ModelMatchAbstract match;
	
	/** Liste des objectifs remportés */
	private ArrayList<ModelObjectifRemporte> objectifsRemportes = new ArrayList<ModelObjectifRemporte>();
	
	// Attributs scalaires
	
	/** Résultat */
	private int resultat;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param participant participant
	 * @param match match
	 * @param infos informations de la participation
	 */
	public ModelParticipation(ModelParticipant participant, ModelMatchAbstract match, InfosModelParticipation infos) {
		// Retenir le participant et le match
		this.participant = participant;
		this.match = match;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param participant participant
	 * @param match match
	 * @param participation participation
	 */
	protected ModelParticipation(ModelParticipant participant, ModelMatchAbstract match, ModelParticipation participation) {
		// Appeller le constructeur principal
		this(participant, match, participation.getInfos());
		
		// Récupérer l'id
		this.setId(participation.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer le résultat
	 * @return résultat
	 */
	public int getResultat () {
		return resultat;
	}
	
	/**
	 * Récupérer le participant
	 * @return participant
	 */
	public ModelParticipant getParticipant () {
		return this.participant;
	}
	
	/**
	 * Récupérer le match
	 * @return match
	 */
	public ModelMatchAbstract getMatch () {
		return this.match;
	}
	
	/**
	 * Récupérer l'objectif remporté associé à un objectif
	 * @param objectif objectif
	 * @return objectif remporté associé à l'objectif
	 */
	public ModelObjectifRemporte getObjectifRemporte (ModelObjectif objectif) {
		// Rechercher l'objectif remporte correspondant à l'objet
		for (ModelObjectifRemporte objectifRemporte : this.objectifsRemportes) {
			if (objectif.equals(objectifRemporte.getObjectif())) {
				return objectifRemporte;
			}
		}
		
		// Retourner null si pas d'objectif remporte correspondant
		return null;
	}
	
	/**
	 * Récupérer la liste des objectifs remportés
	 * @return liste des objectifs remportés
	 */
	public ArrayList<ModelObjectifRemporte> getObjectifsRemportes () {
		return new ArrayList<ModelObjectifRemporte>(this.objectifsRemportes);
	}
	
	/**
	 * Récupérer le nombre de points remportés
	 * @return nombre de points remportés
	 */
	public double getPoints () {
		// Initialiser le nombre de points
		double points = 0;
		
		// Comptabiliser les points des objectifs
		for(ModelObjectif objectif : FrontModel.get().getConcours().getObjectifs()) {
			// Récupérer l'objectif remporté
			ModelObjectifRemporte objectifRemporte = this.getObjectifRemporte(objectif);
				
			// Vérifier l'objectif a bien été remporté
			if(objectifRemporte != null) {
				// Récupérer la quantité d'objectif remporté
				int quantite = objectifRemporte.getQuantite();
					
				// Incrémenter le nombre de points via l'opération de l'objectif autant de fois que nécéssaire
				for (int i = 0; i < quantite; i++) {
					points = objectif.operation(points);
				}
			}
		}
		
		// Points de victoire, d'égalité et de défaite
		ModelConcours concours = FrontModel.get().getConcours();
		double pvictoire = concours.getPointsVictoire();
		double pegalite = concours.getPointsEgalite();
		double pdefaite = concours.getPointsDefaite();
		
		// Comptabiliser les points du résultat
		switch (this.getResultat()) {
			case InfosModelParticipation.RESULTAT_VICTOIRE:
				points += pvictoire;
				break;
			case InfosModelParticipation.RESULTAT_EGALITE:
				points += pegalite;
				break;
			case InfosModelParticipation.RESULTAT_DEFAITE:
				points += pdefaite;
				break;
		}
		
		// Retourner le nombre de points
		return points;
	}
	
	// Setters
	
	/**
	 * Définir les informations de la participation
	 * @param infos informations de la participation
	 */
	protected void setInfos (InfosModelParticipation infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.resultat = infos.getResultat();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir le participant
	 * @param participant participant
	 * @throws ContestOrgErrorException
	 */
	protected void setParticipant(ModelParticipant participant) throws ContestOrgErrorException {
		// Retenir le nouveau participant
		this.participant = participant;
		
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
			// Vérifier si l'objectif de l'objectif remporté n'a pas déjà été remporté
			if (this.getObjectifRemporte(objectifRemporte.getObjectif()) != null) {
				throw new ContestOrgErrorException("L'objectif a déjà été remporté");
			}
			
			// Ajouter l'objectif remporté
			this.objectifsRemportes.add(objectifRemporte);
			
			// Fire add
			this.fireAdd(objectifRemporte, this.objectifsRemportes.size() - 1);
		} else {
			throw new ContestOrgErrorException("L'objectif remporté existe déjà dans la participation");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer un objectif remporté
	 * @param objectifRemporte objectif remporté
	 * @throws ContestOrgErrorException
	 */
	protected void removeObjectifRemporte (ModelObjectifRemporte objectifRemporte) throws ContestOrgErrorException {
		// Retirer l'objectif remporté
		int index;
		if ((index = this.objectifsRemportes.indexOf(objectifRemporte)) != -1) {
			// Remove
			this.objectifsRemportes.remove(objectifRemporte);
			
			// Fire remove
			this.fireRemove(objectifRemporte, index);
		} else {
			throw new ContestOrgErrorException("L'objectif remporté n'existe pas dans la participation");
		}
	}
	
	// Updaters
	
	/**
	 * Mettre à jour la liste des objectifs remportés
	 * @param list liste des objectifs remportés source
	 * @throws ContestOrgErrorException
	 */
	public void updateObjectifsRemportes(TrackableList<Pair<String, InfosModelObjectifRemporte>> list) throws ContestOrgErrorException {
		this.updates(new ModelObjectifRemporte.UpdaterForParticipation(FrontModel.get().getConcours(), this), this.objectifsRemportes, list, true, null);
	}
	
	/**
	 * Cloner la participation
	 * @param participant participant
	 * @param match match
	 * @return clone de la participation
	 */
	protected ModelParticipation clone (ModelParticipant participant, ModelMatchAbstract match) {
		return new ModelParticipation(participant, match, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelParticipation getInfos () {
		InfosModelParticipation infos = new InfosModelParticipation(this.resultat);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter la participation à la liste des removers
			removers.add(this);
			
			// Retirer la participation du match
			for (ModelObjectifRemporte objectifRemporte : this.objectifsRemportes) {
				if (!removers.contains(objectifRemporte)) {
					objectifRemporte.delete(removers);
				}
			}
			this.fireClear(ModelObjectif.class);
			
			// Retirer la participation du participant
			if (this.participant != null) {
				if (!removers.contains(this.participant)) {
					this.participant.removeParticipation(this);
				}
				this.participant = null;
				this.fireClear(ModelParticipant.class);
			}
			
			// Retirer la participation du match
			if (this.match != null) {
				if (!removers.contains(this.match)) {
					this.match.removeParticipation(this);
				}
				this.match = null;
				this.fireClear(ModelMatchAbstract.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
