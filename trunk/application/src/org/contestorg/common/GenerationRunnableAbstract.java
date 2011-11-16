package common;

import interfaces.IGenerationListener;

import java.util.ArrayList;

public abstract class GenerationRunnableAbstract<T> implements Runnable
{
	// Listeners
	private ArrayList<IGenerationListener<T>> listeners = new ArrayList<IGenerationListener<T>>();
	
	// Arret demandé ? (s'en tenir à la meilleure génération trouvée jusque là)
	protected boolean arret = false;
	
	// Annulation demandée ? (sans considérer la meilleure génération trouvée jusque là)
	protected boolean annuler = false;
	
	// Demander l'arret
	public void stop() {
		this.arret = true;
	}
	
	// Demander l'annulation
	public void cancel() {
		this.annuler = true;
	}
	
	// Ajouter/Supprimer des listeners
	public void addListener(IGenerationListener<T> listener) {
		this.listeners.add(listener);
	}
	public void removeListener(IGenerationListener<T> listener) {
		this.listeners.remove(listener);
	}
	
	// Fire des listeners
	protected void fireFin() {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.progressionFin();
		}
	}
	protected void fireMessage(String message) {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.progressionMessage(message);
		}
	}
	protected void fireAvancement(double progression) {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.progressionAvancement(progression);
		}
	}
	protected void fireMax(T object) {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.generationMax(object);
		}
	}
	protected void fireArret() {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.generationArret();
		}
	}
	protected void fireAnnulation() {
		for(IGenerationListener<T> listener : this.listeners) {
			listener.generationAnnulation();
		}
	}
}
