package org.contestorg.views;

import java.awt.Window;

import javax.swing.JCheckBox;

import org.contestorg.common.Quintuple;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosConfiguration;
import org.contestorg.infos.InfosConfigurationCouple;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'éditon d'une phase qualificative
 */
@SuppressWarnings("serial")
public class JDPhaseQualifEditer extends JDPhaseQualifAbstract
{
	/** Ancienne configuration */
	private InfosConfiguration<String> ancienneConfiguration;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de la phase qualificative
	 * @param nomCategorie nom de la catégorie de destination
	 * @param nomPoule nom de la poule de destination
	 * @param infos informations de la phase qualificative
	 */
	public JDPhaseQualifEditer(Window w_parent, ICollector<Quintuple<String, String, InfosConfiguration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, Triple<InfosConfiguration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer une phase qualificative", collector, nomCategorie, nomPoule);
		
		// Déséléctionner les participants qui ne peuvent pas participer
		for(JCheckBox checkbox : this.jcbs_participants) {
			checkbox.setSelected(false);
		}
		for(InfosConfigurationCouple<String> couple : infos.getFirst().getCouples()) {
			if(couple.getParticipantA() != null) {
				this.jcbs_participants[this.participants.indexOf(couple.getParticipantA())].setSelected(true);
			}
			if(couple.getParticipantB() != null) {
				this.jcbs_participants[this.participants.indexOf(couple.getParticipantB())].setSelected(true);
			}
		}
		
		// Remplir les entrées avec les informations de la phase qualificative
		this.setConfiguration(infos.getFirst());
		
		// Retenir l'ancienne configuration
		this.ancienneConfiguration = infos.getFirst();
	}
	
	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Remplir les entrées avec les informations de la phase qualificative
		this.setConfiguration(this.ancienneConfiguration);
				
		// Simuler le clic sur OK
		this.ok();
	}
	
}
