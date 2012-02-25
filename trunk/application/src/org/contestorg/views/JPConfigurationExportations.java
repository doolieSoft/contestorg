package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelTheme;

/**
 * Panel de configuration des exportations et diffusions
 */
@SuppressWarnings("serial")
public class JPConfigurationExportations extends JPConfigurationAbstract implements TableModelListener
{
	/** TableModel de la liste des exportations */
	private TMExportations tm_exportations;

	/** TableModel de la liste des diffusions */
	private TMDiffusions tm_diffusions;

	/** Exportation qui fait office de publication */
	private JComboBox<String> jcb_publication;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JPConfigurationExportations(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// Liste des exportations
		this.jp_contenu.add(ViewHelper.title("Liste des exportations", ViewHelper.H1));
		this.tm_exportations = new TMExportations(this.w_parent);
		this.tm_exportations.addValidator(ContestOrg.get().getExportationsValidator());
		this.jp_contenu.add(new JPTable<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>>(this.w_parent, this.tm_exportations));

		// Exportation par défaut pour la publication
		this.jp_contenu.add(ViewHelper.title("Exportation pour la publication", ViewHelper.H1));
		this.jcb_publication = new JComboBox<String>();
		this.jcb_publication.addItem("Pas de publication");
		this.jp_contenu.add(this.jcb_publication);
		this.jp_contenu.add(Box.createVerticalStrut(5));

		// Liste des diffusions
		this.jp_contenu.add(ViewHelper.title("Liste des diffusions", ViewHelper.H1));
		this.tm_diffusions = new TMDiffusions(this.w_parent);
		this.tm_diffusions.addValidator(ContestOrg.get().getDiffusionsValidator());
		this.jp_contenu.add(new JPTable<Pair<InfosModelDiffusion,InfosModelTheme>>(this.w_parent, this.tm_diffusions));

		// Ecouter les TableModel
		this.tm_exportations.addTableModelListener(this);
	}

	/**
	 * Récupérer la liste des exportations
	 * @return liste des exportations
	 */
	public TrackableList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> getExportations () {
		return new TrackableList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>>(this.tm_exportations);
	}
	
	/**
	 * Récupérer la liste des diffusions
	 * @return liste des diffusions
	 */
	public TrackableList<Pair<InfosModelDiffusion,InfosModelTheme>> getDiffusions() {
		return new TrackableList<Pair<InfosModelDiffusion,InfosModelTheme>>(this.tm_diffusions);
	}
	
	/**
	 * Récupérer l'indice de l'exportation qui fait office de publication
	 * @return indice de l'exportation qui fait office de publication
	 */
	public int getPublication () {
		return this.jcb_publication.getSelectedIndex() - 1;
	}
	
	/**
	 * Définir la liste des exportations
	 * @param exportations liste des exportations
	 */
	public void setExportations(ArrayList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> exportations) {
		this.tm_exportations.fill(exportations);
	}
	
	/**
	 * Définir la liste des diffusions
	 * @param diffusions liste des diffusions
	 */
	public void setDiffusions(ArrayList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions) {
		this.tm_diffusions.fill(diffusions);
	}

	/**
	 * Définir l'indice de l'exportation qui fait office de publication
	 * @param index indice de l'exportation qui fait office de publication
	 */
	public void setPublication (Integer index) {
		this.jcb_publication.setSelectedIndex(index == null ? 0 : index+1);
	}

	/**
	 * @see JPConfigurationAbstract#check()
	 */
	@Override
	public boolean check () {
		return true;
	}

	/**
	 * @see TableModelListener#tableChanged(TableModelEvent)
	 */
	@Override
	public void tableChanged (TableModelEvent event) {
		// Récupérer l'index de l'item qui a changé
		int changedIndex = event.getFirstRow();

		// Mettre à jour à liste
		switch (event.getType()) {
			case TableModelEvent.INSERT:
				// Ajouter l'élément dans la liste
				this.jcb_publication.addItem(this.tm_exportations.get(changedIndex).getFirst().getNom());
				break;
			case TableModelEvent.UPDATE:
				// Retirer et ajouter l'élément dans la liste
				int selectedIndex = this.jcb_publication.getSelectedIndex();
				this.jcb_publication.removeItemAt(changedIndex + 1);
				this.jcb_publication.insertItemAt(this.tm_exportations.get(changedIndex).getFirst().getNom(), changedIndex + 1);
				this.jcb_publication.setSelectedIndex(selectedIndex);
				break;
			case TableModelEvent.DELETE:
				// Retirer l'élément dans la liste
				this.jcb_publication.removeItemAt(changedIndex + 1);
				break;
		}
	}
}
