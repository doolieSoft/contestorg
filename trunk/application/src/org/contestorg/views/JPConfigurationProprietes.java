package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelPropriete;

/**
 * Panel de configuration des propriétés de participant
 */
@SuppressWarnings("serial")
public class JPConfigurationProprietes extends JPConfigurationAbstract
{
	/** TableModel des propriétés de participant */
	private TMProprietes tm_proprietes;
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JPConfigurationProprietes(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// Liste des propriétés
		this.jp_contenu.add(ViewHelper.title("Liste des propriétés des participants", ViewHelper.H1));
		this.tm_proprietes = new TMProprietes(this.w_parent);
		this.tm_proprietes.addValidator(ContestOrg.get().getProprietesValidator());
		this.jp_contenu.add(new JPTable<InfosModelPropriete>(this.w_parent, this.tm_proprietes));
	}
	
	/**
	 * Récupérer la liste des propriétés de participant
	 * @return liste des propriétés de participant
	 */
	public TrackableList<InfosModelPropriete> getProprietes() {
		return new TrackableList<InfosModelPropriete>(this.tm_proprietes);
	}
	
	/**
	 * Définir la liste des propriétés de participant
	 * @param proprietes liste des propriétés de participant
	 */
	public void setProprietes(ArrayList<InfosModelPropriete> proprietes) {
		this.tm_proprietes.fill(proprietes);
	}

	/**
	 * @see JPConfigurationAbstract#check()
	 */
	public boolean check () {
		return true;
	}
}
