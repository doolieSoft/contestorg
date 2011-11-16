package org.contestorg.out;


import java.util.ArrayList;

import org.contestorg.common.OperationAbstract;
import org.contestorg.common.OperationRunnableAbstract;
import org.contestorg.interfaces.IOperation;
import org.contestorg.interfaces.IOperationListener;
import org.contestorg.models.ModelCheminFTP;
import org.contestorg.models.ModelCheminLocal;
import org.contestorg.models.ModelExportation;



public abstract class ExportationAbstract
{
	// Lancer l'exportation
	public abstract IOperation export ();
	
	// Lancer une exportation
	public static IOperation export(ModelExportation exportation) {
		IOperation operation = null;
		if(exportation.getChemin() instanceof ModelCheminLocal) {
			operation = new ExportationLocal(exportation.getTheme().toInformation(), ((ModelCheminLocal)exportation.getChemin()).getChemin()).export();
		} else if(exportation.getChemin() instanceof ModelCheminFTP) {
			operation = new ExportationFTP(exportation.getTheme().toInformation(), ((ModelCheminFTP)exportation.getChemin()).toInformation().getInfosFTP()).export();
		}
		return operation;
	}
	
	// Lancer une liste d'exportation
	public static IOperation export(final ArrayList<ModelExportation> exportations) {
		return new OperationAbstract() {
			@Override
			public OperationRunnableAbstract getRunnable () {
				return new Export(exportations);
			}
		};
	}
	
	// Classe permettant l'export
	private static class Export extends OperationRunnableAbstract implements IOperationListener {
		
		// Exportations restantes
		private ArrayList<ModelExportation> exportations;
		
		// Nombre total d'exportations
		private int nbExportations;
		
		// Exportations réussies/échoués/arretées
		private ArrayList<ModelExportation> reussites = new ArrayList<ModelExportation>();
		private ArrayList<ModelExportation> echecs = new ArrayList<ModelExportation>();
		private ArrayList<ModelExportation> arrets = new ArrayList<ModelExportation>();
		
		// Constructeur
		public Export(ArrayList<ModelExportation> exportations) {
			// Retenir la liste des exportations
			this.exportations = exportations;
			
			// Retenir le nombre d'exportations
			this.nbExportations = exportations.size();
		}
		
		// Implémentation de run
		@Override
		public void run () {
			// Récupérer l'exportation
			ModelExportation exportation = this.exportations.get(0);
			
			// Lancer l'opération
			IOperation operation = ExportationAbstract.export(exportation);
			operation.addListener(this);
			operation.operationStart();
			
			// Fire
			this.fireMessage("Opération \""+exportation.getNom()+"\" lancée !");
		}
		
		// Implémentation de clean
		@Override
		protected void clean () {
		}

		// Implémentation de IOperationListener
		@Override
		public void progressionAvancement (double progression) {
		}
		@Override
		public void progressionMessage (String message) {
			// Transférer le message
			this.fireMessage("Exportation \""+this.exportations.get(0).getNom()+"\" : \""+message+"\"");
		}
		@Override
		public void progressionFin () {
			// Retirer l'exportation
			this.exportations.remove(0);

			// Fire
			this.fireAvancement(((double)this.nbExportations-(double)this.exportations.size())/(double)this.nbExportations);
			
			// Vérifier s'il faut lancer la prochaine exportation
			if(this.arret) {
				// Signaler l'arret des exportations
				this.arret();
			} else if(this.exportations.size() != 0) {
				// Lancer la prochaine exportation
				this.run();
			} else {
				// Vérifier si des exportations ont échouées
				if(this.echecs.size() != 0) {
					// Lister les opérations échouées
					StringBuilder liste = new StringBuilder();
					for(int i=0;i<this.echecs.size();i++) {
						if(i != 0) {
							if(i != this.echecs.size()-1) {
								liste.append(", ");
							} else {
								liste.append(" et ");
							}
						}
						liste.append("\""+this.echecs.get(i).getNom()+"\"");
					}
					
					// Signaler l'échec 
					this.echec("Les exportation "+liste.toString()+" ont échoué...");
				} else {
					// Signaler la réussite
					this.reussite("Toutes les exportations ont bien été executée !");
				}
			}
		}
		@Override
		public void operationReussite () {
			// Comptabiliser la réussite
			this.reussites.add(this.exportations.get(0));
			
			// Fire
			this.fireMessage("Exportation \""+this.exportations.get(0).getNom()+"\" réussie !");
		}
		@Override
		public void operationEchec () {
			// Comptabiliser l'echec
			this.echecs.add(this.exportations.get(0));
			
			// Fire
			this.fireMessage("Exportation \""+this.exportations.get(0).getNom()+"\" échouée...");
		}
		@Override
		public void operationArret () {
			// Comptabiliser l'arrêt
			this.arrets.add(this.exportations.get(0));
			
			// Fire
			this.fireMessage("Exportation \""+this.exportations.get(0).getNom()+"\" arrêtée...");
		}
	}
}
