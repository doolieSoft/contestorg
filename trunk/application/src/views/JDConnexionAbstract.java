package views;

import java.awt.Window;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
abstract public class JDConnexionAbstract extends JDPattern
{

	// Textfields
	protected JTextField jtf_host = new JTextField();
	protected JTextField jtf_port = new JTextField();
	protected JTextField jtf_login = new JTextField();
	protected JPasswordField jtf_password = new JPasswordField();

	// Constructeur
	public JDConnexionAbstract(Window w_parent, String titre) {
		// Appeler le constructeur du parent
		super(w_parent, titre);

		// En construction
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.pwarning("Le mode serveur n'est pas encore disponible."));
		this.jb_valider.setVisible(false);

		// Informations de connexion
		this.jp_contenu.add(ViewHelper.title("Connexion", ViewHelper.H1));
		JLabel[] jls_hote = {new JLabel("Hote : "),new JLabel("Port : ")};
		JComponent[] jcs_hote = {this.jtf_host,this.jtf_port};
		this.jp_contenu.add(ViewHelper.inputs(jls_hote, jcs_hote));
		
		// Informations d'identification
		this.jp_contenu.add(ViewHelper.title("Identification", ViewHelper.H1));
		JLabel[] jls_connexion = {new JLabel("Nom d'utilisateur : "),new JLabel("Mot de passe : ")};
		JComponent[] jcs_connexion = {this.jtf_login,this.jtf_password};
		this.jp_contenu.add(ViewHelper.inputs(jls_connexion, jcs_connexion));

		// Pack
		this.pack();
	}

	// Vérifier la validité des données
	protected boolean check () {
		if (ViewHelper.empty(this.jtf_host, this.jtf_port, this.jtf_login, this.jtf_password)) {
			// Message d'erreur
			ViewHelper.derror(this, "Toutes les informations n'ont pas été données !");
		} else {
			try {
				// Convertion de données
				Integer.parseInt(this.jtf_port.getText());

				// Les données sont correctes
				return true;
			} catch (NumberFormatException exception) {
				// Message d'erreur
				ViewHelper.derror(this, "Le numéro de port doit être un nombre entier !");
			}
		}

		// Les données ne sont pas correctes
		return false;
	}
}
