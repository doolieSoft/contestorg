package org.contestorg.models;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.contestorg.events.Action;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.IHistoryListener;

/**
 * Modèle de données pour tableau de matchs des phases qualificatives
 */
public class TableModelPhasesQualifsMatchs implements TableModel, IClosableTableModel, IHistoryListener
{
	/** Concours */
	private ModelConcours concours;
	
	/** Catégorie */
	private ModelCategorie categorie;
	
	/** Poule */
	private ModelPoule poule;
	
	/** Phase qualificative */
	private ModelPhaseQualificative phaseQualif;
	
	/** Liste des matchs */
	private ArrayList<ModelMatchPhasesQualifs> matchs = new ArrayList<ModelMatchPhasesQualifs>();
	
	/** Liste des listeners */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	// Constructeurs
	
	/**
	 * Constructeur avec le concours comme conteneur
	 * @param concours
	 */
	public TableModelPhasesQualifsMatchs(ModelConcours concours) {
		this(concours, null, null, null);
	}
	
	/**
	 * Constructeur avec une catégorie comme conteneur
	 * @param categorie
	 */
	public TableModelPhasesQualifsMatchs(ModelCategorie categorie) {
		this(null, categorie, null, null);
	}
	
	/**
	 * Constructeur avec une poule comme conteneur
	 * @param poule
	 */
	public TableModelPhasesQualifsMatchs(ModelPoule poule) {
		this(null, null, poule, null);
	}
	
	/**
	 * Constructeur avec une phase qualificative comme conteneur
	 * @param phaseQualif
	 */
	public TableModelPhasesQualifsMatchs(ModelPhaseQualificative phaseQualif) {
		this(null, null, null, phaseQualif);
	}
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param categorie catégorie
	 * @param poule poule
	 * @param phaseQualif phase qualificative
	 */
	private TableModelPhasesQualifsMatchs(ModelConcours concours, ModelCategorie categorie, ModelPoule poule, ModelPhaseQualificative phaseQualif) {
		// Retenir le concours, la categorie, la poule et la phase
		this.concours = concours;
		this.categorie = categorie;
		this.poule = poule;
		this.phaseQualif = phaseQualif;
		
		// Initialiser la liste des participants
		if (this.concours != null) {
			this.matchs = this.concours.getMatchsPhasesQualifs();
		} else if (this.categorie != null) {
			this.matchs = this.categorie.getMatchsPhasesQualifs();
		} else if (this.poule != null) {
			this.matchs = this.poule.getMatchsPhasesQualifs();
		} else if (this.phaseQualif != null) {
			this.matchs = this.phaseQualif.getMatchs();
		}
		
		// Ecouter l'historique du concours
		FrontModel.get().getHistory().addListener(this);
	}
	
	/**
	 * Rafraichir la liste des matchs
	 */
	private void refresh () {
		// Retenir le nombre de matchs dans l'ancienne liste
		int nbMatchsAnciens = this.matchs.size();
		
		// Mettre à jour la liste des participants
		if (this.concours != null) {
			this.matchs = this.concours.getMatchsPhasesQualifs();
		} else if (this.categorie != null) {
			this.matchs = this.categorie.getMatchsPhasesQualifs();
		} else if (this.poule != null) {
			this.matchs = this.poule.getMatchsPhasesQualifs();
		} else if (this.phaseQualif != null) {
			this.matchs = this.phaseQualif.getMatchs();
		}
		
		// Retenir le nombre de matchs dans la nouvelle liste
		int nbMatchsNouveaux = this.matchs.size();
		
		// Signaler les modifications
		if(nbMatchsAnciens > 0 || nbMatchsNouveaux > 0) {
			// Min/Max
			int max = Math.max(nbMatchsAnciens,nbMatchsNouveaux);
			int min = Math.min(nbMatchsAnciens,nbMatchsNouveaux);
			
			// Updates
			if(nbMatchsAnciens > 0 && nbMatchsNouveaux != 0) {
				this.fireRowUpdated(0, min-1);
			}
			
			// Inserts/Deletes
			if(nbMatchsAnciens < nbMatchsNouveaux) {
				this.fireRowInserted(min, max-1);
			} else if(nbMatchsAnciens > nbMatchsNouveaux) {
				this.fireRowDeleted(min, max-1);
			}
		}
	}
	
	// Signalements
	
	/**
	 * Signaler un événement
	 * @param first indice de la prémière ligne concernée
	 * @param last indice de la dernière ligne concernée
	 * @param type type de l'événement
	 */
	protected void fire (int first, int last, int type) {
		TableModelEvent event = new TableModelEvent(this, first, last, TableModelEvent.ALL_COLUMNS, type);
		for (TableModelListener listener : this.listeners) {
			listener.tableChanged(event);
		}
	}
	
	/**
	 * Signaler l'insertion de lignes
	 * @param first indice de la prémière ligne concernée
	 * @param last indice de la dernière ligne concernée
	 */
	protected void fireRowInserted (int first, int last) {
		// Fire des listeners de TableModel
		this.fire(first, last, TableModelEvent.INSERT);
	}
	
	/**
	 * Signaler la modification de lignes
	 * @param first indice de la prémière ligne concernée
	 * @param last indice de la dernière ligne concernée
	 */
	protected void fireRowUpdated (int first, int last) {
		// Fire des listeners de TableModel
		this.fire(first, last, TableModelEvent.UPDATE);
	}
	
	/**
	 * Signaler la supression de lignes
	 * @param first indice de la prémière ligne concernée
	 * @param last indice de la dernière ligne concernée
	 */
	protected void fireRowDeleted (int first, int last) {
		// Fire des listeners de TableModel
		this.fire(first, last, TableModelEvent.DELETE);
	}
	
	/**
	 * Récupérer le décalage en fonction du conteneur
	 * @return décalage en fonction du conteneur
	 */
	private int getDecallage () {
		if (this.phaseQualif != null) {
			return 3;
		} else if (this.poule != null) {
			return 2;
		} else if (this.categorie != null) {
			return 1;
		} else {
			return 0;
		}
	}
	
	// Implémentation de TableModel
	@Override
	public void addTableModelListener (TableModelListener listener) {
		this.listeners.add(listener);
	}
	@Override
	public void removeTableModelListener (TableModelListener listener) {
		this.listeners.remove(listener);
	}
	@Override
	public Class<?> getColumnClass (int column) {
		switch (column + this.getDecallage()) {
			case 0: // Catégorie
				return String.class;
			case 1: // Poule
				return String.class;
			case 2: // Phase
				return Integer.class;
			case 3: // Participant A
				return String.class;
			case 4: // Résultats A
				return Integer.class;
			case 5: // Points A
				return Double.class;
			case 6: // Participant B
				return String.class;
			case 7: // Résultats B
				return Integer.class;
			case 8: // Points B
				return Double.class;
		}
		return null;
	}
	@Override
	public int getColumnCount () {
		return 9 - this.getDecallage();
	}
	@Override
	public String getColumnName (int column) {
		switch (column + this.getDecallage()) {
			case 0:
				return "Catégorie";
			case 1:
				return "Poule";
			case 2:
				return "Phase";
			case 3:
				return "Participant A";
			case 4:
				return "Résultat";
			case 5:
				return "Points";
			case 6:
				return "Participant B";
			case 7:
				return "Résultat";
			case 8:
				return "Points";
		}
		return null;
	}
	@Override
	public int getRowCount () {
		return this.matchs.size();
	}
	@Override
	public Object getValueAt (int index, int column) {
		// Se proteger contre les effets de bord liés à l'ordre de fire des listeners de l'historique
		if(this.matchs.get(index).getPhaseQualificative() == null) {
			return null;
		}
		
		// Retourner la donnée demandée
		switch (column + this.getDecallage()) {
			case 0: // Catégorie
				return this.matchs.get(index).getPhaseQualificative().getPoule().getCategorie().getNom();
			case 1: // Poule
				return this.matchs.get(index).getPhaseQualificative().getPoule().getNom();
			case 2: // Phase
				return this.matchs.get(index).getPhaseQualificative().getNumero();
			case 3: // Participant A
				ModelParticipant participantA = this.matchs.get(index).getParticipationA().getParticipant();
				return participantA == null ? null : participantA.getNom();
			case 4: // Résultats A
				return this.matchs.get(index).getParticipationA().getResultat();
			case 5: // Points A
				return this.matchs.get(index).getParticipationA().getPoints();
			case 6: // Participant B
				ModelParticipant participantB = this.matchs.get(index).getParticipationB().getParticipant();
				return participantB == null ? null : participantB.getNom();
			case 7: // Résultats B
				return this.matchs.get(index).getParticipationB().getResultat();
			case 8: // Points B
				return this.matchs.get(index).getParticipationB().getPoints();
		}
		return null;
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return false;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
	}
	
	// Implémentation de IHistoryListener
	@Override
	public void historyActionPushed (Action action) {
		this.refresh();
	}
	@Override
	public void historyActionPoped (Action action) {
		this.refresh();
	}
	@Override
	public void historyInit () {
		// Retenir le concours
		this.concours = FrontModel.get().getConcours();
		this.categorie = null;
		this.poule = null;
		
		// Rafraichir la liste des participants
		this.refresh();
	}
	
	// Implémentation de IClosableTableModel
	@Override
	public void close () {
		FrontModel.get().getHistory().removeListener(this);
	}
	
}
