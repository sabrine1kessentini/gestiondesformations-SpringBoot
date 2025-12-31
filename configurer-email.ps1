# Script PowerShell pour configurer l'envoi d'emails
# Gestion Formation

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Configuration Email - Gestion Formation" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# ============================================
# MODIFIEZ CES VALEURS AVEC VOS INFORMATIONS
# ============================================
$env:MAIL_USERNAME = "votre-email@gmail.com"
$env:MAIL_PASSWORD = "votre-mot-de-passe-app-gmail"

# ============================================
# NE MODIFIEZ PAS LE RESTE
# ============================================

Write-Host "Configuration:" -ForegroundColor Green
Write-Host "  MAIL_USERNAME: $env:MAIL_USERNAME" -ForegroundColor Yellow
Write-Host "  MAIL_PASSWORD: ******** (masque)" -ForegroundColor Yellow
Write-Host ""
Write-Host "Demarrage de l'application..." -ForegroundColor Green
Write-Host ""

# Lancer l'application Spring Boot
mvn spring-boot:run

