package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite etre tenue au courant de l'avancement d'une génération d'un objet de classe T
 */
public interface IGenerationListener<T> extends IProgressionListener
{

	// Recoit la meilleure génération trouvée jusque là
	public void generationMax (T object);

	// Recoit un message comme quoi la génération a été arreté (il faut considérer la meilleure génération trouvée jusque là)
	public void generationArret ();

	// Recoit un message comme quoi la génération a été annulé (il ne faut pas considérer la meilleure génération trouvée jusque là)
	public void generationAnnulation ();

}
