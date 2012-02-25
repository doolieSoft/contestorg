package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.events.Event;
import org.contestorg.events.EventAdd;
import org.contestorg.events.EventClear;
import org.contestorg.events.EventDelete;
import org.contestorg.events.EventMove;
import org.contestorg.events.EventRemove;
import org.contestorg.events.EventUpdate;
import org.contestorg.infos.InfosModelAbstract;
import org.contestorg.interfaces.IEventListener;
import org.contestorg.interfaces.ILinker;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.interfaces.IValidator;

/**
 * Modèle abstrait
 */
public abstract class ModelAbstract
{
	
	/** Identifiant */
	private Integer id = null;
	
	/** Est supprimé ? */
	private boolean isDeleted = false;
	
	/** Liste des listeners */
	private ArrayList<IEventListener> listeners = new ArrayList<IEventListener>();
	
	/**
	 * Vérifier si un modèle est égal ?
	 * @return modèle égal ?
	 */
	public boolean equals (Object object) {
		return object != null && object.getClass() == this.getClass() && ((ModelAbstract)object).getId() == this.getId();
	}
	
	// Getters
	
	/**
	 * Récupérer l'identifiant
	 * @return identifiant
	 */
	public Integer getId () {
		// Retourner l'id du model en prenant soin de le créer s'il n'en a pas
		return this.id == null ? this.id = ModelAbstract.counter++ : this.id;
	}
	
	/**
	 * Savoir si le modèle est supprimée
	 * @return modèle supprimé ?
	 */
	public boolean isDeleted() {
		return this.isDeleted;
	}
	
	/**
	 * Récupérer les informations
	 * @return informations
	 */
	public abstract InfosModelAbstract getInfos ();
	
	// Setters
	
	/**
	 * Définir l'identifiant
	 * @param id identifiant
	 */
	public void setId (Integer id) {
		// Vérifier si le compteur n'est pas inférieur à l'id
		if (id >= ModelAbstract.counter) {
			ModelAbstract.counter = id + 1;
		}
		
		// Enregistrer l'id
		this.id = id;
	}
	
	/**
	 * Définir les informations
	 * @param infos nouvelles informations
	 */
	protected void setInfos (InfosModelAbstract infos) {	
	}
	
	// Listeners
	
	/**
	 * Ajouter un listener
	 * @param listener listener
	 */
	public void addListener (IEventListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Retirer un listener
	 * @param listener listener
	 */
	public void removeListener (IEventListener listener) {
		this.listeners.remove(listener);
	}
	
	// Suppressions
	
	/**
	 * Supprimer le modèle
	 * @throws ContestOrgModelException
	 */
	protected void delete () throws ContestOrgModelException {
		this.delete(new ArrayList<ModelAbstract>());
	}
	
	/**
	 * Supprimer le modèle
	 * @param origin origine du message de suppression
	 * @throws ContestOrgModelException
	 */
	protected void delete (ModelAbstract origin) throws ContestOrgModelException {
		ArrayList<ModelAbstract> removers = new ArrayList<ModelAbstract>();
		removers.add(origin);
		this.delete(removers);
	}
	
	/**
	 * Supprimer le modèle
	 * @param removers liste des modèles traversées par le message de suppression
	 * @throws ContestOrgModelException
	 */
	protected abstract void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException;
	
	// Signalements
	
	/**
	 * Signaler un évenement
	 * @param event évenement
	 */
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
	
	/**
	 * Signaler la mise à jour du modèle
	 */
	protected void fireUpdate () {
		// Ajouter le modèle à la liste des models
		if (!ModelAbstract.models.contains(this)) {
			ModelAbstract.models.add(this);
		}
		
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventUpdate(this));
	}
	
	/**
	 * Signaler la suppression du modèle
	 */
	protected void fireDelete () {
		// Retenir la suppression
		this.isDeleted = true;
		
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventDelete(this));
		
		// Retirer le modèle de la liste des models
		if (ModelAbstract.fires && !ModelAbstract.models.contains(this)) {
			ModelAbstract.models.remove(this);
		}
	}
	
	/**
	 * Signaler l'ajout d'un modèle associé 
	 * @param associate modèle associé
	 * @param index indice d'ajout
	 */
	protected void fireAdd (ModelAbstract associate, int index) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventAdd(this, associate, index));
	}
	
	/**
	 * Signaler l'ajout d'un modèle associé
	 * @param associate modèle associé
	 * @param index indice d'ajout
	 * @param role rôle du modèle associé
	 */
	protected void fireAdd (ModelAbstract associate, int index, Integer role) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventAdd(this, associate, index, role));
	}
	
	/**
	 * Signaler la suppression d'un modèle associé
	 * @param associate modèle associé
	 * @param index indice de suppression
	 */
	protected void fireRemove (ModelAbstract associate, int index) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventRemove(this, associate, index));
	}
	
	/**
	 * Signaler la suppression d'un modèle associé
	 * @param associate modèle associé
	 * @param index indice de suppression
	 * @param role rôle du modèle associé
	 */
	protected void fireRemove (ModelAbstract associate, int index, Integer role) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventRemove(this, associate, index, role));
	}
	
	/**
	 * Signaler le déplacement d'un modèle associé
	 * @param associate modèle associé
	 * @param before indice source
	 * @param after indice cible
	 */
	protected void fireMove (ModelAbstract associate, int before, int after) {
		// Envoyer l'évenemtn à tous les listeners
		this.fire(new EventMove(this, associate, before, after));
	}
	
	/**
	 * Signaler la suppression d'une liste de modèles associés
	 * @param associate classe des modèles associés
	 */
	protected void fireClear (Class<?> associate) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventClear(this, associate));
	}
	
	/**
	 * Signaler la suppression d'une liste de modèles associés
	 * @param associate classe des modèles associés
	 * @param role rôle des modèles associés
	 */
	protected void fireClear (Class<?> associate, Integer role) {
		// Envoyer l'évenement à tous les listeners
		this.fire(new EventClear(this, associate, role));
	}
	
	// Modèles associés
	
	/**
	 * Mettre à jour une liste de modèles associés
	 * @param updater objet de mise à jour
	 * @param target modèles associés cible
	 * @param source modèle associés source
	 * @param remove appliquer des suppressions si nécéssaire ?
	 * @param role rôle des modèles associés
	 * @throws ContestOrgModelException
	 */
	protected <L, M extends ModelAbstract> void updates (IUpdater<L, M> updater, ArrayList<M> target, TrackableList<L> source, boolean remove, Integer role) throws ContestOrgModelException {
		// Modifier les modèles à modifier
		for (int before : source.getUpdates()) {
			// Rechercher l'index correspondant à before dans la nouvelle liste
			for (int after = 0; after < source.getCorrespondances().size(); after++) {
				if (source.getCorrespondances().get(after) == before) {
					// Modifier le modèle
					updater.update(target.get(before), source.get(after));
				}
			}
		}
		
		// Retenir quels models sont à supprimer
		ArrayList<ModelAbstract> suppressions = new ArrayList<ModelAbstract>();
		for (int index : source.getDeletions()) {
			suppressions.add(target.get(index));
		}
		
		// Déplacer les modèles à déplacer et fires correspondants
		int after = 0;
		for (int before : source.getCorrespondances()) {
			if(source.getDeletions().indexOf(before) == -1) {
				if (before != -1 && (after < before || after != before && source.getCorrespondances().get(after) == -1)) {
					target.add(Math.min(target.size()-1,after), target.remove(before)); // Deplacer le modèle
					this.fireMove(target.get(Math.min(target.size()-1,after)), before, Math.min(target.size()-1,after)); // Fire
				}
				after++;
			}
		}
		
		// Ajouter les modèles à ajouter et fires correspondants
		int indexA = 0;
		for (int correspondance : source.getCorrespondances()) {
			if (correspondance == -1) {
				target.add(indexA, updater.create(source.get(indexA))); // Ajouter le modèle
				this.fireAdd(target.get(indexA), indexA); // Fire
			}
			indexA++;
		}
		
		// Supprimer les modèles à supprimer et fires correspondants
		for (ModelAbstract associate : suppressions) {
			int indexB = target.indexOf(associate);
			target.remove(associate); // Retirer le modèle
			if (remove) {
				associate.delete(this); // Supprimer le modèle
			}
			this.fireRemove(associate, indexB, role); // Fire
		}
	}
	
	/**
	 * Associer/Désassocier des modèles
	 * @param linker objet d'association
	 * @param list modèles associés
	 * @throws ContestOrgModelException
	 */
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
	
	/** Identifiant pour le futur modèle créé */
	private static int counter = 0;
	
	/** Modèles créés */
	protected static ArrayList<ModelAbstract> models = new ArrayList<ModelAbstract>();
	
	/** Liste des listeners */
	private static ArrayList<IEventListener> listenersStatic = new ArrayList<IEventListener>();
	
	/** Indicateur d'arrêt des signalements */
	private static boolean fires = true;
	
	/**
	 * Nettoyer les propriétés statiques
	 */
	protected static void clean () {
		ModelAbstract.counter = 0;
		ModelAbstract.models = new ArrayList<ModelAbstract>();
		ModelAbstract.listenersStatic = new ArrayList<IEventListener>();
		ModelAbstract.fires = true;
	}
	
	// Listeners statiques
	
	/**
	 * Ajouter un listener statique
	 * @param listener listener statique à ajouter
	 */
	public static void addStaticListener (IEventListener listener) {
		ModelAbstract.listenersStatic.add(listener);
	}
	
	/**
	 * Retirer un listener statique
	 * @param listener listener statique à retirer
	 */
	protected static void removeStaticListener (IEventListener listener) {
		ModelAbstract.listenersStatic.remove(listener);
	}
	
	// Signalements
	
	/**
	 * Démarrer les signalements
	 */
	public static void startFires () {
		ModelAbstract.fires = true;
	}
	
	/**
	 * Arrêter les signalements
	 */
	public static void stopFires () {
		ModelAbstract.fires = false;
	}
	
	// Recherche
	
	/**
	 * Rechercher un modèle d'après son identifiant
	 * @param id identifiant
	 * @return modèle trouvé
	 */
	public static ModelAbstract search (int id) {
		// Rechercher le modèle correspondant à l'id
		for (ModelAbstract model : ModelAbstract.models) {
			if (model.getId() == id) {
				return model;
			}
		}
		
		// Retourner null si pas de résultat trouvé
		return null;
	}
	
	/**
	 * Rechercher une liste de modèles d'après un validateur
	 * @param validateur validateur
	 * @return liste de modèles validés par le validateur
	 */
	protected static ArrayList<ModelAbstract> search (IValidator<ModelAbstract> validateur) {
		// Initialiser la liste résultat
		ArrayList<ModelAbstract> resultat = new ArrayList<ModelAbstract>();
		
		// Ajouter les modèles de la classe donnée dans la liste résultat
		for (ModelAbstract model : new ArrayList<ModelAbstract>(ModelAbstract.models)) {
			if (validateur.validate(model)) {
				resultat.add(model);
			}
		}
		
		// Retourner la liste résultat
		return resultat;
	}
	
}
