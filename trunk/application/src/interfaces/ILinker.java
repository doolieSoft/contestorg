package interfaces;

/**
 * Interface à implémenter si une classe souhaite gérer les liens entre les objets d'une classe avec l'objet d'une autre
 * @param <L> classe à des objets à lier
 */
public interface ILinker<L>
{	
	// Créer le lien
	public void link(L object);
	
	// Supprimer le lien
	public void unlink(L object);
}
