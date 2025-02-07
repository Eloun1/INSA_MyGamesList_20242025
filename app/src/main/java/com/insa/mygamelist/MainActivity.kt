package com.insa.mygamelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathSegment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.Games
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


@Serializable
object GameList

@Serializable
data class GameDetail(val gameId : Long)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {

            MyGamesListTheme {
                ApplicationNavigable()
            }
        }
    }


    @Composable
    fun ApplicationNavigable() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = GameList) {
            composable<GameList> {
                HomeScreen(navController)
            }
            composable<GameDetail> {
                backStackEntry ->
                    val gameDetail =
                       backStackEntry.toRoute<GameDetail>()
                    DetailJeu(navController, gameDetail.gameId)
            }
        }
    }

    @Composable
    fun AfficherUnJeu(gameId: Long, navController: NavController) {
        val game = IGDB.games.find { it.id == gameId } ?: return

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { navController.navigate(GameDetail(gameId)) },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https:${IGDB.covers.find { it.id == game.cover }!!.url}",
                    contentDescription = "" + game.name,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
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
                        color = Color(0xFFEBA834)
                    )
                }
            }
        }
    }

    @Composable
    fun HomeScreen(navController: NavController) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = Color.Magenta,
                        titleContentColor = Color.Black,
                    ),
                    title = { Text("My Games List") },
                    navigationIcon = {
                        IconButton(onClick = {finish()}){
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Quitter l'application"
                            )
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding((0.5).dp),
            ) {
                itemsIndexed(IGDB.games) { index, game ->
                    AfficherUnJeu(game.id, navController)
                }
            }
        }
    }


    @Composable
    fun DetailJeu(navController: NavController, gameId: Long) {
        val game = IGDB.games.find { it.id == gameId } ?: return
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = Color.Yellow,
                        titleContentColor = Color.Black),
                    title = { Text(game.name) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) { // 👈 Retour
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Retour"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.spacedBy(50.dp)
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = game.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )

                    Spacer(modifier = Modifier.padding(20.dp))

                    AsyncImage(
                        model = "https:${IGDB.covers.find { it.id == game.cover }!!.url}",
                        contentDescription = "" + game.name,
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.padding(20.dp))
                    //faire une "box" horizontale scrollable sur le coté --> un row

                    Text(
                        text = game.summary,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                }
            }
        }
    }

}