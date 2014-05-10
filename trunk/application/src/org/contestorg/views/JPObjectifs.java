package org.contestorg.views;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelObjectifPoints;
import org.contestorg.infos.InfosModelObjectifPourcentage;
import org.contestorg.infos.InfosModelObjectifRemporte;

/**
 * Panel des objectifs remportés au cours d'un match
 */
@SuppressWarnings("serial")
public class JPObjectifs extends JPanel implements ChangeListener
{	
	/** Liste des objectifs disponibles */
	private ArrayList<InfosModelObjectif> objectifsDisponibles;
	
	/** Points remportés par le participant A */
	private double pointsA = 0;
	
	/** Points remportés par le participant B */
	private double pointsB = 0;
	
	/** Listeners */
	private ArrayList<ChangeListener> listeners;
	
	// Entrées
	
	/** Nombres d'objectifs remportés par le participant A */
	private JSpinner[] js_objectifsA;

	/** Nombres d'objectifs remportés par le participant B */
	private JSpinner[] js_objectifsB;
	
	/** Liste des objectifs remportés par le participant A */
	private TrackableList<Pair<String,InfosModelObjectifRemporte>> objectifsRemportesA = new TrackableList<Pair<String,InfosModelObjectifRemporte>>();
	
	/** Liste des objectifs remportés par le participant B */
	private TrackableList<Pair<String,InfosModelObjectifRemporte>> objectifsRemportesB = new TrackableList<Pair<String,InfosModelObjectifRemporte>>();
	
	/**
	 * Constructeur
	 */
	public JPObjectifs() {
		// Définir le layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Remplir la liste des objetifs
		this.objectifsDisponibles = ContestOrg.get().getCtrlPhasesQualificatives().getListeObjectifs();
		if(objectifsDisponibles.size() != 0) {
			JLabel[] jls_objectifsA = new JLabel[this.objectifsDisponibles.size()];
			JLabel[] jls_objectifsB = new JLabel[this.objectifsDisponibles.size()];

			this.js_objectifsA = new JSpinner[this.objectifsDisponibles.size()];
			this.js_objectifsB = new JSpinner[this.objectifsDisponibles.size()];
			
			for(int i=0;i<this.objectifsDisponibles.size();i++) {
				this.js_objectifsA[i] = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
				this.js_objectifsB[i] = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
				jls_objectifsA[i] = new JLabel(this.objectifsDisponibles.get(i).getNom()+" : ");
				jls_objectifsB[i] = new JLabel(" "+this.objectifsDisponibles.get(i).getNom()+" : ");
				this.js_objectifsA[i].addChangeListener(this);
				this.js_objectifsB[i].addChangeListener(this);
			}

			JPanel jp_objectifs = new JPanel(new GridLayout(1,2));
			jp_objectifs.add(ViewHelper.inputs(jls_objectifsA, this.js_objectifsA));
			jp_objectifs.add(ViewHelper.inputs(jls_objectifsB, this.js_objectifsB));
			this.add(jp_objectifs);
		}
		
		// Initialiser la liste de listeners
		this.listeners = new ArrayList<ChangeListener>();
	}
	
	/**
	 * Ajouter un listener
	 * @param listener listener
	 */
	public void addChangeListener(ChangeListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Retirer un listener
	 * @param listener listener
	 */
	public void removeChangeListener(ChangeListener listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * Récupérer les points remportés par le participant A
	 * @return points remportés par le participant A
	 */
	public double getPointsA() {
		return this.pointsA;
	}
	
	/**
	 * Récupérer les points remportés par le participant B
	 * @return points remportés par le participant B
	 */
	public double getPointsB() {
		return this.pointsB;
	}
	
	// Vider les objectifs remportés
	public void clear() {
		// Vider la liste des objectifs remportés
		for(int i=0;i<this.objectifsDisponibles.size();i++) {
			this.js_objectifsA[i].setValue(0);
			this.js_objectifsB[i].setValue(0);
		}
	}
	
	/**
	 * Vérifier si le participant A a remporté des objectifs
	 * @return le participant A a remporté des objectifs ?
	 */
	public boolean isObjectifsRemportesA() {
		return this.objectifsRemportesA.size() != 0;
	}
	
	/**
	 * Vérifier si le participant B a remporté des objectifs
	 * @return le participant B a remporté des objectifs ?
	 */
	public boolean isObjectifsRemportesB() {
		return this.objectifsRemportesB.size() != 0;
	}
	
	/**
	 * Récupérer la liste des objectifs remportés par le participant A
	 * @return liste des objectifs remportés par le participant A
	 */
	public TrackableList<Pair<String, InfosModelObjectifRemporte>> getObjectifsRemportesA() {
		return this.objectifsRemportesA;
	}
	
	/**
	 * Récupérer la liste des objectifs remportés par le participant B
	 * @return liste des objectifs remportés par le participant B
	 */
	public TrackableList<Pair<String, InfosModelObjectifRemporte>> getObjectifsRemportesB() {
		return this.objectifsRemportesB;
	}
	
	/**
	 * Définir la liste des objectifs remportés par le participant A
	 * @param objectifs liste des objectifs remportés par le participant A
	 */
	public void setObjectifsRemportesA(ArrayList<Pair<String, InfosModelObjectifRemporte>> objectifs) {
		this.objectifsRemportesA.fill(objectifs);
		for(Pair<String, InfosModelObjectifRemporte> objectifRemporte : objectifs) {
			for (int i=0;i<this.objectifsDisponibles.size();i++) {
				if(objectifsDisponibles.get(i).getNom().equals(objectifRemporte.getFirst())) {
					this.js_objectifsA[i].setValue(objectifRemporte.getSecond().getQuantite());
				}
			}
		}
	}
	
	/**
	 * Définir la liste des objectifs remportés par le participant B
	 * @param objectifs liste des objectifs remportés par le participant B
	 */
	public void setObjectifsRemportesB(ArrayList<Pair<String, InfosModelObjectifRemporte>> objectifs) {
		this.objectifsRemportesB.fill(objectifs);
		for(Pair<String, InfosModelObjectifRemporte> objectifRemporte : objectifs) {
			for (int i=0;i<this.objectifsDisponibles.size();i++) {
				if(this.objectifsDisponibles.get(i).getNom().equals(objectifRemporte.getFirst())) {
					this.js_objectifsB[i].setValue(objectifRemporte.getSecond().getQuantite());
				}
			}
		}
	}

	/**
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void stateChanged (ChangeEvent eventRecu) {
		// Mettre à jour la liste des objectifs remportés
		for(int i=0;i<this.objectifsDisponibles.size();i++) {
			if (this.js_objectifsA[i] == eventRecu.getSource()) {
				// Récupérer les nouveaux nombres d'objectifs remportés
				int valeurA = (Integer)this.js_objectifsA[i].getValue();
				
				// Vérifier si l'objectif remporté avait déjà été ajouté
				boolean ancienA = false;
				int indexA = 0;
				for(Pair<String,InfosModelObjectifRemporte> objectifRemporte : this.objectifsRemportesA) {
					if(this.objectifsDisponibles.get(i).getNom().equals(objectifRemporte.getFirst())) {
						ancienA = true;
					} else if(!ancienA) {
						indexA++;
					}
				}
				
				// Ajouter/Modifier/Supprimer l'objectif remporté
				if(ancienA && valeurA == 0) { // Supprimer
					this.objectifsRemportesA.remove(indexA);
				} else if(ancienA) { // Modifier
					this.objectifsRemportesA.update(indexA, new Pair<String, InfosModelObjectifRemporte>(this.objectifsDisponibles.get(i).getNom(), new InfosModelObjectifRemporte(valeurA)));
				} else if(valeurA != 0) { // Ajouter
					this.objectifsRemportesA.add(new Pair<String, InfosModelObjectifRemporte>(this.objectifsDisponibles.get(i).getNom(), new InfosModelObjectifRemporte(valeurA)));
				}
			}
			if (this.js_objectifsB[i] == eventRecu.getSource()) {
				// Récupérer les nouveaux nombres d'objectifs remportés
				int valeurB = (Integer)this.js_objectifsB[i].getValue();
				
				// Vérifier si l'objectif remporté avait déjà été ajouté
				boolean ancienB = false;
				int indexB = 0;
				for(Pair<String,InfosModelObjectifRemporte> objectifRemporte : this.objectifsRemportesB) {
					if(this.objectifsDisponibles.get(i).getNom().equals(objectifRemporte.getFirst())) {
						ancienB = true;
					} else if(!ancienB) {
						indexB++;
					}
				}
				
				// Ajouter/Modifier/Supprimer l'objectif remporté
				if(ancienB && valeurB == 0) { // Supprimer
					this.objectifsRemportesB.remove(indexB);
				} else if(ancienB) { // Modifier
					this.objectifsRemportesB.update(indexB, new Pair<String, InfosModelObjectifRemporte>(this.objectifsDisponibles.get(i).getNom(), new InfosModelObjectifRemporte(valeurB)));
				} else if(valeurB != 0) { // Ajouter
					this.objectifsRemportesB.add(new Pair<String, InfosModelObjectifRemporte>(this.objectifsDisponibles.get(i).getNom(), new InfosModelObjectifRemporte(valeurB)));
				}
			}
		}
		
		// Mettre à jour les points remportés par les participants
		this.pointsA = 0;
		this.pointsB = 0;
		for(int i=0;i<this.objectifsDisponibles.size();i++) {
			int nbObjectifsA = (Integer)this.js_objectifsA[i].getValue();
			for (int j=0;j<nbObjectifsA;j++) {
				this.pointsA = this.incrementerPointsObjectif(this.objectifsDisponibles.get(i), this.pointsA);
			}
			int nbObjectifsB = (Integer)this.js_objectifsB[i].getValue();
			for (int j=0;j<nbObjectifsB;j++) {
				this.pointsB = this.incrementerPointsObjectif(this.objectifsDisponibles.get(i), this.pointsB);
			}
		}
		
		// Prévenir les listeners
		ChangeEvent eventEnvoye = new ChangeEvent(this);
		for (ChangeListener listener : this.listeners) {
			listener.stateChanged(eventEnvoye);
		}
	}
	
	/**
	 * Incrémenter le nombre de points pour un objectif
	 * @param objectif objectif
	 * @param points nombre de points
	 * @return nombre de points incrémenté
	 */
	private double incrementerPointsObjectif(InfosModelObjectif objectif, double points) {
		if(objectif instanceof InfosModelObjectifPoints) {
			InfosModelObjectifPoints objectifPoints = (InfosModelObjectifPoints)objectif;
			points += objectifPoints.getPoints();
			if(objectifPoints.getBorneParticipation() != null && (points < 0 && points < objectifPoints.getBorneParticipation() || points > 0 && points > objectifPoints.getBorneParticipation())) {
				points = objectifPoints.getBorneParticipation();
			}
		} else if(objectif instanceof InfosModelObjectifPourcentage) {
			InfosModelObjectifPourcentage objectifPourcentage = (InfosModelObjectifPourcentage)objectif;
			double augmentation = points * objectifPourcentage.getPourcentage() / 100;
			if(objectifPourcentage.getBorneAugmentation() != null && (augmentation < 0 && augmentation < objectifPourcentage.getBorneAugmentation() || augmentation > 0 && augmentation > objectifPourcentage.getBorneAugmentation())) {
				augmentation = objectifPourcentage.getBorneAugmentation();
			}
			points += augmentation;
			if(objectifPourcentage.getBorneParticipation() != null && (points < 0 && points < objectifPourcentage.getBorneParticipation() || points > 0 && points > objectifPourcentage.getBorneParticipation())) {
				points = objectifPourcentage.getBorneParticipation();
			}
		}
		return points;
	}
}
