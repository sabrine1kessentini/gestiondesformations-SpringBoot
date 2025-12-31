# Guide de Dépannage - Envoi d'Emails

## 🔍 Diagnostic du Problème

### Vérification du Mode Actuel

Lorsque vous inscrivez un étudiant, vérifiez les logs de la console. Vous devriez voir :

**Mode Simulation (si JavaMailSender n'est pas configuré) :**
```
═══════════════════════════════════════════════════════════
📧 EMAIL SIMULÉ - INSCRIPTION
═══════════════════════════════════════════════════════════
À: etudiant@example.com
Sujet: Inscription au cours: ...
...
═══════════════════════════════════════════════════════════
```

**Mode Réel (si JavaMailSender est configuré) :**
```
✅ Email d'inscription envoyé avec succès à: etudiant@example.com
✅ Email de notification envoyé avec succès au formateur: formateur@example.com
```

---

## 🛠️ Solutions

### Solution 1 : Mode Simulation (Développement)

**Par défaut**, si `MAIL_USERNAME` et `MAIL_PASSWORD` ne sont pas configurés, le système fonctionne en **mode simulation**. Les emails sont affichés dans la console.

✅ **Avantages :**
- Pas besoin de configuration
- Fonctionne immédiatement
- Parfait pour le développement

❌ **Inconvénients :**
- Les emails ne sont pas réellement envoyés
- Affichage uniquement dans la console

**Pour vérifier que ça fonctionne :**
1. Inscrivez un étudiant à un cours
2. Regardez la console de l'application
3. Vous devriez voir les messages d'email simulés

---

### Solution 2 : Configuration avec Gmail (Production)

Pour envoyer de **vrais emails**, configurez Gmail :

#### Étape 1 : Créer un mot de passe d'application Gmail

1. Allez sur [Google Account](https://myaccount.google.com/)
2. Activez la **validation en 2 étapes** (obligatoire)
3. Allez dans **Sécurité** → **Mots de passe des applications**
4. Créez un nouveau mot de passe d'application
5. Copiez le mot de passe généré (16 caractères)

#### Étape 2 : Configurer les variables d'environnement

**Windows (CMD) :**
```cmd
set MAIL_USERNAME=votre-email@gmail.com
set MAIL_PASSWORD=votre-mot-de-passe-app
```

**Windows (PowerShell) :**
```powershell
$env:MAIL_USERNAME="votre-email@gmail.com"
$env:MAIL_PASSWORD="votre-mot-de-passe-app"
```

**Linux/Mac :**
```bash
export MAIL_USERNAME=votre-email@gmail.com
export MAIL_PASSWORD=votre-mot-de-passe-app
```

#### Étape 3 : Redémarrer l'application

Après avoir configuré les variables, redémarrez l'application.

#### Étape 4 : Tester

1. Inscrivez un étudiant à un cours
2. Vérifiez les logs : vous devriez voir `✅ Email d'inscription envoyé avec succès`
3. Vérifiez la boîte mail de l'étudiant et du formateur

---

### Solution 3 : Configuration avec Mailtrap (Tests)

**Mailtrap** est un service gratuit pour tester les emails sans envoyer de vrais emails.

#### Étape 1 : Créer un compte Mailtrap

1. Allez sur [Mailtrap.io](https://mailtrap.io/)
2. Créez un compte gratuit
3. Créez une nouvelle inbox
4. Récupérez les identifiants SMTP

#### Étape 2 : Modifier `application.properties`

```properties
spring.mail.host=smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=votre-username-mailtrap
spring.mail.password=votre-password-mailtrap
spring.mail.properties.mail.smtp.auth=true
spring.mail.from=noreply@gestion-formation.com
```

#### Étape 3 : Redémarrer et tester

Les emails seront capturés dans votre inbox Mailtrap au lieu d'être envoyés.

---

## 🔎 Vérification des Logs

### Logs à surveiller

**Si JavaMailSender n'est pas configuré :**
```
WARN  EmailService - JavaMailSender non configuré - Mode simulation activé
```

**Si l'envoi réussit :**
```
INFO  EmailService - ✅ Email d'inscription envoyé avec succès à: etudiant@example.com
```

**Si l'envoi échoue :**
```
ERROR EmailService - ❌ Erreur lors de l'envoi de l'email d'inscription: ...
```

### Activer les logs détaillés

Dans `application.properties`, ajoutez :
```properties
logging.level.com.iit.formation.service.EmailService=DEBUG
logging.level.org.springframework.mail=DEBUG
```

---

## ❌ Erreurs Courantes

### Erreur : "Authentication failed"

**Cause :** Mauvais identifiants ou mot de passe d'application non utilisé

**Solution :**
- Vérifiez que vous utilisez un **mot de passe d'application** (pas votre mot de passe Gmail normal)
- Vérifiez que la validation en 2 étapes est activée
- Vérifiez que `MAIL_USERNAME` et `MAIL_PASSWORD` sont correctement définis

### Erreur : "Connection refused"

**Cause :** Problème de connexion au serveur SMTP

**Solution :**
- Vérifiez que le port est correct (587 pour Gmail)
- Vérifiez votre connexion internet
- Vérifiez que le firewall n'bloque pas le port

### Erreur : "Could not convert socket to TLS"

**Cause :** Problème avec STARTTLS

**Solution :**
- Vérifiez que `spring.mail.properties.mail.smtp.starttls.enable=true` est présent
- Essayez le port 465 avec SSL au lieu de 587 avec STARTTLS

### Aucune erreur mais aucun email reçu

**Cause :** Le mode simulation est actif

**Solution :**
- Vérifiez les logs de la console pour voir les emails simulés
- Configurez `MAIL_USERNAME` et `MAIL_PASSWORD` pour activer l'envoi réel

---

## ✅ Checklist de Vérification

- [ ] Les logs montrent que l'email est tenté d'être envoyé
- [ ] `MAIL_USERNAME` est défini (si vous voulez envoyer de vrais emails)
- [ ] `MAIL_PASSWORD` est défini (si vous voulez envoyer de vrais emails)
- [ ] Le mot de passe d'application Gmail est utilisé (pas le mot de passe normal)
- [ ] La validation en 2 étapes est activée sur Gmail
- [ ] L'application a été redémarrée après la configuration
- [ ] Les logs ne montrent pas d'erreurs d'authentification
- [ ] Les emails apparaissent dans la console (mode simulation) ou sont reçus (mode réel)

---

## 🧪 Test Rapide

Pour tester rapidement si l'email fonctionne :

1. **Démarrez l'application**
2. **Connectez-vous en tant qu'étudiant**
3. **Inscrivez-vous à un cours**
4. **Vérifiez la console** :
   - Mode simulation : vous verrez les emails simulés
   - Mode réel : vous verrez les messages de succès dans les logs

---

## 📝 Notes Importantes

1. **Par défaut, le système fonctionne en mode simulation** - c'est normal et attendu si vous n'avez pas configuré les identifiants email.

2. **Les erreurs d'email ne bloquent pas l'inscription** - l'inscription se fait même si l'email échoue.

3. **Les logs sont maintenant plus détaillés** - vous verrez exactement ce qui se passe.

4. **Pour la production**, configurez toujours un serveur SMTP fiable (Gmail, SendGrid, AWS SES, etc.).

---

## 🔗 Ressources

- [Spring Boot Mail Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/io.html#io.email)
- [Gmail - Créer un mot de passe d'application](https://support.google.com/accounts/answer/185833)
- [Mailtrap - Service de test d'email](https://mailtrap.io/)


