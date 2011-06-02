<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">

	<!-- Templates pour documents HTML -->
	<xsl:template name="html-equipe-statut">
		<xsl:param name="id" />
		<span class="statut-{//equipe[@id=$id]/@statut}">
			<xsl:choose>
				<xsl:when test="//equipe[@id=$id]/@statut = 'absente'">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Absente
						</xsl:when>
						<xsl:otherwise>
							Absent
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="//equipe[@id=$id]/@statut = 'presente'">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Présente
						</xsl:when>
						<xsl:otherwise>
							Présent
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="//equipe[@id=$id]/@statut = 'homologuee'">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Homologuée
						</xsl:when>
						<xsl:otherwise>
							Homologué
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="//equipe[@id=$id]/@statut = 'forfait'">
					Forfait
				</xsl:when>
				<xsl:when test="//equipe[@id=$id]/@statut = 'disqualifiee'">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Disqualifiée
						</xsl:when>
						<xsl:otherwise>
							Disqualifié
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</span>
	</xsl:template>
	<xsl:template name="html-participation-resultat">
		<xsl:param name="id" />
		<span class="resultat-{//participation[@id = $id]/@resultat}">
			<xsl:choose>
				<xsl:when test="//participation[@id = $id]/@resultat = 'attente'">
					Attente
				</xsl:when>
				<xsl:when test="//participation[@id = $id]/@resultat = 'victoire'">
					Victoire
				</xsl:when>
				<xsl:when test="//participation[@id = $id]/@resultat = 'egalite'">
					Egalité
				</xsl:when>
				<xsl:when test="//participation[@id = $id]/@resultat = 'defaite'">
					Défaite
				</xsl:when>
				<xsl:when test="//participation[@id = $id]/@resultat = 'forfait'">
					Forfait
				</xsl:when>
			</xsl:choose>
			(<xsl:value-of select="//participation[@id = $id]/@points" /> pts)
		</span>
	</xsl:template>
	<xsl:template name="brs">
    	<xsl:param name="text"/>
    	<xsl:variable name="newline"><xsl:text>
</xsl:text></xsl:variable>
	    <xsl:choose>
	      <xsl:when test="contains($text, $newline)">
	        <xsl:value-of select="substring-before( $text, $newline)"/>
	        <br/>
	        <xsl:call-template name="brs">
	          <xsl:with-param name="text" select="substring-after( $text, $newline)"/>
	        </xsl:call-template>
	      </xsl:when>
	      <xsl:otherwise>
	        <xsl:value-of select="$text"/>
	      </xsl:otherwise>
	    </xsl:choose>
  </xsl:template> 
	
	<!-- Templates pour documents PDF -->
	<xsl:template name="pdf-titre-h1">
		<xsl:param name="titre" />
		<fo:block font-size="15pt" space-before="5mm" space-after="2mm" font-weight="bold"><xsl:value-of select="$titre" /></fo:block>
	</xsl:template>
	<xsl:template name="pdf-titre-h2">
		<xsl:param name="titre" />
		<fo:block font-size="12pt" space-before="5mm" space-after="2mm" font-weight="bold"><xsl:value-of select="$titre" /></fo:block>
	</xsl:template>
	<xsl:template name="pdf-titre-h3">
		<xsl:param name="titre" />
		<fo:block font-size="11pt" space-before="3mm" space-after="1mm" font-weight="bold"><xsl:value-of select="$titre" /></fo:block>
	</xsl:template>
	<xsl:template name="pdf-erreur">
		<xsl:param name="erreur" />
		<fo:block space-before="5mm" space-after="5mm" font-weight="bold" color="red"><xsl:value-of select="$erreur" /></fo:block>
	</xsl:template>
	<xsl:template name="pdf-entete">
		<xsl:param name="titre" />
		<fo:block text-align="center" font-weight="bold"><xsl:value-of select="$titre" /></fo:block>
	</xsl:template>
	<xsl:template name="pdf-pied">
		<fo:block>
			<fo:table width="100%">
				<fo:table-column column-width="20%" />
				<fo:table-column column-width="60%" />
				<fo:table-column column-width="20%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block text-align="left">
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block text-align="center">
								Document généré le
								<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('dd/MM/yyyy'),java:java.util.Date.new())" />
								à
								<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('HH:mm:ss'),java:java.util.Date.new())" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block text-align="right">
								Page <fo:page-number />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
	<xsl:template name="pdf-equipe-statut">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="//equipe[@id=$id]/@statut = 'absente'">
				<fo:block color="red">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Absente
						</xsl:when>
						<xsl:otherwise>
							Absent
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</xsl:when>
			<xsl:when test="//equipe[@id=$id]/@statut = 'presente'">
				<fo:block color="orange">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Présente
						</xsl:when>
						<xsl:otherwise>
							Présent
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</xsl:when>
			<xsl:when test="//equipe[@id=$id]/@statut = 'homologuee'">
				<fo:block color="green">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Homologuée
						</xsl:when>
						<xsl:otherwise>
							Homologué
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</xsl:when>
			<xsl:when test="//equipe[@id=$id]/@statut = 'forfait'">
				<fo:block color="black">
					Forfait
				</fo:block>
			</xsl:when>
			<xsl:when test="//equipe[@id=$id]/@statut = 'disqualifiee'">
				<fo:block color="red">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Disqualifiée
						</xsl:when>
						<xsl:otherwise>
							Disqualifié
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="pdf-participation-resultat">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="//participation[@id = $id]/@resultat = 'attente'">
				<fo:inline color="grey">Attente</fo:inline>
			</xsl:when>
			<xsl:when test="//participation[@id = $id]/@resultat = 'victoire'">
				<fo:inline color="green">Victoire</fo:inline>
			</xsl:when>
			<xsl:when test="//participation[@id = $id]/@resultat = 'egalite'">
				<fo:inline color="orange">Egalité</fo:inline>
			</xsl:when>
			<xsl:when test="//participation[@id = $id]/@resultat = 'defaite'">
				<fo:inline color="red">Défaite</fo:inline>
			</xsl:when>
			<xsl:when test="//participation[@id = $id]/@resultat = 'forfait'">
				<fo:inline color="black">Forfait</fo:inline>
			</xsl:when>
		</xsl:choose>
		(<xsl:value-of select="//participation[@id = $id]/@points" /> pts)
	</xsl:template>
	
	<!-- Template pour afficher un contenu N fois -->
	<xsl:template name="loop">
		<xsl:param name="n" />
		<xsl:param name="contenu" />
		<xsl:choose>
			<xsl:when test="$n != 0">
				<xsl:copy-of select="$contenu" />
				<xsl:call-template name="loop">
					<xsl:with-param name="n"><xsl:number value="number($n)-1" /></xsl:with-param>
					<xsl:with-param name="contenu"><xsl:copy-of select="$contenu" /></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>