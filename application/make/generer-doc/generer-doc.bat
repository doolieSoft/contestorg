@echo OFF

REM Générer la documentation CHM
mkdir temp
cd temp
mkdir images
xcopy /e ..\..\..\doc\images .\images
java -jar ..\..\..\lib\saxon-6.5.5\saxon.jar ..\..\..\doc\documentation.xml ..\docbook-xsl-1.76.1\htmlhelp\htmlhelp.xsl
hhc htmlhelp
copy htmlhelp.chm ..\..\..\doc\documentation.chm
cd ..
rmdir /s /q temp

REM Se rendre dans le répertoire de la documentation
cd ..\..\doc

REM Générer la documentation HTML
java -jar ..\lib\saxon-6.5.5\saxon.jar documentation.xml ..\make\generer-doc\docbook-xsl-1.76.1\html\docbook.xsl > documentation.html

REM Générer la documentation PDF
java -jar ..\lib\saxon-6.5.5\saxon.jar documentation.xml ..\make\generer-doc\docbook-xsl-1.76.1\fo\docbook.xsl > documentation.fo
java -jar ..\lib\fop-1.0\fop-1.0.jar documentation.fo -pdf documentation.pdf
del documentation.fo