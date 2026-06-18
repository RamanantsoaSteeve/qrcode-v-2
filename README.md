2. Lancer l'intégralité de l'application (Full Stack)
Cette commande démarre automatiquement la base de données PostgreSQL, le backend Spring Boot et le frontend Angular. Tout le projet communique parfaitement et fonctionne immédiatement.

Bash
docker-compose up --build
Frontend Angular : Disponible sur http://localhost:4200

Backend Spring Boot : Disponible sur http://localhost:8080

3. Lancer uniquement le Backend et la Base de Données (Mode API)
Si tu souhaites travailler sur le code de ton Frontend Angular en local tout en exécutant uniquement l'API et la base de données PostgreSQL dans Docker, utilise la commande suivante :

Bash
docker-compose up api
⚙️ Configuration Requise
Avant de lancer l'application, veille à bien configurer tes accès et variables d'environnement :

Côté Backend (backend/src/main/resources/application.properties) :
Properties
# Configuration de la Base de données PostgreSQL
spring.datasource.url=jdbc:postgresql://db:5432/votre_db
spring.datasource.username=postgres
spring.datasource.password=votre_mot_de_passe

# Configuration SMTP pour l'envoi des codes OTP par Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=votre-email@gmail.com
spring.mail.password=votre-mot-de-passe-application-google

# Propriété JWT
jwt.secret=VotreCleSecreteTresLongueEtSecuriseePourLeTokenJWT
Côté Frontend (frontend/src/environments/environment.ts) :
Ajoute ta configuration du SDK Firebase pour faire fonctionner le Google Login :

TypeScript
export const environment = {
  production: false,
  firebaseConfig: {
    apiKey: "VOTRE_API_KEY",
    authDomain: "VOTRE_AUTH_DOMAIN",
    projectId: "VOTRE_PROJECT_ID",
    storageBucket: "VOTRE_STORAGE_BUCKET",
    messagingSenderId: "VOTRE_MESSAGING_SENDER_ID",
    appId: "VOTRE_APP_ID"
  }
};



# 📦 QR Product Manager & Advanced Authentication System

Une application Full-Stack moderne permettant de gérer des produits, de générer dynamiquement des QR Codes et de sécuriser l'accès via une authentification double niveau (Firebase Google Sign-In + Validation par code OTP Email + JWT).

L'application est entièrement internationalisée et propose une interface dynamique et responsive.

---

## 📸 Aperçu & Captures d'écran

### 🖥️ Application en Production
| Page de Connexion & OTP | Tableau de Bord & Gestion QR |
| :---: | :---: |
| <img src="https://via.placeholder.com/400x250?text=Login+%26+Firebase+Google+Auth" width="400" alt="Login & OTP"/> | <img src="https://via.placeholder.com/400x250?text=Product+Management+%26+QR+Code" width="400" alt="Dashboard"/> |

> *Vous pouvez également glisser-déposer une courte vidéo démo (.mp4 ou .gif) directement dans cette section sur GitHub.*

---

## 🚀 Fonctionnalités

### 🔐 1. Authentification Robuste & Gestion de Session
* **Inscription & Connexion Locale :** Formulaire standard sécurisé par cryptage des mots de passe.
* **Connexion Sociale :** Authentification via Google intégrée de manière transparente avec **Firebase Auth**.
* **Vérification Double Facteur par Email (OTP) :** Envoi automatique d'un code secret par email lors de la connexion. L'accès à l'application reste bloqué tant que le code exact n'est pas fourni.
* **Sécurisation par Token JWT :** Une fois le code vérifié, l'API fournit un token JWT pour sécuriser toutes les requêtes suivantes.
* **Session Interactive :** Gestion fluide des flux d'entrée et de sortie de l'utilisateur (Connexion/Déconnexion en temps réel).

### 🛒 2. Gestion Dynamique des Produits & QR Codes
* **CRUD Complet :** Stockage, modification et suppression des produits en base de données.
* **Génération de QR Code unique :** Création automatique d'un code QR incluant les détails du produit (Nom, Prix, ID) directement depuis l'application.
* **Outils Avancés de Tableaux :** Tri des produits (par nom, prix), filtrage dynamique et suppression rapide.

### 🌐 3. Support Multilingue (Internationalisation)
Intégration de **Transloco** côté Frontend permettant de basculer instantanément entre trois langues :
* 🇫🇷 **Français**
* 🇲🇬 **Malagasy**
* 🇬🇧 **English**

---

## 🛠️ Stack Technique

* **Backend :** Java / Spring Boot, Spring Security, JWT, Java Stream API
* **Frontend :** Angular, Transloco (i18n), TailwindCSS / Bootstrap (Interface interactive)
* **Base de données :** PostgreSQL
* **Services Tiers :** Firebase Auth (Google Sign-In), Java Mail Sender (OTP Email)
* **Conteneurisation :** Docker / Docker Compose

---

## 📦 Déploiement rapide avec Docker

Le projet est entièrement conteneurisé. Assurez-vous d'avoir Docker et Docker Compose installés sur votre machine.

### 1. Cloner le projet
```bash
git clone [https://github.com/votre-utilisateur/votre-repo.git](https://github.com/votre-utilisateur/votre-repo.git)
cd votre-repo
