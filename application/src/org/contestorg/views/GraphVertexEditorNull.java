package org.contestorg.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphCellEditor;

/**
 * Editeur de cellule du graphe des phases éliminatoires pour une cellule non éditable 
 */
@SuppressWarnings("serial")
public class GraphVertexEditorNull extends AbstractCellEditor implements GraphCellEditor
{

	/** Objet associé à la cellule */
	private Object value;
	
	/** Label d'édition */
	private JLabel jl_edition;
	
	/**
	 * Constructeur
	 * @param value objet associé à la cellule
	 * @param width largeur de l'éditeur
	 * @param height hauteur de l'éditeur
	 */
	public GraphVertexEditorNull(Object value, int width, int height) {
		// Retenir l'objet associé à la cellule
		this.value = value;
		
		// Créer le label d'édition
		this.jl_edition = new JLabel("Non éditable",new ImageIcon("img/farm/32x32/error.png"),SwingConstants.LEFT);
		this.jl_edition.setOpaque(true);
		this.jl_edition.setBackground(new Color(250, 90, 90));
		this.jl_edition.setBorder(new LineBorder(new Color(250, 90, 90), 5));
		this.jl_edition.setPreferredSize(new Dimension(width, height));
	}
	
	/**
	 * @see GraphCellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue () {
		// Retourner l'objet associé à l'éditeur
		return this.value;
	}

	/**
	 * @see GraphCellEditor#getGraphCellEditorComponent(JGraph, Object, boolean)
	 */
	@Override
	public Component getGraphCellEditorComponent (JGraph graph, Object value, boolean isSelected) {
		// Demander à Swing de remettre à plus tard la création et l'affichage de la fenêtre d'édition
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run () {
				// Attendre une seconde
				try { Thread.sleep(1000); } catch (InterruptedException e) { }
				
				// Arreter l'édition
				stopCellEditing();
			}
		});
		
		// Retourner le label d'édition
		return this.jl_edition;
	}
	
}
