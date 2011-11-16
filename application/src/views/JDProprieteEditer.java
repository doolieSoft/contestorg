package views;

import infos.InfosModelPropriete;
import interfaces.ICollector;

import java.awt.Window;

@SuppressWarnings("serial")
public class JDProprieteEditer extends JDProprieteAbstract
{

	// Constructeur
	public JDProprieteEditer(Window w_parent, ICollector<InfosModelPropriete> collector, InfosModelPropriete infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer une propriété", collector);
		
		// TODO Remplir les entrées avec les informations de la propriétés
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
