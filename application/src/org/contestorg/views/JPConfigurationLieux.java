package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;

/**
 * Panel de configuration des lieux
 */
@SuppressWarnings("serial")
public class JPConfigurationLieux extends JPConfigurationAbstract
{

	/** TableModel de la liste des lieux */
	private TMLieux tm_lieux;
	
	/**
	 * Constructeur JPConcoursLieux
	 * @param w_parent fenêtre parent
	 */
	public JPConfigurationLieux(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// Liste des lieux
		this.jp_contenu.add(ViewHelper.title("Liste des lieux", ViewHelper.H1));
		this.tm_lieux = new TMLieux(this.w_parent);
		this.tm_lieux.addValidator(ContestOrg.get().getLieuxValidator());
		this.jp_contenu.add(new JPTable<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>>(this.w_parent, this.tm_lieux, true, true, true, true, true));
	}
	
	/**
	 * Récupérer la liste des lieux
	 * @return liste des lieux
	 */
	public TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> getLieux() {
		return new TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>>(this.tm_lieux);
	}
	
	/**
	 * Définir la liste des lieux
	 * @param lieux liste des lieux
	 */
	public void setLieux(ArrayList<Triple<InfosModelLieu,ArrayList<InfosModelEmplacement>,ArrayList<InfosModelHoraire>>> lieux) {
		this.tm_lieux.fill(this.getArrayListsToLists(lieux));
	}

	/**
	 * Transformer les ArrayList<InfosModelEmplacement>/ArrayList<InfosModelHoraire> en List<InfosModelEmplacement>/List<InfosModelHoraire>
	 * @param arraylists arraylists
	 * @return lists
	 */
	private ArrayList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> getArrayListsToLists(ArrayList<Triple<InfosModelLieu,ArrayList<InfosModelEmplacement>,ArrayList<InfosModelHoraire>>> arraylists) {
		// Initialiser la List
		ArrayList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> lieux = new ArrayList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>>();
		
		// Modifier les ArrayLists en Lists
		if(arraylists != null) {
			// Pour chaque ArrayList
			for(Triple<InfosModelLieu,ArrayList<InfosModelEmplacement>,ArrayList<InfosModelHoraire>> lieu : arraylists) {
				// Modifier l'ArrayList des emplacements en List
				TrackableList<InfosModelEmplacement> emplacements = new TrackableList<InfosModelEmplacement>(lieu.getSecond());

				// Modifier l'ArrayList des horaires en List
				TrackableList<InfosModelHoraire> horaires = new TrackableList<InfosModelHoraire>(lieu.getThird());
				
				// Ajouter le lieu dans la List
				lieux.add(new Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>(lieu.getFirst(), emplacements, horaires));
			}
		}
		
		// Retourner la List
		return lieux;
	}

	/**
	 * @see JPConfigurationAbstract#check()
	 */
	public boolean check () {
		return true;
	}
}
