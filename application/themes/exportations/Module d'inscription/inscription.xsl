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
		<html>
			<!-- Head -->
			<xsl:call-template name="head" />
			
			<!-- Body -->
			<body>
				<!-- Page -->
				<div id="page">
					<!-- Header -->
					<xsl:call-template name="header" />
					
					<!-- Menu -->
					<xsl:call-template name="menu" />
					
					<h2>Inscription</h2>
					
					<div class="bloc">
						<xsl:call-template name="php-start" />
						
						<xsl:call-template name="php-end" />
					</div>
					
					<!-- Footer -->
					<xsl:call-template name="footer" />
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>