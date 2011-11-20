﻿package org.contestorg.views;


import java.awt.Window;

import javax.swing.JCheckBox;

import org.contestorg.common.Triple;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.Couple;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;


@SuppressWarnings("serial")
public class JDPhaseQualifEditer extends JDPhaseQualifAbstract
{

	// Constructeur
	public JDPhaseQualifEditer(Window w_parent, ICollector<Triple<Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer une phase qualificative", collector, nomCategorie, nomPoule);
		
		// Déséléctionner les participants qui ne peuvent pas participer
		for(JCheckBox checkbox : this.jcbs_participants) {
			checkbox.setSelected(false);
		}
		for(Couple<String> couple : infos.getFirst().getCouples()) {
			if(couple.getParticipantA() != null) {
				this.jcbs_participants[this.participants.indexOf(couple.getParticipantA())].setSelected(true);
			}
			if(couple.getParticipantB() != null) {
				this.jcbs_participants[this.participants.indexOf(couple.getParticipantB())].setSelected(true);
			}
		}
		
		// Remplir les entrées avec les informations de la phase qualificative
		this.setConfiguration(infos.getFirst());
	}
	
}