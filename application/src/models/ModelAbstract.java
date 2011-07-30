package models;

import infos.InfosModelAbstract;
import interfaces.IEventListener;
import interfaces.ILinker;
import interfaces.IUpdater;
import interfaces.IValidator;

import java.util.ArrayList;

import common.TrackableList;

import events.Event;
import events.EventAdd;
import events.EventClear;
import events.EventDelete;
import events.EventMove;
import events.EventRemove;
import events.EventUpdate;

public abstract class ModelAbstract
{
	
	// Attributs scalaires
	private Integer id = null;
	private boolean isDeleted = false;
	
	// Listeners de l'instance
	private ArrayList<IEventListener> listeners = new ArrayList<IEventListener>();
	
	// Getters
	public Integer getId () {
		// Retourner l'id du model en prenant soin de le créer s'il n'en a pas
		return this.id == null ? this.id = ModelAbstract.counter++ : this.id;
	}
	public boolean isDeleted() {
		return this.isDeleted;
	}
	
	// Setters
	public void setId (Integer id) {
		// Vérifier si le compteur n'est pas inférieur à l'id
		if (id >= ModelAbstract.counter) {
			ModelAbstract.counter = id + 1;
		}
		
		// Enregistrer l'id
		this.id = id;
	}
	protected void setInfos (InfosModelAbstract infos) {
		
	}
	
	// Ajouter un listener
	public void addListener (IEventListener listener) {
		this.listeners.add(listener);
	}
	public void removeListener (IEventListener listener) {
		this.listeners.remove(listener);
	}
	
	// Equals
	public boolean equals (Object object) {
		return object != null && object.getClass() == this.getClass() && ((ModelAbstract)object).getId() == this.getId();
	}
	
	// ToInformation
	public abstract InfosModelAbstract toInformation ();
	
	// Remove
	protected void delete () throws ContestOrgModelException {
		this.delete(new ArrayList<ModelAbstract>());
	}
	protected void delete (ModelAbstract origin) throws ContestOrgModelException {
		ArrayList<ModelAbstract> removers = new ArrayList<ModelAbstract>();
		removers.add(origin);
		this.delete(removers);
	}
	protected abstract void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException;
	
	// Fire des listeners
	private void fire (Event event) {
		// Vérifier si les fires doivent être envoyés
		if (ModelAbstract.fires) {
			// Envoyer l'évenement aux listeners de l'instance et aux listeners statiques
			for (IEventListener listener : new ArrayList<IEventListener>(this.listeners)) {
				listener.event(event);
			}
			for (IEventListener listener : new ArrayList<IEventListener>(ModelAbstract.listenersStatic)) {
				listener.event(event);
			}
		}
	}
	
	// Fires d'évenements sur l'instance
	protected void fireUpdate () {
		// Ajouter le model à la liste des models
		if (!ModelAbstract.models.contains(this)) {
			ModelAbstract.models.add(this);
		}
		
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventUpdate(this));
	}
	protected void fireDelete () {
		// Retenir la suppression
		this.isDeleted = true;
		
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventDelete(this));
		
		// Retirer le model de la liste des models
		if (ModelAbstract.fires && !ModelAbstract.models.contains(this)) {
			ModelAbstract.models.remove(this);
		}
	}
	
	// Fires d'évenement sur des liens avec d'autres instances
	protected void fireAdd (ModelAbstract associate, int index) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventAdd(this, associate, index));
	}
	protected void fireAdd (ModelAbstract associate, int index, Integer role) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventAdd(this, associate, index, role));
	}
	protected void fireRemove (ModelAbstract associate, int index) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventRemove(this, associate, index));
	}
	protected void fireRemove (ModelAbstract associate, int index, Integer role) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventRemove(this, associate, index, role));
	}
	protected void fireMove (ModelAbstract associate, int before, int after) {
		// Envoyer l'évenemtn à tous les listeners
		this.fire(new EventMove(this, associate, before, after));
	}
	protected void fireClear (Class<?> associate) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventClear(this, associate));
	}
	protected void fireClear (Class<?> associate, Integer role) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventClear(this, associate, role));
	}
	
	// Mettre à jour un ArrayList
	protected <L, M extends ModelAbstract> void updates (IUpdater<L, M> updater, ArrayList<M> associates, TrackableList<L> list, boolean remove, Integer role) throws ContestOrgModelException {
		// Modifier les models à modifier
		for (int before : list.getUpdates()) {
			// Rechercher l'index correspondant à before dans la nouvelle liste
			for (int after = 0; after < list.getCorrespondances().size(); after++) {
				if (list.getCorrespondances().get(after) == before) {
					// Modifier le model
					updater.update(associates.get(before), list.get(after));
				}
			}
		}
		
		// Retenir quels models sont à supprimer
		ArrayList<ModelAbstract> suppressions = new ArrayList<ModelAbstract>();
		for (int index : list.getDeletions()) {
			suppressions.add(associates.get(index));
		}
		
		// Déplacer les models à déplacer et fires correspondants
		int after = 0;
		for (int before : list.getCorrespondances()) {
			if(list.getDeletions().indexOf(before) == -1) {
				if (before != -1 && (after < before || after != before && list.getCorrespondances().get(after) == -1)) {
					associates.add(Math.min(associates.size()-1,after), associates.remove(before)); // Deplacer le model
					this.fireMove(associates.get(Math.min(associates.size()-1,after)), before, Math.min(associates.size()-1,after)); // Fire
				}
				after++;
			}
		}
		
		// Ajouter les models à ajouter et fires correspondants
		int indexA = 0;
		for (int correspondance : list.getCorrespondances()) {
			if (correspondance == -1) {
				associates.add(indexA, updater.create(list.get(indexA))); // Ajouter le model
				this.fireAdd(associates.get(indexA), indexA); // Fire
			}
			indexA++;
		}
		
		// Supprimer les models à supprimer et fires correspondants
		for (ModelAbstract associate : suppressions) {
			int indexB = associates.indexOf(associate);
			associates.remove(associate); // Retirer le model
			if (remove) {
				associate.delete(this); // Supprimer le model
			}
			this.fireRemove(associate, indexB, role); // Fire
		}
	}
	protected <T> void links (ILinker<T> linker, TrackableList<T> list) throws ContestOrgModelException {
		// Récupérer les originaux
		ArrayList<T> originaux = list.getOriginaux();
		
		// Supprimer les liens à supprimer
		for (int index : list.getDeletions()) {
			linker.unlink(originaux.get(index));
		}
		
		// Créer les liens à créer
		int index = 0;
		for (int correspondance : list.getCorrespondances()) {
			if (correspondance == -1) {
				linker.link(list.get(index));
			}
			index++;
		}
	}
	
	// Id à prendre par le futur model crée
	private static int counter = 0;
	
	// Models
	protected static ArrayList<ModelAbstract> models = new ArrayList<ModelAbstract>();
	
	// Listeners statiques
	private static ArrayList<IEventListener> listenersStatic = new ArrayList<IEventListener>();
	
	// Indicateur d'arret des fires
	private static boolean fires = true;
	
	// Nettoyer les propriétés statiques
	protected static void clean () {
		ModelAbstract.counter = 0;
		ModelAbstract.models = new ArrayList<ModelAbstract>();
		ModelAbstract.listenersStatic = new ArrayList<IEventListener>();
		ModelAbstract.fires = true;
	}
	
	// Ajouter un observer statique
	public static void addStaticListener (IEventListener listener) {
		ModelAbstract.listenersStatic.add(listener);
	}
	protected static void removeStaticListener (IEventListener listener) {
		ModelAbstract.listenersStatic.remove(listener);
	}
	
	// Arreter/Reprendre les fires
	public static void startFires () {
		ModelAbstract.fires = true;
	}
	public static void stopFires () {
		ModelAbstract.fires = false;
	}
	
	// Recherche dans les models
	public static ModelAbstract search (int id) {
		// Rechercher le model correspondant à l'id
		for (ModelAbstract model : ModelAbstract.models) {
			if (model.getId() == id) {
				return model;
			}
		}
		
		// Retourner null si pas de résultat trouvé
		return null;
	}
	protected static ArrayList<ModelAbstract> search (IValidator<ModelAbstract> valideur) {
		// Initialiser la liste résultat
		ArrayList<ModelAbstract> resultat = new ArrayList<ModelAbstract>();
		
		// Ajouter les models de la classe donnée dans la liste résultat
		for (ModelAbstract model : ModelAbstract.models) {
			if (valideur.validate(model)) {
				resultat.add(model);
			}
		}
		
		// Retourner la liste résultat
		return resultat;
	}
	
}
