package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.events.Action;
import org.contestorg.events.Event;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelEquipe;
import org.contestorg.interfaces.ICelluleModel;
import org.contestorg.interfaces.ICelluleModelListener;
import org.contestorg.interfaces.IEventListener;
import org.contestorg.interfaces.IGraphModel;
import org.contestorg.interfaces.IHistoryListener;


public class CelluleModelPhasesElimsMatch implements ICelluleModel<InfosModelCategorie,InfosModelEquipe>, IEventListener, IHistoryListener
{
	// Graphe associé à la cellule
	private IGraphModel<InfosModelCategorie,InfosModelEquipe> graphe;
	
	// Match associé à la cellule
	private ModelMatchPhasesElims match;
	
	// Vainqueur associée au match
	private ModelParticipation participation;
	private ModelEquipe equipe;
	
	// Listeners
	private ArrayList<ICelluleModelListener> listeners = new ArrayList<ICelluleModelListener>();
	
	// Données modifiées ?
	private boolean isChanged = false;
	
	// Constructeur
	public CelluleModelPhasesElimsMatch(IGraphModel<InfosModelCategorie,InfosModelEquipe> graphe, ModelMatchPhasesElims match) {
		// Retenir le graph
		this.graphe = graphe;
		
		if(match != null) {
			// Retenir/Ecouter le match
			this.match = match;
			this.match.addListener(this);
			
			// Retenir/Ecouter le vainqueur
			this.participation = this.match.getVainqueur();
			if(this.participation != null) {
				this.participation.addListener(this);
				this.equipe = this.participation.getEquipe();
				this.equipe.addListener(this);
			}
			
			// Ecouter l'historique
			FrontModel.get().getHistory().addListener(this);
		}
	}

	// Implémentation de ICelluleModel
	@Override
	public InfosModelEquipe getObject () {
		return this.equipe != null ? this.equipe.toInformation() : null;
	}
	@Override
	public boolean isEditable() {
		if(this.match != null) {
			return (this.match.getMatchPrecedantA() == null || this.match.getMatchPrecedantA().isEffectue()) && (this.match.getMatchPrecedantB() == null || this.match.getMatchPrecedantB().isEffectue());
		} else {
			return false;
		}
	}
	@Override
	public IGraphModel<InfosModelCategorie, InfosModelEquipe> getGraphe() {
		return this.graphe;
	}
	@Override
	public void close () {
		// Ne plus écouter le match et le vainqueur
		if(this.match != null) {
			this.match.removeListener(this);
		}
		if(this.participation != null) {
			this.participation.removeListener(this);
		}
		if(this.equipe != null) {
			this.equipe.removeListener(this);
		}
		
		// Ne plus écouter l'historique
		FrontModel.get().getHistory().removeListener(this);
	}
	@Override
	public void addListener (ICelluleModelListener listener) {
		this.listeners.add(listener);
	}
	@Override
	public void removeListener (ICelluleModelListener listener) {
		this.listeners.remove(listener);
	}
	
	// Recharger la cellule
	private void reload() {
		// Indicateur de modification
		boolean change = false;
		
		// Vérifier si la participation vainqueur a changé
		if(this.match.getVainqueur() != null && !this.match.getVainqueur().equals(this.participation) || this.participation != null && !this.participation.equals(this.match.getVainqueur())) {
			// Ne plus ecouter l'ancienne participation vainqueur
			if(this.participation != null) {
				this.participation.removeListener(this);
			}
			
			// Changer la participation vainqueur
			this.participation = this.match.getVainqueur();
			
			// Ecouter la nouvelle participation vainqueur
			if(this.participation != null) {
				this.participation.addListener(this);
			}
			
			// Retenir la modification
			change = true;
		}
		
		// Vérifier si l'équipe vainqueur a changé
		if(this.participation !=null && this.participation.getEquipe() != null && !this.participation.getEquipe().equals(this.equipe) || this.equipe != null && (this.participation == null || !this.equipe.equals(this.participation.getEquipe()))) {
			// Ne plus écouter l'ancienne équipe vainqueur
			if(this.equipe != null) {
				this.equipe.removeListener(this);
			}
			
			// Changer l'équipe vainqueur
			this.equipe = this.participation == null ? null : this.participation.getEquipe();
	
			// Ecouter la nouvelle équipe vainqueur
			if(this.equipe != null) {
				this.equipe.addListener(this);
			}
			
			// Retenir la modification
			change = true;
		}
		
		// Fires
		if(change) {
			for(ICelluleModelListener listener : new ArrayList<ICelluleModelListener>(this.listeners)) {
				listener.updateCellule();
			}
		}
		
		// La cellule est à jour
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
		// Recharger la cellule
		if(this.isChanged) {
			this.reload();
		}
	}
	@Override
	public void historyActionPoped (Action action) {
		// Recharger la cellule
		if(this.isChanged) {
			this.reload();
		}
	}
	@Override
	public void historyInit () {
		// Recharger la cellule
		if(this.isChanged) {
			this.reload();
		}
	}
	
}
