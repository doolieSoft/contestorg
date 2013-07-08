package org.contestorg.models;


import java.util.ArrayList;
import java.util.Date;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.infos.InfosModelMatch;
import org.contestorg.infos.InfosModelParticipation;

/**
 * Match abstrait
 */
abstract public class ModelMatchAbstract extends ModelAbstract
{
	// Roles
	
	/** Participation A */
	public static final int PARTCIPATION_A = 1;
	
	/** Participation B */
	public static final int PARTCIPATION_B = 2;
	
	// Attributs objets
	
	/** Emplacement */
	protected ModelEmplacement emplacement;
	
	/** Participation A */
	protected ModelParticipation participationA;
	
	/** Participation B */
	protected ModelParticipation participationB;
	
	// Attributs scalaires
	
	/** Date */
	private Date date;
	
	/** Détails */
	private String details;
	
	/**
	 * Constructeur
	 * @param emplacement emplacement
	 */
	public ModelMatchAbstract(ModelEmplacement emplacement, InfosModelMatch infos) {
		// Retenir l'emplacement
		this.emplacement = emplacement;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	// Getters
	
	/**
	 * Récupérer la date
	 * @return date
	 */
	public Date getDate () {
		return this.date;
	}
	
	/**
	 * Récupérer les détails
	 * @return détails
	 */
	public String getDetails() {
		return this.details;
	}
	
	/**
	 * Récupérer l'emplacement
	 * @return emplacement
	 */
	public ModelEmplacement getEmplacement() {
		return this.emplacement;
	}
	
	/**
	 * Récupérer la participation A
	 * @return participation A
	 */
	public ModelParticipation getParticipationA () {
		return this.participationA;
	}
	
	/**
	 * Récupérer la participation B
	 * @return participation B
	 */
	public ModelParticipation getParticipationB () {
		return this.participationB;
	}
	
	/**
	 * Récupérer le vainqueur
	 * @return vainqueur
	 */
	public ModelParticipation getVainqueur() {
		if(this.isEffectue()) {
			if(this.getParticipationA().getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE) {
				return this.getParticipationA();
			}
			if(this.getParticipationB().getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE) {
				return this.getParticipationB();
			}
		}
		return null;
	}
	
	/**
	 * Récupérer le perdant
	 * @return perdant
	 */
	public ModelParticipation getPerdant() {
		if(this.isEffectue()) {
			if(this.getParticipationA().getResultat() == InfosModelParticipation.RESULTAT_DEFAITE) {
				return this.getParticipationA();
			}
			if(this.getParticipationB().getResultat() == InfosModelParticipation.RESULTAT_DEFAITE) {
				return this.getParticipationB();
			}
		}
		return null;
	}
	
	/**
	 * Récupérer l'adversaire d'un participant
	 * @param participant participant
	 * @return adversaire d'un participant
	 */
	public ModelParticipation getAdversaire(ModelParticipant participant) {
		if(this.getParticipationA() != null && participant.equals(this.getParticipationA().getParticipant())) {
			return this.getParticipationB();
		}
		if(this.getParticipationB() != null && participant.equals(this.getParticipationB().getParticipant())) {
			return this.getParticipationA();
		}
		return null;
	}
	
	/**
	 * Savoir si le match a été effectué
	 * @return match effectué ?
	 */
	public boolean isEffectue() {
		return this.participationA != null && this.participationB != null && this.participationA.getResultat() != InfosModelParticipation.RESULTAT_ATTENTE && this.participationB.getResultat() != InfosModelParticipation.RESULTAT_ATTENTE;
	}
	
	// Setters
	
	/**
	 * Définir les informations du match
	 * @param infos informations du match
	 */
	protected void setInfos (InfosModelMatch infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Retenir les attributs
		this.date = infos.getDate();
		this.details = infos.getDetails();
	}
	
	/**
	 * Définir la date
	 * @param date date
	 */
	public void setDate (Date date) {
		// Enregistrer la date
		this.date = date;
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir l'emplacmeent
	 * @param emplacement emplacement
	 */
	public void setEmplacement(ModelEmplacement emplacement) {
		// Enregistrer l'emplacement
		this.emplacement = emplacement;
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir la participation A
	 * @param participation participation A
	 * @throws ContestOrgErrorException
	 */
	public void setParticipationA (ModelParticipation participation) throws ContestOrgErrorException {
		// Enregistrer la participation
		this.participationA = participation;
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir la participation B
	 * @param participation participation B
	 * @throws ContestOrgErrorException
	 */
	public void setParticipationB (ModelParticipation participation) throws ContestOrgErrorException {
		// Enregistrer la participation
		this.participationB = participation;
		
		// Fire update
		this.fireUpdate();
	}
	
	// Removers
	
	/**
	 * Supprimer une participation
	 * @param participation participation
	 * @throws ContestOrgErrorException
	 */
	protected void removeParticipation (ModelParticipation participation) throws ContestOrgErrorException {
		if (participation.equals(this.participationA)) {
			this.setParticipationA(null);
		}
		if (participation.equals(this.participationB)) {
			this.setParticipationB(null);
		}
	}
	
	// Actions
	
	/**
	 * Planifier une liste de matchs
	 * @param matchs liste des matchs
	 * @param date début de la planification
	 * @param duree durée d'un match (en minutes)
	 * @param interval interval minimal entre deux matchs (en minutes)
	 * @param pause pause minimal d'un participant entre deux matchs (en minutes)
	 * @param lieux liste des lieux
	 */
	protected static void planifier (ArrayList<? extends ModelMatchAbstract> matchs, Date date, int duree, int interval, int pause, ArrayList<ModelLieu> lieux) {
		// FIXME
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter le match à la liste des removers
			removers.add(this);
			
			// Retirer le match de l'emplacement
			if(this.emplacement != null) {
				if (!removers.contains(this.emplacement)) {
					this.emplacement.removeMatch(this);
				}
				this.emplacement = null;
				this.fireClear(ModelEmplacement.class);
			}
			
			// Supprimer la participation A du match
			if (this.participationA != null) {
				if (!removers.contains(this.participationA)) {
					this.participationA.delete(removers);
				}
				this.participationA = null;
				this.fireClear(ModelParticipation.class, PARTCIPATION_A);
			}
			
			// Supprimer la participation B du match
			if (this.participationB != null) {
				if (!removers.contains(this.participationB)) {
					this.participationB.delete(removers);
				}
				this.participationB = null;
				this.fireClear(ModelParticipation.class, PARTCIPATION_B);
			}
		}
	}
	
}
