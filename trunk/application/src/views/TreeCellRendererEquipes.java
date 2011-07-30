package views;

import infos.InfosModelCategorie;
import infos.InfosModelConcours;
import infos.InfosModelEquipe;
import infos.InfosModelPoule;
import interfaces.ITreeNode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreeCellRenderer;

import controlers.ContestOrg;

public class TreeCellRendererEquipes implements TreeCellRenderer
{
	// Implémentation de TreeCellRenderer
	@Override
	public Component getTreeCellRendererComponent (JTree jtree, Object object, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
		// Caster l'objet en node
		ITreeNode node = (ITreeNode)object;
		
		// Récupérer l'image et le nom du node
		String image = null, nom = null;
		switch(node.getLevel()) {
			case 0: // Catégories
				image = isExpanded ? "img/farm/16x16/drawer_open.png" : "img/farm/16x16/drawer.png";
				nom = "Catégories";
				break;
			case 1: // Catégorie
				image = "img/farm/16x16/folder.png";
				nom = ((InfosModelCategorie)node.getObject()).getNom();
				break;
			case 2: // Poule
				image = "img/farm/16x16/table.png";
				nom = ((InfosModelPoule)node.getObject()).getNom();
				break;
			case 3: // Equipe
				image = ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "img/farm/16x16/group.png" : "img/farm/16x16/user_green.png";
				nom = ((InfosModelEquipe)node.getObject()).getNom();
				break;
		}
		
		// Créer le label
		JLabel label = new JLabel(nom,new ImageIcon(image),SwingConstants.LEFT);
		if(isSelected) {
			label.setBackground(new Color(131,180,225));
			label.setOpaque(true);
			label.setBorder(new LineBorder(new Color(131,180,225), 4));
		} else {
			label.setBackground(new Color(254,254,254));
			label.setOpaque(true);
			label.setBorder(new LineBorder(new Color(254,254,254), 4));
		}
		
		// Créer et retourner le panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(BorderLayout.CENTER,label);
		return panel;
	}
	
}
