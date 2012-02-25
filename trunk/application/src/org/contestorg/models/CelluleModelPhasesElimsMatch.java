package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.events.Action;
import org.contestorg.events.Event;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.interfaces.ICelluleModel;
import org.contestorg.interfaces.ICelluleModelListener;
import org.contestorg.interfaces.IEventListener;
import org.contestorg.interfaces.IGraphModel;
import org.contestorg.interfaces.IHistoryListener;

/**
 * Modèle de données d'une cellule de match du graphe des phases éliminatoires d'une catégorie
 */
public class CelluleModelPhasesElimsMatch implements ICelluleModel<InfosModelCategorie,InfosModelParticipant>, IEventListener, IHistoryListener
{
	/** Graphe associé à la cellule */
	private IGraphModel<InfosModelCategorie,InfosModelParticipant> graphe;
	
	/** Match associé à la cellule */
	private ModelMatchPhasesElims match;
	
	/** Participation du vainqueur du match */
	private ModelParticipation participation;
	
	/** Vainqueur du match */
	private ModelParticipant participant;
	
	/** Liste des listeners */
	private ArrayList<ICelluleModelListener> listeners = new ArrayList<ICelluleModelListener>();
	
	/** Données modifiées ? */
	private boolean isChanged = false;
	
	/**
	 * Constructeur
	 * @param graphe modèle de données du graphe des phases éliminatoires d'une catégorie
	 * @param match match associé à la cellule
	 */
	public CelluleModelPhasesElimsMatch(IGraphModel<InfosModelCategorie,InfosModelParticipant> graphe, ModelMatchPhasesElims match) {
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
				this.participant = this.participation.getParticipant();
				this.participant.addListener(this);
			}
			
			// Ecouter l'historique
			FrontModel.get().getHistory().addListener(this);
		}
	}
	
	/**
	 * Recharger la cellule
	 */
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
		
		// Vérifier si le participant vainqueur a changé
		if(this.participation !=null && this.participation.getParticipant() != null && !this.participation.getParticipant().equals(this.participant) || this.participant != null && (this.participation == null || !this.participant.equals(this.participation.getParticipant()))) {
			// Ne plus écouter l'ancien participant vainqueur
			if(this.participant != null) {
				this.participant.removeListener(this);
			}
			
			// Changer le participant vainqueur
			this.participant = this.participation == null ? null : this.participation.getParticipant();
	
			// Ecouter le nouveau participant vainqueur
			if(this.participant != null) {
				this.participant.addListener(this);
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

	// Implémentation de ICelluleModel
	@Override
	public InfosModelParticipant getObject () {
		return this.participant != null ? this.participant.getInfos() : null;
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
	public IGraphModel<InfosModelCategorie, InfosModelParticipant> getGraph() {
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
		if(this.participant != null) {
			this.participant.removeListener(this);
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
