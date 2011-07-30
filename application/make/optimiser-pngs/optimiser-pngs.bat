REM Optimiser la taille de tous les PNG nécéssitant une optimisation

REM Optimiser les écrans de la documentation
for /f %%a IN ('dir /b ..\..\doc\images\ecrans\*.png') do call optimiser-png.bat ..\..\doc\images\ecrans\ %%a

