package org.contestorg.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.contestorg.common.OperationAbstract;
import org.contestorg.common.OperationRunnableAbstract;
import org.contestorg.infos.InfosConnexionFTP;
import org.contestorg.interfaces.IOperationListener;

/**
 * Classe d'aide à la réalisation d'opérations FTP
 */
public class FTPHelper
{
	/**
	 * Classe permettant de se connecter à un serveur FTP
	 */
	public static class Client
	{
		/** Client FTP */
		private FTPClient client;
		
		/**
		 * Constructeur
		 */
		public Client() {
			// Initialisation du client FTP
			this.client = new FTPClient();
		}
		
		/**
		 * Connexion au serveur FTP
		 * @param host hôte
		 * @param port port
		 * @return opération réussie ?
		 */
		public boolean connect (String host, int port) {
			// Tentative de connexion au serveur
			try {
				// Se connecter
				this.client.connect(host, port);
				
				// Vérifier si la connexion a réussie
				if (!FTPReply.isPositiveCompletion(this.client.getReplyCode())) {
					// Erreur
					return false;
				}
			} catch (SocketException e) {
				// Erreur
				return false;
			} catch (IOException e) {
				// Erreur
				return false;
			}
			
			// Opération réussie
			return true;
		}
		
		/**
		 * Identification sur le serveur
		 * @param username nom d'utilisateur
		 * @param password mot de passe
		 * @return opération réussie ?
		 */
		public boolean login (String username, String password) {
			// Tentative d'identification sur le serveur
			try {
				// Identification sur le serveur
				if (!this.client.login(username, password)) {
					// Erreur
					return false;
				}
			} catch (IOException e) {
				// Erreur
				return false;
			}
			
			// Opération réussie
			return true;
		}
		
		/**
		 * Utiliser le mode actif
		 */
		public void setActiveMode () {
			this.client.enterLocalActiveMode();
		}
		
		/**
		 * Utiliser le mode passif
		 */
		public void setPassiveMode () {
			this.client.enterLocalPassiveMode();
		}
		
		/**
		 * Tester si le serveur FTP peut répondre à la commande XCRC
		 * @return le serveur FTP peut répondre à la commande XCRC ?
		 * @throws IOException
		 */
		private boolean hasXCRCSupport () throws IOException {
			// Récupérer la liste des commandes possibles
			this.client.sendCommand("feat");
			String response = client.getReplyString();
			
			// Vérifier la présence de la commande XCRC
			Scanner scanner = new Scanner(response);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("XCRC")) {
					return true;
				}
			}
			
			// La commande XCRC n'a pas été trouvée
			return false;
		}
		
		/**
		 * Vérifier la similitude d'un fichier en local avec un fichier distant
		 * @param file fichier local
		 * @param path chemin du fichier distant
		 * @return fichiers similaires ?
		 */
		public boolean isFileExists (File file, String path) {
			try {
				// Vérifier s'il est possible d'effectuer la commande XCRC
				if (this.hasXCRCSupport()) {
					// Récupérer le CRC du fichier local
					String crc = Integer.toHexString((int)FileUtils.checksumCRC32(file)).toUpperCase();
					
					// Récupérer le CRC du fichier distant
					this.client.sendCommand("XCRC "+path);
					String response = this.client.getReplyString().trim();
					
					// Vérifier si les deux CRC correspondent
					if (response.endsWith(crc)) {
						return true;
					}
				}
			} catch (IOException e) { }
			
			// Une erreur s'est produite ou bien les deux CRC ne correspondent pas
			return false;
		}
		
		/**
		 * Envoyer un fichier
		 * @param file fichier local
		 * @param target chemin de destination
		 * @return opération réussie
		 */
		public boolean sendFile (File file, String target) {
			// Tentative d'envoie du fichier
			try {
				// Utiliser le mode binaire
				try { this.client.setFileType(FTPClient.BINARY_FILE_TYPE); } catch (IOException e) { }
				
				// Envoyer le fichier
				FileInputStream input = new FileInputStream(file);
				boolean success = this.client.storeFile(target, input);
				input.close();
				
				// Vérifier si l'envoie du fichier a réussi
				if (!success) {
					// Erreur
					return false;
				}
			} catch (IOException e) {
				// Erreur
				return false;
			}
			
			// Opération réussie
			return true;
		}
		
		/**
		 * Supprimer un fichier
		 * @param path chemin du fichier distant
		 * @return opération réussie ?
		 */
		public boolean deleteFile (String path) {
			// Tentative de suppresion du fichier
			try {
				// Supprimer le fichier
				if (!this.client.deleteFile(path)) {
					// Erreur
					return false;
				}
			} catch (IOException e) {
				// Erreur
				return false;
			}
			
			// Opération réussie
			return true;
		}
		
		/**
		 * Se déconnecter du serveur
		 * @return opération réussie ?
		 */
		public boolean disconnect () {
			// Se déconnecter du serveur
			if (this.client.isConnected()) {
				try {
					// Se déconnecter
					this.client.disconnect();
				} catch (IOException e) {
					// Erreur
					return false;
				}
			}

			// Opération réussie
			return true;
		}
	}
	
	/**
	 * Classe permettant de tester une connexion FTP
	 */
	public static class Tester extends OperationAbstract
	{
		
		/** Opération */
		private OperationRunnableAbstract runnable;
		
		/**
		 * Constructeur
		 * @param infos informations de la connexion FTP
		 */
		public Tester(InfosConnexionFTP infos) {
			// Créer le runnable
			this.runnable = new Tester.Runnable(infos);
		}
		
		/**
		 * Constructeur
		 * @param infos informations de la connexion FTP
		 * @param listener listener
		 */
		public Tester(InfosConnexionFTP infos, IOperationListener listener) {
			// Créer le runnable
			this.runnable = new Tester.Runnable(infos);
			
			// Ajouter le listener
			this.runnable.addListener(listener);
		}
		
		/**
		 * @see OperationRunnableAbstract#getRunnable()
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
			
			/** Client FTP */
			private FTPHelper.Client client;
			
			/** Client FTP connecté ? */
			private boolean clientConnected = false;
			
			/** Informations de connexion FTP */
			private InfosConnexionFTP infos;
			
			/**
			 * Constructeur
			 * @param infos informations de la connexion FTP
			 */
			public Runnable(InfosConnexionFTP infos) {
				// Retenir les informations de connexion FTP
				this.infos = infos;
			}
			
			/**
			 * @see Runnable#run()
			 */
			@Override
			public void run () {
				// Créer le client FTP
				this.fireMessage("Initialisation du client FTP ...");
				this.client = new FTPHelper.Client();
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Se connecter au serveur
				this.fireMessage("Tentative de connexion au serveur FTP ...");
				if (!this.client.connect(this.infos.getHost(), this.infos.getPort())) {
					this.echec("Erreur lors de la connexion au serveur FTP (vérifiez l'hote et le port)");
					return;
				}
				this.clientConnected = true;
				this.fireAvancement(0.2); // Avancement à 20%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// S'identifier sur le serveur
				this.fireMessage("Tentative d'identification sur le serveur FTP ...");
				if (!this.client.login(this.infos.getUsername(), this.infos.getPassword())) {
					this.echec("Erreur d'identification sur le serveur FTP (vérifiez vos identifiants)");
					return;
				}
				this.fireAvancement(0.4); // Avancement à 40%
				
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
				this.fireAvancement(0.5); // Avancement à 50%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Choix du mode passif/actif
				this.fireMessage("Choix du mode passif/actif ...");
				switch (this.infos.getMode()) {
					case InfosConnexionFTP.MODE_ACTIF:
						this.client.setActiveMode();
						break;
					case InfosConnexionFTP.MODE_PASSIF:
						this.client.setPassiveMode();
						break;
				}
				
				// Envoyer le fichier de test sur le serveur FTP
				this.fireMessage("Tentative d'envoie du fichier de test sur le serveur FTP ...");
				if (!this.client.sendFile(this.testFile, this.infos.getPath() + "contestorg-ftp-test")) {
					this.echec("Erreur lors de l'envoie du fichier de test sur le serveur FTP (vérifiez le chemin et les droits d'accès)");
					return;
				}
				testFile.delete(); // Supprimer le fichier de test
				this.fireAvancement(0.7); // Avancement à 70%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Suppression du fichier de test du serveur FTP
				this.fireMessage("Tentative de suppression du fichier de test ...");
				if (!this.client.deleteFile(this.infos.getPath() + "contestorg-ftp-test")) {
					this.echec("Erreur lors de la suppression du fichier de test");
					return;
				}
				this.fireAvancement(8.0); // Avancement à 80%
				
				// Vérifier si le test doit etre annulé
				if (this.arret) {
					this.arret();
					return;
				}
				
				// Se déconnecter du serveur FTP
				this.fireMessage("Deconnexion du serveur FTP ...");
				this.client.disconnect();
				this.clientConnected = false;
				this.fireMessage("Deconnecté du serveur FTP");
				this.fireAvancement(1.0); // Avancement à 100%
				
				// Test réussi
				this.reussite("Test de connexion au serveur FTP réussi !");
			}
			
			/**
			 * @see OperationRunnableAbstract#clean()
			 */
			protected void clean () {
				// Supprimer le fichier de test si nécéssaire
				if (this.testFile != null && this.testFile.exists()) {
					this.testFile.delete();
				}
				
				// Se déconnecter du serveur FTP si nécéssaire
				if (this.client != null && this.clientConnected) {
					this.client.disconnect();
				}
			}
		}
		
	}
}
