package interfaces;

/**
 * Interface à implémenter si une classe souhaite etre considérée comme un fournisseur de T
 * @param <T> classe de l'instance fournit
 */
public interface IFournisseur<T>
{
	// Fournir la donnée
	public T get();
}
