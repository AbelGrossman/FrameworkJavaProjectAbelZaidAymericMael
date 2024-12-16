# README

```markdown
# 📚 Projet INF2 - Java Framework : Mini-Kahoot en Quarkus 🎮

Bienvenue sur notre projet réalisé dans le cadre de la matière **INF2 - Java Framework** !
Nous avons décidé de relever un défi amusant et technique : développer une application en **Java avec Quarkus**, inspirée du célèbre outil interactif **Kahoot**. 🏆

---

## 1. Objectif du Projet 🎯

L'application vise à fournir une expérience ludique en proposant des **microservices sous forme de challenges et de jeux**.
Voici ce que notre application met à disposition :

### 🛠️ Liste des Microservices
1. **Génération des Questions via IA** 🤖
   Les questions du "Kahoot" sont automatiquement générées grâce à une API d'intelligence artificielle, comme **Google AI**.

2. **Système de Matchmaking** 🔗
   Ce service permet à plusieurs joueurs de rejoindre une même partie en temps réel.

3. **Système de Classement** 🥇
   Un leaderboard dynamique pour motiver les joueurs et afficher les scores.

4. **Gestion de Partie** 🎲
   Gestion du protocole et des règles concernant le déroulement de la session de jeu.

5. **Gestion de Compte Joueur** 👤
   Chaque joueur peut créer, gérer et personnaliser son compte pour participer aux défis.(CRUD)

---

## 🛠️ Stack Technologique

- **Framework** : [Quarkus](<https://quarkus.io/>)
- **Langage** : Java
- **Outils de Build** : Maven
- **Conteneurisation** : Docker *(envisagé)*

---

## 2. Comment Lancer l'Application ⚙️

### **Prérequis**
- Installer Java 17 ou une version supérieure :
  [Guide d'installation pour Java](<https://adoptium.net/>).
- Installer Maven :
  [Guide d'installation pour Maven](<https://maven.apache.org/install.html>).
- Installer Quarkus CLI *(facultatif)* :
  ```bash
  curl -Ls <https://sh.quarkus.io/install.sh> | bash

```

### 2. **Clonez le Dépôt**

```bash
git clone <https://github.com/AbelGrossman/InfinitQuizz>
cd InfinitQuizz

```

### 3. **Lancer en Mode Développement**

Quarkus facilite le développement avec son mode live-coding 🚀. Pour démarrer l'application :

```bash
./mvnw quarkus:dev

```

Votre application sera accessible à l'adresse :

[http://localhost:8080](http://localhost:8080/)

---

## 🐳 [Optionnel] Conteneurisation avec Docker

### 1. **Créer l'Image Docker**

Une fois Docker installé, vous pouvez générer une image de votre application :

```bash
./mvnw package -Dquarkus.container-image.build=true

```

### 2. **Lancer l'Application dans un Conteneur**

```bash
docker run -i --rm -p 8080:8080 votre-image-docker

```

---

## 📄 Licence

Ce projet est sous licence MIT. Consultez le fichier `LICENSE` pour plus de détails.

---

Merci pour votre intérêt et amusez-vous bien en explorant notre projet ! 🚀
