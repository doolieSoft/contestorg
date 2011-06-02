package views;

import infos.Fichier;
import infos.InfosModelTheme;
import infos.Parametre;
import infos.Theme;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import common.Pair;

@SuppressWarnings("serial")
public class JPTheme extends JPanel implements ItemListener
{
	
	// Thèmes
	private ArrayList<Theme> themes;
	
	// Panel des thèmes
	private JPanel jp_themes;
	
	// Entrées
	protected JComboBox jcb_themes = new JComboBox();
	protected HashMap<String,Pair<HashMap<String,JCParametreAbstract>,HashMap<String,JTextField>>> jcs_themes_parametres_cibles = new HashMap<String, Pair<HashMap<String,JCParametreAbstract>,HashMap<String,JTextField>>>();
	
	// Erreur ?
	private boolean error = false;
	
	// Fenetre
	private Window w_parent;
		
	// Constructeur
	public JPTheme(Window w_parent, ArrayList<Theme> themes) {
		this(w_parent, themes, false);
	}
	public JPTheme(Window w_parent, ArrayList<Theme> themes, boolean local) {
		// Configurer le panel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Retenir la fenetre
		this.w_parent = w_parent;
		
		// Retenir les thèmes
		this.themes = themes;
		
		// Vérifier s'il y a des thèmes disponibles
		if(themes != null && themes.size() != 0) {
			// Créer la liste et le panel des thèmes
			this.jcb_themes = new JComboBox();
			this.add(this.jcb_themes);
			this.add(Box.createVerticalStrut(5));
			this.jp_themes = new JPanel(new CardLayout());
			this.add(this.jp_themes);
			
			// Ajouter les thèmes
			int nbParametres = 0, nbFichiers = 0;
			for(Theme theme : this.themes) {
				// Ajouter le thème
				this.jcb_themes.addItem(theme.getNom());
				
				// Retenir le thème
				this.jcs_themes_parametres_cibles.put(theme.getNom(), new Pair<HashMap<String,JCParametreAbstract>, HashMap<String,JTextField>>(new HashMap<String, JCParametreAbstract>(), new HashMap<String, JTextField>()));
				
				// Créer le panel
				JPanel jp_theme = new JPanel();
				jp_theme.setLayout(new BoxLayout(jp_theme, BoxLayout.Y_AXIS));
				
				// Ajouter les paramètres
				ArrayList<Parametre> parametres = theme.getParametres();
				JLabel[] jls_theme_parametres = new JLabel[parametres.size()];
				JCParametreAbstract[] jcs_theme_parametres = new JCParametreAbstract[parametres.size()];
				for(int i=0;i<parametres.size();i++) {					
					// Ajouter le label et l'input
					jls_theme_parametres[i] = new JLabel(parametres.get(i).getNom()+" "+(parametres.get(i).isOptionnel() ? "(optionnel) " : "")+": ");
					jcs_theme_parametres[i] = JCParametreAbstract.create(parametres.get(i));
					
					// Description éventuelle
					if(parametres.get(i).getDescription() != null) {
						jls_theme_parametres[i].setToolTipText(parametres.get(i).getDescription());
						jcs_theme_parametres[i].setToolTipText(parametres.get(i).getDescription());
					}
					
					// Valeur par défaut eventuelle
					if(parametres.get(i).getDefaut() != null) {
						jcs_theme_parametres[i].setValeur(parametres.get(i).getDefaut());
					}
					
					// Retenir l'input
					this.jcs_themes_parametres_cibles.get(theme.getNom()).getFirst().put(parametres.get(i).getId(), jcs_theme_parametres[i]);
				}
				if(parametres.size() > 0) {
					JPanel jp_theme_parametres_hauteur = new JPanel(new BorderLayout());
					jp_theme_parametres_hauteur.add(ViewHelper.title("Paramètres", ViewHelper.H2),BorderLayout.NORTH);
					jp_theme_parametres_hauteur.add(ViewHelper.inputs(jls_theme_parametres, jcs_theme_parametres),BorderLayout.SOUTH);
					jp_theme.add(jp_theme_parametres_hauteur,theme);
					nbParametres += parametres.size();
				}
				
				// Lier chacun des composants à la liste des composants
				for(JCParametreAbstract jc_theme_parametres : jcs_theme_parametres) {
					jc_theme_parametres.link(jcs_theme_parametres);
				}
				
				// Ajouter les fichiers
				ArrayList<Fichier> fichiers = theme.getFichiers();
				for(Fichier fichier : new ArrayList<Fichier>(fichiers)) {
					if(fichier.isFixe()) {
						fichiers.remove(fichier);
					}
				}
				JLabel[] jls_theme_fichiers = new JLabel[fichiers.size()];
				JTextField[] jcs_theme_fichiers = new JTextField[fichiers.size()];
				for(int i=0;i<fichiers.size();i++) {
					// Ajouter le label et l'input
					jls_theme_fichiers[i] = new JLabel(fichiers.size() == 1 ? "" : fichiers.get(i).getNom()+" : ");
					jcs_theme_fichiers[i] = new JTextField(fichiers.get(i).getCible());
					
					// Description éventuelle
					if(fichiers.get(i).getDescription() != null) {
						jls_theme_fichiers[i].setToolTipText(fichiers.get(i).getDescription());
						jcs_theme_fichiers[i].setToolTipText(fichiers.get(i).getDescription());
					}
					
					// Retenir l'input
					this.jcs_themes_parametres_cibles.get(theme.getNom()).getSecond().put(fichiers.get(i).getId(), jcs_theme_fichiers[i]);
				}
				if(fichiers.size() > 0 && (!local || local && fichiers.size() != 1)) {
					JPanel jp_theme_fichiers_hauteur = new JPanel(new BorderLayout());
					jp_theme_fichiers_hauteur.add(ViewHelper.title(fichiers.size() == 1 ? "Fichier" : "Fichiers", ViewHelper.H2),BorderLayout.NORTH);
					jp_theme_fichiers_hauteur.add(ViewHelper.inputs(jls_theme_fichiers, jcs_theme_fichiers),BorderLayout.SOUTH);
					jp_theme.add(jp_theme_fichiers_hauteur);
					nbFichiers += fichiers.size();
				}
				
				// Ajouter le panel du thème
				JPanel jp_theme_hauteur = new JPanel(new BorderLayout());
				jp_theme_hauteur.add(jp_theme,BorderLayout.NORTH);
				this.jp_themes.add(jp_theme_hauteur,theme.getNom());
			}
			
			// Saviez-vous ?
			if(nbParametres > 0 || nbFichiers > 0) {
				this.add(Box.createVerticalStrut(5));
			}
			this.add(ViewHelper.pinformation("Saviez-vous que vous pouvez créer vos propres thèmes ?","doc/Aide.pdf","Ouvrir l'aide au format PDF"));
			
			// Ecouter la liste des thèmes
			this.jcb_themes.addItemListener(this);
		} else {
			// Erreur
			ViewHelper.derror(this.w_parent, "Aucun thème valide n'a été trouvé.");
			this.add(ViewHelper.perror("Aucun thème valide n'a été trouvé."));
		}
	}
	
	// Récupérer le thème
	public InfosModelTheme getTheme() {
		// Récupérer le thème séléctionné
		Theme theme = this.themes.get(this.jcb_themes.getSelectedIndex());
		
		// Récupérer et vérifier les données
		boolean erreur = false;
		String chemin = theme.getChemin();
		HashMap<String,String> parametres = new HashMap<String,String>();
		for(Parametre parametre : theme.getParametres()) {
			// Récupérer le composant
			JCParametreAbstract composant = this.jcs_themes_parametres_cibles.get(theme.getNom()).getFirst().get(parametre.getId());
			
			// Vérifier si le composant est valide
			String message = composant.getError();
			if(message == null) {
				// Ajouter le paramètre
				parametres.put(parametre.getId(), composant.getValeur());
			} else {
				// Erreur
				erreur = true;
				ViewHelper.derror(this.w_parent, "Erreur sur les paramètres du thème", message);
			}
		}
		HashMap<String,String> fichiers = new HashMap<String,String>();
		for(Fichier fichier : theme.getFichiers()) {
			// Récupérer le chemin du fichier
			String valeur = this.jcs_themes_parametres_cibles.get(theme.getNom()).getSecond().get(fichier.getId()).getText().trim();
			
			// Vérifier si l'entrée n'est pas vide
			if(!valeur.isEmpty()) {
				// Ajouter le fichier
				fichiers.put(fichier.getId(), valeur);
			} else {
				// Erreur
				erreur = true;
				ViewHelper.derror(this.w_parent, "Erreur sur les fichiers du thème", "");
			}
		}
		
		// Créer et retourner les informations
		if(!erreur) {
			return new InfosModelTheme(chemin, parametres, fichiers);
		}
		return null;
	}
	
	// Définir le thème
	public void setTheme(InfosModelTheme themeUtilise) {
		// Vérifier si la liste des thèmes existe
		if(this.jcb_themes != null) {
			// Initialisé les booléens indiquant si le thème utilisé existe et s'il est compatible d'après les paramètres trouvés et ceux disponibles
			boolean trouve = false, compatible = true;
			
			// Pour chacun des thèmes trouvés
			for (int i = 0; i < this.themes.size(); i++) {
				// Si le thème correspont au thème utilisé
				if (themeUtilise.getChemin().equals(this.themes.get(i).getChemin())) {
					// Thème trouvé
					trouve = true;
					
					// Séléctioner le thème
					this.jcb_themes.setSelectedIndex(i);
					
					// Compter le nombre de paramètre obligatoires dans le thème
					int nbParametresObligatoires = 0;
					for(Parametre parametre : this.themes.get(i).getParametres()) {
						if(!parametre.isOptionnel()) {
							nbParametresObligatoires++;
						}
					}
					
					// Renseigner les paramètres du thème
					int nbParametresObligatoiresRenseignes = 0;
					ArrayList<String> parametresRenseignes = new ArrayList<String>();
					HashMap<String,String> parametresRestants = new HashMap<String,String>(themeUtilise.getParametres());
					while(parametresRestants.size() > 0) {
						// Pour chacun des paramètre dans du thème restants à renseigner
						for(Entry<String,String> parametreUtilise : new HashMap<String,String>(parametresRestants).entrySet()) {
							// Récupérer le paramètre correspondant
							Parametre parametre = this.themes.get(i).getParametre(parametreUtilise.getKey());
							
							// Vérifier que le paramètre correspondant existe
							if(parametre == null) {
								// Retirer le paramètre
								parametresRestants.remove(parametreUtilise.getKey());
								
								// Retenir le problème de compatibilité
								compatible = false;
							} else {
								// Vérifier si le paramètre dépendant du paramètre courant a déjà été renseigné
								if(parametre.getDependance() == null || this.themes.get(i).getParametre(parametre.getDependance()) == null || parametresRenseignes.contains(parametre.getDependance())) {
									// Renseigner le paramètre
									this.jcs_themes_parametres_cibles.get(themeUtilise.getNom()).getFirst().get(parametreUtilise.getKey()).setValeur(parametreUtilise.getValue());
									parametresRenseignes.add(parametreUtilise.getKey());
									parametresRestants.remove(parametreUtilise.getKey());
									
									// Comptabiliser le paramètre renseigné
									if(!parametre.isOptionnel()) {
										nbParametresObligatoiresRenseignes++;
									}
								}
							}
						}
					}
					if(nbParametresObligatoires != nbParametresObligatoiresRenseignes) {
						// Retenir le problème de compatibilité
						compatible = false;
					}
					
					// Renseigner les fichiers du thème
					for(Entry<String,String> fichierUtilise : themeUtilise.getFichiers().entrySet()) {
						// Récupérer le fichier correspondant
						Fichier fichier = this.themes.get(i).getFichier(fichierUtilise.getKey());
						
						// Vérifier que le fichier correspondant existe
						if(fichier == null) {
							// Retenir le problème de compatibilité
							compatible = false;
						} else {
							// Renseigner la cible du fichier
							this.jcs_themes_parametres_cibles.get(themeUtilise.getNom()).getSecond().get(fichierUtilise.getKey()).setText(fichierUtilise.getValue());
						}
					}
				}
			}
			
			// Vérifier si le thème a été trouvé
			if(!trouve) {
				ViewHelper.derror(this.w_parent, "Erreur sur le thème", "Le thème utilisé n'a pas été retrouvé.");
			} else if(!compatible) {
				ViewHelper.derror(this.w_parent, "Erreur sur le thème", "Des problèmes de compatibilité avec le thème ont été trouvés.");
			}
		}
	}
	
	// Récupérer la cible du fichier unique
	public String getCible() {
		return this.jcs_themes_parametres_cibles.get(this.themes.get(this.jcb_themes.getSelectedIndex()).getNom()).getSecond().values().iterator().next().getText();
	}
	
	// Définir le cible du fichier unique
	public void setCible(String cible) {
		this.jcs_themes_parametres_cibles.get(this.themes.get(this.jcb_themes.getSelectedIndex()).getNom()).getSecond().values().iterator().next().setText(cible);
	}
	
	// Récupérer le nombre de fichiers
	public int getNbFichiers() {
		return this.jcs_themes_parametres_cibles.get(this.themes.get(this.jcb_themes.getSelectedIndex()).getNom()).getSecond().size(); 
	}
	
	// Implémentation de ItemListener
	@Override
	public void itemStateChanged (ItemEvent event) {
		CardLayout layout = (CardLayout)(this.jp_themes.getLayout());
		layout.show(this.jp_themes, (String)event.getItem());
	}
		
	// Savoir s'il y a des erreurs
	public boolean isError() {
		return this.themes == null || this.themes.size() == 0 || this.error;
	}
	
}
