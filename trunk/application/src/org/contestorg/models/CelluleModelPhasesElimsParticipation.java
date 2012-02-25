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
 * Modèle de données d'une cellule de participation du graphe des phases éliminatoires d'une catégorie
 */
public class CelluleModelPhasesElimsParticipation implements ICelluleModel<InfosModelCategorie,InfosModelParticipant>, IEventListener, IHistoryListener
{	
	/** Graphe associé à la cellule */
	private IGraphModel<InfosModelCategorie,InfosModelParticipant> graphe;
	
	/** Participation associée à la cellule */
	private ModelParticipation participation;
	
	/** Participant associé à la participation */
	private ModelParticipant participant;
	
	/** Liste des listeners */
	private ArrayList<ICelluleModelListener> listeners = new ArrayList<ICelluleModelListener>();
	
	/** Données modifiées ? */
	private boolean isChanged = false;
	
	/** Cellule éditable ? */
	private boolean isEditable = true; 
	
	/**
	 * Constructeur
	 * @param graphe graphe associé à la cellule
	 * @param participation participation associée à la cellule
	 */
	public CelluleModelPhasesElimsParticipation(IGraphModel<InfosModelCategorie,InfosModelParticipant> graphe, ModelParticipation participation) {
		// Retenir le graph
		this.graphe = graphe;
		
		if(participation != null) {
			// Retenir/Ecouter la participation
			this.participation = participation;
			this.participation.addListener(this);
			
			// Retenir/Ecouter le participant associé
			this.participant = this.participation.getParticipant();
			if(this.participant != null) {
				this.participant.addListener(this);
			}
			
			// Ecouter l'historique
			FrontModel.get().getHistory().addListener(this);
		}
	}
	
	/**
	 * Définir si la cellule est éditable ou non
	 * @param isEditable cellule éditable ?
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	/**
	 * Recharger la cellule
	 */
	private void reload() {
		// Vérifier si le participant associé à la participation a changé
		if(this.participation.getParticipant() != null && !this.participation.getParticipant().equals(this.participant) || this.participant != null && !this.participant.equals(this.participation.getParticipant())) {
			// Ne plus écouter l'ancien participant
			if(this.participant != null) {
				this.participant.removeListener(this);
			}
			
			// Changer le participant
			this.participant = this.participation.getParticipant();
	
			// Ecouter le nouveau participant
			if(this.participant != null) {
				this.participant.addListener(this);
			}
		}
		
		// Fires
		for(ICelluleModelListener listener : new ArrayList<ICelluleModelListener>(this.listeners)) {
			listener.updateCellule();
		}
		
		// La cellule est à jour
		this.isChanged = false;
	}
	
	// Implémentation de ICelluleModel
	@Override
	public InfosModelParticipant getObject () {
		return this.participant == null ? null : this.participant.getInfos();
	}
	@Override
	public boolean isEditable() {
		// Vérifier si l'édition est autorisée
		if(!this.isEditable) {
			return false;
		}
		
		// Vérifier si la participation ou le participant est à définir
		if(this.participation == null || this.participation.getParticipant() == null) {
			 return true;
		}
		
		// Vérifier si le match de la participation n'est pas effectué
		ModelMatchPhasesElims match = ((ModelMatchPhasesElims)this.participation.getMatch());
		return !match.isEffectue() && match.getMatchPrecedantA() == null && match.getMatchPrecedantB() == null;
	}
	@Override
	public void close () {
		// Ne plus ecouter la participation et le participant associé
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
	public IGraphModel<InfosModelCategorie, InfosModelParticipant> getGraph() {
		return this.graphe;
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
