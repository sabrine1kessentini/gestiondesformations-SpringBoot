# Guide pour Générer les Diagrammes UML

## Prérequis

Pour générer les diagrammes UML à partir des fichiers PlantUML, vous avez plusieurs options :

### Option 1 : PlantUML Online (Recommandé pour débuter)

1. Allez sur http://www.plantuml.com/plantuml/uml/
2. Copiez le contenu d'un fichier `.puml`
3. Collez-le dans l'éditeur
4. Le diagramme sera généré automatiquement
5. Exportez en PNG, SVG ou PDF

### Option 2 : Extension VS Code

1. Installez l'extension "PlantUML" dans VS Code
2. Ouvrez un fichier `.puml`
3. Utilisez `Ctrl+Shift+P` → "PlantUML: Export Current Diagram"

### Option 3 : PlantUML Local

1. Téléchargez PlantUML : http://plantuml.com/download
2. Installez Java (requis)
3. Exécutez : `java -jar plantuml.jar diagram.puml`

### Option 4 : IntelliJ IDEA

1. Installez le plugin "PlantUML integration"
2. Ouvrez un fichier `.puml`
3. Clic droit → "PlantUML" → "Generate Diagram"

## Fichiers Disponibles

Les fichiers PlantUML sont dans le dossier `diagrams/` :

- `use-case-diagram.puml` : Diagramme de cas d'utilisation
- `class-diagram.puml` : Diagramme de classes
- `sequence-inscription.puml` : Diagramme de séquence (inscription)
- `component-diagram.puml` : Diagramme de composants

## Personnalisation

Vous pouvez modifier les fichiers `.puml` pour :
- Changer les couleurs (ajouter `skinparam`)
- Modifier le style (thème)
- Ajouter des notes
- Ajuster la disposition

## Exemple de Modification

Pour changer le thème, modifiez la première ligne :
```plantuml
!theme plain  → !theme aws-orange
```

Pour ajouter des couleurs :
```plantuml
skinparam class {
    BackgroundColor LightBlue
    BorderColor DarkBlue
}
```

## Outils Alternatifs

Si vous préférez d'autres outils :

- **Draw.io** : https://app.diagrams.net/ (gratuit, en ligne)
- **Lucidchart** : https://www.lucidchart.com/ (payant)
- **StarUML** : http://staruml.io/ (gratuit)
- **Visual Paradigm** : https://www.visual-paradigm.com/ (payant)

## Intégration dans le Rapport

Une fois les diagrammes générés :

1. Exportez-les en PNG ou SVG (haute résolution)
2. Insérez-les dans votre rapport Word/LaTeX
3. Ajoutez des légendes et descriptions
4. Référencez-les dans le texte

## Conseils

- **Résolution** : Exportez en haute résolution (300 DPI minimum)
- **Format** : PNG pour Word, SVG pour LaTeX/HTML
- **Taille** : Ajustez la taille dans l'outil avant export
- **Légendes** : Ajoutez des légendes claires sous chaque diagramme

