package org.contestorg.views;

import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de définition du poids des participants en vue de les affecter à une poule
 */
@SuppressWarnings("serial")
public class JDPoulesPoidsParticipants extends JDPattern
{
	/** Collecteur des poids des participants */
	private ICollector<HashMap<String,Integer>> collector;
	
	/** Noms des participants */
	private ArrayList<String> participants = new ArrayList<String>();
	
	/** Poids des participants */
	private JSpinner[] js_poids;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param participants liste des participants
	 * @param poids poids des participants
	 * @param collector collecteur des poids des participants 
	 */
	public JDPoulesPoidsParticipants(Window w_parent, HashMap<String,Integer> poids, ICollector<HashMap<String,Integer>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Poids des équipes" : "Poids des participants");
		
		// Retenir le collecteur des poids des participants
		this.collector = collector;
		
		// Titre
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Poids des équipes" : "Poids des participants"));
		
		// Récupérer et trier la liste des participants
		this.participants.addAll(poids.keySet());
		Collections.sort(this.participants);
		
		// Construire la liste des participants
		JPanel jp_participants = new JPanel();
		jp_participants.setLayout(new BoxLayout(jp_participants, BoxLayout.Y_AXIS));
		
		JScrollPane jsp_participants = new JScrollPane(jp_participants);
		jsp_participants.setPreferredSize(new Dimension(jsp_participants.getPreferredSize().width, 140));
		this.jp_contenu.add(jsp_participants);
		
		this.js_poids = new JSpinner[this.participants.size()];
		JLabel[] jls_poids = new JLabel[this.participants.size()];
		
		int i = 0;
		for(String participant : this.participants) {
			this.js_poids[i] = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
			this.js_poids[i].setValue(poids.get(participant));
			jls_poids[i] = new JLabel(participant+" : ");
			i++;
		}
		jp_participants.add(ViewHelper.inputs(jls_poids, this.js_poids));
		
		// Information
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.pinformation("Les poids évitent de générer des poules déséquilibrées."));
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Collecter les poids
		HashMap<String,Integer> poids = new HashMap<String,Integer>();
		for(int i=0;i<this.js_poids.length;i++) {
			poids.put(this.participants.get(i),(Integer)this.js_poids[i].getValue());
		}
		
		// Retourner les informations
		this.collector.collect(poids);
	}

	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}
	
}
