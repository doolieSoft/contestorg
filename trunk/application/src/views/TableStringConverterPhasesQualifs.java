package views;

import infos.InfosModelParticipation;

import java.text.DecimalFormat;

import javax.swing.table.TableModel;
import javax.swing.table.TableStringConverter;

public class TableStringConverterPhasesQualifs extends TableStringConverter
{
	// Implémentation de toString
	@Override
	public String toString (TableModel model, int row, int column) {		
		// Récupérer l'objet de la cellule
		Object object = model.getValueAt(row, column);
		
		// Transformer la objet en string
		switch(column + 9 - model.getColumnCount()) {
			case 0: // Catégorie
				return (String)object;
			case 1: // Poule
				return (String)object;
			case 2: // Phase
				return String.valueOf((Integer)object);
			case 3: // Equipe A
				return object == null ? null : (String)object;
			case 4: // Résultat A
				switch((Integer)object) {
					case InfosModelParticipation.RESULTAT_VICTOIRE:
						return "0";
					case InfosModelParticipation.RESULTAT_EGALITE:
						return "1";
					case InfosModelParticipation.RESULTAT_DEFAITE:
						return "2";
					case InfosModelParticipation.RESULTAT_FORFAIT:
						return "3";
					case InfosModelParticipation.RESULTAT_ATTENTE:
						return "4";
				}
			case 5: // Points A
				return new DecimalFormat("0000.00").format((Double)object);
			case 6: // Equipe B
				return object == null ? null : (String)object;
			case 7: // Résultat B
				switch((Integer)object) {
					case InfosModelParticipation.RESULTAT_VICTOIRE:
						return "0";
					case InfosModelParticipation.RESULTAT_EGALITE:
						return "1";
					case InfosModelParticipation.RESULTAT_DEFAITE:
						return "2";
					case InfosModelParticipation.RESULTAT_FORFAIT:
						return "3";
					case InfosModelParticipation.RESULTAT_ATTENTE:
						return "4";
				}
			case 8: // Points B
				return new DecimalFormat("0000.00").format((Double)object);
		}
		return null;
	}
}
