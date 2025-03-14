    DESCRIPTION :
Cette application est une application mobile développée en Kotlin avec le module Jetpack Compose.
Elle permet à l'utilisateur de visionner une liste de jeux vidéos issue d'une base de données fournie au début du projet, d'en afficher les détails et de les marquer comme favoris. 


    FONCTIONNALITES :
  Fonctionnalités obligatoires : 
- Affichage d'une liste de jeux vidéos
- Filtrage des jeux par nom, genre et plateforme
- Marquage et démarquage en favori des jeux
- Affichage des détails de chaque jeu
- Navigation entre écran principal et écran de détail des jeux
  Fonctionnalités bonus : 
- Swipe de jeu en jeu suivant la liste des jeux filtrés sur l'écran d'accueil. Par exemple, si le mode "favori" est activé, on ne pourra swipe que de jeu favori en jeu favori (marche aussi avec l'état de la barre de recherche...).
- malgré la fermeture de l’app, les favoris persistent par l'utilisation de SharedPreferences --> pas réussi


    ARCHITECTURE :
Le projet est composé de plusieurs fichiers :
- MainActivity.kt :
    Classe principale qui lance l'application et gère la navigation entre écrans.
    Elle contient également les variables des listes des jeux favoris (et la fonction pour les ajouter/enlever).
- HomeScreen.kt :
    Ecran principal affichant la liste des jeux dans une lazyColumn.
    Elle contient une barre de navigation depuis laquelle il est possible de quitter l'application, d'afficher la barre de recherche pour filtrer les jeux, et de filtrer les jeux par favoris.
- ComponentsHomeScreen.kt :
    Définition des composants utilisés dans l'écran principal :
      - CarteJeu : affichage d'une carte avec l'icône, le nom, les genres, les plateformes et la note d'un jeu. En cliquant sur l'icône coeur, le jeu est ajouté/enlevé à la liste des favoris.
      - SearchBar : barre de recherche interactive qui s'actualise en direct pour filtrer les jeux
- DetailJeu.kt :
    Ecran qui affiche les détails d'un jeu : nom, icône, genres, icônes des plateformes et description, ainsi que son statut de favori ou non.
    Il est également possible d'ajouter/enlever le  des favoris depuis cet écran tout en conservant la cohérence avec la carte de détail des jeux.
- IGDB.kt :
    Gestion de la base de données et stockage des différentes informations des jeux.

