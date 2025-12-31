@echo off
echo ========================================
echo   Configuration Email - Gestion Formation
echo ========================================
echo.

REM ============================================
REM MODIFIEZ CES VALEURS AVEC VOS INFORMATIONS
REM ============================================
set MAIL_USERNAME=votre-email@gmail.com
set MAIL_PASSWORD=votre-mot-de-passe-app-gmail

REM ============================================
REM NE MODIFIEZ PAS LE RESTE
REM ============================================

echo Configuration:
echo   MAIL_USERNAME: %MAIL_USERNAME%
echo   MAIL_PASSWORD: ******** (masque)
echo.
echo Demarrage de l'application...
echo.

REM Lancer l'application Spring Boot
mvn spring-boot:run

pause

