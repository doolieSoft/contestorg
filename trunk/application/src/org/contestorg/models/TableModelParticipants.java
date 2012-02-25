package org.contestorg.models;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.contestorg.events.Action;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.IHistoryListener;

/**
 * Modèle de données pour tableau de participants
 */
public class TableModelParticipants implements TableModel, IClosableTableModel, IHistoryListener
{
	
	/** Concours */
	private ModelConcours concours;
	
	/** Catégorie */
	private ModelCategorie categorie;
	
	/** Poule */
	private ModelPoule poule;
	
	/** Liste des participants */
	private ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
	
	/** Liste des listeners */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	// Constructeurs
	
	/**
	 * Constructeur avec le concours comme conteneur
	 * @param concours concours
	 */
	public TableModelParticipants(ModelConcours concours) {
		this(concours, null, null);
	}
	
	/**
	 * Constructeur avec une catégorie comme conteneur
	 * @param categorie catégorie
	 */
	public TableModelParticipants(ModelCategorie categorie) {
		this(null, categorie, null);
	}
	
	/**
	 * Constructeur avec une poule comme conteneur
	 * @param poule poule
	 */
	public TableModelParticipants(ModelPoule poule) {
		this(null, null, poule);
	}
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param categorie catégorie
	 * @param poule poule
	 */
	private TableModelParticipants(ModelConcours concours, ModelCategorie categorie, ModelPoule poule) {
		// Retenir le concours, la categorie et la poule
		this.concours = concours;
		this.categorie = categorie;
		this.poule = poule;
		
		// Initialiser la liste des participants
		if (this.concours != null) {
			this.participants = this.concours.getParticipants();
		} else if (this.categorie != null) {
			this.participants = this.categorie.getParticipants();
		} else if (this.poule != null) {
			this.participants = this.poule.getParticipants();
		}
		
		// Ecouter l'historique du concours
		FrontModel.get().getHistory().addListener(this);
	}
	
	/**
	 * Rafraichir la liste des participants
	 */
	private void refresh () {
		// Retenir le nombre de participants dans l'ancienne liste
		int nbParticipantsAnciens = this.participants.size();
		
		// Mettre à jour la liste des participants
		if (this.concours != null) {
			this.participants = this.concours.getParticipants();
		} else if (this.categorie != null) {
			this.participants = this.categorie.getParticipants();
		} else if (this.poule != null) {
			this.participants = this.poule.getParticipants();
		}
		
		// Retenir le nombre de participants dans la nouvelle liste
		int nbParticipantsNouveaux = this.participants.size();
		
		// Signaler les modifications
		if(nbParticipantsAnciens > 0 || nbParticipantsNouveaux > 0) {
			// Min/Max
			int max = Math.max(nbParticipantsAnciens,nbParticipantsNouveaux);
			int min = Math.min(nbParticipantsAnciens,nbParticipantsNouveaux);
			
			// Updates
			if(nbParticipantsAnciens > 0 && nbParticipantsNouveaux != 0) {
				this.fireRowUpdated(0, min-1);
			}
			
			// Inserts/Deletes
			if(nbParticipantsAnciens < nbParticipantsNouveaux) {
				this.fireRowInserted(min, max-1);
			} else if(nbParticipantsAnciens > nbParticipantsNouveaux) {
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
		if (this.poule != null) {
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
			case 0:
				return String.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			case 3:
				return String.class;
			case 4:
				return Double.class;
			case 5:
				return Double.class;
			case 6:
				return Integer.class;
			case 7:
				return String.class;
			case 8:
				return InfosModelParticipant.Statut.class;
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
				return "Stand";
			case 3:
				return "Nom";
			case 4:
				return "Rang";
			case 5:
				return "Points";
			case 6:
				return "Victoires";
			case 7:
				return "Ville";
			case 8:
				return "Statut";
		}
		return null;
	}
	@Override
	public int getRowCount () {
		return this.participants.size();
	}
	@Override
	public Object getValueAt (int index, int column) {
		switch (column + this.getDecallage()) {
			case 0:
				return this.participants.get(index).getPoule().getCategorie().getNom();
			case 1:
				return this.participants.get(index).getPoule().getNom();
			case 2:
				return this.participants.get(index).getStand();
			case 3:
				return this.participants.get(index).getNom();
			case 4:
				return this.participants.get(index).getRangPhasesQualifs();
			case 5:
				return this.participants.get(index).getPoints();
			case 6:
				return this.participants.get(index).getNbVictoires();
			case 7:
				return this.participants.get(index).getVille();
			case 8:
				return this.participants.get(index).getStatut();
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
