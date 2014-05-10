package org.contestorg.common;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.contestorg.interfaces.ITrackableListListener;
import org.contestorg.interfaces.ITrackableListValidator;

/**
 * Liste suivable
 * @param <T> classe des objets de la liste
 */
public class TrackableList<T> implements Iterable<T>
{
	
	/** Liste courante */
	private ArrayList<T> tsModifies = new ArrayList<T>();
	
	/** Liste initiale */
	private ArrayList<T> tsOriginaux = new ArrayList<T>();
	
	/** Tableau de correspondance entre la liste actuelle et la liste d'origine (-1 pour les ajouts) */
	private ArrayList<Integer> correspondances = new ArrayList<Integer>();
	
	/** Index des élements de la liste de départ qui ont été modifiés */
	private ArrayList<Integer> modifications = new ArrayList<Integer>();
	
	/** Index des élements de la liste de départ qui ont été supprimés */
	private ArrayList<Integer> suppressions = new ArrayList<Integer>();
	
	/** Validateurs d'opérations sur les ts */
	private ArrayList<ITrackableListValidator<T>> validators = new ArrayList<ITrackableListValidator<T>>();
	
	/** Erreurs rencontrés sur la dernière opération */
	protected ArrayList<String> erreurs = new ArrayList<String>();
	
	/** Liste des listeners */
	private ArrayList<ITrackableListListener<T>> listeners = new ArrayList<ITrackableListListener<T>>();

	// Constructeurs
	
	/**
	 * Constructeur
	 */
	public TrackableList() {
	}
	
	/**
	 * Constructeur avec initialisation
	 * @param list liste d'initialisation
	 */
	public TrackableList(ArrayList<T> list) {
		// Vérifier que la liste ne soit pas null
		if(list != null) {
			// Remplir la liste avec les ts d'origine
			this.fill(list);
		}
	}
	
	/**
	 * Constructeur avec initialisation
	 * @param list liste d'initialisation
	 */
	public TrackableList(TrackableList<T> list) {
		// Vérifier que la liste ne soit pas null
		if(list != null) {
			// Remplir la liste avec les ts d'origine
			this.fill(list,true,true);
		}
	}
	
	// Méthodes de liste
	
	/**
	 * Récupérer un élément de la liste
	 * @param row indice de l'élément à récupérer
	 * @return élément récupérer
	 */
	public T get(int row) {
		return this.tsModifies.get(row);
	}
	
	/**
	 * Récupérer l'indice d'un élément
	 * @param t élément
	 * @return indice de l'élément
	 */
	public int getRow(T t) {
		return this.tsModifies.indexOf(t);
	}
	
	/**
	 * Récupérer la taille de la liste courante
	 * @return taille de la liste courante
	 */
	public int size() {
		return this.tsModifies.size();
	}
	
	/**
	 * Récupérer une copie de la liste courante
	 * @return copie de la liste courante
	 */
	public ArrayList<T> getModifies () {
		return new ArrayList<T>(this.tsModifies);
	}
	
	/**
	 * Récupérer une copie de la liste initiale
	 * @return copie de la liste initiale
	 */
	public ArrayList<T> getOriginaux() {
		return new ArrayList<T>(this.tsOriginaux);
	}
	
	/**
	 * Initialisation la liste
	 * @param list liste d'initialisation
	 */
	public void fill(ArrayList<T> list) {
		// Fires de suppression
		int row = this.tsModifies.size()-1;
		while(row >= 0) {
			this.fireRowDeleted(row, this.tsModifies.remove(row));
			row--;
		}
		
		// Initialiser les listes de suivi
		this.tsModifies = new ArrayList<T>(list);
		this.tsOriginaux = new ArrayList<T>(list);
		this.correspondances = new ArrayList<Integer>();
		this.suppressions = new ArrayList<Integer>();
		this.modifications = new ArrayList<Integer>();

		// Définir les liens entre la liste actuelle et la liste d'origine
		for (int i = 0; i < tsModifies.size(); i++) {
			this.correspondances.add(i);
		}
		
		// Fires d'ajout
		row++;
		while(row < this.tsModifies.size()) {
			this.fireRowInserted(row, this.tsModifies.get(row));
			row++;
		}
	}
	
	/**
	 * Initialisation la liste
	 * @param list liste d'initialisation
	 * @param changeValidors utiliser les validateurs de la liste d'initialisation ?
	 * @param changeListeners utilisateur les listeners de la liste d'initialisation ?
	 */
	public void fill(TrackableList<T> list, boolean changeValidors, boolean changeListeners) {
		// Fires de suppression
		int row = this.tsModifies.size()-1;
		while(row >= 0) {
			this.fireRowDeleted(row, this.tsModifies.remove(row));
			row--;
		}
		
		// Recopier le contenu de la liste
		this.tsModifies = new ArrayList<T>(list.tsModifies);
		this.tsOriginaux = new ArrayList<T>(list.tsOriginaux);
		this.correspondances = new ArrayList<Integer>(list.correspondances);
		this.suppressions = new ArrayList<Integer>(list.suppressions);
		this.modifications = new ArrayList<Integer>(list.modifications);
		
		// Recopier les valideurs si nécéssaire
		if(changeValidors) {
			this.validators = new ArrayList<ITrackableListValidator<T>>(list.validators);
		}
		
		// Recoper les listeners si nécéssaire
		if(changeListeners) {
			this.listeners = new ArrayList<ITrackableListListener<T>>(list.listeners);
		}
		
		// Fires d'ajout
		row++;
		while(row < this.tsModifies.size()) {
			this.fireRowInserted(row, this.tsModifies.get(row));
			row++;
		}
	}
	
	/**
	 * Lier la liste à une autre liste
	 * @param list à lier
	 */
	public void link(TrackableList<T> list) {
		// Fires de suppression
		this.tsModifies = new ArrayList<T>(list.tsModifies);
		while(this.tsModifies.size() != 0) {
			this.fireRowDeleted(this.tsModifies.size()-1, this.tsModifies.remove(this.tsModifies.size()-1));
		}
		
		// Recopier le contenu de la liste
		this.tsModifies = list.tsModifies;
		this.tsOriginaux = list.tsOriginaux;
		this.correspondances = list.correspondances;
		this.suppressions = list.suppressions;
		this.modifications = list.modifications;
		
		// Recopier les valideurs
		this.validators = list.validators;
		
		// Recoper les listeners
		this.listeners = list.listeners;
		
		// Fires d'ajout
		int row = 0;
		while(row < this.tsModifies.size()) {
			this.fireRowInserted(row, this.tsModifies.get(row));
			row++;
		}
	}
	
	/**
	 * Ajouter un élément dans la liste
	 * @param t élément à ajouter
	 * @return ajout effectué ?
	 */
	public boolean add (T t) {
		// Vérifier si les validateurs acceptent l'ajout
		if(this.validateAdd(t)) {
			// Ajouter l'élément
			this.tsModifies.add(t);
			
			// Ajouter -1 dans la liste des correspondances
			this.correspondances.add(-1);
	
			// Fire des listeners
			this.fireRowInserted(this.tsModifies.size() - 1, t);
			
			// L'ajout a bien été effectué
			return true;
		}
		
		// L'ajout n'a pas été effectué
		return false;
	}
	
	/**
	 * Modifier un élément de la liste
	 * @param row indice de l'élément à modifier
	 * @param after élément après la modification
	 * @return modification effectuée ?
	 */
	public boolean update (int row, T after) {
		// Vérifier si les validateurs acceptent la modification
		if(this.validateUpdate(row,after)) {
			// Changer l'élément
			T before = this.tsModifies.set(row, after);
	
			// Vérifier si l'élément fait partie de la liste initiale
			int correspondance = new Integer(this.correspondances.get(row));
			if(correspondance != -1) {
				// Vérifier si l'élément d'origine n'a pas déjà été modifié
				if(!this.modifications.contains(correspondance)) {
					// Déclarer la modification
					this.modifications.add(correspondance);
				}
			}
	
			// Fire des listeners
			this.fireRowUpdated(row, before, after);
			
			// La modification a bien été effectuée
			return true;
		}
		
		// La modification n'a pas été effectuée
		return false;
	}
	
	/**
	 * Déclarer la modification d'un élément de la liste initiale 
	 * @param row indice de l'élément dans la liste initiale
	 */
	public void update(int row) {
		// Vérifier si l'élément fait partie de la liste initiale
		int correspondance = new Integer(this.correspondances.get(row));
		if(correspondance != -1) {
			// Vérifier si l'élément d'origine n'a pas déjà été modifié
			if(!this.modifications.contains(correspondance)) {
				// Déclarer la modification
				this.modifications.add(correspondance);
			}
		}
	}
	
	/**
	 * Supprimer un élément de la liste
	 * @param row indice de l'élément
	 * @return suppression effectuée ?
	 */
	public boolean remove (int row) {
		// Vérifier si les validateurs acceptent la suppression
		if(this.validateDelete(row)) {
			// Retirer l'élément
			T t = this.tsModifies.remove(row);
	
			// Vérifier si l'élément est un élément de la liste initiale
			int correspondance = this.correspondances.get(row);
			if(correspondance != -1) {
				// Déclarer la suppression
				this.suppressions.add(correspondance);
				
				// Supprimer l'élément de la liste des modifications
				if(this.modifications.contains(correspondance)) {
					this.modifications.remove(this.modifications.indexOf(correspondance));
				}
			}
			
			// Supprimer des correspondances
			this.correspondances.remove(row);
	
			// Fire des listeners
			this.fireRowDeleted(row, t);
			
			// La suppression a bien été effectuée
			return true;
		}
		
		// La suppression n'a pas été effectuée
		return false;
	}
	
	// Méthodes d'organisation
	
	/**
	 * Déplacer un élément vers le haut
	 * @param row indice de l'élément à déplacer vers le haut
	 * @return déplacement effectué ?
	 */
	public boolean moveUp(int row) {
		// Vérifier si les validateurs acceptent le déplacement
		if(this.validateMove(row, -1)) {
			// Déplacer l'élément et la correspondance vers le haut
			Collections.swap(this.tsModifies, row, row-1);
			Collections.swap(this.correspondances, row, row-1);
			
			// Fires des listeners
			this.fireRowUpdated(row, this.tsModifies.get(row-1), this.tsModifies.get(row));
			this.fireRowUpdated(row-1, this.tsModifies.get(row), this.tsModifies.get(row-1));
			
			// Le déplacement a bien été effectué
			return true;
		}
		
		// Le déplacement n'a pas été effectué
		return false;
	}
	
	/**
	 * Déplacer un élément vers le bas
	 * @param row indice de l'élément à déplacer vers le bas
	 * @return déplacement effectué ?
	 */
	public boolean moveDown(int row) {
		if(this.validateMove(row, +1)) {
			// Déplacer l'élément et la correspondance vers le bas
			Collections.swap(this.tsModifies, row, row+1);
			Collections.swap(this.correspondances, row, row+1);
			
			// Fires des listeners
			this.fireRowUpdated(row, this.tsModifies.get(row+1), this.tsModifies.get(row));
			this.fireRowUpdated(row+1, this.tsModifies.get(row), this.tsModifies.get(row+1));
			
			// Le déplacement a bien été effectué
			return true;
		}
		
		// Le déplacement n'a pas été effectué
		return false;
	}
	
	// Méthodes de validation
	
	/**
	 * Valider un ajout
	 * @param t élément à ajouter
	 * @return ajout validé ?
	 */
	public boolean validateAdd(T t) {
		// Valider l'ajout
		this.erreurs = new ArrayList<String>();
		for(ITrackableListValidator<T> validator : this.validators) {
			String erreur = validator.validateAdd(t, this);
			if(erreur != null) {
				this.erreurs.add(erreur);
			}
		}
		return this.erreurs.size() == 0;
	}
	
	/**
	 * Valider une modification
	 * @param row indice de la ligne à modifier
	 * @param t nouvel élément
	 * @return modification validée ?
	 */
	public boolean validateUpdate(int row, T t) {
		// Valider la modification
		this.erreurs = new ArrayList<String>();
		for(ITrackableListValidator<T> validator : this.validators) {
			String erreur = validator.validateUpdate(row, t, this);
			if(erreur != null) {
				this.erreurs.add(erreur);
			}
		}
		return this.erreurs.size() == 0;
	}
	
	/**
	 * Valider une suppression
	 * @param row row indice de la ligne à supprimer
	 * @return suppression validée ?
	 */
	public boolean validateDelete(int row) {
		// Valider la suppression
		this.erreurs = new ArrayList<String>();
		for(ITrackableListValidator<T> validator : this.validators) {
			String erreur = validator.validateDelete(row,this);
			if(erreur != null) {
				this.erreurs.add(erreur);
			}
		}
		return this.erreurs.size() == 0;
	}
	
	/**
	 * Valider un déplacement
	 * @param row indice de l'élément à déplacer
	 * @param movement mouvement à valider
	 * @return mouvement validé ?
	 */
	public boolean validateMove(int row, int movement) {
		// Valider le déplacement
		this.erreurs = new ArrayList<String>();
		for(ITrackableListValidator<T> validator : this.validators) {
			String erreurA = validator.validateMove(row, movement, this);
			if(erreurA != null) {
				this.erreurs.add(erreurA);
			}
			String erreurB = validator.validateMove(row+movement, movement > 0 ? -1 : +1, this);
			if(erreurB != null) {
				this.erreurs.add(erreurB);
			}
		}
		return this.erreurs.size() == 0;
	}
	
	/**
	 * Ajouter un validateur
	 * @param validator validateur à ajouter
	 */
	public void addValidator(ITrackableListValidator<T> validator) {
		this.validators.add(validator);
	}
	
	/**
	 * Supprimer un validateur
	 * @param validator validateur à supprimer
	 */
	public void removeValidator(ITrackableListValidator<T> validator) {
		this.validators.remove(validator);
	}
	
	// Méthodes de tracabilité
	
	/**
	 * Récupérer la liste des correspondances
	 * @return liste des correspondances
	 */
	public ArrayList<Integer> getCorrespondances () {
		return new ArrayList<Integer>(this.correspondances);
	}
	
	/**
	 * Récupérer la liste des modifications
	 * @return liste des modifications
	 */
	public ArrayList<Integer> getUpdates () {
		return new ArrayList<Integer>(this.modifications);
	}
	
	/**
	 * Récupérer la liste des suppressions
	 * @return liste des suppressions
	 */
	public ArrayList<Integer> getDeletions () {
		return new ArrayList<Integer>(this.suppressions);
	}
	
	// Méthode sur les listeners
	
	/**
	 * Ajouter un listener
	 * @param listener listener à ajouter
	 */
	public void addITrackableListListener(ITrackableListListener<T> listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Supprimer listener
	 * @param listener listener à supprimer
	 */
	public void removeITrackableListListener(ITrackableListListener<T> listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * Fire des listeners sur l'ajout d'un élément
	 * @param row indice de l'élément ajouté
	 * @param inserted élément ajouté
	 */
	protected void fireRowInserted (int row, T inserted) {
		// Fire des listeners de ITrackableListListener
		for(ITrackableListListener<T> listener : this.listeners) {
			listener.addEvent(row, inserted);
		}
	}
	
	/**
	 * Fire des listeners sur la modification d'un élément
	 * @param row indice de l'élément modifié
	 * @param before élément avant la modification
	 * @param after élément après la modification
	 */
	protected void fireRowUpdated (int row, T before, T after) {
		// Fire des listeners de ITrackableListListener
		for(ITrackableListListener<T> listener : this.listeners) {
			listener.updateEvent(row, before, after);
		}
	}
	
	/**
	 * Fire des listeners sur la suppression d'un élément
	 * @param row indice de l'élément supprimé
	 * @param deleted élément supprimé
	 */
	protected void fireRowDeleted (int row, T deleted) {
		// Fire des listeners de ITrackableListListener
		for(ITrackableListListener<T> listener : this.listeners) {
			listener.removeEvent(row, deleted);
		}
	}
	
	// Implémentation de Iterable
	public Iterator<T> iterator() {
		return this.tsModifies.iterator();
	}
	
}
