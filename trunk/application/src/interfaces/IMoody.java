package interfaces;

/**
 * Interface à implémenter si une classe souhaite etre considérée comme une classe d'instances lunatiques
 */
public interface IMoody
{
	// Savoir si l'instance lunatique possède un état donné
	public boolean is(int etat);
	
	// Ajouter/Supprimer des listeners
	public void addListener(IMoodyListener listener);
	public void removeListener(IMoodyListener listener);
}
