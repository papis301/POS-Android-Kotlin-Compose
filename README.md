# 📦 StockManager

StockManager est une application Android de gestion de stock et de caisse (POS) conçue pour aider les petits commerces à gérer leurs produits, leurs ventes et leur inventaire depuis un téléphone mobile.

## ✨ Fonctionnalités

### 🛒 Caisse (POS)

* Vente rapide de produits
* Ajout automatique au panier
* Calcul du total
* Validation des ventes
* Interface optimisée portrait et paysage
* Panier intégré (Bottom Sheet)

### 📦 Gestion des produits

* Ajouter un produit
* Modifier un produit
* Supprimer un produit
* Réapprovisionnement du stock
* Gestion des catégories
* Affichage du stock disponible
* Blocage des ventes lorsque le stock atteint 0

### 📊 Historique

* Historique des ventes
* Consultation des détails d’une facture

### 💾 Sauvegarde & restauration

* Export des données au format JSON
* Sauvegarde locale sur le téléphone
* Restauration des données

## 📱 Captures d’écran

*(Ajouter ici les captures d’écran de l’application)*

## 🛠️ Technologies utilisées

* Kotlin
* Jetpack Compose
* Room Database
* Hilt
* Navigation Compose
* Material 3
* Gson

## 🚀 Installation

Cloner le projet :

```bash
git clone https://github.com/papis301/StockManager.git
```

Ouvrir avec Android Studio :

```bash
File → Open → StockManager
```

Lancer :

```bash
Run ▶
```

## Architecture

```text
presentation/
 ├── screen/
 ├── viewmodel/

domain/
 ├── model/

data/
 ├── repository/
 ├── local/
```

## Configuration minimale

* Android 8.0 (API 26)
* RAM recommandée : 2 Go+
* Stockage : 100 Mo+

## Feuille de route

* [x] Gestion des produits
* [x] Gestion du stock
* [x] Historique des ventes
* [x] Sauvegarde / restauration
* [ ] Images produits
* [ ] Impression facture
* [ ] Synchronisation cloud
* [ ] Multi-utilisateurs
* [ ] Tableau de bord avancé

## Contribution

Les contributions sont les bienvenues.

1. Fork
2. Créer une branche
3. Commit
4. Push
5. Pull Request

## Licence

Projet distribué sous licence MIT.

---

Développé avec ❤️ par Pisco
