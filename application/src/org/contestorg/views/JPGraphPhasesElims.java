package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.interfaces.IGraphModel;
import org.contestorg.interfaces.IGraphModelListener;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.VertexView;

/**
 * Panel du graphe des phases éliminatoires
 */
@SuppressWarnings("serial")
public class JPGraphPhasesElims extends JPanel implements IGraphModelListener {
	
	/** Instance de JGraph */
	private JGraph jg_graphe;
	
	/** Modèle de données associé au graphe */
	private IGraphModel<InfosModelCategorie,InfosModelParticipant> model;
	
	/** Largeur maximal des cellules */
	private int largeurMaxCellule = 0;
	
	/** Liste des cellules */
	private DefaultGraphCell cells[];
	
	/** Liste des ports */
	private DefaultPort ports[];
	
	/** Liste des liens A */
	private DefaultEdge edgesA[];
	
	/** Liste des liens B */
	private DefaultEdge edgesB[];
	
	/** Liste des cellules */
	private ArrayList<GraphVertexViewPhasesElims> cellules = new ArrayList<GraphVertexViewPhasesElims>();
	
	/** Graphe de grande finale ? */
	private boolean grandeFinale;
	
	/** Graphe effacé ? */
	private boolean clear = true;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param grandeFinale grande finale ?
	 */
	public JPGraphPhasesElims(Window w_parent, boolean grandeFinale) {
		// Appeller le constructeur du parent
		super(new GridLayout(1,1));
		
		// Retenir s'il s'agit du grande phase
		this.grandeFinale = grandeFinale;
		
		// Créer l'instance de Jgraph
		GraphModel model = new DefaultGraphModel();
		CellViewFactory factory = new CellViewsFactoryPhasesElims(w_parent,this);
		GraphLayoutCache layout = new GraphLayoutCache(model, factory);
		this.jg_graphe = new JGraph(model, layout);
		
		// Paramètrer le graphique
		this.jg_graphe.setEditClickCount(1);
		this.jg_graphe.setMoveable(false);
		this.jg_graphe.setSizeable(false);
		this.jg_graphe.setSelectionEnabled(false);
		this.jg_graphe.setAntiAliased(true);
		
		// Ajouter le graphe au panel
		JScrollPane jsp = new JScrollPane(this.jg_graphe);
		this.add(jsp, BorderLayout.CENTER);
	}
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param nomCategorie nom de la catégorie
	 * @param grandeFinale grande finale ?
	 */
	public JPGraphPhasesElims(Window w_parent, String nomCategorie, boolean grandeFinale) {
		// Appeller le constructeur principal
		this(w_parent, grandeFinale);
		
		// Définir la catégorie
		this.setCategorie(nomCategorie);
	}
	
	/**
	 * Définir la catégorie
	 * @param nomCategorie nom de la catégorie
	 */
	public void setCategorie(String nomCategorie) {
		// Vérifier si la catégorie a bien changé
		if(this.model == null || !this.model.getObject().getNom().equals(nomCategorie)) {
			// Ne plus écouter le modèle associé au graphe et le fermer
			if(this.model != null) {
				this.model.removeListener(this);
				this.model.close();
			}
			
			// Récupérer/Retenir/Ecouter le modèle associé au graphe
			this.model = this.grandeFinale ? ContestOrg.get().getCtrlPhasesEliminatoires().getGraphModelGrandeFinale(nomCategorie) : ContestOrg.get().getCtrlPhasesEliminatoires().getGraphModelPetiteFinale(nomCategorie);
			this.model.addListener(this);
			
			// Rafraichir le graphe
			this.refresh();
		}
	}
	
	/**
	 * Effacer le graphe
	 */
	public void clear() {
		this.clear(false);
	}
	
	/**
	 * Effacer le graphe
	 * @param keepModel garder le modèle de données ?
	 */
	private void clear(boolean keepModel) {
		// Retenir que le graphe a été effacé
		this.clear = true;
		
		// Vider le graphique si nécéssaire
		if(this.cells != null && this.cells.length != 0) {
			this.jg_graphe.getGraphLayoutCache().remove(this.cells);
		}
		if(this.ports != null && this.ports.length != 0) {
			this.jg_graphe.getGraphLayoutCache().remove(this.ports);
		}
		if(this.edgesA != null && this.edgesA.length != 0) {
			this.jg_graphe.getGraphLayoutCache().remove(this.edgesA);
		}
		if(this.edgesB != null && this.edgesB.length != 0) {
			this.jg_graphe.getGraphLayoutCache().remove(this.edgesB);
		}
		this.cells = null;
		this.ports = null;
		this.edgesA = null;
		this.edgesB = null;
		
		// Fermer les cellules et vider la liste
		for(GraphVertexViewPhasesElims cellule : this.cellules) {
			cellule.close();
		}
		this.cellules.clear();
		
		// Perdre la référence du graphe
		if(!keepModel) {
			this.model = null;
		}
	}
	
	/**
	 * Rafraichir le graphe
	 */
	private void refresh() {
		// Effacer le graphe
		this.clear(true);
		
		// Vérifier s'il y a au moins deux participants
		if(this.model != null && this.model.size() > 1) {
			// Retenir que le graphe n'est plus effacé
			this.clear = false;
			
			// Nombre de phases
			int nbPhases = 0;
			for(int i=this.model.size();i>1;i=i/2) {
				nbPhases++;
			}
			
			// Position de départ et décallages vertical/horizontal
			int startX = 10, startY = 10;
			int shiftX = 35, shiftY = 10;
			
			// Mettre à jour la largeur maximale des cellules
			this.largeurMaxCellule = 0;
			for(int numeroCellule=0;numeroCellule<2*this.model.size()-1;numeroCellule++) {
				this.largeurMaxCellule = Math.max(this.largeurMaxCellule, GraphVertexViewPhasesElims.getRenderComponent(this.model.getCellule(numeroCellule)).getPreferredSize().width);
			}
	
			// Créer et ajouter les cellules des phases éliminatoires
			@SuppressWarnings("unused")
			int posX = startX, posY = startY, topX = startX, topY = startY, numeroCellule = 0;
			this.cells = new DefaultGraphCell[2*this.model.size()+1];
			this.ports = new DefaultPort[2*this.model.size()+1];
			this.edgesA = new DefaultEdge[this.model.size()];
			this.edgesB = new DefaultEdge[this.model.size()];
			for(int numPhase=0;numPhase<nbPhases+1;numPhase++) {
				// Nombre de matchs dans la phase
				int nbMatchsPhase = new Double(Math.pow(2,nbPhases-numPhase)).intValue();
				
				// Nombre de matchs dans la phase précédant
				int nbMatchsPhasePrecedante = numPhase == 0 ? 0 : new Double(Math.pow(2,nbPhases-numPhase+1)).intValue();
				
				// Créer et ajouter les cellules de la phase éliminatoire
				for(int numeroCellulePhase=0;numeroCellulePhase<nbMatchsPhase;numeroCellulePhase++,numeroCellule++) {
					// Ajouter la cellule
					this.addCellule(String.valueOf(numeroCellule), posX, posY, numeroCellule, numeroCellule, numeroCellule-this.model.size(), numeroCellule-this.model.size(), numeroCellule+numeroCellulePhase-nbMatchsPhasePrecedante+0, numeroCellule+numeroCellulePhase-nbMatchsPhasePrecedante+1);
					
					// Prendre en compte les dimensions des cellules dans les décallages
					if(numeroCellule == 0) {
						shiftY += this.jg_graphe.getCellBounds(this.cells[numeroCellule]).getHeight();
						shiftX += this.jg_graphe.getCellBounds(this.cells[numeroCellule]).getWidth();
					}
					
					// Décaller la position
					posY += shiftY;
				}
				
				if(numPhase != nbPhases) {
					// Décaller la position
					posX += shiftX;
					posY = topY+shiftY/2;
					
					// Modifier la position de départ
					topX = posX;
					topY = posY;
		
					// Modifier le décallage vertical
					shiftY = shiftY*2;
				}
			}
		}
	}
	
	/**
	 * Ajouter une cellule au graphe
	 * @param data donnée de la cellule
	 * @param posX position en X
	 * @param posY position en Y
	 * @param indexCell indice de la cellule
	 * @param indexPort indice du port vers le match suivant
	 * @param indexEdgeA indice du lien vers le march précédant A
	 * @param indexEdgeB indice du lien vers le match précédant B
	 * @param indexPortA indice du port vers le match précédant A
	 * @param indexPortB indice du port vers le match précédent B
	 */
	@SuppressWarnings("rawtypes")
	private void addCellule(Object data, int posX, int posY, int indexCell, int indexPort, int indexEdgeA, int indexEdgeB, int indexPortA, int indexPortB) {
		// Créer la cellule
		this.cells[indexCell] = new DefaultGraphCell(data);

		// Positionner de la cellule
		GraphConstants.setBounds(this.cells[indexCell].getAttributes(), new Rectangle2D.Double(posX,posY,0,0));

		// Créer un port et l'ajouter à la cellule
		this.ports[indexPort] = new DefaultPort();
		this.cells[indexCell].add(this.ports[indexPort]);

		// Ajouter la cellule au graphe
		this.jg_graphe.getGraphLayoutCache().insert(this.cells[indexCell]);
		
		// Ajouter les liens avec les deux matchs précédants
		if(indexEdgeA >= 0 && indexEdgeB >= 0 && indexPortA >= 0 && indexPortB >= 0) {
			// Créer les ponts
			this.edgesA[indexEdgeA] = new DefaultEdge();
			this.edgesB[indexEdgeB] = new DefaultEdge();
			
			// Paramétrer le pont A
			Map attributesEdgeA = this.edgesA[indexEdgeA].getAttributes();
			GraphConstants.setEditable(attributesEdgeA, false);
			GraphConstants.setRouting(attributesEdgeA, GraphConstants.ROUTING_SIMPLE);
			GraphConstants.setEndFill(attributesEdgeA, true);
			GraphConstants.setLineEnd(attributesEdgeA, GraphConstants.ARROW_CLASSIC);
			GraphConstants.setSelectable(attributesEdgeA, false);
			this.edgesA[indexEdgeA].setAttributes(new AttributeMap(attributesEdgeA));

			// Paramétrer le pont B
			Map attributesEdgeB = this.edgesB[indexEdgeB].getAttributes();
			GraphConstants.setEditable(attributesEdgeB, false);
			GraphConstants.setRouting(attributesEdgeB, GraphConstants.ROUTING_SIMPLE);
			GraphConstants.setEndFill(attributesEdgeB, true);
			GraphConstants.setLineEnd(attributesEdgeB, GraphConstants.ARROW_CLASSIC);
			GraphConstants.setSelectable(attributesEdgeB, false);
			this.edgesB[indexEdgeB].setAttributes(new AttributeMap(attributesEdgeB));
			
			// Ajouter les ponts au graphe
			this.jg_graphe.getGraphLayoutCache().insertEdge(this.edgesA[indexEdgeA], this.ports[indexPortA], this.ports[indexPort]);
			this.jg_graphe.getGraphLayoutCache().insertEdge(this.edgesB[indexEdgeB], this.ports[indexPortB], this.ports[indexPort]);
		}
	}
	
	/**
	 * Récupérer la largeur maximale de la cellule
	 * @return largeur maximale de la cellule
	 */
	public int getLargeurMaxCellule() {
		return this.largeurMaxCellule+20;
	}
	
	/**
	 * Récupérer le modèle de données
	 * @return modèle de données
	 */
	public IGraphModel<InfosModelCategorie, InfosModelParticipant> getModel() {
		return this.model;
	}
	
	/**
	 * Récupérer l'image du graphe
	 * @return image du graphe
	 */
	public BufferedImage getImage() {
		return this.jg_graphe.getImage(this.jg_graphe.getBackground(), 0);
	}
	
	/**
	 * Savoir s'il s'agit d'un graphe de grande finale
	 * @return graphe de grande finale ?
	 */
	public boolean isGrandeFinale() {
		return this.grandeFinale;
	}
	
	/**
	 * Savoir si le graphe a été effacé
	 * @return graphe effacé ?
	 */
	public boolean isClear() {
		return this.clear;
	}
	
	/**
	 * Indiquer qu'une cellule a été créée
	 * @param cellule cellule créée
	 */
	public void addCellule(GraphVertexViewPhasesElims cellule) {
		this.cellules.add(cellule);
	}
	
	/**
	 * Indiquer qu'une cellule a été modifiée
	 * @param cellule cellule modifiée
	 */
	public void updateCellule(VertexView cellule) {
		if(this.model != null) {
			// Récupérer la nouvelle largeur maximale
			int nouvelleLargeurMaxCellule = 0;
			for(int numeroCellule=0;numeroCellule<2*this.model.size()-1;numeroCellule++) {
				nouvelleLargeurMaxCellule = Math.max(nouvelleLargeurMaxCellule, GraphVertexViewPhasesElims.getRenderComponent(this.model.getCellule(numeroCellule)).getPreferredSize().width);
			}
			
			// Vérifier si la largeur maximale n'est pas modifiée
			if(nouvelleLargeurMaxCellule != this.largeurMaxCellule) {
				// Rafraichir toute le graphe
				this.refresh();
			} else {
				// Rafraichir uniquement la cellule
				this.jg_graphe.getGraphLayoutCache().update(cellule);
			}
		}
	}
	
	/**
	 * @see IGraphModelListener#reloadGraphe()
	 */
	@Override
	public void reloadGraphe () {
		// Rafraichir le graphe
		this.refresh();
	}

}
