package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite etre considérée comme un transformateur d'objet de classe L en objet de classe M
 */
public interface ITransformer<L,M>
{
	// Transformer l'objet de classe L en objet de classe M
	public M transform(L object);
}
