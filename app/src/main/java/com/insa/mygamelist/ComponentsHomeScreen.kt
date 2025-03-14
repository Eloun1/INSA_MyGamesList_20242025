package com.insa.mygamelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.IGDB

@Composable
fun CarteJeu(gameId: Long, navController: NavController,
             favoriteGames : Set<Long>, onToggleFavorite : (Long) -> Unit,
             gameIds: List<Long> //on ajoute ce paramètre pour pouvoir swipe entre les jeux sélectionnés (favoris, recherche...)
) {
    val game = IGDB.games.find { it.id == gameId } ?: return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate(GameDetail(gameId, gameIds)) }, //permettre le swipe des jeux sélectionnés
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https:${IGDB.covers.find { it.id == game.cover }!!.url}",
                contentDescription = game.name,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column (
                modifier = Modifier.weight(1f) //Starfoullah c'est de la magie --> mettre une
                // taille au pif pour mettre les autres éléments
                //d'abord et qu'ils apparaissent tous sur la carte
            ){
                Text(text = game.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Genres: ${IGDB.getGenreNames(game.genres)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Plateformes: ${IGDB.getPlatformNames(game.platforms)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Note: ${String.format("%.1f", game.total_rating)}/100",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC100)
                )
            }

            IconButton(onClick = {onToggleFavorite(gameId)}){
                Icon(
                    imageVector = if (favoriteGames.contains(gameId)) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.Favorite
                    },
                    contentDescription = "Ajouter aux favoris" ,
                    tint = if (favoriteGames.contains(gameId)) {
                        Color.Red
                    } else {
                        Color.Gray
                    }
                )
            }

        }
    }
}


@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Rechercher...") },
        textStyle = TextStyle(fontSize = 16.sp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Rechercher") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Effacer")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
    )
}
