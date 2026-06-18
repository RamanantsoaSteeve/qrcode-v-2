# 📦 QR Product Manager & Advanced Authentication System

<p align="center">
  <img src="[https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)" alt="Spring Boot" />
  <img src="[https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)" alt="Angular" />
  <img src="[https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)" alt="PostgreSQL" />
  <img src="[https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)" alt="Docker" />
  <img src="[https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)" alt="Firebase" />
  <img src="[https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=F50057](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=F50057)" alt="JWT" />
</p>

Une application **Full-Stack conteneurisée** robuste permettant de gérer un stock de produits, de générer dynamiquement des **QR Codes** uniques et de sécuriser l'accès via un workflow d'authentification double niveau (Firebase Google Sign-In + Validation par code OTP Email + Token JWT).

L'application intègre une gestion **multilingue complète** et propose une interface dynamique, interactive et hautement réactive (In/Out utilisateur en temps réel).

---

## 📸 Aperçu de l'Application

### 🖥️ Production & Démo visuelle
| 🔐 Connexion, Google Auth & Code OTP | 🛒 Gestion des Produits & QR Codes |
| :---: | :---: |
| <img src="[https://via.placeholder.com/420x260?text=Login,+Firebase+%26+Email+Verification](https://via.placeholder.com/420x260?text=Login,+Firebase+%26+Email+Verification)" width="420" alt="Login & OTP Workflow"/> | <img src="[https://via.placeholder.com/420x260?text=Tableau+de+Bord,+Tri+%26+Generation+QR](https://via.placeholder.com/420x260?text=Tableau+de+Bord,+Tri+%26+Generation+QR)" width="420" alt="Product Dashboard"/> |

> 💡 **Astuce de présentation :** Remplace ces images par des captures d'écran réelles ou glisse-dépose directement un fichier `.mp4` ou un `.gif` animé dans cette section sur l'éditeur de GitHub pour afficher une démo vidéo en direct !

---

## 🚀 Fonctionnalités Clés

### 🔐 1. Authentification Avancée & Session Interactive
* **Inscription & Connexion Locale :** Formulaire standard sécurisé avec hachage des mots de passe.
* **Connexion Sociale (Social Login) :** Authentification rapide via Google propulsée par le SDK **Firebase Auth**.
* **Vérification Double Facteur par Email (Code OTP) :** Envoi automatique d'un code secret à l'adresse e-mail de l'utilisateur. L'accès reste restreint tant que le code n'est pas validé.
* **Sécurisation par Token JWT :** Une fois le code vérifié, l'API backend génère un token JWT utilisé pour autoriser de manière sécurisée toutes les requêtes utilisateur.
* **Session Interactive :** Cyber de vie fluide de connexion et déconnexion, bloquant ou autorisant l'accès instantanément.

### 🛒 2. Gestion et Manipulation des Produits
* **Stockage Complet (CRUD) :** Enregistrement complet des produits en base de données avec leur **Nom** et leur **Prix**.
* **Génération Dynamique de QR Code :** Génération instantanée d'un QR code unique propre à chaque produit directement sur l'application.
* **Manipulation Interactive du Tableau :** Fonctionnalités intégrées pour **trier**, **filtrer**, et **effacer** les produits à la volée.

### 🌐 3. Support Multilingue Natif (i18n)
Grâce à l'intégration de **Transloco** côté Frontend, l'utilisateur peut changer instantanément la langue de toute l'application sans recharger la page :
* 🇫🇷 **Français**
* 🇲🇬 **Malagasy**
* 🇬🇧 **Anglais**

---

## 🛠️ Stack Technique

* **Backend :** Java / Spring Boot, Spring Security, JWT, Java Stream API
* **Frontend :** Angular, Transloco (i18n), TailwindCSS / Bootstrap
* **Base de données :** PostgreSQL
* **Services Cloud / Tiers :** Firebase Auth (Google Sign-In), Java Mail Sender (OTP)
* **Conteneurisation & DevOps :** Docker / Docker Compose

---

## 🐳 Déploiement Rapide avec Docker

Le projet intègre une configuration Docker automatisée via `docker-compose.yml`. Tout est orchestré pour fonctionner immédiatement en une seule commande.

### 1️⃣ Cloner le projet
```bash
git clone https://github.com/votre-utilisateur/votre-repo.git
cd votre-repo
