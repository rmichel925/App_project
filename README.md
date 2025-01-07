Part of Android Development - ISMIN

Course followed by students of Mines St Etienne, ISMIN - M2 Computer Science.

[![Mines St Etienne](./logo.png)](https://www.mines-stetienne.fr/)

# Consignes du projet

---

## Présentation projet  
**API NestJS** | **App Android**  
**Requêtes HTTP**  

Réalisation d’une API avec NestJS et d’une application Android permettant de visualiser des données OpenData, par exemple info sur des musées, bars, aliments, etc.  

---

### API NestJS 
**Données OpenData**  
- Format JSON  
- Doit contenir :  
  - Un champ correspondant à l’URL d’une image ou un type permettant d’afficher des images différentes dans une liste  
  - Un champ correspondant à une latitude/longitude  
- À charger dans l’API lors du démarrage  

---

## Specs - API NestJS  
- Charger les données OpenData dans l’API Nest lors de son démarrage  
- Exposer des URLs pour faire des requêtes permettant de :  
  - Récupérer un résumé de toutes les données (i.e. seulement les infos les plus importantes pour l’affichage sur une carte + liste + favori ou non).  
    **Exemple :** `GET /beers`  
  - Récupérer le détail d’une donnée (pour l’affichage dans l’écran de détails).  
    **Exemple :** `GET /beers/:beerId`  
  - Mettre une donnée en favori ou non  
    **Exemple :** `PUT /beers/:beerId` (avec un body)  
- Déployé sur CleverCloud  

---

## Bonus - API NestJS  
- Ajouter un endpoint pour créer de nouvelles données  
- Ajouter un endpoint de recherche  
- Gérer la pagination des données sur le endpoint retournant le résumé des données  

---

## App Android  
- Récupération des données de l’API puis affichage sur :  
  - Une carte  
  - Une liste  
  - Un écran avec le détail d’une donnée  
- Possibilité de mettre en favori certains éléments  

---

## Specs - App Android  
- Application composée au minimum de :  
  - **3 Fragments** (la liste + l’écran avec les infos)  
  - **2 Activities**  
- Une **Toolbar** sera présente et permettra de rafraîchir les données récupérées et affichées  

---

## Bonus - App Android  
- **Amélioration de l’expérience utilisateur :**  
  - Mise en place d’un système de recherche/filtre sur la liste affichée  
- **Enrichissements techniques :**  
  - Mise en place d’une base de données locale pour afficher la liste d’éléments en mode hors connexion  
  - Utilisation de LiveData ou d’Observable pour la récupération de données dans la BDD  
