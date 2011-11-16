package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public class JDHoraireEditer extends JDHoraireAbstract
{

	// Constructeur
	public JDHoraireEditer(Window w_parent, ICollector<InfosModelHoraire> collector, InfosModelHoraire infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un horaire", collector);
		
		// Remplir les entrées avec les données de l'horaire
		this.jtf_heure_debut.setText(InfosModelHoraire.getHeureHumain(infos.getDebut()));
		this.jtf_heure_fin.setText(InfosModelHoraire.getHeureHumain(infos.getFin()));
		this.jl_jours.setSelectedIndices(InfosModelHoraire.getJoursIndex(infos.getJours()));
	}

}
