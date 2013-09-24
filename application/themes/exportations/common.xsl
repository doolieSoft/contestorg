<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	<!-- Importation des templates communs -->
	<xsl:import href="../common.xsl"/>
	
	<!-- Paramètres-->
	<xsl:param name="nbPages">#</xsl:param>
	
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
		<fo:block padding-top="15pt">
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
								Page <fo:page-number />/<xsl:value-of select="$nbPages" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
	<xsl:template name="pdf-participant-statut">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="//participant[@id=$id]/@statut = 'absente'">
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
			<xsl:when test="//participant[@id=$id]/@statut = 'presente'">
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
			<xsl:when test="//participant[@id=$id]/@statut = 'homologuee'">
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
			<xsl:when test="//participant[@id=$id]/@statut = 'forfait'">
				<fo:block color="black">
					Forfait
				</fo:block>
			</xsl:when>
			<xsl:when test="//participant[@id=$id]/@statut = 'disqualifiee'">
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
		<xsl:param name="afficherPoints" />
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
		<xsl:if test="$afficherPoints = 1">
			(<xsl:value-of select="//participation[@id = $id]/@points" /> pts)
		</xsl:if>
	</xsl:template>
	
	<!-- Templates pour documents HTML -->
	<xsl:template name="html-entete">
		<div id="header">
			<!-- Barre colorée -->
			<div id="bar">
				<div id="colorA"></div>
				<div id="colorB"></div>
				<div id="colorC"></div>
				<div id="colorD"></div>
				<div id="colorE"></div>
				<div id="colorF"></div>
				<div id="colorG"></div>
				<div id="colorH"></div>
				<div id="colorI"></div>
				<div id="colorJ"></div>
			</div>
			
			<!-- Titre -->
			<h1>
				<xsl:value-of select="/concours/@nom" />
				<xsl:if test="/concours/@lieu != ''">
					(<xsl:value-of select="/concours/@lieu" />)
				</xsl:if>
			</h1>
		</div>
	</xsl:template>
	<xsl:template name="html-pied">
		<div id="footer">
			Page générée le
			<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('dd/MM/yyyy'),java:java.util.Date.new())" />
			à
			<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('HH:mm:ss'),java:java.util.Date.new())" />
			à l'aide du logiciel d'organisation de tournois
			<a href="http://www.elfangels.fr/contestorg/">ContestOrg</a>
		</div>
	</xsl:template>
	
	<!-- Templates pour documents PHP -->
	<xsl:template name="php-start">
		<xsl:text disable-output-escaping="yes">&lt;?php</xsl:text>
	</xsl:template>
	<xsl:template name="php-end">
		<xsl:text disable-output-escaping="yes">?></xsl:text>
	</xsl:template>
	
</xsl:stylesheet>