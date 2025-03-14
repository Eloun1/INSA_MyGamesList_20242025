package com.insa.mygamelist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.NavController
import com.insa.mygamelist.data.IGDB
import kotlin.system.exitProcess

//fonction pour l'écran d'accueil principal de l'appli : affichage de la liste des jeux

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, favoriteGames: Set<Long>, onToggleFavorite: (Long) -> Unit
) {
    // variable pour gérer l'affichage des favoris par l'appui sur un bouton favori
    var favoritesOnlyActive by rememberSaveable { mutableStateOf(false) }
    // variable pour gérer la barre de recherche
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchActive by rememberSaveable { mutableStateOf(false)}

    // liste pour filtrer les jeux à afficher en fonction
    // de l'état du bouton favori et de l'état de la barre de recherche
    val filteredGames = IGDB.games.filter { game ->
        //variables pour savoir si le jeu considéré est à afficher
        val isFavorite = favoriteGames.contains(game.id)
        val nameMatch = game.name.contains(searchQuery, ignoreCase = true)
        val genreMatch = game.genres.any { genreId ->
            IGDB.genres.find { it.id == genreId }?.name?.contains(searchQuery, ignoreCase = true) == true
        }
        val platformMatch = game.platforms.any { platform ->
            IGDB.platforms.find { it.id == platform }?.name?.contains(searchQuery, ignoreCase = true) == true
        }
        val matchesSearch = nameMatch || genreMatch || platformMatch
        if (favoritesOnlyActive) isFavorite && matchesSearch else matchesSearch
    }

    // composition de l'écran principal
    Scaffold(
        // barre principale : nom de l'appli, croix pour quitter, étoile pour n'afficher que les fav
        topBar = {
            Column(modifier = Modifier.animateContentSize()) { // afficher la barre de recherche de manière dynamique
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black,
                    ),
                    title = { Text("Ma Liste de Jeux") },
                    navigationIcon = {
                        IconButton(onClick = { exitProcess(0) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Quitter l'application",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { favoritesOnlyActive = !favoritesOnlyActive }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Afficher uniquement les favoris",
                                tint = if (favoritesOnlyActive) {
                                    Color.Black
                                } else {
                                    Color.Gray
                                }
                            )
                        }
                        IconButton(onClick = { isSearchActive = !isSearchActive }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Rechercher..."
                            )
                        }
                    }
                )
                if (isSearchActive) {
                    SearchBar(query = searchQuery,
                        onQueryChange = { searchQuery = it })
                }
            }
        }

    ) { //colonne pour afficher les jeux (fonctions CarteJeu dans une lazy colonne)
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (filteredGames.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucun jeu trouvé",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredGames) { game ->
                        CarteJeu(game.id, navController, favoriteGames, onToggleFavorite, filteredGames.map{it.id}) //passe la liste des jeux filtrés en paramètres pour permettre le swipe
                    }
                }
            }
        }
    }
}