package views;

import infos.InfosModelEquipe;

import java.text.DecimalFormat;

import javax.swing.table.TableModel;
import javax.swing.table.TableStringConverter;

public class TableStringConverterEquipes extends TableStringConverter
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
			case 2: // Stand
				return (String)object;
			case 3: // Nom
				return (String)object;
			case 4: // Rang
				return new DecimalFormat("00").format((Integer)object);
			case 5: // Points
				return new DecimalFormat("0000.00").format((Double)object);
			case 6: // Victoires
				return new DecimalFormat("00").format((Integer)object);
			case 7: // Ville
				return (String)object;
			case 8: // Statut
				return String.valueOf(((InfosModelEquipe.Statut)object).ordinal());
		}
		return null;
	}
	
}
