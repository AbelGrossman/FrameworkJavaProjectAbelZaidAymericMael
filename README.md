# README

# 📚 Projet INF2 - Java Framework : Mini-Kahoot en Quarkus 🎮

Bienvenue sur notre projet réalisé dans le cadre de la matière **INF2 - Java Framework** !
Nous avons décidé de relever un défi amusant et technique : développer une application en **Java avec Quarkus**, inspirée du célèbre outil interactif **Kahoot**. 🏆
Le jeu fonctionne par un système de parties thématiques. Chaque partie a un thème unique, et est composée de six joueurs.

## 1. Objectif du Projet 🎯

L'application vise à fournir une expérience ludique en proposant des **microservices sous forme de challenges et de jeux**. L'expèrience utilisateur se déroule uniquement sur une interface web.

Voici ce que notre application met à disposition, dans l'ordre :

### 🛠️ Liste des Microservices

1. **Création de partie**
   Chaque joueur se connecte ou crée un nouveau compte. Il peut ensuite sélectionner un thème et attendre dans une file d'attente jusqu'à ce qu'une partie soit créée.

2. **Système de Matchmaking** 🔗
   Ce microservice récupère les informations des joueurs envoyées par création partie. Il crée des groupe de six joueurs en se basant sur leur rang et le thème.  

3. **Génération des Questions via IA** 🤖
   Les questions du "Kahoot" sont automatiquement générées grâce à une API d'intelligence artificielle, en fonction d'un thème et d'une difficulté.

4. **Déroulé du jeu** 🎲
   Les joueurs rejoignent la partie et répondent aux questions. Un classement en fonction du score et du temps de réponse est affiché à la fin de la partie. 

5. **Statistiques**
   Les statistiques sont mises à jour après chaque partie en fonction des rangs.

---

## 2. 🛠️ Stack Technologique

- **Framework** : [Quarkus](<https://quarkus.io/>)
- **Langages** : Java, HTML, JS, CSS
- **Outils de Build** : Maven

---

## 3. Comment Lancer l'Application ⚙️

### **Prérequis**
- Installer Java 17 ou une version supérieure :
  [Guide d'installation pour Java](<https://adoptium.net/>).
- Installer Maven :
  [Guide d'installation pour Maven](<https://maven.apache.org/install.html>).
- Installer Quarkus CLI :
  ```bash
  curl -Ls <https://sh.quarkus.io/install.sh> | bash

```

### 1. **Clonez le Dépôt**

```bash
git clone <https://github.com/AbelGrossman/InfinitQuizz>
cd InfinitQuizz

```

### 2. **Lancer en Mode Développement**

Quarkus facilite le développement avec son mode live-coding 🚀. Pour démarrer l'application :

```bash
./mvnw quarkus:dev

```

Votre application sera accessible à l'adresse :

[http://localhost:8080](http://localhost:8080/)

Ce lien vous renverra vers la page principale.

--- 

## 4. Diagramme de Séquence

![Diagramme de séquence (1)](https://github.com/user-attachments/assets/af98dd23-39a4-4079-b685-5f296ab17ae3)

---

## 4. Démo


---

## 📄 Licence

Ce projet est sous licence MIT. Consultez le fichier `LICENSE` pour plus de détails.

---

Merci pour votre intérêt et amusez-vous bien en explorant notre projet ! 🚀
