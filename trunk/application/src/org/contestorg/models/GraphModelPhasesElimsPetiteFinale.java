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



public class GraphModelPhasesElimsPetiteFinale implements IGraphModel<InfosModelCategorie,InfosModelParticipant>, IEventListener, IHistoryListener
{
	// Catégorie associée au graphe
	private ModelCategorie categorie;
	
	// Phases éliminatoires associées à la catégorie
	private ModelPhasesEliminatoires phases;
	
	// Petite finale associée aux phases finales
	private ModelMatchPhasesElims petiteFinale;
	
	// Listeners
	private ArrayList<IGraphModelListener> listeners = new ArrayList<IGraphModelListener>();
	
	// Cellules
	private CelluleModelPhasesElimsMatch cellulePetiteFinale;
	private CelluleModelPhasesElimsParticipation celluleParticipationA;
	private CelluleModelPhasesElimsParticipation celluleParticipationB;

	// Données modifiées ?
	private boolean isChanged = false;
	
	// Constructeur
	public GraphModelPhasesElimsPetiteFinale(ModelCategorie categorie) {
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
	
	// Implémentation de IGraphModel
	@Override
	public InfosModelCategorie getObject() {
		return this.categorie == null ? null : this.categorie.toInfos();
	}
	@Override
	public ICelluleModel<InfosModelCategorie,InfosModelParticipant> getCellule (int index) {
		switch(index) {
			case 0: return this.celluleParticipationA;
			case 1: return this.celluleParticipationB;
			case 2 : return this.cellulePetiteFinale;
			default: return null;
		}
	}
	@Override
	public int indexOf(ICelluleModel<InfosModelCategorie,InfosModelParticipant> cellule) {
		if(cellule == this.cellulePetiteFinale) {
			return 2;
		} else if(cellule == this.celluleParticipationA) {
			return 0;
		} else if(cellule == this.celluleParticipationB) {
			return 1;
		}
		return -1;
	}
	@Override
	public int size () {
		return this.celluleParticipationA != null && this.celluleParticipationB != null ? 2 : 0;
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

	// Recharger le graphe
	private void reload() {
		// Fermer toutes les cellules
		if(this.cellulePetiteFinale != null) {
			this.cellulePetiteFinale.close();
			this.cellulePetiteFinale = null;
		}
		if(this.celluleParticipationA != null) {
			this.celluleParticipationA.close();
			this.celluleParticipationA = null;
		}
		if(this.celluleParticipationB != null) {
			this.celluleParticipationB.close();
			this.celluleParticipationB = null;
		}
		
		// Ne plus écouter les anciennes phases éliminatoires
		if(this.phases != null) {
			this.phases.removeListener(this);
		}
		
		// Ne plus écouter l'ancienne petite finale
		if(this.petiteFinale != null) {
			this.petiteFinale.removeListener(this);
		}
		
		// Changer les phases éliminatoires
		this.phases = this.categorie.getPhasesEliminatoires();
		
		// Changer la petite finale
		if(this.phases != null) {
			this.petiteFinale = this.phases.getPetiteFinale();
		}
		
		// Ecouter les nouvelles phases éliminatoires
		if(this.phases != null) {
			this.phases.addListener(this);
		}
		
		// Ecouter la nouvelle petite finale
		if(this.petiteFinale != null) {
			this.petiteFinale.addListener(this);
		}
		
		// Remplir la liste des cellules
		if(this.petiteFinale != null && !this.petiteFinale.isDeleted()) {
			this.cellulePetiteFinale = new CelluleModelPhasesElimsMatch(this, this.phases == null ? null : this.phases.getPetiteFinale());
			this.celluleParticipationA = new CelluleModelPhasesElimsParticipation(this, this.phases == null || this.phases.getPetiteFinale() == null ? null : this.phases.getPetiteFinale().getParticipationA());
			this.celluleParticipationA.setEditable(false);
			this.celluleParticipationB = new CelluleModelPhasesElimsParticipation(this, this.phases == null || this.phases.getPetiteFinale() == null ? null : this.phases.getPetiteFinale().getParticipationB());
			this.celluleParticipationB.setEditable(false);
		}
		
		// Fires
		for(IGraphModelListener listener : new ArrayList<IGraphModelListener>(this.listeners)) {
			listener.reloadGraphe();
		}
		
		// Le graphe est à jour
		this.isChanged = false;
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
