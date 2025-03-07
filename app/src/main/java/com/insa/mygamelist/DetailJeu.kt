package com.insa.mygamelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.data.Platforms

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailJeu(navController: NavController, gameId: Long,
              favoriteGames : Set<Long>, onToggleFavorite : (Long) -> Unit) {
    val game = IGDB.games.find { it.id == gameId } ?: return
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black),
                title = { Text(game.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // 👈 Retour
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {onToggleFavorite(gameId)}){
                        Icon(
                            imageVector = if (favoriteGames.contains(gameId)) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.Favorite
                            },
                            contentDescription = "Ajouter aux favoris",
                            tint = if (favoriteGames.contains(gameId)){
                                Color.Red
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
                .background(Color(0xFFD3D3D3)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.spacedBy(50.dp)
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = game.name,
                    fontSize = 22.sp,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )

                //Spacer(modifier = Modifier.padding(20.dp)) //inutile quand on utilise une colonne

                AsyncImage(
                    model = "https:${IGDB.covers.find { it.id == game.cover }!!.url}",
                    contentDescription = "" + game.name,
                    contentScale = ContentScale.Crop
                )

                //mettre un détail des genres

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp) // Ajout espace entre les éléments
                ) {
                    items(game.platforms.size) { index ->
                        val platformId = game.platforms[index] // on récupère l'ID
                        val platform = IGDB.platforms.find { it.id == platformId }
                        platform?.let {
                            PlatformAfficher(it)
                        }
                    }
                }

                //Spacer(modifier = Modifier.padding(20.dp))

                Text(
                    text = game.summary,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
    }
}

@Composable
fun PlatformAfficher(platform: Platforms) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        val logoUrl = IGDB.platform_logos.find { it.id == platform.platform_logo }?.url

        Box( // On met l'image dans un conteneur pour éviter qu'elle déborde
            modifier = Modifier
                .size(50.dp), // Taille fixe de l'élément
            contentAlignment = Alignment.Center
        ){
            AsyncImage(
                model = "https://$logoUrl",
                contentDescription = platform.name,
                modifier = Modifier
                    .size(50.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}