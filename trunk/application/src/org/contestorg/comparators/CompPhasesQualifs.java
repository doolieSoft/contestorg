package org.contestorg.comparators;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.contestorg.infos.InfosConfigurationCouple;
import org.contestorg.models.ModelParticipant;



/**
 * Comparateur qui permet de déterminer :
 * - le meilleur participant en fonction de leur rang aux phases qualificatives
 * - le couple qui a une différence minimal de niveaux entre ses deux participants
 */
@SuppressWarnings("rawtypes")
abstract public class CompPhasesQualifs implements Comparator
{

	/** Comparateur suivant */
	private CompPhasesQualifs comparateurSuivant;
	
	/** Sens de comparaison */
	private int sens;
	
	/** Numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase) */
	private int phaseQualifMax;
	
	/** Est-ce que le classement a été établi */
	private boolean isClassementEtabli = false;
	
	/** Classement des participants */
	private Map<ModelParticipant,Integer> classement;
	
	// Sens
	
	/** Sens ascendant (de la plus petite valeur à la plus grande) */
	public static int SENS_ASCENDANT = +1;
	/** Sens descendant (de la plus grande valeur à la plus petite) */
	public static int SENS_DESCENDANT = -1;

	/**
	 * Constructeur
	 * @param comparateurSuivant comparateur suivant
	 * @param sens sens de comparaison
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifs(CompPhasesQualifs comparateurSuivant, int sens, int phaseQualifMax) {
		this.comparateurSuivant = comparateurSuivant;
		this.sens = sens;
		this.phaseQualifMax = phaseQualifMax;
	}
	
	/**
	 * Etablir le classement des participants
	 * @param participants participants
	 */
	@SuppressWarnings("unchecked")
	public Map<ModelParticipant,Integer> etablirClassement(List<ModelParticipant> participants) {
		// Le classement n'a pas été établi
		this.isClassementEtabli = false;
		
		// Initialiser le classement
		this.classement = new HashMap<ModelParticipant, Integer>();
		
		// Vérifier si le nombre de participants à trier n'est pas trop grand
		if(this.getMaxParticipants() != -1 && participants.size() > this.getMaxParticipants()) {
			// Vérifier s'il y a un comparateur suivant
			if(this.comparateurSuivant == null) {
				// Considérer tous les participants comme ex-aequos
				for(ModelParticipant participant : participants) {
					this.classement.put(participant, 1);
				}
			} else {
				// Etablir le classement avec le comparateur suivant
				this.classement = this.comparateurSuivant.etablirClassement(participants);
			}
		} else {
			// Trier la liste de participants
			Collections.sort(participants,this);
			
			// Etablir le classement
			int classement = 1;
			ModelParticipant dernierParticipant = null;
			List<ModelParticipant> participantsExAequos = new ArrayList<ModelParticipant>();
			for(ModelParticipant participant : participants) {
				if(dernierParticipant != null) {
					if(this.compare(participant, dernierParticipant) != 0) {
						if(this.comparateurSuivant != null && participantsExAequos.size() != 0) {
							for(Entry<ModelParticipant, Integer> classementComparateurSuivant : this.comparateurSuivant.etablirClassement(participantsExAequos).entrySet()) {
								this.classement.put(classementComparateurSuivant.getKey(), classementComparateurSuivant.getValue().intValue()+classement-1);
							}
							classement += participantsExAequos.size();
							participantsExAequos.clear();
						} else {
							classement++;
						}
					} else if(this.comparateurSuivant != null) {
						if(participantsExAequos.size() == 0) {
							participantsExAequos.add(dernierParticipant);
						}
						participantsExAequos.add(participant);
					}
				}
				this.classement.put(participant, classement);
				dernierParticipant = participant;
			}
			if(this.comparateurSuivant != null && participantsExAequos.size() != 0) {
				for(Entry<ModelParticipant, Integer> classementComparateurSuivant : this.comparateurSuivant.etablirClassement(participantsExAequos).entrySet()) {
					this.classement.put(classementComparateurSuivant.getKey(), classementComparateurSuivant.getValue().intValue()+classement-1);
				}
			}  
		}
		
		// Le classement a été établi
		this.isClassementEtabli = true;
		
		// Retourer le classement
		return this.classement;
	}

	// Implémentations de compare
	@SuppressWarnings("unchecked")
	public final int compare (Object objectA, Object objectB) {
		if (objectA == null || objectB == null) {
			return objectA == null && objectB == null ? 0 : (objectB == null ? 1 : -1);
		} else if (objectA instanceof ModelParticipant && objectB instanceof ModelParticipant) {
			return this.compare((ModelParticipant)objectA, (ModelParticipant)objectB);
		} else if (objectA instanceof InfosConfigurationCouple && objectB instanceof InfosConfigurationCouple) {
			return this.compare((InfosConfigurationCouple)objectA, (InfosConfigurationCouple)objectB);
		}
		return 0;
	}
	public final int compare (ModelParticipant participantA, ModelParticipant participantB) {
		// Vérifier si le classement a déjà été établi
		if(isClassementEtabli) {
			return Integer.compare(this.classement.get(participantA), this.classement.get(participantB));
		} else {
			// Vérifier si l'un des deux participants n'est pas null
			if (participantA == null || participantB == null) {
				return participantA == null && participantB == null ? 0 : (participantB == null ? 1 : -1);
			}
	
			// Vérifier si les deux participants appartiennent à la même poule
			if (!participantA.getPoule().equals(participantB.getPoule())) {
				// Comparer les participants selon leur rang
				int rangA = participantA.getRangPhasesQualifs();
				int rangB = participantB.getRangPhasesQualifs();
				if (rangA != rangB) {
					return rangA > rangB ? 1 : -1;
				}
				return 0;
			}
	
			// Comparer les deux participants
			double valueA = this.getValue(participantA,participantB,this.phaseQualifMax);
			double valueB = this.getValue(participantB,participantA,this.phaseQualifMax);
			return this.sens * Double.compare(valueA, valueB);
		}
	}
	public final int compare (InfosConfigurationCouple<ModelParticipant> coupleA, InfosConfigurationCouple<ModelParticipant> coupleB) {
		// Vérifier si l'un des deux couples n'est pas null
		if (coupleA == null || coupleB == null) {
			return coupleA == null && coupleB == null ? 0 : (coupleB == null ? 1 : -1);
		}

		// Comparer l'écart entre les deux couples
		double differenceA = this.getDifference(coupleA.getParticipantA(), coupleA.getParticipantB());
		double differenceB = this.getDifference(coupleB.getParticipantA(), coupleB.getParticipantB());
		int result = Double.compare(differenceB, differenceA);

		// Utiliser le comparateur suivant si le résultat est nul
		return this.comparateurSuivant != null && result == 0 ? this.comparateurSuivant.compare(coupleA, coupleB) : result;
	}

	/**
	 * Calculer la différence entre la valeur de deux participants
	 * @param participantA participant A
	 * @param participantB participant B
	 * @return différence entre la valeur des deux participants
	 */
	private double getDifference (ModelParticipant participantA, ModelParticipant participantB) {
		return Math.abs((participantA == null ? 0 : this.getValue(participantA,participantB,this.phaseQualifMax)) - (participantB == null ? 0 : this.getValue(participantB,participantA,this.phaseQualifMax)));
	}
	
	/**
	 * Récupérer le comparateur suivant
	 * @return comparateur suivant
	 */
	public CompPhasesQualifs getComparateurSuivant() {
		return this.comparateurSuivant;
	}
	
	/**
	 * Récupérer le classement des participants
	 * @return classement des participants
	 */
	public Map<ModelParticipant,Integer> getClassement() {
		return this.classement;
	}
	
	/**
	 * Récupérer le nombre maximal de participants qu'il est possible de comparer
	 * @return nombre maximal de participants qu'il est possible de comparer (-1 pour ignorer le nombre de participants)
	 */
	protected abstract int getMaxParticipants();

	/**
	 * Récupérer la valeur d'un participant à comparer
	 * @param participant participant
	 * @param adversaire adversaire
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return valeur du participant à comparer
	 */
	protected abstract double getValue (ModelParticipant participant,ModelParticipant adversaire, int phaseQualifMax);

}
