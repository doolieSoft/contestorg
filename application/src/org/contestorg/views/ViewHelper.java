package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

/**
 * Classe d'aide à la construction de composants graphiques
 */
public class ViewHelper
{

	// Tailles de titre
	
	/** Titre de niveau 1 */
	public final static int H1 = 16;
	/** Titre de niveau 2 */
	public final static int H2 = 14;
	/** Titre de niveau 3 */
	public final static int H3 = 12;
	
	// Chemins d'images
	
	/** Petite erreur */
	public static final String ICON_SMALL_ERROR = "img/farm/16x16/exclamation.png";
	/** Petit avertissement */
	public static final String ICON_SMALL_WARNING = "img/farm/16x16/error.png";
	/** Petite information */
	public static final String ICON_SMALL_INFO = "img/farm/16x16/information.png";
	/** Petite confirmation */
	public static final String ICON_SMALL_CONFIRMATION = "img/farm/16x16/help.png";
	
	/** Grande erreur */
	public static final String ICON_BIG_ERROR = "img/farm/32x32/exclamation.png";
	/** Grand avertissement */
	public static final String ICON_BIG_WARNING = "img/farm/32x32/error.png";
	/** Grande information */
	public static final String ICON_BIG_INFO = "img/farm/32x32/information.png";
	/** Grande confirmation */
	public static final String ICON_BIG_CONFIRMATION = "img/farm/32x32/help.png";

	/**
	 * Créer un panel de titre
	 * @param message message du titre
	 * @param image image du titre
	 * @param size taille du titre
	 * @return panel du titre
	 */
	public static JPanel title (String message, String image, int size) {
		JLabel label;
		if (image == null) {
			label = new JLabel(message, SwingConstants.LEFT);
		} else {
			label = new JLabel(message, new ImageIcon(image), SwingConstants.LEFT);
		}
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, size));
		JPanel panel = ViewHelper.left(label);
		panel.setBorder(new EmptyBorder(0, 0, 5, 0));
		return panel;
	}
	
	/**
	 * Créer un panel de titre
	 * @param message message du titre
	 * @return panel du titre
	 */
	public static JPanel title (String message) {
		return ViewHelper.title(message, null, ViewHelper.H1);
	}
	
	/**
	 * Créer un panel de titre
	 * @param message message du titre
	 * @param image image du titre
	 * @return panel du titre
	 */
	public static JPanel title (String message, String image) {
		return ViewHelper.title(message, image, ViewHelper.H1);
	}
	
	/**
	 * Créer un titre
	 * @param message message du titre
	 * @param size taille du titre
	 * @return panel du titre
	 */
	public static JPanel title (String message, int size) {
		return ViewHelper.title(message, null, size);
	}
	
	/**
	 * Créer un panel avec des composants alignés sur la gauche
	 * @param components composants
	 * @return panel avec les composants alignés sur la gauche
	 */
	public static JPanel left(JComponent... components) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		panel.setOpaque(false);
		for(int i=0;i<components.length;i++) {
			if(i != 0) {
				panel.add(Box.createHorizontalStrut(5));
			}
			panel.add(components[i]);
		}
		return panel;
	}
	
	/**
	 * Créer un panel avec des composants alignés au centre
	 * @param components components
	 * @return panel avec les composants alignés au centre
	 */
	public static JPanel center(JComponent... components) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		panel.setOpaque(false);
		for(int i=0;i<components.length;i++) {
			if(i != 0) {
				panel.add(Box.createHorizontalStrut(5));
			}
			panel.add(components[i]);
		}
		return panel;
	}

	/**
	 * Créer un panel de labels alignés sur la gauche et associés à des composants
	 * @param labels labels
	 * @param composants composants
	 * @return panel avec les labels alignés sur la gauche et associés aux composants
	 */
	public static JPanel inputs (JLabel[] labels, JComponent[] composants) {
		// Créer le panel
		JPanel panel = new JPanel(new GridBagLayout());

		// Créer la contrainte pour les labels
		GridBagConstraints contrainte_labels = new GridBagConstraints();
		contrainte_labels.gridx = 0;
		contrainte_labels.weightx = 0;
		contrainte_labels.insets = new Insets(0, 0, 2, 0);
		contrainte_labels.anchor = GridBagConstraints.BASELINE_LEADING;

		// Créer la contrainte pour les composants
		GridBagConstraints contrainte_composants = new GridBagConstraints();
		contrainte_composants.gridx = 1;
		contrainte_composants.weightx = 1;
		contrainte_composants.insets = new Insets(0, 0, 2, 0);
		contrainte_composants.gridwidth = GridBagConstraints.REMAINDER;
		contrainte_composants.anchor = GridBagConstraints.BASELINE_LEADING;
		contrainte_composants.fill = GridBagConstraints.HORIZONTAL;

		// Ajouter les élément dans le panel
		for (int i = 0; i < labels.length; i++) {
			// Lier le label au composant
			labels[i].setLabelFor(composants[i]);

			// Ajouter le label et le composant
			contrainte_labels.gridy = i;
			contrainte_composants.gridy = i;
			panel.add(labels[i], contrainte_labels);
			panel.add(composants[i], contrainte_composants);
		}

		// Retourner le panel
		return panel;
	}

	/**
	 * Vérifier si une entrée est vide
	 * @param inputs entrées
	 * @return une entrée vide ?
	 */
	public static boolean empty (JTextField... inputs) {
		for (JTextField input : inputs) {
			if (input.getText().trim().equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Créer un panel d'erreur
	 * @param message message d'erreur
	 * @return panel d'erreur
	 */
	public static JPanel perror (String message) {
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(new Color(250, 90, 90));
		panel.add(new JLabel(new ImageIcon(ViewHelper.ICON_SMALL_ERROR)));
		panel.add(new JLabel(message));
		return panel;
	}

	/**
	 * Afficher un message d'erreur
	 * @param parent fenêtre parent
	 * @param title titre de la fenêtre
	 * @param message message d'erreur
	 */
	public static void derror (Component parent, String title, String message) {
		JOptionPane.showMessageDialog(parent, new JLabel("<html>" + message.replace("\n", "<br/>").replace(" ", "&nbsp;") + "</html>"), title, JOptionPane.ERROR_MESSAGE, new ImageIcon(ViewHelper.ICON_BIG_ERROR));
	}
	
	/**
	 * Afficher un message d'erreur
	 * @param parent fenêtre parent
	 * @param message message d'erreur
	 */
	public static void derror (Component parent, String message) {
		ViewHelper.derror(parent, "Erreur", message);
	}

	/**
	 * Créer un panel d'avertissement
	 * @param message message d'avertissement
	 * @return panel d'avertissement
	 */
	public static JPanel pwarning (String message) {
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(new Color(250, 180, 76));
		panel.add(new JLabel(new ImageIcon(ViewHelper.ICON_SMALL_WARNING)));
		panel.add(new JLabel(message));
		return panel;
	}

	/**
	 * Afficher un message d'avertissement
	 * @param parent fenêtre parent
	 * @param title titre de la fenêtre
	 * @param message message d'avertissement
	 */
	public static void dwarning (Component parent, String title, String message) {
		JOptionPane.showMessageDialog(parent, new JLabel("<html>" + message.replace("\n", "<br/>").replace(" ", "&nbsp;") + "</html>"), title, JOptionPane.WARNING_MESSAGE, new ImageIcon(ViewHelper.ICON_BIG_WARNING));
	}
	
	/**
	 * Afficher un message d'avertissement
	 * @param parent fenêtre parent
	 * @param message message d'avertissement
	 */
	public static void dwarning (Component parent, String message) {
		ViewHelper.dwarning(parent, "Avertissement", message);
	}

	/**
	 * Créer un panel d'information
	 * @param message message d'information
	 * @param path fichier d'aide à ouvrir lors du clic
	 * @return le panel d'information
	 */
	public static JPanel pinformation (String message, final String path, String info) {
		final JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(new Color(135, 180, 222));
		panel.add(new JLabel(new ImageIcon(ViewHelper.ICON_SMALL_INFO)), BorderLayout.WEST);
		panel.add(new JLabel(message), BorderLayout.CENTER);
		if(path != null) {
			panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			panel.setToolTipText(info);
			panel.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased (MouseEvent event) {	
				}
				@Override
				public void mousePressed (MouseEvent event) {	
				}
				@Override
				public void mouseExited (MouseEvent event) {
					panel.setBackground(new Color(135, 180, 222));
				}
				@Override
				public void mouseEntered (MouseEvent event) {
					panel.setBackground(new Color(162, 197, 230));
				}
				@Override
				public void mouseClicked (MouseEvent event) {
					File file = new File(path);
					if (file.exists()) {
						try {
							Desktop.getDesktop().open(file);
						} catch (IOException e) {
							ViewHelper.derror(panel.getTopLevelAncestor(), "Erreur de l'ouverture du fichier d'aide.");
						}
					} else {
						ViewHelper.derror(panel.getTopLevelAncestor(), "Le fichier d'aide n'a pas été trouvé.");
					}
				}
			});
		}
		return panel;
	}
	
	/**
	 * Créer un panel d'information
	 * @param message message d'information
	 * @return panel d'information
	 */
	public static JPanel pinformation (String message) {
		return ViewHelper.pinformation(message,null,null);
	}

	/**
	 * Afficher un message d'information
	 * @param parent fenêtre parent
	 * @param title titre de la fenêtre
	 * @param message message d'information
	 */
	public static void dinformation (Component parent, String title, String message) {
		JOptionPane.showMessageDialog(parent, new JLabel("<html>" + message.replace("\n", "<br/>").replace(" ", "&nbsp;") + "</html>"), title, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(ViewHelper.ICON_BIG_INFO));
	}
	
	/**
	 * Afficher un message d'information
	 * @param parent fenêtre parent
	 * @param message message d'information
	 */
	public static void dinformation (Component parent, String message) {
		ViewHelper.dinformation(parent, "Information", message);
	}

	/**
	 * Afficher une demande de confirmation
	 * @param parent fenêtre parent
	 * @param title titre de la fenêtre
	 * @param message message de confirmation
	 * @param warning avertissement ?
	 * @return demande confirmée ?
	 */
	public static boolean confirmation (Component parent, String title, String message, boolean warning) {
		if (JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(warning ? ViewHelper.ICON_BIG_WARNING : ViewHelper.ICON_BIG_CONFIRMATION)) == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Afficher une demande de confirmation
	 * @param parent fenêtre parent
	 * @param title titre de la fenêtre
	 * @param message message de confirmation
	 * @return demande confirmée ?
	 */
	public static boolean confirmation (Component parent, String title, String message) {
		return ViewHelper.confirmation(parent, title, message, false);
	}
	
	/**
	 * Afficher une demande de confirmation
	 * @param parent fenêtre parent
	 * @param message message de confirmation
	 * @param warning avertissement ?
	 * @return demande confirmée ?
	 */
	public static boolean confirmation (Component parent, String message, boolean warning) {
		return ViewHelper.confirmation(parent, warning ? "Attention" : "Confirmation", message, warning);
	}
	
	/**
	 * Afficher une demande de confirmation
	 * @param parent fenêtre parent ?
	 * @param message message de confirmation
	 * @return demande confirmée ?
	 */
	public static boolean confirmation (Component parent, String message) {
		return ViewHelper.confirmation(parent, "Confirmation", message, false);
	}

	/**
	 * Ouvrir une fenêtre de sauvegarde de fichier
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param extension extension du type de fichier
	 * @param description description du type de fichier
	 * @param defaut fichier par défaut
	 * @return chemin du fichier choisi
	 */
	public static String sauvegarder (Component parent, String titre, String extension, String description, File defaut, FileSystemView view) {
		while (true) {
			// Creer le JFileChooser
			JFileChooser fileChooser = new JFileChooser(view);
			if(defaut.isDirectory()) {
				fileChooser.setCurrentDirectory(defaut);
			} else {
				fileChooser.setSelectedFile(defaut);
			}
			fileChooser.setDialogTitle(titre == null ? "Sauvegarder un fichier" : titre);
			fileChooser.setApproveButtonText("Sauvegarder");
			if(extension != null && description != null) {
				fileChooser.setFileFilter(new FiltreFichier(extension, description));
			}

			// Ouvrir le JFileChooser
			int reponse = fileChooser.showSaveDialog(parent);

			// Si l'utilisateur a choisi un fichier
			if (reponse == JFileChooser.APPROVE_OPTION) {
				// Chemin du fichier
				String chemin = fileChooser.getSelectedFile().getPath();
				
				// Rajouter l'extension si non précisée
				if(extension != null) {
					chemin = chemin.endsWith("." + extension) ? chemin : chemin + "." + extension;
				}

				// Tester si le fichier existe
				File fichier = new File(chemin);
				if (!fichier.exists() || ViewHelper.confirmation(parent, "Désirez-vous écraser le fichier existant ?")) {
					// Retourner le chemin du fichier
					return chemin;
				}
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Ouvrir une fenêtre de sauvegarde de fichier
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param extension extension du type de fichier
	 * @param description description du type de fichier
	 * @param view vue sur le système de fichier
	 * @return chemin du fichier choisi
	 */
	public static String sauvegarder (Component parent, String titre, String extension, String description, FileSystemView view) {
		return ViewHelper.sauvegarder(parent, titre, extension, description, view.getHomeDirectory(), view);
	}
	
	/**
	 * Ouvrir une fenêtre de sauvegarde de fichier
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param extension extension du type de fichier
	 * @param description description du type de fichier
	 * @param defaut fichier par défaut
	 * @return chemin du fichier choisi
	 */
	public static String sauvegarder (Component parent, String titre, String extension, String description, File defaut) {
		return ViewHelper.sauvegarder(parent, titre, extension, description, defaut, FileSystemView.getFileSystemView());
	}
	
	/**
	 * Ouvrir une fenêtre de sauvegarde de fichier
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param extension extension du type de fichier
	 * @param description description du type de fichier
	 * @return chemin du fichier choisi
	 */
	public static String sauvegarder (Component parent, String titre, String extension, String description) {
		return ViewHelper.sauvegarder(parent, titre, extension, description, FileSystemView.getFileSystemView().getHomeDirectory(), FileSystemView.getFileSystemView());
	}

	/**
	 * Ouvrir une fenêtre d'ouverture de fichier
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param extension extension du type de fichier
	 * @param description description du type de fichier
	 * @param defaut fichier par défaut
	 * @param view vue du système de fichiers
	 * @return chemin du fichier choisi
	 */
	public static String ouvrir (Component parent, String titre, String extension, String description, File defaut, FileSystemView view) {
		while (true) {
			// Creer le JFileChooser
			JFileChooser fileChooser = new JFileChooser(view);
			fileChooser.setCurrentDirectory(defaut);
			if (extension != null && description != null) {
				fileChooser.setDialogTitle(titre == null ? "Ouvrir un fichier" : titre);
				fileChooser.setApproveButtonText("Ouvrir");
				fileChooser.setFileFilter(new FiltreFichier(extension, description));
			} else {
				fileChooser.setDialogTitle(titre == null ? "Séléctionner un répertoire" : titre);
				fileChooser.setApproveButtonText("Séléctionner");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
			}

			// Ouvrir le JFileChooser
			int reponse = fileChooser.showOpenDialog(parent);

			// Si l'utilisateur a choisi un fichier
			if (reponse == JFileChooser.APPROVE_OPTION) {
				// Chemin du fichier
				String chemin = fileChooser.getSelectedFile().getPath();

				// Tester le fichier choisi
				File fichier = view.createFileObject(chemin);
				if (!fichier.exists()) {
					ViewHelper.derror(parent, "Le fichier choisi n'existe pas.");
					fichier = null;
				} else if (!fichier.isFile()) {
					ViewHelper.derror(parent, "Le fichier choisi n'est pas un fichier.");
					fichier = null;
				}

				// Si aucune erreur
				if (fichier != null) {
					// Retourner le chemin du fichier
					return chemin;
				}
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Ouvrir une fenêtre d'ouverture de fichier
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param extension extension du type de fichier
	 * @param description description du type de fichier
	 * @param view vue du système de fichiers
	 * @return chemin du fichier choisi
	 */
	public static String ouvrir (Component parent, String titre, String extension, String description, FileSystemView view) {
		return ViewHelper.ouvrir(parent, titre, extension, description, view.getHomeDirectory(), view);
	}
	
	/**
	 * Ouvrir une fenêtre d'ouverture de fichier
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param extension extension du type de fichier
	 * @param description description du type de fichier
	 * @param defaut fichier par défaut
	 * @return chemin du fichier choisi
	 */
	public static String ouvrir (Component parent, String titre, String extension, String description, File defaut) {
		return ViewHelper.ouvrir(parent, titre, extension, description, defaut, FileSystemView.getFileSystemView());
	}
	
	/**
	 * Ouvrir une fenêtre d'ouverture de fichier
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param extension extension du type de fichier
	 * @param description description du type de fichier
	 * @return chemin du fichier choisi
	 */
	public static String ouvrir (Component parent, String titre, String extension, String description) {
		return ViewHelper.ouvrir(parent, titre, extension, description, FileSystemView.getFileSystemView().getHomeDirectory(), FileSystemView.getFileSystemView());
	}

	/**
	 * Ouvrir une fenêtre de sélection de répertoire
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param defaut répertoire par défaut
	 * @param view vue du système de fichiers
	 * @return chemin du répertoire choisi
	 */
	public static String selectionner (Component parent, String titre, File defaut, FileSystemView view) {
		while (true) {
			// Creer le JFileChooser
			JFileChooser fileChooser = new JFileChooser(view);
			fileChooser.setCurrentDirectory(defaut);
			fileChooser.setDialogTitle(titre == null ? "Séléctionner un répertoire" : titre);
			fileChooser.setApproveButtonText("Séléctionner");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);

			// Ouvrir le JFileChooser
			int reponse = fileChooser.showOpenDialog(parent);

			// Si l'utilisateur a choisi un fichier
			if (reponse == JFileChooser.APPROVE_OPTION) {
				// Chemin du chemin
				String chemin = fileChooser.getSelectedFile().getPath();

				// Tester le fichier choisi
				File repertoire = view.createFileObject(chemin);
				if (!repertoire.exists()) {
					ViewHelper.derror(parent, "Le répertoire choisi n'existe pas.");
					repertoire = null;
				} else if (!repertoire.isDirectory()) {
					ViewHelper.derror(parent, "Le répertoire choisi n'est pas un répertoire.");
					repertoire = null;
				}

				// Si aucune erreur
				if (repertoire != null) {
					// Retourner le chemin du répertoire
					return chemin;
				}
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Ouvrir une fenêtre de sélection de répertoire
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param view vue du système de fichiers
	 * @return chemin du répertoire choisi
	 */
	public static String selectionner (Component parent, String titre, FileSystemView view) {
		return ViewHelper.selectionner(parent, titre, view.getHomeDirectory(), view);
	}
	
	/**
	 * Ouvrir une fenêtre de sélection de répertoire
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @param defaut répertoire par défaut
	 * @return chemin du répertoire choisi
	 */
	public static String selectionner (Component parent, String titre, File defaut) {
		return ViewHelper.selectionner(parent, titre, defaut, FileSystemView.getFileSystemView());
	}
	
	/**
	 * Ouvrir une fenêtre de sélection de répertoire
	 * @param parent fenêtre parent
	 * @param titre titre de la fenêtre
	 * @return chemin du répertoire choisi
	 */
	public static String selectionner (Component parent, String titre) {
		return ViewHelper.selectionner(parent, titre, FileSystemView.getFileSystemView().getHomeDirectory(), FileSystemView.getFileSystemView());
	}

}
