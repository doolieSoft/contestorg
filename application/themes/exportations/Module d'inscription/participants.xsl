<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	<xsl:import href="common.xsl"/>
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<!-- Require du script d'initialisation -->
		<xsl:call-template name="php-start" />
			// Require du script d'initialisation
			require('initialisation.php');
		<xsl:call-template name="php-end" />
		<html>
			<!-- Head -->
			<xsl:call-template name="head" />
			
			<!-- Body -->
			<body>
				<!-- Page -->
				<div id="page">
					<!-- Header -->
					<xsl:call-template name="html-entete" />
					
					<!-- Menu -->
					<xsl:call-template name="menu" />
					
					<!-- Titre de la page -->
					<h2>
						Espace des 
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">équipes</xsl:when>
							<xsl:otherwise>joueurs</xsl:otherwise> 
						</xsl:choose>
					</h2>
					
					<xsl:call-template name="php-start" />							
						// Vérifier si l'utilisateur courant est connecté en tant que participant
						if(isset($_SESSION['participant'])) {
					<xsl:call-template name="php-end" />
					
						<!-- Avancement de l'inscription -->
						<h3>Avancement de votre inscription</h3>
						<div class="bloc">
							<xsl:call-template name="participants-avancement-inscription" />
						</div>
					
					<xsl:call-template name="php-start" />
						} else {
					<xsl:call-template name="php-end" />
					
						<!-- Inscription -->
						<a name="inscription"></a>
						<h3>Inscription</h3>
						<div class="bloc">
							<!-- Formulaire d'inscription -->
							<xsl:call-template name="participants-inscription" />
						</div>
						
						<!-- Connexion -->
						<a name="connexion"></a>
						<h3>Connexion</h3>
						<div class="bloc">
							<!-- Formulaire de connexion -->
							<xsl:call-template name="participants-connexion" />
						</div>
				
					<xsl:call-template name="php-start" />
						}
					<xsl:call-template name="php-end" />
					
					<!-- Footer -->
					<xsl:call-template name="html-pied" />
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>