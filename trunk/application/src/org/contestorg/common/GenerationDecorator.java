package org.contestorg.common;


import java.util.ArrayList;

import org.contestorg.interfaces.IGeneration;
import org.contestorg.interfaces.IGenerationListener;
import org.contestorg.interfaces.ITransformer;

/**
 * Décorateur d'une génération
 * @param <L> classe des objets générés par la génération initiale
 * @param <M> classe des objets "générés" par le décorateur
 */
public class GenerationDecorator<L,M> implements IGeneration<M>, IGenerationListener<L>
{
	/** Génération initiale */
	private IGeneration<L> generation;
	
	/** Transformer */
	private ITransformer<L,M> transformer;
	
	/** Liste des listeners */
	private ArrayList<IGenerationListener<M>> listeners = new ArrayList<IGenerationListener<M>>();
	
	/**
	 * Constructeur
	 * @param generation génération initiale
	 * @param transformer transformeur de L en M
	 */
	public GenerationDecorator(IGeneration<L> generation,ITransformer<L,M> transformer) {
		// Retenir la génération et le transformeur
		this.generation = generation;
		this.transformer = transformer;
		
		// Ecouter la generation
		this.generation.addListener(this);
	}

	// Implémentation de IGeneration
	@Override
	public void generationStart () {
		this.generation.generationStart();
	}
	@Override
	public void generationStop () {
		this.generation.generationStop();
	}
	@Override
	public void generationCancel () {
		this.generation.generationCancel();
	}
	@Override
	public void addListener (IGenerationListener<M> listener) {
		this.listeners.add(listener);
	}
	@Override
	public void removeListener (IGenerationListener<M> listener) {
		this.listeners.remove(listener);
	}

	// Implémentation de IGenerationListener
	@Override
	public void progressionAvancement (double progression) {
		for(IGenerationListener<M> listener : this.listeners) {
			listener.progressionAvancement(progression);
		}
	}
	@Override
	public void progressionMessage (String message) {
		for(IGenerationListener<M> listener : this.listeners) {
			listener.progressionMessage(message);
		}
	}
	@Override
	public void progressionFin () {
		for(IGenerationListener<M> listener : this.listeners) {
			listener.progressionFin();
		}
	}
	@Override
	public void generationMax (L object) {
		for(IGenerationListener<M> listener : this.listeners) {
			listener.generationMax(this.transformer.transform(object));
		}
	}
	@Override
	public void generationArret () {
		for(IGenerationListener<M> listener : this.listeners) {
			listener.generationArret();
		}
	}
	@Override
	public void generationAnnulation () {
		for(IGenerationListener<M> listener : this.listeners) {
			listener.generationAnnulation();
		}
	}
	
}
