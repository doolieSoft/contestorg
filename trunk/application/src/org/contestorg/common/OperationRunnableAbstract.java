package org.contestorg.common;


import java.util.ArrayList;

import org.contestorg.interfaces.IOperationListener;

/**
 * Classe facilitant la mise en place de l'éxecution d'une opération
 */
public abstract class OperationRunnableAbstract implements Runnable
{
	/** Liste des listeners */
	private ArrayList<IOperationListener> listeners = new ArrayList<IOperationListener>();
	
	/** Arret demandé ? */
	protected boolean arret = false;
	
	/**
	 * Demander l'arret de l'opération
	 */
	public void stop() {
		this.arret = true;
	}
	
	/**
	 * Ajouter un listener
	 * @param listener listener
	 */
	public void addListener(IOperationListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Supprimer un listener
	 * @param listener listener
	 */
	public void removeListener(IOperationListener listener) {
		this.listeners.remove(listener);
	}
	
	// Fire des listeners
	
	/**
	 * Envoyer un message d'avancement de l'opération
	 * @param message message
	 */
	protected void fireMessage(String message) {
		for(IOperationListener listener : this.listeners) {
			listener.progressionMessage(message);
		}
	}
	
	/**
	 * Signaler l'avancement de l'opération
	 * @param progression progression de 0 à 1
	 */
	protected void fireAvancement(double progression) {
		for(IOperationListener listener : this.listeners) {
			listener.progressionAvancement(progression);
		}
	}
	
	/**
	 * Signaler la réussite de l'opération
	 */
	private void fireReussite() {
		for(IOperationListener listener : this.listeners) {
			listener.operationReussite();
			listener.progressionFin();
		}
	}
	
	/**
	 * Signaler l'échec de l'opération 
	 */
	private void fireEchec() {
		for(IOperationListener listener : this.listeners) {
			listener.operationEchec();
			listener.progressionFin();
		}
	}
	
	/**
	 * Signaler l'arrêt de la génération suite à une demande d'arrêt
	 */
	private void fireArret() {
		for(IOperationListener listener : this.listeners) {
			listener.operationArret();
			listener.progressionFin();
		}
	}
	
	/**
	 * Signaler l'arret de l'opération et nettoyer l'opération
	 */
	protected void arret () {
		// Nettoyer le test
		this.clean();
		
		// Prévenir de l'arret
		this.fireArret();
	}
	
	/**
	 * Signaler la réussite de l'opération
	 * @param message message de réussite
	 * @param clean nettoyer l'opération
	 */
	protected void reussite (String message, boolean clean) {
		// Nettoyer le test
		if(clean) {
			this.clean();
		}
		
		// Envoyer un message au listener
		this.fireMessage(message);
		
		// Prévenir la réussite
		this.fireReussite(); 
	}
	
	/**
	 * Signaler la réussite de l'opération et nettoyer l'opération
	 * @param message message de réussite
	 */
	protected void reussite (String message) {
		this.reussite(message, true);
	}
	
	/**
	 * Signaler l'échec du test et nettoyer l'opération
	 * @param message message d'échec
	 */
	protected void echec (String message) {
		// Nettoyer le test
		this.clean();
		
		// Envoyer un message au listener
		this.fireMessage(message);
		
		// Prévenir le l'echec
		this.fireEchec();
	}
	
	/**
	 * Méthode à impléter qui doit nettoyer l'opération en vue de son arrêt
	 */
	protected abstract void clean();
	
}
