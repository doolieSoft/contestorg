package org.contestorg.views;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.text.SimpleDateFormat;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosMiseAJour;
import org.contestorg.infos.InfosMiseAJour.Modification;
import org.contestorg.infos.InfosMiseAJour.Telechargement;
import org.contestorg.preferences.Preferences;

/**
 * Boîte de dialogue de mise à jour
 */
@SuppressWarnings("serial")
public class JDMiseAJour extends JDPattern implements MouseListener
{
	/** Informations sur la mise à jour */
	private InfosMiseAJour infos;
	
	// Tableaux de modifications
	
	/** Tableau des évolutions développées */
	private JTable jt_evolutions;
	
	/** Tableau des anomalies corrigées */
	private JTable jt_anomalies;
	
	/** Tableau des revues effectuées */
	private JTable jt_revues;
	
	/** Tableau des tâches effectuées */
	private JTable jt_taches;
	
	// Entrées
	
	/** Choix du téléchargement */
	private JComboBox jcb_telechargement = new JComboBox();
	
	/** Vérifier les mises à jour */
	private JCheckBox jcb_verifier = new JCheckBox("Vérifier les mises à jour ?",true);
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param infos informations sur la mise à jour
	 */
	public JDMiseAJour(Window w_parent, InfosMiseAJour infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Mise à jour disponible");
		
		// Retenir les informations sur la mise à jour
		this.infos = infos;
		
		// Informations
		this.jp_contenu.add(ViewHelper.title("Informations"));
		JTextField jtf_numero = new JTextField(infos.getNumero());
		JTextField jtf_publication = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(infos.getDate().getTime()));
		JLabel[] jls = { new JLabel("Numéro de version : "), new JLabel("Date de publication : "), new JLabel("Choix du téléchargement : ") };
		JComponent[] jcs = { jtf_numero, jtf_publication, this.jcb_telechargement };
		this.jp_contenu.add(ViewHelper.inputs(jls, jcs));
		
		// Ajouter la liste des téléchargement disponibles
		for(Telechargement telechargement : infos.getTelechargements()) {
			this.jcb_telechargement.addItem(telechargement.getNom());
		}
		
		// Vérifier s'il y a des modifications 
		if(infos.getEvolutions().size() != 0 || infos.getAnomalies().size() != 0 || infos.getRevues().size() != 0 || infos.getTaches().size() != 0) {
			// Modifications
			this.jp_contenu.add(ViewHelper.title("Modifications"));

			// Informations supplémentaires
			this.jp_contenu.add(ViewHelper.pinformation("Effectuez un double-clic sur les modifications pour plus d'informations."));
			
			// Liste des évolutions développées
			if(infos.getEvolutions().size() != 0) {
				this.jp_contenu.add(ViewHelper.title("Evolutions développées",ViewHelper.H2));
				TMString tm_evolutions = new TMString("Description");
				for(Modification evolution : infos.getEvolutions()) {
					tm_evolutions.add(evolution.getDescription());
				}
				this.jt_evolutions = new JTable(tm_evolutions);
				this.jt_evolutions.addMouseListener(this);
				JScrollPane jsp_evolutions = new JScrollPane(this.jt_evolutions);
				jsp_evolutions.setPreferredSize(new Dimension(jsp_evolutions.getPreferredSize().width, 85));
				this.jp_contenu.add(jsp_evolutions);
			}
			
			// Liste des anomalies corrigées
			if(infos.getAnomalies().size() != 0) {
				this.jp_contenu.add(ViewHelper.title("Anomalies corrigées",ViewHelper.H2));
				TMString tm_anomalies = new TMString("Description");
				for(Modification anomalie : infos.getAnomalies()) {
					tm_anomalies.add(anomalie.getDescription());
				}
				this.jt_anomalies = new JTable(tm_anomalies);
				JScrollPane jsp_anomalies = new JScrollPane(this.jt_anomalies);
				this.jt_anomalies.addMouseListener(this);
				jsp_anomalies.setPreferredSize(new Dimension(jsp_anomalies.getPreferredSize().width, 85));
				this.jp_contenu.add(jsp_anomalies);
			}
			
			// Liste des revues effectuées
			if(infos.getRevues().size() != 0) {
				this.jp_contenu.add(ViewHelper.title("Revues effectuées",ViewHelper.H2));
				TMString tm_revues = new TMString("Description");
				for(Modification revue : infos.getRevues()) {
					tm_revues.add(revue.getDescription());
				}
				this.jt_revues = new JTable(tm_revues);
				JScrollPane jsp_revues = new JScrollPane(this.jt_revues);
				this.jt_revues.addMouseListener(this);
				jsp_revues.setPreferredSize(new Dimension(jsp_revues.getPreferredSize().width, 85));
				this.jp_contenu.add(jsp_revues);
			}
			
			// Liste des tâches effectuées
			if(infos.getTaches().size() != 0) {
				this.jp_contenu.add(ViewHelper.title("Tâches effectuées",ViewHelper.H2));
				TMString tm_taches = new TMString("Description");
				for(Modification tache : infos.getTaches()) {
					tm_taches.add(tache.getDescription());
				}
				this.jt_taches = new JTable(tm_taches);
				JScrollPane jsp_taches = new JScrollPane(this.jt_taches);
				this.jt_taches.addMouseListener(this);
				jsp_taches.setPreferredSize(new Dimension(jsp_taches.getPreferredSize().width, 85));
				this.jp_contenu.add(jsp_taches);
			}
		}
		
		// Demander à l'utilisateur s'il souhaite vérifier les mises à jour
		this.jp_contenu.add(ViewHelper.left(this.jcb_verifier));
		this.jcb_verifier.addActionListener(this);
		
		// Modifier le nom du bouton "valider"
		this.jb_valider.setText("Télécharger");
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Lancer le téléchargement
		if(this.infos.getTelechargements().size() != 0) {
			try {
				Desktop.getDesktop().browse(new URI(this.infos.getTelechargements().get(this.jcb_telechargement.getSelectedIndex()).getURL()));
			} catch (Exception e) {
				ViewHelper.derror(this, "Erreur lors de l'ouverture du ticket associé à la modification.");
			}
		}
		
		// Masquer la fenêtre
		this.setVisible(false);
	}
	
	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Masquer la fenêtre
		this.setVisible(false);
	}
	
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed (ActionEvent event) {
		if(event.getSource() == this.jcb_verifier) {
			// Modifier les préférences
			ContestOrg.get().getPreferences().setAndSave(Preferences.VERIFIER_MISES_A_JOUR, this.jcb_verifier.isSelected());
		} else {
			// Appeller le actionPerformed du parent
			super.actionPerformed(event);
		}
	}
	
	// Implémentation de MouseListener

	/**
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	@Override
	public void mouseClicked (MouseEvent event) {
		// Vérifier s'il s'agit d'un double-clic
		if(event.getClickCount() == 2) {
			// Déclarer le ticket
			String ticket = null;
			
			// Vérifier d'où vient le double-clic
			if(event.getSource() == this.jt_evolutions) {
				// Récupérer le ticket associé à l'évolution
				ticket = this.infos.getEvolutions().get(this.jt_evolutions.getSelectedRow()).getTicket();
			} else if(event.getSource() == this.jt_anomalies) {
				// Récupérer le ticket associé à l'anomalie
				ticket = this.infos.getAnomalies().get(this.jt_anomalies.getSelectedRow()).getTicket();
			} else if(event.getSource() == this.jt_revues) {
				// Récupérer le ticket associé à la revue
				ticket = this.infos.getRevues().get(this.jt_revues.getSelectedRow()).getTicket();
			} else if(event.getSource() == this.jt_taches) {
				// Récupérer le ticket associé à la tâche
				ticket = this.infos.getTaches().get(this.jt_taches.getSelectedRow()).getTicket();
			}
			
			// Vérifier s'il y a un ticket associé à la modification
			if(ticket != null && !ticket.isEmpty()) {
				try {
					Desktop.getDesktop().browse(new URI(ticket));
				} catch (Exception e) {
					ViewHelper.derror(this, "Erreur lors de l'ouverture du ticket associé à la modification.");
				}
			}
		}
	}

	/**
	 * @see MouseListener#mouseEntered(MouseEvent)
	 */
	@Override
	public void mouseEntered (MouseEvent event) {
	}

	/**
	 * @see MouseListener#mouseExited(MouseEvent)
	 */
	@Override
	public void mouseExited (MouseEvent event) {
	}

	/**
	 * @see MouseListener#mousePressed(MouseEvent)
	 */
	@Override
	public void mousePressed (MouseEvent event) {
	}

	/**
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */
	@Override
	public void mouseReleased (MouseEvent event) {
	}
	
}
