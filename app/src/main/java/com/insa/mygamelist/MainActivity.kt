package com.insa.mygamelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.Games
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {

            MyGamesListTheme {
                Scaffold(topBar = {
                    TopAppBar(colors = topAppBarColors(
                        containerColor = Color.Magenta,
                        titleContentColor = Color.Black,
                    ), title = { Text("My Games List") })
                }, modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding((0.5).dp),
                    ) {
                        itemsIndexed(IGDB.games) { index, game ->
                            AfficherUnJeu(game.id, modifier = Modifier.padding((0.5).dp))
                        }
                    }

                }
            }
        }
    }

    @Composable
    fun AfficherUnJeu(gameId: Long, modifier : Modifier){

        val game = IGDB.games.find { it.id == gameId } ?: return

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https:${IGDB.covers.find { it.id==game.cover }!!.url}",
                    contentDescription = ""+game.name,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = game.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Genres: ${IGDB.getGenreNames(game.genres)}", fontSize = 14.sp, color = Color.Gray)
                    Text(text = "Plateformes: ${IGDB.getPlatformNames(game.platforms)}", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = "Note: ${String.format("%.1f", game.total_rating)}/100",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEBA834)
                    )
                }
            }
        }
    }
}