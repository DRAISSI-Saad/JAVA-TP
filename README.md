# 🎯 TP Java Swing - Gestion de Stock

Ce projet est une application de gestion de stock développée en **Java Swing** avec connexion à une base de données **MySQL**.  
Il permet d’ajouter des produits en les associant à une **famille** et une **sous-famille**, avec une interface graphique simple.

---

## 🧩 Fonctionnalités

- 🔍 Chargement dynamique des familles et sous-familles depuis la base de données
- ➕ Insertion d’un nouveau produit (code, désignation, prix)
- 🗂 Association du produit à une famille et une sous-famille
- ❌ Fermeture propre de l'application avec gestion de la connexion

---

## 🛠️ Technologies utilisées

- Java (Swing)
- JDBC
- MySQL
- IntelliJ IDEA
- Git + GitHub

---
## 📷 Aperçu de l'application

> `![Aperçu](./screenshot.png)`


---

# 📊 Structure de la base de données - Gestion de stock

Ce projet utilise une base de données MySQL composée de 3 tables principales :

---

## 🗂️ Tables et relations

### 1. `famille`
- Contient les familles de produits.
- **Relation :** 1 `famille` possède plusieurs `sfamille`.

### 2. `sfamille`
- Contient les sous-familles liées à une famille.
- **Relation :**
    - Chaque `sfamille` est liée à une `famille` (`codeF` → `famille.codeF`)
    - 1 `sfamille` peut avoir plusieurs `produit`.

### 3. `produit`
- Contient les produits du stock.
- **Relation :**
    - Chaque `produit` est lié à une `famille` (`codeF`) et une `sfamille` (`codeS`).

---

## 🔗 Schéma relationnel (simplifié)

famille (1) ───< sfamille (1) ───< produit

---
> - `sfamille.codeF` → `famille.codeF`
> - `produit.codeF` → `famille.codeF`
> - `produit.codeS` → `sfamille.codeS`