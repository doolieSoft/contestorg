<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Cible XML -->
	<xsl:output method="xml" encoding="utf-8" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<xsl:copy-of select="." />
	</xsl:template>
</xsl:stylesheet>
