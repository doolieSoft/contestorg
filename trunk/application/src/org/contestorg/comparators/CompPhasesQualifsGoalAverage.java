package org.contestorg.comparators;

import org.contestorg.infos.InfosModelCritereClassementGoalAverage;
import org.contestorg.models.ModelObjectif;
import org.contestorg.models.ModelParticipant;

/**
 * Comparateur qui prend en compte le goal-average par différence aux phases qualificatives
 */
public class CompPhasesQualifsGoalAverage extends CompPhasesQualifs
{
	/** Type */
	private int type;
	
	/** Méthode utilisée */
	private int methode;
	
	/** Donnée utilisée */
	private int donnee;

	/** Objectif */
	private ModelObjectif objectif;

	/**
	 * Constructeur
	 * @param sens sens
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @param type type
	 * @param methode méthode utilisée
	 * @param donnee donnée utilisée
	 * @param objectif objectif qui permet de comparer deux participants aux phases qualificatives
	 */
	public CompPhasesQualifsGoalAverage(int sens,int phaseQualifMax,int type,int methode,int donnee,ModelObjectif objectif) {
		this(null, sens, phaseQualifMax, type, methode, donnee, objectif);
	}
	
	/**
	 * Constructeur
	 * @param comparateurSupp comparateur supplémentaire
	 * @param sens sens
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @param type type
	 * @param methode méthode utilisée
	 * @param donnee donnée utilisée
	 * @param objectif objectif qui permet de comparer deux participants aux phases qualificatives
	 */
	public CompPhasesQualifsGoalAverage(CompPhasesQualifs comparateurSupp, int sens, int phaseQualifMax, int type, int methode, int donnee, ModelObjectif objectif) {
		super(comparateurSupp, sens, phaseQualifMax);
		this.type = type;
		this.methode = methode;
		this.donnee = donnee;
		this.objectif = objectif;
	}

	/**
	 * @see CompPhasesQualifs#getValue(ModelParticipant, ModelParticipant, int)
	 */
	@Override
	protected double getValue (ModelParticipant participant, ModelParticipant adversaire, int phaseQualifMax) {
		if(participant == null) {
			return 0;
		}
		switch(this.donnee) {
			case InfosModelCritereClassementGoalAverage.DONNEE_POINTS:
				if(this.methode == InfosModelCritereClassementGoalAverage.METHODE_DIFFERENCE) {
					if(this.type == InfosModelCritereClassementGoalAverage.TYPE_GENERAL) {
						return participant.getPoints(false,true,phaseQualifMax)-participant.getPointsAdversaires(false,true,phaseQualifMax);
					} else {
						return participant.getPointsContreAdversaire(adversaire,false,true,phaseQualifMax)-participant.getPointsParAdversaire(adversaire,false,true,phaseQualifMax);
					}
				} else {
					double pointsParticipant, pointsAdversaires;
					if(this.type == InfosModelCritereClassementGoalAverage.TYPE_GENERAL) {
						pointsParticipant = participant.getPoints(false,true,phaseQualifMax);
						pointsAdversaires = participant.getPointsAdversaires(false,true,phaseQualifMax);
					} else {
						pointsParticipant = participant.getPointsContreAdversaire(adversaire,false,true,phaseQualifMax);
						pointsAdversaires = participant.getPointsParAdversaire(adversaire,false,true,phaseQualifMax);
					}
					return pointsParticipant/(pointsAdversaires == 0 ? 1 : pointsAdversaires);
				}
			case InfosModelCritereClassementGoalAverage.DONNEE_RESULTAT:
				if(this.methode == InfosModelCritereClassementGoalAverage.METHODE_DIFFERENCE) {
					if(this.type == InfosModelCritereClassementGoalAverage.TYPE_GENERAL) {
						return participant.getNbVictoires(false,true,phaseQualifMax)-participant.getNbDefaites(false,true,phaseQualifMax)-participant.getNbForfaits(false,true,phaseQualifMax);
					} else {
						return participant.getNbVictoiresContreAdversaire(adversaire,false,true,phaseQualifMax)-participant.getNbDefaitesContreAdversaire(adversaire,false,true,phaseQualifMax)-participant.getNbForfaitsContreAdversaire(adversaire,false,true,phaseQualifMax);
					}
				} else {
					int nbVictoires, nbDefaites, nbForfaits;
					if(this.type == InfosModelCritereClassementGoalAverage.TYPE_GENERAL) {
						nbVictoires = participant.getNbVictoires(false,true,phaseQualifMax);
						nbDefaites = participant.getNbDefaites(false,true,phaseQualifMax);
						nbForfaits = participant.getNbForfaits(false,true,phaseQualifMax);
					} else {
						nbVictoires = participant.getNbVictoiresContreAdversaire(adversaire,false,true,phaseQualifMax);
						nbDefaites = participant.getNbDefaitesContreAdversaire(adversaire,false,true,phaseQualifMax);
						nbForfaits = participant.getNbForfaitsContreAdversaire(adversaire,false,true,phaseQualifMax);
					}
					return ((double)nbVictoires)/(nbDefaites+nbForfaits == 0 ? 1 : ((double)nbDefaites+(double)nbForfaits));
				}
			case InfosModelCritereClassementGoalAverage.DONNEE_QUANTITE_OBJECTIF:
				if(this.methode == InfosModelCritereClassementGoalAverage.METHODE_DIFFERENCE) {
					if(this.type == InfosModelCritereClassementGoalAverage.TYPE_GENERAL) {
						return participant.getQuantiteObjectifRemportee(this.objectif,false,true,phaseQualifMax)-participant.getQuantiteObjectifRemporteeAdversaires(this.objectif,false,true,phaseQualifMax);
					} else {
						return participant.getQuantiteObjectifRemporteeContreAdversaire(adversaire,this.objectif,false,true,phaseQualifMax)-participant.getQuantiteObjectifRemporteeParAdversaire(adversaire,this.objectif,false,true,phaseQualifMax);
					}
				} else {
					int quantiteParticipant, quantiteAdversaires;
					if(this.type == InfosModelCritereClassementGoalAverage.TYPE_GENERAL) {
						quantiteParticipant = participant.getQuantiteObjectifRemportee(this.objectif,false,true,phaseQualifMax);
						quantiteAdversaires = participant.getQuantiteObjectifRemporteeAdversaires(this.objectif,false,true,phaseQualifMax);
					} else {
						quantiteParticipant = participant.getQuantiteObjectifRemporteeContreAdversaire(adversaire,this.objectif,false,true,phaseQualifMax);
						quantiteAdversaires = participant.getQuantiteObjectifRemporteeParAdversaire(adversaire,this.objectif,false,true,phaseQualifMax);
					}
					return ((double)quantiteParticipant)/(quantiteAdversaires == 0 ? 1 : ((double)quantiteAdversaires));
				}
		}
		return 0;
	}

	/**
	 * @see CompPhasesQualifs#getMaxParticipants()
	 */
	@Override
	protected int getMaxParticipants () {
		return this.type == InfosModelCritereClassementGoalAverage.TYPE_GENERAL ? -1 : 2;
	}

}
