package models;

import infos.InfosModelCategorie;
import infos.InfosModelEquipe;
import interfaces.ICelluleModel;
import interfaces.ICelluleModelListener;
import interfaces.IEventListener;
import interfaces.IGraphModel;
import interfaces.IHistoryListener;

import java.util.ArrayList;

import events.Action;
import events.Event;

public class CelluleModelPhasesElimsParticipation implements ICelluleModel<InfosModelCategorie,InfosModelEquipe>, IEventListener, IHistoryListener
{	
	// Graphe associé à la cellule
	private IGraphModel<InfosModelCategorie,InfosModelEquipe> graphe;
	
	// Participation associée à la cellule
	private ModelParticipation participation;
	
	// Equipe associée à la participation
	private ModelEquipe equipe;
	
	// Listeners
	private ArrayList<ICelluleModelListener> listeners = new ArrayList<ICelluleModelListener>();
	
	// Données modifiées ?
	private boolean isChanged = false;
	
	// Cellule éditable
	private boolean isEditable = true; 
	
	// Constructeur
	public CelluleModelPhasesElimsParticipation(IGraphModel<InfosModelCategorie,InfosModelEquipe> graphe, ModelParticipation participation) {
		// Retenir le graph
		this.graphe = graphe;
		
		if(participation != null) {
			// Retenir/Ecouter la participation
			this.participation = participation;
			this.participation.addListener(this);
			
			// Retenir/Ecouter l'équipe associée
			this.equipe = this.participation.getEquipe();
			if(this.equipe != null) {
				this.equipe.addListener(this);
			}
			
			// Ecouter l'historique
			FrontModel.get().getHistory().addListener(this);
		}
	}
	
	// Empecher l'édition
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	// Implémentation de ICelluleModel
	@Override
	public InfosModelEquipe getObject () {
		return this.equipe == null ? null : this.equipe.toInformation();
	}
	@Override
	public boolean isEditable() {
		// Vérifier si l'édition est autorisée
		if(!this.isEditable) {
			return false;
		}
		
		// Vérifier si la participation ou l'équipe est à définir
		if(this.participation == null || this.participation.getEquipe() == null) {
			 return true;
		}
		
		// Vérifier si le match de la participation n'est pas effectué
		ModelMatchPhasesElims match = ((ModelMatchPhasesElims)this.participation.getMatch());
		return !match.isEffectue() && match.getMatchPrecedantA() == null && match.getMatchPrecedantB() == null;
	}
	@Override
	public void close () {
		// Ne plus ecouter la participation et l'équipe associée
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
	public IGraphModel<InfosModelCategorie, InfosModelEquipe> getGraphe() {
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
	
	// Recharger la cellule
	private void reload() {
		// Vérifier si l'équipe associée à la participation a changé
		if(this.participation.getEquipe() != null && !this.participation.getEquipe().equals(this.equipe) || this.equipe != null && !this.equipe.equals(this.participation.getEquipe())) {
			// Ne plus écouter l'ancienne équipe
			if(this.equipe != null) {
				this.equipe.removeListener(this);
			}
			
			// Changer l'équipe
			this.equipe = this.participation.getEquipe();
	
			// Ecouter la nouvelle équipe
			if(this.equipe != null) {
				this.equipe.addListener(this);
			}
		}
		
		// Fires
		for(ICelluleModelListener listener : new ArrayList<ICelluleModelListener>(this.listeners)) {
			listener.updateCellule();
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
