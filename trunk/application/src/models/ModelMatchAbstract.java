package models;

import infos.InfosModelMatch;
import infos.InfosModelParticipation;

import java.util.ArrayList;
import java.util.Date;

/**
 * Match
 */
abstract public class ModelMatchAbstract extends ModelAbstract
{
	// Roles
	public static final int PARTCIPATION_A = 1;
	public static final int PARTCIPATION_B = 2;
	
	// Attributs objets
	protected ModelParticipation participationA;
	protected ModelParticipation participationB;
	
	// Attributs scalaires
	private Date date;
	private String details;
	
	// Getters
	public Date getDate () {
		return this.date;
	}
	public String getDetails() {
		return this.details;
	}
	public ModelParticipation getParticipationA () {
		return this.participationA;
	}
	public ModelParticipation getParticipationB () {
		return this.participationB;
	}
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
	public boolean isEffectue() {
		return this.participationA != null && this.participationB != null && this.participationA.getResultat() != InfosModelParticipation.RESULTAT_ATTENTE && this.participationB.getResultat() != InfosModelParticipation.RESULTAT_ATTENTE;
	}
	
	// Setters
	protected void setInfos (InfosModelMatch infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Retenir les attributs
		this.date = infos.getDate();
		this.details = infos.getDetails();
	}
	public void setDate (Date date) {
		// Enregistrer la date
		this.date = date;
		
		// Fire update
		this.fireUpdate();
	}
	public void setParticipationA (ModelParticipation participation) throws ContestOrgModelException {
		// Enregistrer la participation
		this.participationA = participation;
		
		// Fire update
		this.fireUpdate();
	}
	public void setParticipationB (ModelParticipation participation) throws ContestOrgModelException {
		// Enregistrer la participation
		this.participationB = participation;
		
		// Fire update
		this.fireUpdate();
	}
	
	// Removers
	protected void removeParticipation (ModelParticipation participation) throws ContestOrgModelException {
		if (participation.equals(this.participationA)) {
			this.setParticipationA(null);
		}
		if (participation.equals(this.participationB)) {
			this.setParticipationB(null);
		}
	}
	
	// Actions
	protected static void planifier (ArrayList<? extends ModelMatchAbstract> matchs, Date date, int duree, int interval, int pause, ArrayList<ModelLieu> lieux) {
		// FIXME
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter le match à la liste des removers
			removers.add(this);
			
			// Supprimer la participation A du match
			if (this.participationA != null) {
				if (!removers.contains(this.participationA)) {
					this.participationA.delete(this);
				}
				this.participationA = null;
				this.fireClear(ModelParticipation.class, PARTCIPATION_A);
			}
			
			// Supprimer la participation B du match
			if (this.participationB != null) {
				if (!removers.contains(this.participationB)) {
					this.participationB.delete(this);
				}
				this.participationB = null;
				this.fireClear(ModelParticipation.class, PARTCIPATION_B);
			}
		}
	}
	
}
