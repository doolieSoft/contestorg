package views;

import infos.Configuration;
import infos.Couple;
import infos.InfosModelMatchPhasesQualifs;
import infos.InfosModelPhaseQualificative;
import interfaces.ICollector;

import java.awt.Window;

import javax.swing.JCheckBox;

import common.Triple;

@SuppressWarnings("serial")
public class JDPhaseQualifEditer extends JDPhaseQualifAbstract
{

	// Constructeur
	public JDPhaseQualifEditer(Window w_parent, ICollector<Triple<Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer une phase qualificative", collector, nomCategorie, nomPoule);
		
		// Déséléctionner les équipes non participantes
		for(JCheckBox checkbox : this.jcbs_equipesParticipantes) {
			checkbox.setSelected(false);
		}
		for(Couple<String> couple : infos.getFirst().getCouples()) {
			if(couple.getEquipeA() != null) {
				this.jcbs_equipesParticipantes[this.equipesParticipantes.indexOf(couple.getEquipeA())].setSelected(true);
			}
			if(couple.getEquipeB() != null) {
				this.jcbs_equipesParticipantes[this.equipesParticipantes.indexOf(couple.getEquipeB())].setSelected(true);
			}
		}
		
		// Remplir les entrées avec les informations de la phase qualificative
		this.setConfiguration(infos.getFirst());
	}
	
}
