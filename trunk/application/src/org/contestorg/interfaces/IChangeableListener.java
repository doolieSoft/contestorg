package interfaces;

/**
 * Interface à implémenter si une classe souhaite etre tenu au courant des changements d'une instance IChangeable
 */
public interface IChangeableListener<T>
{
	// Recoit un évenement comme quoi l'instance écoutée à changé
	public void change(T value);
	
}
