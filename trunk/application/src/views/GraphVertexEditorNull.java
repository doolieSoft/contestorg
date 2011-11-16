package views;

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

@SuppressWarnings("serial")
public class GraphVertexEditorNull extends AbstractCellEditor implements GraphCellEditor
{

	// Objet associé à l'éditeur
	private Object value;
	
	// Label d'édition
	private JLabel jl_edition;
	
	// Constructeur
	public GraphVertexEditorNull(Object value, int width, int height) {
		// Retenir l'objet associé à l'éditeur
		this.value = value;
		
		// Créer le label d'édition
		this.jl_edition = new JLabel("Non éditable",new ImageIcon("img/farm/32x32/error.png"),SwingConstants.LEFT);
		this.jl_edition.setOpaque(true);
		this.jl_edition.setBackground(new Color(250, 90, 90));
		this.jl_edition.setBorder(new LineBorder(new Color(250, 90, 90), 5));
		this.jl_edition.setPreferredSize(new Dimension(width, height));
	}
	
	// Implémentation de getCellEditor
	@Override
	public Object getCellEditorValue () {
		// Retourner l'objet associé à l'éditeur
		return this.value;
	}

	// Implémentation de getGraphCellEditorComponent
	@Override
	public Component getGraphCellEditorComponent (JGraph graph, Object value, boolean isSelected) {
		// Demander à Swing de remettre à plus tard la création et l'affichage de la fenetre d'édition
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
