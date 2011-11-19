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
					
					<h2>Accueil</h2>
					
					<div class="bloc">
						Bienvenue sur l'interface d'inscription des
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">équipes</xsl:when>
							<xsl:otherwise>joueurs</xsl:otherwise> 
						</xsl:choose> !
					</div>
					
					<!-- Footer -->
					<xsl:call-template name="html-pied" />
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>