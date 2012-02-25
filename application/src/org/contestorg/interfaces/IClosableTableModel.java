package org.contestorg.interfaces;

import javax.swing.table.TableModel;

/**
 * Interface à implémenter si une classe souhaite être considérée comme un modèle de données pour tableau que l'on peut fermer
 */
public interface IClosableTableModel extends TableModel
{
	/**
	 * Fermer le modèle de données
	 */
	public void close();
}
