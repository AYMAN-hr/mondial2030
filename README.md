# 🏆 Mondial 2030 - Plateforme de Gestion des Tickets

Application de gestion de billetterie pour la Coupe du Monde 2030 (Maroc - Espagne - Portugal).

![Java](https://img.shields.io/badge/Java-17+-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17+-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Hibernate](https://img.shields.io/badge/Hibernate-6.2.7-green)
![Maven](https://img.shields.io/badge/Maven-3.8+-red)

## 📋 Description

Cette application permet de gérer la billetterie pour les matchs de la Coupe du Monde 2030. Elle offre des fonctionnalités complètes pour les utilisateurs et les administrateurs.

### Fonctionnalités Utilisateur
- ✅ Inscription et connexion
- ✅ Consultation des matchs disponibles
- ✅ Achat de tickets par match et catégorie
- ✅ Historique des achats (Mes Tickets)
- ✅ Tirage au sort pour gagner des tickets
- ✅ Génération de codes QR

### Fonctionnalités Administrateur
- ✅ Gestion des tickets (CRUD)
- ✅ Gestion des matchs
- ✅ Gestion des utilisateurs
- ✅ Statistiques de vente
- ✅ Export CSV
- ✅ Réinitialisation des données de test

## 🛠️ Technologies

| Technologie | Version | Utilisation |
|-------------|---------|-------------|
| Java | 17+ | Langage principal |
| JavaFX | 17+ | Interface graphique |
| Maven | 3.8.5 | Gestion des dépendances |
| Hibernate | 6.2.7 | ORM |
| MySQL | 8.0 | Base de données |
| Docker | - | Conteneurisation MySQL |

## 📁 Structure du Projet

```
mondial2030/
├── src/main/java/com/mondial/ticket/
│   ├── model/          # Entités (Ticket, Match, User)
│   ├── dao/            # Data Access Objects
│   ├── service/        # Logique métier
│   ├── view/           # Contrôleurs JavaFX
│   └── util/           # Utilitaires
├── src/main/resources/
│   ├── hibernate.cfg.xml
│   └── com/mondial/ticket/
│       ├── model/      # Mappings Hibernate
│       └── view/       # Fichiers FXML
├── docker-compose.yml
└── pom.xml
```

## 🚀 Installation

### Prérequis
- Java JDK 17+
- Maven 3.8+
- Docker (pour MySQL)

### 1. Cloner le repository
```bash
git clone https://github.com/VOTRE_USERNAME/mondial2030.git
cd mondial2030
```

### 2. Lancer la base de données MySQL
```bash
docker-compose up -d
```

### 3. Compiler et exécuter
```bash
mvn clean compile exec:java
```

## 👤 Comptes par Défaut

| Rôle | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| User | user | user123 |

## 📸 Captures d'Écran

### Écran de Connexion
- Page de login avec option d'inscription
- Validation des identifiants

### Interface Tickets
- Liste des tickets par match
- Filtrage par catégorie (VIP, Standard, Tribune)
- Achat en un clic

### Gestion des Matchs
- CRUD complet pour les matchs
- Informations: équipes, stade, ville, pays, phase

## 🗄️ Base de Données

### Table `users`
- id, username, password, role, nom, email

### Table `tickets`
- id, nom_match, categorie, prix, status, acheteur

### Table `matchs`
- id, equipe1, equipe2, stade, ville, pays, phase

## 📝 Documentation

Voir le fichier [COMPTE_RENDU_PROJET.md](COMPTE_RENDU_PROJET.md) pour la documentation complète.

## 🔧 Configuration

### hibernate.cfg.xml
```xml
<property name="connection.url">jdbc:mysql://localhost:3306/mondial2030</property>
<property name="connection.username">root</property>
<property name="connection.password">mondial2030</property>
```

### docker-compose.yml
```yaml
services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: mondial2030
      MYSQL_DATABASE: mondial2030
    ports:
      - "3306:3306"
```

## 📄 Licence

Ce projet est développé dans un cadre éducatif.

## 👥 Auteur

Développé pour le projet Mondial 2030.

---

*Coupe du Monde 2030 - Maroc 🇲🇦 | Espagne 🇪🇸 | Portugal 🇵🇹*

