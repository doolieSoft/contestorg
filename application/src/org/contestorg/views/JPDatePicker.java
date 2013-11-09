package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Panel de sélection d'une date
 */
@SuppressWarnings("serial")
public class JPDatePicker extends JPanel implements ActionListener, MouseListener, ChangeListener
{

	/** Date séléctionnée */
	private Calendar dateSelectionnee;

	/** Date affichée */
	private Calendar dateAffichee;
	
	/** Label du mois et de l'année */
	private JLabel jl_moisAnnee;
	
	/** Bouton pour passer au mois précédant */
	private JButton jb_moisPrecedant;
	
	/** Bouton pour passer au mois suivant */
	private JButton jb_moisSuivant;
	
	/** Panel du calendrier */
	private JPanel jp_calendrier;
	
	/** Labels des jours du mois */
	private JLabel[] jl_jours;
	
	/** Label du jour séléctionné */
	private JLabel jl_jourSelectionne;
	
	/** Jour séléctionné dans le mois ? */
	private boolean jourSelectionneDansMois = false;
	
	/** Heures */
	private JSpinner js_heures;
	
	/** Minutes */
	private JSpinner js_minutes;
	
	/** Mois de l'année */
	private String[] mois;
	
	/** Jours de la semaine */
	private String[] jours;
	
	/**
	 * Constructeur
	 */
	public JPDatePicker() {
		// Configurer le panel
		this.setLayout(new BorderLayout());
		
		// Récupérer la date courante
		this.dateSelectionnee = Calendar.getInstance();
		this.dateAffichee = (Calendar)this.dateSelectionnee.clone();
		
		// Liste des mois
		this.mois = new String[12];
		this.mois[0] = "Janvier";
		this.mois[1] = "Février";
		this.mois[2] = "Mars";
		this.mois[3] = "Avril";
		this.mois[4] = "Mai";
		this.mois[5] = "Juin";
		this.mois[6] = "Juillet";
		this.mois[7] = "Août";
		this.mois[8] = "Septembre";
		this.mois[9] = "Octobre";
		this.mois[10] = "Novembre";
		this.mois[11] = "Décembre";
		
		// Liste des jours
		this.jours = new String[7];
		this.jours[0] = "Lundi";
		this.jours[1] = "Mardi";
		this.jours[2] = "Mercredi";
		this.jours[3] = "Jeudi";
		this.jours[4] = "Vendredi";
		this.jours[5] = "Samedi";
		this.jours[6] = "Dimanche";
		
		// Ajouter le mois et l'année
		this.jl_moisAnnee = new JLabel();
		this.jl_moisAnnee.setHorizontalAlignment(SwingConstants.CENTER);
		this.jl_moisAnnee.setForeground(Color.WHITE);
		this.jb_moisPrecedant = new JButton("<");
		this.jb_moisPrecedant.setToolTipText("Mois précédant");
		this.jb_moisSuivant = new JButton(">");
		this.jb_moisSuivant.setToolTipText("Mois suivant");
		JPanel jp_moisAnnee = new JPanel(new BorderLayout());
		jp_moisAnnee.setBackground(new Color(55, 133, 207));
		jp_moisAnnee.add(this.jb_moisPrecedant,BorderLayout.WEST);		
		jp_moisAnnee.add(this.jl_moisAnnee,BorderLayout.CENTER);
		jp_moisAnnee.add(this.jb_moisSuivant,BorderLayout.EAST);
		this.add(jp_moisAnnee,BorderLayout.NORTH);
		
		// Ajouter le calendrier
		this.jp_calendrier = new JPanel(new GridLayout(7,7));
		this.add(this.jp_calendrier,BorderLayout.CENTER);
		
		for(int jour=0;jour<7;jour++) {
			JLabel jl_jour = new JLabel(this.jours[jour].substring(0, 3)+".",SwingConstants.CENTER);
			jl_jour.setOpaque(true);
			jl_jour.setBackground(new Color(200,200,200));
			jl_jour.setForeground(Color.BLACK);
			this.jp_calendrier.add(jl_jour);
		}
		
		this.jl_jours = new JLabel[42];
		for(int i=0;i<42;i++) {
			this.jl_jours[i] = new JLabel();
			this.jl_jours[i].setOpaque(true);
			this.jl_jours[i].setHorizontalAlignment(SwingConstants.CENTER);
			this.jl_jours[i].addMouseListener(this);
			this.jp_calendrier.add(this.jl_jours[i]);
		}
		
		// Ajouter l'heure
		this.js_heures = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
		this.js_minutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
		JPanel jp_heure = new JPanel(new GridLayout(1, 4, 2, 0));
		jp_heure.add(new JLabel("Heures :"));
		jp_heure.add(this.js_heures);
		jp_heure.add(new JLabel("Minutes :"));
		jp_heure.add(this.js_minutes);
		this.add(jp_heure,BorderLayout.SOUTH);
		
		// Rafraîchir
		this.rafraichir();
		
		// Ecouter les boutons
		this.jb_moisPrecedant.addActionListener(this);
		this.jb_moisSuivant.addActionListener(this);
		
		// Ecouter les champs
		this.js_heures.addChangeListener(this);
		this.js_minutes.addChangeListener(this);
	}
	
	/**
	 * Définir la date
	 */
	public void setDate(Date date) {
		// Retenir la date
		this.dateSelectionnee.setTime(date);
		this.dateAffichee.setTime(date);
		
		// Rafraichir
		this.rafraichir();
	}
	
	/**
	 * Récupérer la date
	 * @return date 
	 */
	public Date getDate() {
		return this.dateSelectionnee.getTime();
	}
	
	/**
	 * Rafraichir
	 */
	private void rafraichir() {
		// Mettre à jour le mois et l'année
		this.jl_moisAnnee.setText(this.mois[this.dateAffichee.get(Calendar.MONTH)]+" "+this.dateAffichee.get(Calendar.YEAR));
		
		// Informations pour établir le calendrier
		Calendar date = (Calendar)this.dateAffichee.clone();
		date.set(Calendar.DAY_OF_MONTH, 1);
		int premierJourDuMois = date.get(Calendar.DAY_OF_WEEK)-2;
		if(premierJourDuMois == -1) {
			premierJourDuMois = 6;
		}
		date.add(Calendar.MONTH,1);
		date.add(Calendar.DATE,-1);
		int nombreJoursMoisCourant = date.get(Calendar.DAY_OF_MONTH);
		date.set(Calendar.DAY_OF_MONTH, 1);
		date.add(Calendar.DATE,-1);
		int nombreJoursMoisPrecedant = date.get(Calendar.DAY_OF_MONTH);
		date.add(Calendar.DATE,1);
		
		// Jour courant
		int jourCourant = nombreJoursMoisPrecedant-premierJourDuMois;
		
		// Couleurs du jour courant
		Color couleurFondJourCourant = Color.WHITE;
		Color couleurTexteJourCourant = Color.GRAY;
		
		// Réinitialiser le jour séléctionné
		this.jl_jourSelectionne = null;
		
		// Mettre à jour les boutons du calendrier
		int i = 0; boolean avantLeMois = true, apresLeMois = false;
		for(int semaine=0;semaine<6;semaine++) {
			for(int jour=0;jour<7;jour++) {
				// Définir le jour du mois 
				if(semaine == 0 && jour == premierJourDuMois) {
					jourCourant = 1;
					avantLeMois = false;
				} else if(semaine != 0 && jourCourant == nombreJoursMoisCourant) {
					jourCourant = 1;
					apresLeMois = true;
				} else {
					jourCourant++;
				}
				this.jl_jours[i].setText(String.valueOf(jourCourant));
				
				// Définir la couleur du label
				couleurFondJourCourant = Color.WHITE;
				couleurTexteJourCourant = Color.BLACK;
				if(avantLeMois || apresLeMois) {
					couleurTexteJourCourant = new Color(170, 170, 170);
				}
				if(this.dateSelectionnee.get(Calendar.DAY_OF_MONTH) == jourCourant) {
					if(avantLeMois) {
						Calendar dateMoisPrecedant = (Calendar)this.dateAffichee.clone();
						dateMoisPrecedant.add(Calendar.MONTH,-1);
						if(
							this.dateSelectionnee.get(Calendar.MONTH) == dateMoisPrecedant.get(Calendar.MONTH) &&
							this.dateSelectionnee.get(Calendar.YEAR) == dateMoisPrecedant.get(Calendar.YEAR)
						) {
							couleurFondJourCourant = new Color(118, 172, 222);
							couleurTexteJourCourant = Color.WHITE;
							this.jl_jourSelectionne = this.jl_jours[i];
							this.jourSelectionneDansMois = false;
						}
					} else if(apresLeMois) {
						Calendar dateMoisSuivant = (Calendar)this.dateAffichee.clone();
						dateMoisSuivant.add(Calendar.MONTH,1);
						if(
							this.dateSelectionnee.get(Calendar.MONTH) == dateMoisSuivant.get(Calendar.MONTH) &&
							this.dateSelectionnee.get(Calendar.YEAR) == dateMoisSuivant.get(Calendar.YEAR)
						) {
							couleurFondJourCourant = new Color(118, 172, 222);
							couleurTexteJourCourant = Color.WHITE;
							this.jl_jourSelectionne = this.jl_jours[i];
							this.jourSelectionneDansMois = false;
						}
					} else {
						if(
							this.dateSelectionnee.get(Calendar.MONTH) == this.dateAffichee.get(Calendar.MONTH) &&
							this.dateSelectionnee.get(Calendar.YEAR) == this.dateAffichee.get(Calendar.YEAR)
						) {
							couleurFondJourCourant = new Color(55, 133, 207);
							couleurTexteJourCourant = Color.WHITE;
							this.jl_jourSelectionne = this.jl_jours[i];
							this.jourSelectionneDansMois = true;
						}
					}
				}
				this.jl_jours[i].setBackground(couleurFondJourCourant);
				this.jl_jours[i].setForeground(couleurTexteJourCourant);
				
				// Incrémenter i
				i++;
			}
		}
		
		// Mettre à jour l'heure
		this.js_heures.setValue(this.dateSelectionnee.get(Calendar.HOUR_OF_DAY));
		this.js_minutes.setValue(this.dateSelectionnee.get(Calendar.MINUTE));
	}
	
	/**
	 * Récupérer la date associée à un label
	 * @param jl_jour label
	 * @return date associée au label
	 */
	private Calendar getDatePourUnLabel(JLabel jl_jour) {
		for(int i=0;i<this.jl_jours.length;i++) {
			if(this.jl_jours[i] == jl_jour) {
				Calendar date = (Calendar)this.dateAffichee.clone();
				date.set(Calendar.DAY_OF_MONTH, 1);
				int premierJourDuMois = date.get(Calendar.DAY_OF_WEEK)-2;
				if(premierJourDuMois == -1) {
					premierJourDuMois = 6;
				}
				date.add(Calendar.DATE,i-premierJourDuMois);
				date.set(Calendar.HOUR_OF_DAY,this.dateSelectionnee.get(Calendar.HOUR_OF_DAY));
				date.set(Calendar.MINUTE,this.dateSelectionnee.get(Calendar.MINUTE));
				return date;
			}
		}
		return null;
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed (ActionEvent e) {
		if(e.getSource() == this.jb_moisPrecedant) {
			this.dateAffichee.add(Calendar.MONTH, -1);
			this.rafraichir();
		} else if(e.getSource() == this.jb_moisSuivant) {
			this.dateAffichee.add(Calendar.MONTH, 1);
			this.rafraichir();
		}
	}

	/**
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	@Override
	public void mouseClicked (MouseEvent e) {
		if(e.getSource() instanceof JLabel) {
			if(this.jl_jourSelectionne != null) {
				this.jl_jourSelectionne.setBackground(Color.WHITE);
				if(this.jourSelectionneDansMois) {
					this.jl_jourSelectionne.setForeground(Color.BLACK);
				} else {
					this.jl_jourSelectionne.setForeground(new Color(170, 170, 170));
				}
			}
			this.dateSelectionnee = this.getDatePourUnLabel((JLabel)e.getSource());
			this.rafraichir();
		}
	}

	/**
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	@Override
	public void mousePressed (MouseEvent e) {
	}

	/**
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	@Override
	public void mouseReleased (MouseEvent e) {
	}

	/**
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	@Override
	public void mouseEntered (MouseEvent e) {
	}

	/**
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	@Override
	public void mouseExited (MouseEvent e) {
	}

	/**
	 * @see ChangeListener#stateChanged(ChangeEvent)
	 */
	@Override
	public void stateChanged (ChangeEvent e) {
		if(e.getSource() == this.js_heures) {
			this.dateSelectionnee.set(Calendar.HOUR_OF_DAY, new Integer((Integer)this.js_heures.getValue()).intValue());
		} else if(e.getSource() == this.js_minutes) {
			this.dateSelectionnee.set(Calendar.MINUTE, new Integer((Integer)this.js_minutes.getValue()).intValue());
		}
	}
}
