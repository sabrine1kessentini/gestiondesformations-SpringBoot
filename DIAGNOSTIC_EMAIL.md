# Diagnostic - Emails non envoyés lors de l'inscription

## 🔍 Comment vérifier si les emails fonctionnent

### Étape 1 : Vérifier les logs de la console

Lorsque vous inscrivez un étudiant à un cours, **regardez la console où l'application Spring Boot s'exécute**. Vous devriez voir :

#### Si le mode simulation est actif (normal par défaut) :
```
═══════════════════════════════════════════════════════════
📧 EMAIL SIMULÉ - INSCRIPTION
═══════════════════════════════════════════════════════════
À: etudiant@example.com
Sujet: Inscription au cours: ...
...
═══════════════════════════════════════════════════════════
```

**Si vous voyez ces messages** : ✅ Le système fonctionne en mode simulation (normal si MAIL_USERNAME n'est pas configuré)

**Si vous ne voyez RIEN** : ❌ Il y a un problème - l'email n'est pas appelé

---

### Étape 2 : Vérifier les logs détaillés

Dans la console, cherchez ces lignes :
```
INFO  EmailService - Tentative d'envoi d'email d'inscription
INFO  EmailService - Étudiant: ... (...)
INFO  EmailService - Cours: ... (...)
```

Si vous voyez ces logs : ✅ Le service est appelé
Si vous ne voyez pas ces logs : ❌ Le service n'est pas appelé

---

## 🐛 Problèmes courants et solutions

### Problème 1 : Aucun message dans la console

**Cause possible :** Le service d'email n'est pas appelé

**Solution :**
1. Vérifiez que l'inscription s'est bien passée (l'étudiant apparaît dans "Mes Cours")
2. Vérifiez les logs pour voir si `inscrireEtudiant` est appelé
3. Vérifiez qu'il n'y a pas d'exception silencieuse

### Problème 2 : Messages de simulation visibles mais pas d'email réel

**Cause :** Mode simulation actif (normal si MAIL_USERNAME n'est pas configuré)

**Solution :** Configurez les identifiants email (voir ci-dessous)

### Problème 3 : Erreur "Email de l'étudiant est vide"

**Cause :** L'étudiant n'a pas d'email défini

**Solution :** Vérifiez que l'étudiant a un email valide dans la base de données

---

## ✅ Solution : Activer l'envoi réel d'emails

### Option 1 : Mode Simulation (Développement - Par défaut)

**C'est le mode actuel** - Les emails sont affichés dans la console mais pas envoyés.

**Avantages :**
- Fonctionne immédiatement
- Pas besoin de configuration
- Parfait pour tester

**Pour vérifier :**
1. Inscrivez un étudiant
2. Regardez la console de l'application
3. Vous devriez voir les messages d'email simulés

---

### Option 2 : Envoi réel avec Gmail

#### Configuration :

1. **Créer un mot de passe d'application Gmail :**
   - Allez sur https://myaccount.google.com/
   - Sécurité → Validation en 2 étapes (activez-la)
   - Sécurité → Mots de passe des applications
   - Créez un nouveau mot de passe d'application
   - Copiez le mot de passe (16 caractères)

2. **Configurer les variables d'environnement :**

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

3. **Redémarrer l'application**

4. **Tester :**
   - Inscrivez un étudiant
   - Vérifiez les logs : vous devriez voir `✅ Email d'inscription envoyé avec succès`
   - Vérifiez la boîte mail de l'étudiant

---

## 🔧 Test de diagnostic

Pour tester rapidement :

1. **Démarrez l'application**
2. **Connectez-vous en tant qu'étudiant**
3. **Allez sur "Cours Disponibles"**
4. **Inscrivez-vous à un cours**
5. **Regardez la console de l'application**

**Résultats attendus :**

✅ **Mode simulation (normal) :**
```
═══════════════════════════════════════════════════════════
📧 EMAIL SIMULÉ - INSCRIPTION
═══════════════════════════════════════════════════════════
À: etudiant@example.com
...
═══════════════════════════════════════════════════════════
```

✅ **Mode réel (si configuré) :**
```
INFO  EmailService - ✅ Email d'inscription envoyé avec succès à: etudiant@example.com
```

❌ **Problème :**
- Aucun message dans la console
- Message d'erreur

---

## 📝 Vérifications à faire

- [ ] L'application est démarrée et fonctionne
- [ ] Vous êtes connecté en tant qu'étudiant
- [ ] L'étudiant a un email valide dans son profil
- [ ] Vous regardez la console où l'application s'exécute
- [ ] Les logs montrent que `inscrireEtudiant` est appelé
- [ ] Les logs montrent que `envoyerEmailInscription` est appelé

---

## 💡 Note importante

**Par défaut, le système fonctionne en MODE SIMULATION**. C'est normal et attendu si vous n'avez pas configuré `MAIL_USERNAME` et `MAIL_PASSWORD`.

Les emails sont **affichés dans la console** mais **pas réellement envoyés**. C'est parfait pour le développement.

Pour envoyer de vrais emails, vous devez configurer les identifiants email (voir Option 2 ci-dessus).

