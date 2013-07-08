package org.contestorg.common;

import java.util.Arrays;



/**
 * Outil permettant de générer l'ensemble des configurations de match
 */
public class GenerationConfigurations
{
	/** Nombre total de configurations */
	private int nbTotalConfigurations;
	
	/** Nombre actuel de configurations générés */
	private int nbConfigurations;
	
	/** Nombre de participants */
	private int nbParticipants;
	
	/**
	 * Constructeur
	 * @param nbParticipants nombre de participants
	 */
	public GenerationConfigurations(int nbParticipants) {
		this.setNbParticipants(nbParticipants);
	}
	
	/**
	 * Récupérer le nombre total de configurations
	 * @return nombre total de configurations
	 */
	public int getNbTotalConfigurations () {
		return this.nbTotalConfigurations;
	}
	
	/**
	 * Récupérer le nombre actuel de configurations générées
	 * @return nombre actuel de configurations générées
	 */
	public int getNbConfigurations () {
		return this.nbConfigurations;
	}


	/**
	 * Récupérer le nombre de participants
	 * @return nombre de participants
	 */
	public int getNbParticipants () {
		return this.nbParticipants;
	}

	/**
	 * Définir le nombre de participants
	 * @param nbParticipants nombre de participants
	 */
	public void setNbParticipants (int nbParticipants) {
		// Retenir le nombre de participants
		this.nbParticipants = nbParticipants;
		
		// Calculer le nombre total de configurations
		this.nbTotalConfigurations = --nbParticipants;
		while (nbParticipants > 2) {
			nbParticipants -= 2;
			this.nbTotalConfigurations *= nbParticipants;
		}
	}	

	/**
	 * Générer les configurations
	 * @param listener listener des configurations générées
	 */
	public void generer (IGenerationConfigurationsListener listener) {
		int[] participantsUtilises = new int[this.nbParticipants];
		int[] configuration = new int[this.nbParticipants];
		for (int i = 0; i < this.nbParticipants; i++) {
			participantsUtilises[i] = 0;
		}
		this.nbConfigurations = 0;
		this.generer(listener,participantsUtilises, 0, configuration);
	}
	
	/**
	 * Générer les configurations
	 * @param listener listener des configurations générées
	 * @param participantsUtilises participants utilisés dans l'état actuel de la configuration
	 * @param nbParticipantsUtilises nombre de participants utilis�s dans l'état actuel de la configuration
	 * @param configuration état actuel de la configuration
	 * @return continuer la génération ?
	 */
	private boolean generer (IGenerationConfigurationsListener listener, int[] participantsUtilises, int nbParticipantsUtilises, int[] configuration) {
		int participantA = (int)(Math.random()*this.nbParticipants);
		for(int i=0;i<=this.nbParticipants&&participantsUtilises[participantA]==1;i++,participantA=(participantA+1)%this.nbParticipants) {
			if (i == this.nbParticipants) {
				this.nbConfigurations++;
				return listener.nouvelleConfiguration(this.nbConfigurations-1,configuration);
			}
		}
		int participantB = (int)(Math.random()*this.nbParticipants);
		participantsUtilises[participantA] = 1;
		for (int i=0;i<this.nbParticipants;i++,participantB=(participantB+1)%this.nbParticipants) {
			if (participantsUtilises[participantB] == 0) {
				participantsUtilises[participantB] = 1;
				configuration[nbParticipantsUtilises+0] = participantA;
				configuration[nbParticipantsUtilises+1] = participantB;
				if(!this.generer(listener,participantsUtilises, nbParticipantsUtilises+2, configuration)) {
					return false;
				}
				participantsUtilises[participantB] = 0;
			}
		}
		participantsUtilises[participantA] = 0;
		return true;
	}
	
	/**
	 * Trier un tableau d'objets à partir d'une configuration
	 * @param objets tableau d'objets
	 * @param configuration configuration
	 * @return tableau d'objets trié
	 */
	public static <T> T[] trier(int[] configuration,T[] objets) {
		T[] result = Arrays.copyOf(objets, objets.length);
		for(int i=0;i<configuration.length;i++) {
			result[i] = objets[configuration[i]];
		}
		return result;
	}
	
	/**
	 * Interface à implémenter si une classe souhaite être tenue au courant de toutes les configurations générées
	 */
	public interface IGenerationConfigurationsListener {
		
		/**
		 * Recevoir une nouvelle configuration générée
		 * @parm i indice de la configuration
		 * @param configuration nouvelle configuration générée
		 * @return continuer la génération ?
		 */
		public boolean nouvelleConfiguration(int i,int[] configuration);
		
	}
}
