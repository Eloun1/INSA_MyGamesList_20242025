package com.insa.mygamelist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavController
import com.insa.mygamelist.data.IGDB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    favoriteGames: Set<Long>, onToggleFavorite: (Long) -> Unit
) {

    var favoritesOnlyActive by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val filteredGames = IGDB.games.filter { game ->
        val isFavorite = favoriteGames.contains(game.id)
        val nameMatch = game.name.contains(searchQuery, ignoreCase = true)
        val genreMatch = game.genres.any { genreId ->
            IGDB.genres.find { it.id == genreId }?.name?.contains(
                searchQuery,
                ignoreCase = true
            ) == true
        }
        val platformMatch = game.platforms.any { platform ->
            IGDB.platforms.find { it.id == platform }?.name?.contains(
                searchQuery,
                ignoreCase = true
            ) == true
        }
        val matchesSearch = nameMatch || genreMatch || platformMatch
        if (favoritesOnlyActive) isFavorite && matchesSearch else matchesSearch
    }

    /* ANCIENNE VERSION
        derivedStateOf {
            IGDB.games.filter { game ->
                game.name.contains(searchQuery, ignoreCase = true) ||
                        IGDB.platforms.any { platform ->
                            platform.name.contains(searchQuery, ignoreCase = true) && game.platforms.contains(platform.id)
                        } ||
                        game.genres.any { genreId ->
                            val genre = IGDB.genres.find { it.id == genreId }
                            genre?.name?.contains(searchQuery, ignoreCase = true) == true
                        }
            }
        }
        */

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
                title = { Text("Ma Liste de Jeux") },
                actions = {
                    IconButton(onClick = { favoritesOnlyActive = !favoritesOnlyActive }){
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Ajouter aux favoris",
                            tint = if (favoritesOnlyActive){
                                Color.Black
                            } else {
                                Color.Gray
                            }
                        )

                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = {searchQuery = it}
            )
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
                        CarteJeu(game.id, navController, favoriteGames, onToggleFavorite)
                    }
                }
            }
        }
    }
}