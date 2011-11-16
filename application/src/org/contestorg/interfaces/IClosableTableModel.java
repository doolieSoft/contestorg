package org.contestorg.interfaces;

import javax.swing.table.TableModel;

public interface IClosableTableModel extends TableModel
{
	// Fermer le modèle
	public void close();
}
