package views;

import infos.InfosModelDiffusion;
import infos.InfosModelTheme;
import interfaces.IEndListener;
import interfaces.IOperation;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.Pair;

import controlers.ContestOrg;
import controlers.CtrlOut;

@SuppressWarnings("serial")
public class JDDiffusions extends JDPattern implements IEndListener
{
	
	// Diffusions
	private ArrayList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions;
	
	// Status et boutons
	private ArrayList<JLabel> jls_statuts = new ArrayList<JLabel>();
	private ArrayList<JButton> jbs_controle = new ArrayList<JButton>();
	private ArrayList<JButton> jbs_afficher = new ArrayList<JButton>();
	
	// Constructeur
	public JDDiffusions(Window w_parent, ArrayList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions) {
		// Appeller le constructeur du parent
		super(w_parent, "Diffuser");
		
		// Masquer le bouton annuler
		this.jb_annuler.setVisible(false);
		
		// Titre
		this.jp_contenu.add(ViewHelper.title("Diffusion des informations du concours",ViewHelper.H1));
		
		// Retenir les diffusions
		this.diffusions = diffusions;
		
		// Vérifier s'il existe des diffusions
		if(diffusions.size() > 0) {
			// Créer la contrainte pour le statut
			GridBagConstraints contrainte_statuts = new GridBagConstraints();
			contrainte_statuts.gridx = 0;
			contrainte_statuts.weightx = 0;
			contrainte_statuts.insets = new Insets(0, 0, 5, 0);

			// Créer la contrainte pour le label
			GridBagConstraints contrainte_labels = new GridBagConstraints();
			contrainte_labels.gridx = 1;
			contrainte_labels.weightx = 1;
			contrainte_labels.insets = new Insets(0, 0, 5, 0);
			contrainte_labels.fill = GridBagConstraints.HORIZONTAL;

			// Créer la contrainte pour le bouton de controle
			GridBagConstraints contrainte_controle = new GridBagConstraints();
			contrainte_controle.gridx = 2;
			contrainte_controle.weightx = 0;
			contrainte_controle.insets = new Insets(0, 0, 5, 0);

			// Créer la contrainte pour le bouton afficher
			GridBagConstraints contrainte_afficher = new GridBagConstraints();
			contrainte_afficher.gridx = 3;
			contrainte_afficher.weightx = 0;
			contrainte_afficher.insets = new Insets(0, 5, 5, 0);
			
			// Récupérer le controleur
			CtrlOut ctrl = ContestOrg.get().getCtrlOut();
			
			// Ajouter la liste des diffusions
			JPanel panel = new JPanel(new GridBagLayout());
			for(int i=0;i<diffusions.size();i++) {
				// Savoir si la diffusion est demarrée
				boolean demarree = ctrl.isDiffusionDemarree(this.diffusions.get(i).getFirst().getPort());
				
				// Ajouter le statut
				this.jls_statuts.add(new JLabel(new ImageIcon(demarree ? "img/farm/32x32/bullet_green.png" : "img/farm/32x32/bullet_red.png")));
				panel.add(this.jls_statuts.get(i),contrainte_statuts);
				
				// Ajouter le label
				panel.add(new JLabel(this.diffusions.get(i).getFirst().getNom()+" (écoutant sur le port "+this.diffusions.get(i).getFirst().getPort()+")    "),contrainte_labels);
				
				// Ajouter le bouton de controle
				this.jbs_controle.add(new JButton(demarree ? "Stopper" : "Démarrer",new ImageIcon(demarree ? "img/farm/16x16/control_stop_blue.png" : "img/farm/16x16/control_play_blue.png")));
				panel.add(this.jbs_controle.get(i),contrainte_controle);
				
				// Ajouter le bouton afficher
				this.jbs_afficher.add(new JButton("Afficher",new ImageIcon("img/farm/16x16/world.png")));
				panel.add(this.jbs_afficher.get(i),contrainte_afficher);
				
				// Désactiver ou non le bouton afficher
				this.jbs_afficher.get(i).setEnabled(demarree);
				
				// Ecouter les boutons
				this.jbs_controle.get(i).addActionListener(this);
				this.jbs_afficher.get(i).addActionListener(this);
			}
			this.jp_contenu.add(panel);
		} else {
			// Pas encore de diffusions
			this.jp_contenu.add(Box.createVerticalStrut(5));
			this.jp_contenu.add(ViewHelper.pwarning("Il n'y a pas encore de diffusions disponibles."));
		}
		
		// Information
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.pinformation("Rendez-vous dans \"Configurer > Exportations et diffusions\" pour éditer la liste des diffusions."));
		
		// Pack
		this.pack();
	}
	
	// Implémentation de ActionListener
	public void actionPerformed (ActionEvent event) {
		if(this.jbs_controle.contains(event.getSource())) {
			// Savoir s'il s'agit d'un démarrage ou d'un arret
			boolean demarrer = this.jbs_afficher.get(this.jbs_controle.indexOf(event.getSource())).isEnabled();
			
			// Récupérer le port
			int port = this.diffusions.get(this.jbs_controle.indexOf(event.getSource())).getFirst().getPort();
			
			// Récupérer l'opération
			IOperation operation = demarrer ? ContestOrg.get().getCtrlOut().getArreterDiffusionOperation(port) : ContestOrg.get().getCtrlOut().getDemarrerDiffusionOperation(port);
			
			// Créer la fenetre associée à l'opération
			JDOperation jd_operation = new JDOperation(this, demarrer ? "Arreter une diffusion" : "Demarrer une diffusion",operation,true,true);
			
			jd_operation.addListener(this);	// Ecouter la fin de l'opération
			operation.operationStart();		// Démarrer l'opération
			jd_operation.setVisible(true);	// Afficher la fenetre
		} else if(this.jbs_afficher.contains(event.getSource())) {
			// Ouvrir le navigateur sur la diffusion
			try {
				Desktop.getDesktop().browse(new URI("http://"+InetAddress.getLocalHost().getHostAddress()+":"+this.diffusions.get(this.jbs_afficher.indexOf(event.getSource())).getFirst().getPort()+"/"));
			} catch (IOException e) {
				ViewHelper.derror(this, "Erreur lors de l'affichage de la diffusion.");
			} catch (URISyntaxException e) {
				ViewHelper.derror(this, "Erreur lors de l'affichage de la diffusion.");
			}
		} else {
			// Appeller le actionPerformed du parent
			super.actionPerformed(event);
		}
	}

	// Implémentation de ok
	@Override
	protected void ok () {
		// Masquer la fenetre
		this.setVisible(false);
	}
	
	// Implémentation de quit
	@Override
	protected void quit () {
		// Masquer la fenetre
		this.setVisible(false);
	}

	// Fin du demarrage/arret de la diffusion
	@Override
	public void end () {
		// Récupérer le controleur
		CtrlOut ctrl = ContestOrg.get().getCtrlOut();
		
		// Récupérer les diffusions
		ArrayList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions = ctrl.getDiffusions();
		
		// Modifier le statut et les boutons de controle et d'affichage
		for(int i=0;i<diffusions.size();i++) {
			// Savoir si la diffusion est demarrée
			boolean demarree = ctrl.isDiffusionDemarree(diffusions.get(i).getFirst().getPort());
			
			// Changer le statut
			this.jls_statuts.get(i).setIcon(new ImageIcon(demarree ? "img/farm/32x32/bullet_green.png" : "img/farm/32x32/bullet_red.png"));
			
			// Désactiver ou non le bouton afficher
			this.jbs_afficher.get(i).setEnabled(demarree);
			
			// Modifier le bouton de controle
			this.jbs_controle.get(i).setText(demarree ? "Stopper" : "Démarrer");
			this.jbs_controle.get(i).setIcon(new ImageIcon(demarree ? "img/farm/16x16/control_stop_blue.png" : "img/farm/16x16/control_play_blue.png"));
		}
	}
	
}
