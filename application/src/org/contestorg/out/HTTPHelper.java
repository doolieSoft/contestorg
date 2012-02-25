package org.contestorg.out;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.contestorg.common.OperationAbstract;
import org.contestorg.common.OperationRunnableAbstract;
import org.contestorg.interfaces.IOperationListener;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Classe d'aide à la réalisation d'opérations FTP
 */
public class HTTPHelper
{
	/**
	 * Classe permettant de faire des requêtes HTTP
	 */
	public static class Browser
	{
		/** Client HTTP */
		private AbstractHttpClient client;
		
		/**
		 * Constructeur
		 */
		public Browser() {
			this.client = new DefaultHttpClient();
		}
		
		/**
		 * Effectuer une requete GET
		 * @param host hôte
		 * @param port port
		 * @param path chemin
		 * @return réponse
		 */
		public HttpResponse get (String host, int port, String path) {
			try {
				// Executer la requete
				return this.client.execute(new HttpGet(host + ":" + String.valueOf(port) + path));
			} catch (ClientProtocolException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
	}
	
	/**
	 * Classe permettant de mettre en place un serveur HTTP
	 */
	public static class Server
	{
		/** Port d'écoute */
		private int port;
		
		/** Serveur HTTP */
		private HttpServer server;
		
		/** Serveur demarré ? */
		private boolean started = false;
		
		/** Ressources */
		private ArrayList<RessourceAbstract> ressources = new ArrayList<RessourceAbstract>();
		
		/**
		 * Constructeur
		 * @param port port
		 */
		public Server(int port) {
			// Retenir le port
			this.port = port;
		}
		
		/**
		 * Initialiser le serveur HTTP
		 * @return tentative réussie ?
		 */
		public boolean init () {
			try {
				// Créer le serveur HTTP
				this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
				
				// Tentative réussie
				return true;
			} catch (IOException e) {
				// Erreur
				return false;
			}
		}
		
		/**
		 * Ajouter une ressource au serveur HTTP
		 * @param ressource ressource
		 */
		public void addRessource (RessourceAbstract ressource) {
			// Retenir la ressources
			this.ressources.add(ressource);
			
			// Modifier le chemin si nécéssaire
			String chemin = ressource.getCible();
			if (!chemin.substring(0, 1).equals("/")) {
				chemin = "/" + chemin;
			}
			
			// Ajouter le handler
			this.server.createContext(chemin, new Handler(ressource));
			if (ressource.isPrincipale()) {
				this.server.createContext("/", new Handler(ressource));
			}
		}
		
		/**
		 * Démarrer le serveur HTTP
		 */
		public void start () {
			// Démarrer le serveur
			this.server.start();
			
			// Retenir que le serveur est demarré
			this.started = true;
		}
		
		/**
		 * Savoir si le serveur est démarré
		 * @return serveur démarré ?
		 */
		public boolean isStarted () {
			return this.started;
		}
		
		/**
		 * Arrêter le serveur HTTP
		 */
		public void stop () {
			// Vérifier si le serveur est bien demarré
			if (this.started) {
				// Arreter le serveur
				this.server.stop(0);
				
				// Retenir que le serveur n'est plus demarré
				this.started = false;
			}
			
			// Nettoyer toutes les ressources
			for (RessourceAbstract ressource : this.ressources) {
				ressource.clean();
			}
		}
	}
	
	/**
	 * Classe permettant de capturer un client sur le serveur HTTP
	 */
	public static class Handler implements HttpHandler
	{
		/** Ressource */
		private RessourceAbstract ressource;
		
		/**
		 * Constructeur
		 * @param ressource ressource
		 */
		public Handler(RessourceAbstract ressource) {
			// Retenir la ressource
			this.ressource = ressource;
		}
		
		/**
		 * @see HttpHandler#handle(HttpExchange)
		 */
		@Override
		public void handle (HttpExchange exchange) throws IOException {
			// Vérouiller la ressource
			this.ressource.lock();
			
			// Récupérer le flux sur la sortie
			OutputStream output = exchange.getResponseBody();
			
			// Récupérer le fichier de la ressource
			File fichier = this.ressource.getFichier();
			
			if (fichier != null) {
				try {
					// Type de contenu
					Headers headers = exchange.getResponseHeaders();
					headers.add("Content-Type", this.ressource.getContentType());
					
					// Headers HTTP
					exchange.sendResponseHeaders(200, fichier.length());
					
					// Lire le fichier
					byte[] content = new byte[(int)fichier.length()];
					FileInputStream fileInputStream = new FileInputStream(fichier);
					BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
					bufferedInputStream.read(content, 0, content.length);
					fileInputStream.close();
					
					// Ecrire dans le flux de sortie
					output.write(content, 0, content.length);
				} catch (FileNotFoundException exc) {
					// Réponse
					String reponse = "La ressource demandée n'existe pas.";
					
					// Headers HTTP
					exchange.sendResponseHeaders(404, reponse.length());
					
					// Ecrire dans le flux de sortie
					output.write(reponse.getBytes());
				}
			} else {
				// Réponse
				String reponse = "Une erreur s'est produite lors de la génération de la ressource.";
				
				// Headers HTTP
				exchange.sendResponseHeaders(500, reponse.length());
				
				// Ecrire dans le flux de sortie
				output.write(reponse.getBytes());
			}
			
			// Fermer le flux de sortie
			output.close();
			
			// Dévérouiller la ressource
			this.ressource.unlock();
		}
	}
	
	/**
	 * Classe permettant de tester un serveur HTTP
	 */
	public static class Tester extends OperationAbstract
	{
		/** Runnable */
		private OperationRunnableAbstract runnable;
		
		/**
		 * Constructeur
		 * @param port port
		 */
		public Tester(int port) {
			// Créer le runnable
			this.runnable = new Tester.Runnable(port);
		}
		
		/**
		 * Constructeur
		 * @param port port
		 * @param listener listener
		 */
		public Tester(int port, IOperationListener listener) {
			// Créer le runnable
			this.runnable = new Tester.Runnable(port);
			
			// Ajouter le listener
			this.runnable.addListener(listener);
		}
		
		/**
		 * @see OperationAbstract#getRunnable()
		 */
		@Override
		public OperationRunnableAbstract getRunnable () {
			return this.runnable;
		}
		
		/**
		 * Classe qui contient le code de l'opération
		 */
		private static class Runnable extends OperationRunnableAbstract
		{
			/** Fichier de test */
			private File testFile;
			
			/** Serveur HTTP */
			private HTTPHelper.Server server;
			
			/** Serveur HTTP démarré ? */
			private boolean serverStarted = false;
			
			/** Port d'écoute du serveur HTTP */
			private int port;
			
			/**
			 * Constructeur
			 * @param port port
			 */
			public Runnable(int port) {
				// Retenir le port du serveur HTTP
				this.port = port;
			}
			
			/**
			 * @see OperationRunnableAbstract#run()
			 */
			@Override
			public void run () {
				// Créer le serveur HTTP
				this.server = new HTTPHelper.Server(this.port);
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Créer un fichier de test
				this.fireMessage("Tentative de création du fichier de test ...");
				this.testFile = new File("temp/" + this.toString());
				try {
					if (!this.testFile.exists() && !this.testFile.createNewFile()) {
						// Erreur
						this.echec("Erreur lors de la création du fichier de test (vérifiez les droits sur les fichiers).");
						return;
					}
				} catch (IOException e) {
					// Erreur
					this.echec("Erreur lors de la création du fichier de test (vérifiez les droits sur les fichiers).");
					return;
				}
				this.fireAvancement(0.1); // Avancement à 10%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Ecrire dans le fichier de test
				this.fireMessage("Ecriture dans le fichier de test ...");
				try {
					BufferedWriter output = new BufferedWriter(new FileWriter(this.testFile, true));
					output.write("test");
					output.close();
				} catch (IOException e) {
					this.echec("Erreur d'écriture dans le fichier de test (vérifiez les droits sur les fichiers).");
					return;
				}
				this.fireAvancement(0.2); // Avancement à 20%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Initialiser le serveur HTTP
				this.fireMessage("Tentative d'initialisation du serveur HTTP ...");
				if (!this.server.init()) {
					this.echec("Erreur d'initialisation du serveur HTTP (vérifiez si le port n'est pas occupé).");
					return;
				}
				this.fireAvancement(0.25); // Avancement à 25%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Capture de l'adresse
				this.fireMessage("Ajout de la ressource de test au serveur ...");
				this.server.addRessource(new RessourceFichier("/test", false, null, null, this.testFile));
				this.fireMessage("Ressource de test ajoutée");
				this.fireAvancement(0.3); // Avancement à 30%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Démarrer le serveur
				this.fireMessage("Démarrage du serveur HTTP ...");
				this.server.start();
				this.serverStarted = true;
				this.fireMessage("Serveur HTTP démarré.");
				this.fireAvancement(0.4); // Avancement à 40%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Tester le serveur
				this.fireMessage("Test d'envoie d'une requete au serveur HTTP ...");
				HTTPHelper.Browser browser = new HTTPHelper.Browser();
				HttpResponse response = browser.get("http://localhost", this.port, "/test");
				if (response == null) {
					this.echec("Erreur lors de l'envoie de la requete au serveur HTTP.");
					return;
				}
				try {
					if (response.getStatusLine().getStatusCode() != 200 || !EntityUtils.toString(response.getEntity()).equals("test")) {
						this.echec("Le serveur HTTP ne répond pas correctement.");
						return;
					}
				} catch (ParseException e) {
					this.echec("Erreur lors de l'envoie de la requete au serveur HTTP.");
					return;
				} catch (IOException e) {
					this.echec("Erreur lors de l'envoie de la requete au serveur.");
					return;
				}
				this.fireAvancement(0.6); // Avancement à 60%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Arreter le serveur
				this.fireMessage("Arret du serveur HTTP ...");
				this.server.stop();
				this.serverStarted = false;
				this.fireMessage("Serveur HTTP arreté.");
				this.fireAvancement(0.8); // Avancement à 80%
				
				// Supprimer le fichier de test
				this.fireMessage("Suppression de fichier de test ...");
				if (!this.testFile.delete()) {
					this.echec("Erreur lors de la suppression du fichier de test.");
					return;
				}
				this.fireMessage("Fichier de test supprimé.");
				this.fireAvancement(1.0); // Avancement à 100%
				
				// Test réussi
				this.reussite("Test de diffusion réussie !");
			}
			
			// Nettoyer le test en vue de son arret
			protected void clean () {
				// Supprimer le fichier de test si nécéssaire
				if (this.testFile != null && this.testFile.exists()) {
					this.testFile.delete();
				}
				
				// Arreter le serveur HTTP si nécéssaire
				if (this.server != null && this.serverStarted) {
					this.server.stop();
				}
			}
		}
	}
}
