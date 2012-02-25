package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.events.Action;
import org.contestorg.events.Event;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.interfaces.ICelluleModel;
import org.contestorg.interfaces.IEventListener;
import org.contestorg.interfaces.IGraphModel;
import org.contestorg.interfaces.IGraphModelListener;
import org.contestorg.interfaces.IHistoryListener;

/**
 * Modèle de données pour le graphe pyramidal de la grande finale
 */
public class GraphModelPhasesElimsGrandeFinale implements IGraphModel<InfosModelCategorie,InfosModelParticipant>, IEventListener, IHistoryListener
{
	/** Catégorie associée au graphe */
	private ModelCategorie categorie;
	
	/** Phases éliminatoires associées à la catégorie */
	private ModelPhasesEliminatoires phases;
	
	/** Liste des listeners */
	private ArrayList<IGraphModelListener> listeners = new ArrayList<IGraphModelListener>();
	
	/** Données modifiées ? */
	private boolean isChanged = false;
	
	/** Cellules */
	private ArrayList<ICelluleModel<InfosModelCategorie,InfosModelParticipant>> cellules = new ArrayList<ICelluleModel<InfosModelCategorie,InfosModelParticipant>>();
	
	/**
	 * Constructeur
	 * @param categorie catégorie associée au graphe
	 */
	public GraphModelPhasesElimsGrandeFinale(ModelCategorie categorie) {
		if(categorie != null) {
			// Retenir/Ecouter la catégorie
			this.categorie = categorie;
			this.categorie.addListener(this);
			
			// (Re)Charger le graphe
			this.reload();
			
			// Ecouter l'historique
			FrontModel.get().getHistory().addListener(this);
		}
	}

	/**
	 * Recharger le graphe
	 */
	private void reload() {
		// Fermer toutes les cellules
		for(ICelluleModel<InfosModelCategorie,InfosModelParticipant> cellule : this.cellules) {
			cellule.close();
		}
		
		// Vider la liste des cellules
		this.cellules.clear();
		
		// Ne plus écouter les anciennes phases éliminatoires
		if(this.phases != null) {
			this.phases.removeListener(this);
		}
		
		// Changer les phases éliminatoires
		this.phases = this.categorie.getPhasesEliminatoires();

		// Vérifier si les phases éliminatoires n'ont pas été supprimées 
		if(this.phases != null && !this.phases.isDeleted()) {
			// Ecouter les nouvelles phases éliminatoires
			this.phases.addListener(this);
			
			// Remplir la liste des cellules
			int nbParticipants = this.phases == null ? 0 : this.phases.getNbParticipants();
			int nbCellules = 2*nbParticipants-1;
			for(int i=0;i<nbCellules;i++) {
				if(i < nbParticipants) {
					// Récupérer le match
					ModelMatchPhasesElims match =  this.phases.getMatch((i-(i%2))/2);
					
					// Récupérer la participation
					ModelParticipation participation = i%2 == 0 ? match.getParticipationA() : match.getParticipationB();
					
					// Ajouter la cellule
					this.cellules.add(new CelluleModelPhasesElimsParticipation(this, participation));
				} else {
					// Ajouter la cellule
					this.cellules.add(new CelluleModelPhasesElimsMatch(this, this.phases.getMatch(i-nbParticipants)));
				}
			}
		}
		
		// Fires
		for(IGraphModelListener listener : new ArrayList<IGraphModelListener>(this.listeners)) {
			listener.reloadGraphe();
		}
		
		// Le graphe est à jour
		this.isChanged = false;
	}
	
	// Implémentation de IGraphModel
	@Override
	public InfosModelCategorie getObject() {
		return this.categorie == null ? null : this.categorie.getInfos();
	}
	@Override
	public ICelluleModel<InfosModelCategorie,InfosModelParticipant> getCellule (int index) {
		return index < this.cellules.size() ? this.cellules.get(index) : null;
	}
	@Override
	public int indexOf(ICelluleModel<InfosModelCategorie,InfosModelParticipant> cellule) {
		return this.cellules.indexOf(cellule);
	}
	@Override
	public int size () {
		return (this.cellules.size()+1)/2;
	}
	@Override
	public void close () {
		// Ne plus écouter la catégorie
		if(this.categorie != null) {
			this.categorie.removeListener(this);
		}
		
		// Ne plus écouter les phases éliminatoires
		if(this.phases != null) {
			this.phases.removeListener(this);
		}
		
		// Ne plus écouter l'historique
		FrontModel.get().getHistory().removeListener(this);
	}
	@Override
	public void addListener (IGraphModelListener listener) {
		this.listeners.add(listener);
	}
	@Override
	public void removeListener (IGraphModelListener listener) {
		this.listeners.remove(listener);
	}

		
	// Implémentation de IEventListener
	@Override
	public void event (Event event) {
		// Retenir le changement
		this.isChanged = true;
	}
	
	// Implémentation de IHistoryListener
	@Override
	public void historyActionPushed (Action action) {
		// Recharger le graphe
		if(this.isChanged) {
			this.reload();
		}
	}
	@Override
	public void historyActionPoped (Action action) {
		// Recharger le graphe
		if(this.isChanged) {
			this.reload();
		}
	}
	@Override
	public void historyInit () {
		// Recharger le graphe
		if(this.isChanged) {
			this.reload();
		}
	}
}
