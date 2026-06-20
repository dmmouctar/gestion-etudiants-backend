# 🎓 Gestion des Étudiants — Backend (Spring Boot)


## 📋 Sommaire

- Technologies utilisées
- Prérequis
- Installation
- Lancer le projet
- Tester l'API
- Structure du projet
- Compte administrateur par défaut

---

## 🛠 Technologies utilisées

Technologie

1. Java 17 : Langage de programmation
2. Spring Boot 3.5.1 : Framework backend
3. Spring Security + JWT : Authentification et sécurité
4. Spring Data JPA / Hibernate : Accès à la base de données
5. MySQL 8 : Base de données
6. Maven : Gestionnaire de dépendances
7. Lombok : Réduction du code répétitif

---

## ✅ Prérequis

Avant de commencer, installez ces logiciels sur votre ordinateur :

1. Java 17 (JDK)
2. MySQL 8
3. Maven
4. IntelliJ IDEA
5. Git


## 📥 Installation pas à pas

### 1. Cloner le projet

Ouvrez un terminal à l'endroit où vous voulez stocker le projet, puis :

```bash
git clone https://github.com/dmmouctar/gestion-etudiants-backend.git
cd gestion-etudiants-backend
```

### 2. Créer la base de données MySQL

1. Démarrez MySQL (via XAMPP ou WAMP)
2. Ouvrez **phpMyAdmin** (`http://localhost/phpmyadmin`)
3. Cliquez sur **Importer** dans le menu du haut
4. Sélectionnez le fichier `database/gestion_etudiant.sql` (fourni dans le dépôt [gestion-etudiants-database](https://github.com/dmmouctar/gestion-etudiants-database))
5. Cliquez sur **Exécuter**

La base `gestion_etudiant` et ses 13 tables sont maintenant créées.

### 3. Ouvrir le projet dans IntelliJ

1. Lancez IntelliJ IDEA
2. **File → Open** → sélectionnez le dossier `gestion-etudiants-backend`
3. Attendez qu'IntelliJ télécharge automatiquement toutes les dépendances Maven

### 4. Configurer la connexion à la base de données

Le fichier `src/main/resources/application.properties` utilise déjà des valeurs par défaut qui fonctionnent pour une installation locale standard (`root` sans mot de passe). Si votre configuration MySQL est différente, modifiez ces lignes :

```properties
spring.datasource.username=root
spring.datasource.password=
```

---

## ▶️ Lancer le projet

1. Assurez-vous que **MySQL est démarré** (XAMPP/WAMP)
2. Dans IntelliJ, ouvrez `GestionEtudiantsApplication.java`
3. Cliquez sur le bouton ▶️ vert (ou `Shift + F10`)

Si tout fonctionne, vous verrez dans la console :

```
✅ Compte admin créé : admin@school.ma / Admin@1234
Tomcat started on port 8080
Started GestionEtudiantsApplication
```

L'API est maintenant accessible sur **http://localhost:8080**

---

## 🧪 Tester l'API

Utilisez [Postman](https://www.postman.com/downloads/) pour tester :

**Connexion (login) :**
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@school.ma",
  "password": "Admin@1234"
}
```

Vous recevrez un token JWT à utiliser dans l'en-tête `Authorization: Bearer VOTRE_TOKEN` pour toutes les autres requêtes.

---

## 📁 Structure du projet

```
src/main/java/com/school/gestion_etudiants/
├── entity/          → Les tables de la base de données (User, Etudiant, Bulletin...)
├── repository/      → Accès aux données (Spring Data JPA)
├── dto/             → Objets d'échange avec le frontend (request/response)
├── mapper/          → Conversion entités ↔ DTOs
├── service/         → Logique métier (calculs de moyennes, etc.)
├── security/        → Authentification JWT
├── controller/      → Points d'entrée de l'API REST
├── config/          → Configuration Spring Security, CORS, données initiales
└── exception/       → Gestion centralisée des erreurs
```

---

## 🔑 Compte administrateur par défaut

Créé automatiquement au premier démarrage :

1. Email  : admin@school.ma
2. Mot de passe : Admin@1234


## 🔗 Projets liés

- Frontend React : [gestion-etudiants-frontend](https://github.com/dmmouctar/gestion-etudiants-frontend)
- Base de données : [gestion-etudiants-database](https://github.com/dmmouctar/gestion-etudiants-database)

---

## 👤 Auteur

**Mamadou Mouctar Diallo**
