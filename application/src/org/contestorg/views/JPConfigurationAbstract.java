package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Panel pour la boîte de dialogue de création/édition de concours
 */
@SuppressWarnings("serial")
public abstract class JPConfigurationAbstract extends JPanel
{
	/** Panel de contenu */
	protected JPanel jp_contenu;

	/** Fenêtre parent */
	protected Window w_parent;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JPConfigurationAbstract(Window w_parent) {
		// Retenir le parent
		this.w_parent = w_parent;

		// Panel de contenu
		this.jp_contenu = new JPanel();
		this.jp_contenu.setLayout(new BoxLayout(this.jp_contenu, BoxLayout.Y_AXIS));

		// Panel en largeur
		JPanel jp_largeur = new JPanel(new BorderLayout());
		jp_largeur.add(this.jp_contenu, BorderLayout.NORTH);

		// Mise en forme du panel
		this.setLayout(new BorderLayout());
		this.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
		this.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		this.add(jp_largeur, BorderLayout.CENTER);
	}

	/**
	 * Vérifier la validité des données
	 * @return données valides ?
	 */
	public abstract boolean check ();
}
