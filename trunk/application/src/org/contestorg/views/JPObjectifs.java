package org.contestorg.views;


import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelParticipationObjectif;



@SuppressWarnings("serial")
public class JPObjectifs extends JPanel
{	
	// Objectifs disponibles
	private ArrayList<String> objectifsDisponibles;
	
	// Entrées
	private JSpinner[] js_objectifsA;
	private JSpinner[] js_objectifsB;
	private TrackableList<Pair<String,InfosModelParticipationObjectif>> objectifsRemportesA = new TrackableList<Pair<String,InfosModelParticipationObjectif>>();
	private TrackableList<Pair<String,InfosModelParticipationObjectif>> objectifsRemportesB = new TrackableList<Pair<String,InfosModelParticipationObjectif>>();
	
	// Constructeur
	public JPObjectifs() {
		// Définir le layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Remplir la liste des objetifs
		this.objectifsDisponibles = new ArrayList<String>(); 
		for(InfosModelObjectif objectifDisponible : ContestOrg.get().getCtrlPhasesQualificatives().getListeObjectifs()) {
			this.objectifsDisponibles.add(objectifDisponible.getNom());
		}
		if(objectifsDisponibles.size() != 0) {
			this.add(ViewHelper.title("Objectifs remportés", ViewHelper.H1));

			JLabel[] jls_objectifsA = new JLabel[objectifsDisponibles.size()];
			JLabel[] jls_objectifsB = new JLabel[objectifsDisponibles.size()];
			
			JComponent[] jcs_objectifsA = new JComponent[objectifsDisponibles.size()];
			JComponent[] jcs_objectifsB = new JComponent[objectifsDisponibles.size()];

			this.js_objectifsA = new JSpinner[objectifsDisponibles.size()];
			this.js_objectifsB = new JSpinner[objectifsDisponibles.size()];
			
			for(int i=0;i<objectifsDisponibles.size();i++) {
				this.js_objectifsA[i] = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
				this.js_objectifsB[i] = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
				jls_objectifsA[i] = new JLabel(objectifsDisponibles.get(i)+" : ");
				jls_objectifsB[i] = new JLabel(" "+objectifsDisponibles.get(i)+" : ");
				jcs_objectifsA[i] = this.js_objectifsA[i];
				jcs_objectifsB[i] = this.js_objectifsB[i];
			}

			JPanel jp_objectifs = new JPanel(new GridLayout(1,2));
			jp_objectifs.add(ViewHelper.inputs(jls_objectifsA, jcs_objectifsA));
			jp_objectifs.add(ViewHelper.inputs(jls_objectifsB, jcs_objectifsB));
			this.add(jp_objectifs);
		}
	}

	// Collecter les objectifs remportés
	public void collect() {
		// Mettre à jour la liste des objectifs remportés
		ArrayList<String> objectifsDisponibles = new ArrayList<String>(); 
		for(InfosModelObjectif objectifDisponible : ContestOrg.get().getCtrlPhasesQualificatives().getListeObjectifs()) {
			objectifsDisponibles.add(objectifDisponible.getNom());
		}
		for(int i=0;i<objectifsDisponibles.size();i++) {
			// Récupérer les nouveaux nombres d'objectifs remportés
			int valeurA = (Integer)this.js_objectifsA[i].getValue();
			int valeurB = (Integer)this.js_objectifsB[i].getValue();
			
			// Vérifier si l'objectif remporté avait été déjà ajouté
			boolean ancienA = false, ancienB = false;
			int indexA = 0, indexB = 0;
			for(Pair<String,InfosModelParticipationObjectif> objectifRemporte : this.objectifsRemportesA) {
				if(objectifsDisponibles.get(i).equals(objectifRemporte.getFirst())) {
					ancienA = true;
				} else if(!ancienA) {
					indexA++;
				}
			}
			for(Pair<String,InfosModelParticipationObjectif> objectifRemporte : this.objectifsRemportesB) {
				if(objectifsDisponibles.get(i).equals(objectifRemporte.getFirst())) {
					ancienB = true;
				} else if(!ancienB) {
					indexB++;
				}
			}
			
			// Ajouter/Modifier/Supprimer l'objectif remporté
			if(ancienA && valeurA == 0) { // Supprimer
				this.objectifsRemportesA.remove(indexA);
			} else if(ancienA) { // Modifier
				this.objectifsRemportesA.update(indexA, new Pair<String, InfosModelParticipationObjectif>(objectifsDisponibles.get(i), new InfosModelParticipationObjectif(valeurA)));
			} else if(valeurA != 0) { // Ajouter
				this.objectifsRemportesA.add(new Pair<String, InfosModelParticipationObjectif>(objectifsDisponibles.get(i), new InfosModelParticipationObjectif(valeurA)));
			}
			if(ancienB && valeurB == 0) { // Supprimer
				this.objectifsRemportesB.remove(indexB);
			} else if(ancienB) { // Modifier
				this.objectifsRemportesB.update(indexB, new Pair<String, InfosModelParticipationObjectif>(objectifsDisponibles.get(i), new InfosModelParticipationObjectif(valeurB)));
			} else if(valeurB != 0) { // Ajouter
				this.objectifsRemportesB.add(new Pair<String, InfosModelParticipationObjectif>(objectifsDisponibles.get(i), new InfosModelParticipationObjectif(valeurB)));
			}
		}
	}
	
	// Vider les objectifs remportés
	public void clear() {
		// Vider la liste des objectifs remportés
		while(this.objectifsRemportesA.size() != 0) {
			this.objectifsRemportesA.remove(0);
		}
		while(this.objectifsRemportesB.size() != 0) {
			this.objectifsRemportesB.remove(0);
		}
	}
	
	// Getters
	public TrackableList<Pair<String, InfosModelParticipationObjectif>> getObjectifsRemportesA() {
		return this.objectifsRemportesA;
	}
	public TrackableList<Pair<String, InfosModelParticipationObjectif>> getObjectifsRemportesB() {
		return this.objectifsRemportesB;
	}
	
	// Setters
	public void setObjectifsRemportesA(ArrayList<Pair<String, InfosModelParticipationObjectif>> objectifs) {
		this.objectifsRemportesA.fill(objectifs);
		for(Pair<String, InfosModelParticipationObjectif> objectifRemporte : objectifs) {
			this.js_objectifsA[this.objectifsDisponibles.indexOf(objectifRemporte.getFirst())].setValue(objectifRemporte.getSecond().getQuantite());
		}
	}
	public void setObjectifsRemportesB(ArrayList<Pair<String, InfosModelParticipationObjectif>> objectifs) {
		this.objectifsRemportesB.fill(objectifs);
		for(Pair<String, InfosModelParticipationObjectif> objectifRemporte : objectifs) {
			this.js_objectifsB[this.objectifsDisponibles.indexOf(objectifRemporte.getFirst())].setValue(objectifRemporte.getSecond().getQuantite());
		}
	}
}
