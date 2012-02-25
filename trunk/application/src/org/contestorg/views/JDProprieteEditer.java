package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'édition d'une propriété
 */
@SuppressWarnings("serial")
public class JDProprieteEditer extends JDProprieteAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de la propriété
	 * @param infos informations de la propriété
	 */
	public JDProprieteEditer(Window w_parent, ICollector<InfosModelPropriete> collector, InfosModelPropriete infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer une propriété", collector);
		
		// Remplir les entrées avec les informations de la propriété
		this.jtf_nom.setText(infos.getNom());
		this.jrb_obligatoire_oui.setSelected(infos.isObligatoire());
		this.jrb_obligatoire_non.setSelected(!infos.isObligatoire());
		switch(infos.getType()) {
			case InfosModelPropriete.TYPE_INT: this.jcb_types.setSelectedItem(JDProprieteAbstract.LABEL_TYPE_INT); break;
			case InfosModelPropriete.TYPE_FLOAT: this.jcb_types.setSelectedItem(JDProprieteAbstract.LABEL_TYPE_FLOAT); break;
			case InfosModelPropriete.TYPE_STRING: this.jcb_types.setSelectedItem(JDProprieteAbstract.LABEL_TYPE_STRING); break;
		}
	}

}
