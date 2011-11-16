package common;

import interfaces.IListListener;
import interfaces.IListValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TrackableList<T> implements Iterable<T>
{

	// Ts
	private ArrayList<T> tsModifies = new ArrayList<T>();
	private ArrayList<T> tsOriginaux = new ArrayList<T>();
	private ArrayList<Integer> correspondances = new ArrayList<Integer>();	// Tableau de correspondance entre la liste actuelle et la liste d'origine (-1 pour les ajouts)
	private ArrayList<Integer> modifications = new ArrayList<Integer>();		// Index des élements de la liste de départ qui ont été modifiés
	private ArrayList<Integer> suppressions = new ArrayList<Integer>();	// Index des élements de la liste de départ qui ont été supprimés
	
	// Validateurs d'opérations sur les ts
	private ArrayList<IListValidator<T>> validators = new ArrayList<IListValidator<T>>();
	
	// Erreurs rencontrés sur la dernière opération
	protected ArrayList<String> erreurs = new ArrayList<String>();
	
	// Listeners
	private ArrayList<IListListener<T>> listeners = new ArrayList<IListListener<T>>();

	// Constructeurs
	public TrackableList() {
	}
	public TrackableList(ArrayList<T> list) {
		if(list != null) {
			// Remplir la liste avec les ts d'origine
			this.fill(list);
		}
	}
	public TrackableList(TrackableList<T> list) {
		// Remplir la liste avec les ts d'origine
		this.fill(list);
	}
	
	// Méthodes de liste
	public T get(int row) {
		return this.tsModifies.get(row);
	}
	public int getRow(T infos) {
		return this.tsModifies.indexOf(infos);
	}
	public int size() {
		return this.tsModifies.size();
	}
	public ArrayList<T> getModifies () {
		return new ArrayList<T>(this.tsModifies);
	}
	public ArrayList<T> getOriginaux() {
		return new ArrayList<T>(this.tsOriginaux);
	}
	public void fill(ArrayList<T> list) {
		// Fires de suppression
		int row = this.tsModifies.size()-1;
		while(row >= 0) {
			this.fireRowDeleted(row, this.tsModifies.remove(row));
			row--;
		}
		
		// Initialiser les ArrayList
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
	public void fill(TrackableList<T> list) {
		this.fill(list, true, true);
	}
	public void fill(TrackableList<T> list, boolean keepValidors, boolean keepListeners) {
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
		
		// Recopier les valideurs
		if(keepValidors) {
			this.validators = new ArrayList<IListValidator<T>>(list.validators);
		}
		
		// Recoper les listeners
		if(keepListeners) {
			this.listeners = new ArrayList<IListListener<T>>(list.listeners);
		}
		
		// Fires d'ajout
		row++;
		while(row < this.tsModifies.size()) {
			this.fireRowInserted(row, this.tsModifies.get(row));
			row++;
		}
	}
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
	public boolean add (T infos) {
		// Vérifier si les validateurs acceptent l'ajout
		if(this.validateAdd(infos)) {
			// Ajouter le T
			this.tsModifies.add(infos);
			
			// Ajouter -1 dans la liste des correspondances
			this.correspondances.add(-1);
	
			// Fire des listeners
			this.fireRowInserted(this.tsModifies.size() - 1, infos);
			
			// L'ajout a bien été effectué
			return true;
		}
		
		// L'ajout n'a pas été effectué
		return false;
	}
	public boolean update (int row, T after) {
		// Vérifier si les validateurs acceptent la modification
		if(this.validateUpdate(row,after)) {
			// Changer le T
			T before = this.tsModifies.set(row, after);
	
			// Vérifier si le T est un T d'origine
			int correspondance = new Integer(this.correspondances.get(row));
			if(correspondance != -1) {
				// Vérifier si le T d'origine n'a pas déjà été modifié
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
	public void update(int row) {
		// Vérifier si le T est un T d'origine
		int correspondance = new Integer(this.correspondances.get(row));
		if(correspondance != -1) {
			// Vérifier si le T d'origine n'a pas déjà été modifié
			if(!this.modifications.contains(correspondance)) {
				// Déclarer la modification
				this.modifications.add(correspondance);
			}
		}
	}
	public boolean remove (int row) {
		// Vérifier si les validateurs acceptent l'ajout
		if(this.validateDelete(row)) {
			// Retirer le T
			T infos = this.tsModifies.remove(row);
	
			// Vérifier si le T est un T d'origine
			int correspondance = this.correspondances.get(row);
			if(correspondance != -1) {
				// Déclarer la suppression
				this.suppressions.add(correspondance);
				
				// Supprimer le T de la liste des modifications
				if(this.modifications.contains(correspondance)) {
					this.modifications.remove(this.modifications.indexOf(correspondance));
				}
			}
			
			// Supprimer des correspondances
			this.correspondances.remove(row);
	
			// Fire des listeners
			this.fireRowDeleted(row, infos);
			
			// La suppression a bien été effectuée
			return true;
		}
		
		// La suppression n'a pas été effectuée
		return false;
	}
	
	// Méthodes d'organisation
	public boolean moveUp(int row) {
		// Vérifier si les validateurs acceptent le déplacement
		if(this.validateMove(row, -1)) {
			// Déplacer le T et la correspondance vers le haut
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
	public boolean moveDown(int row) {
		if(this.validateMove(row, +1)) {
			// Déplacer le T et la correspondance vers le bas
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
	public boolean validateAdd(T infos) {
		// Valider l'ajout
		this.erreurs = new ArrayList<String>();
		for(IListValidator<T> validator : this.validators) {
			String erreur = validator.validateAdd(infos, this);
			if(erreur != null) {
				this.erreurs.add(erreur);
			}
		}
		return this.erreurs.size() == 0;
	}
	public boolean validateUpdate(int row, T infos) {
		// Valider la modification
		this.erreurs = new ArrayList<String>();
		for(IListValidator<T> validator : this.validators) {
			String erreur = validator.validateUpdate(row, infos, this);
			if(erreur != null) {
				this.erreurs.add(erreur);
			}
		}
		return this.erreurs.size() == 0;
	}
	public boolean validateDelete(int row) {
		// Valider la suppression
		this.erreurs = new ArrayList<String>();
		for(IListValidator<T> validator : this.validators) {
			String erreur = validator.validateDelete(row,this);
			if(erreur != null) {
				this.erreurs.add(erreur);
			}
		}
		return this.erreurs.size() == 0;
	}
	public boolean validateMove(int row, int movement) {
		// Valider le déplacement
		this.erreurs = new ArrayList<String>();
		for(IListValidator<T> validator : this.validators) {
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
	public void addValidator(IListValidator<T> validator) {
		this.validators.add(validator);
	}
	public void removeValidator(IListValidator<T> validator) {
		this.validators.remove(validator);
	}
	
	// Méthodes de tracabilité
	public ArrayList<Integer> getCorrespondances () {
		return new ArrayList<Integer>(this.correspondances);
	}
	public ArrayList<Integer> getUpdates () {
		return new ArrayList<Integer>(this.modifications);
	}
	public ArrayList<Integer> getDeletions () {
		return new ArrayList<Integer>(this.suppressions);
	}
	
	// Méthode sur les listeners
	public void addIListListener(IListListener<T> listener) {
		this.listeners.add(listener);
	}
	public void removeIListListener(IListListener<T> listener) {
		this.listeners.remove(listener);
	}
	protected void fireRowInserted (int row, T inserted) {
		// Fire des listeners de IList
		for(IListListener<T> listener : this.listeners) {
			listener.addEvent(row, inserted);
		}
	}
	protected void fireRowUpdated (int row, T before, T after) {
		// Fire des listeners de IList
		for(IListListener<T> listener : this.listeners) {
			listener.updateEvent(row, before, after);
		}
	}
	protected void fireRowDeleted (int row, T deleted) {
		// Fire des listeners de IList
		for(IListListener<T> listener : this.listeners) {
			listener.removeEvent(row, deleted);
		}
	}
	
	// Implémentation de Iterable
	public Iterator<T> iterator() {
		return this.tsModifies.iterator();
	}
	
}
