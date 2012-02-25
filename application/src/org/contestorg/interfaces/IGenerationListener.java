package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenue au courant de l'avancement d'une génération d'un objet
 * @param <T> classe de l'objet généré
 */
public interface IGenerationListener<T> extends IProgressionListener
{

	/**
	 * Recevoir le meilleur objet généré jusque-là
	 * @param objet meilleur objet généré jusque-là
	 */
	public void generationMax (T objet);

	/**
	 * Recevoir un message comme quoi la génération est arretée (il faut considérer le meilleur objet généré jusque-là)
	 */
	public void generationArret ();

	/**
	 * Recevoir un message comme quoi la génération est annulée (il ne faut pas considérer le meilleur objet généré jusque-là)
	 */
	public void generationAnnulation ();

}
