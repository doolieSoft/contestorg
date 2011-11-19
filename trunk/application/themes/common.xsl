<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

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
	<xsl:template name="html-nl2br">
    	<xsl:param name="text"/>
    	<xsl:variable name="newline"><xsl:text>
</xsl:text></xsl:variable>
	    <xsl:choose>
	      <xsl:when test="contains($text, $newline)">
	        <xsl:value-of select="substring-before($text, $newline)"/>
	        <br/>
	        <xsl:call-template name="html-nl2br">
	          <xsl:with-param name="text" select="substring-after($text, $newline)"/>
	        </xsl:call-template>
	      </xsl:when>
	      <xsl:otherwise>
	        <xsl:value-of select="$text"/>
	      </xsl:otherwise>
	    </xsl:choose>
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