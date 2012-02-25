package org.contestorg.views;

import java.awt.Window;

import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosConnexionServeur;

/**
 * Boîte de dialogue de création de connexion
 */
@SuppressWarnings("serial")
public class JDConnexionCreer extends JDConnexionAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JDConnexionCreer(Window w_parent) {
		// Appeler le constructeur du parent
		super(w_parent, "Connexion au serveur");

		// Valeurs par défaut
		InfosConnexionServeur defaut = InfosConnexionServeur.defaut();
		this.jtf_host.setText(defaut.getHost());
		this.jtf_port.setText(String.valueOf(defaut.getPort()));
		this.jtf_login.setText(defaut.getUsername());
		this.jtf_password.setText(defaut.getPassword());
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	public void ok () {
		// Vérifier la validité des données
		if (this.check()) {
			// Informations de connexion
			InfosConnexionServeur infos_connexion = new InfosConnexionServeur(this.jtf_host.getText(), Integer.parseInt(this.jtf_port.getText()), this.jtf_login.getText(), new String(this.jtf_password.getPassword()));

			// Demander la connexion au serveur
			ContestOrg.get().procedureServeurConnexion(infos_connexion);

			// Quitter la fenêtre
			this.quit();
		}
	}

	/**
	 * @see JDPattern#quit()
	 */
	@Override
	public void quit () {
		// Demander la fermeture de la fenêtre de connexion au serveur
		ContestOrg.get().procedureServeurConnexionAnnuler();
	}

}
