package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenu au courant des changements d'un objet IChangeable
 * @param <T> classe de l'objet qui porte les informations du changement
 */
public interface IChangeableListener<T>
{
	/**
	 * Recevoir un évenement comme quoi l'objet écouté à changé
	 * @param informations informations du changement
	 */
	public void change(T informations);
	
}
