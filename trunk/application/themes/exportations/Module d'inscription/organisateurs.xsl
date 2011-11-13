<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	<xsl:import href="common.xsl"/>
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
	
	<!-- Paramètres-->
	<xsl:param name="password" />
  
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
					<xsl:call-template name="html-header" />
					
					<!-- Menu -->
					<xsl:call-template name="menu" />
					
					<h2>Espace des organisateurs</h2>
					
					<xsl:call-template name="php-start" />							
						// Vérifier si l'utilisateur courant est connecté en tant qu'organisateur
						if(isset($_SESSION['organisateur'])) {
					<xsl:call-template name="php-end" />
					
					<!-- Liste des participants -->
					<h3>
						Liste des
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">équipes</xsl:when>
							<xsl:otherwise>joueurs</xsl:otherwise> 
						</xsl:choose>
					</h3>
					<div class="bloc">
						<xsl:call-template name="organisateurs-participants" />
					</div>
					
					<!-- Liste des statuts -->
					<h3>Liste des statuts</h3>
					<div class="bloc">
						<xsl:call-template name="organisateurs-statuts" />
					</div>
					
					<xsl:call-template name="php-start" />
						} else {
					<xsl:call-template name="php-end" />
					
						<!-- Connexion -->
						<h3>Connexion</h3>
						<div class="bloc">
							<xsl:call-template name="organisateurs-connexion">
								<xsl:with-param name="password"><xsl:value-of select="$password" /></xsl:with-param>
							</xsl:call-template>
						</div>
				
					<xsl:call-template name="php-start" />
						}
					<xsl:call-template name="php-end" />
					
					<!-- Footer -->
					<xsl:call-template name="html-footer" />
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>