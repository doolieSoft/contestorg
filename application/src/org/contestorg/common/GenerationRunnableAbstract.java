package org.contestorg.common;


import java.util.ArrayList;

import org.contestorg.interfaces.IGenerationListener;

/**
 * Classe facilitant la mise en place de l'éxecution d'une génération
 * @param <T> classe des objets générés
 */
public abstract class GenerationRunnableAbstract<T> implements Runnable
{
	// Listeners
	private ArrayList<IGenerationListener<T>> listeners = new ArrayList<IGenerationListener<T>>();
	
	// Arret demandé ? (s'en tenir au meilleur T trouvée jusque là)
	protected boolean arret = false;
	
	// Annulation demandée ? (sans considérer le meilleur T trouvée jusque là)
	protected boolean annuler = false;
	
	/**
	 * Demander l'arret de la génération
	 */
	public void stop() {
		this.arret = true;
	}
	
	/**
	 * Demander l'annulation de la génération
	 */
	public void cancel() {
		this.annuler = true;
	}
	
	/**
	 * Ajouter un listener à la génération
	 * @param listener listener
	 */
	public void addListener(IGenerationListener<T> listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Supprimer un listener à la génération
	 * @param listener listener
	 */
	public void removeListener(IGenerationListener<T> listener) {
		this.listeners.remove(listener);
	}
	
	// Fire des listeners
	
	/**
	 * Signaler la fin de la génération
	 */
	protected void fireFin() {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.progressionFin();
		}
	}
	
	/**
	 * Envoyer un message d'avancement de la génération
	 * @param message message
	 */
	protected void fireMessage(String message) {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.progressionMessage(message);
		}
	}
	
	/**
	 * Signaler l'avancement de la génération
	 * @param progression progression de 0 à 1
	 */
	protected void fireAvancement(double progression) {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.progressionAvancement(progression);
		}
	}
	
	/**
	 * Signaler que le meilleur T trouvé jusque là a changé
	 * @param object meilleur T trouvé
	 */
	protected void fireMax(T object) {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.generationMax(object);
		}
	}
	
	/**
	 * Signaler l'arrêt de la génération suite à une demande d'arrêt
	 */
	protected void fireArret() {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.generationArret();
		}
	}
	
	/**
	 * Signaler l'arrêt de la génération suite à une demande d'annulation
	 */
	protected void fireAnnulation() {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.generationAnnulation();
		}
	}
}
