package interfaces;

/**
 * Interface à implémenter si une classe souhaite gérer la création et la modification d'une classe d'objet
 * @param <I> classe des informations de création/modification
 * @param <O> classe des objets à créer/modifier
 */
public interface IUpdater<I,O>
{
	// Créer l'objet M à partir des informations L
	public O create(I infos);
	
	// Mettre à jour l'objet M à partir des informations L 
	public void update(O object, I infos);
}
