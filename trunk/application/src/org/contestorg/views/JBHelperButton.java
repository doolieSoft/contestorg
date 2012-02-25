package org.contestorg.views;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Bouton d'aide
 */
@SuppressWarnings("serial")
public abstract class JBHelperButton extends JButton implements ActionListener
{
	/** Fenêtre parent */
	private Window w_parent;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JBHelperButton(Window w_parent) {
		// Appeller le constructeur du parent
		super("Aide", new ImageIcon("img/farm/16x16/help.png"));

		// S'abonner à ses propres évenements
		this.addActionListener(this);

		// Retenir le parent
		this.w_parent = w_parent;
	}

	/**
	 * Rcupérer le message d'aide
	 * @return message d'aide
	 */
	protected abstract String getMessage ();

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed (ActionEvent event) {
		ViewHelper.dinformation(this.w_parent, this.getMessage());
	}
}
