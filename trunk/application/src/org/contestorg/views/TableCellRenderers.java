package org.contestorg.views;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelParticipation;

/**
 * Classe permettant le rendu de colonnes
 */
public class TableCellRenderers
{
	/**
	 * Classe permettant le render de la colonne "Participant"
	 */
	public static class Participant implements TableCellRenderer {
		/**
		 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent (JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel(object == null ? (ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome") : (String)object);
			if(isSelected) {
				label.setBackground(new Color(184,207,229));
				label.setOpaque(true);
			}
			return label;
		}
	}
	
	/**
	 * Classe permettant le render de la colonne "Rang"
	 */
	public static class Rang implements TableCellRenderer {
		/**
		 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent (JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel(new DecimalFormat("00").format((Integer)object));
			if(isSelected) {
				label.setBackground(new Color(184,207,229));
				label.setOpaque(true);
			}
			return label;
		}
	};

	/**
	 * Classe permettant le render de la colonne "Nombre de points"
	 */
	public static class NbPoints implements TableCellRenderer {
		/**
		 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent (JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel(new DecimalFormat("0000.00").format((Double)object));
			if(isSelected) {
				label.setBackground(new Color(184,207,229));
				label.setOpaque(true);
			}
			return label;
		}
	};

	/**
	 * Classe permettant le render de la colonne "Nombre de victoires"
	 */
	public static class NbVictoires implements TableCellRenderer {
		/**
		 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent (JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel(new DecimalFormat("00").format((Integer)object));
			if(isSelected) {
				label.setBackground(new Color(184,207,229));
				label.setOpaque(true);
			}
			return label;
		}
	};

	/**
	 * Classe permettant le render de la colonne "Statut"
	 */
	public static class Statut implements TableCellRenderer {
		/**
		 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent (JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
			InfosModelParticipant.Statut statut = (InfosModelParticipant.Statut)object;
			ImageIcon icon;
			if(statut == InfosModelParticipant.Statut.DISQUALIFIE || statut == InfosModelParticipant.Statut.FORFAIT) {
				icon = new ImageIcon("img/farm/16x16/bullet_black.png");
			} else if(!ContestOrg.get().getCtrlParticipants().isStatutHomologueActive() && statut == InfosModelParticipant.Statut.PRESENT || statut == InfosModelParticipant.Statut.HOMOLOGUE) {
				icon = new ImageIcon("img/farm/16x16/bullet_green.png");
			} else {
				icon = new ImageIcon("img/farm/16x16/bullet_red.png");
			}
			JLabel label = new JLabel(ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? statut.getNomEquipe() : statut.getNomJoueur(),icon,SwingConstants.LEFT);
			if(isSelected) {
				label.setBackground(new Color(184,207,229));
				label.setOpaque(true);
			}
			return label;
		}
	};

	/**
	 * Classe permettant le render de la colonne "Phase"
	 */
	public static class Phase implements TableCellRenderer {
		/**
		 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent (JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel("Phase "+(((Integer)object)+1));
			if(isSelected) {
				label.setBackground(new Color(184,207,229));
				label.setOpaque(true);
			}
			return label;
		}
	}

	/**
	 * Classe permettant le render de la colonne "Résultat"
	 */
	public static class Resultat implements TableCellRenderer {
		/**
		 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent (JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
			String nom = null, icone = null;
			switch((Integer)object) {
				case InfosModelParticipation.RESULTAT_ATTENTE:
					nom = "Attente";
					icone = "img/farm/16x16/bullet_white.png";
					break;
				case InfosModelParticipation.RESULTAT_VICTOIRE:
					nom = "Victoire";
					icone = "img/farm/16x16/bullet_green.png";
					break;
				case InfosModelParticipation.RESULTAT_EGALITE:
					nom = "Egalité";
					icone = "img/farm/16x16/bullet_yellow.png";
					break;
				case InfosModelParticipation.RESULTAT_DEFAITE:
					nom = "Défaite";
					icone = "img/farm/16x16/bullet_red.png";
					break;
				case InfosModelParticipation.RESULTAT_FORFAIT:
					nom = "Forfait";
					icone = "img/farm/16x16/bullet_black.png";
					break;
			}
			JLabel label = new JLabel(nom,new ImageIcon(icone),SwingConstants.LEFT);
			if(isSelected) {
				label.setBackground(new Color(184,207,229));
				label.setOpaque(true);
			}
			return label;
		}
	};
	
}
