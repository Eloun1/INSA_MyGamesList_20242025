package com.insa.mygamelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme
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
        var searchQuery by rememberSaveable { mutableStateOf("") }
        var favoriteGames by remember { mutableStateOf(mutableSetOf<Long>()) } //liste qui retient les favoris

        fun toggleFavorite(gameId: Long) {
            favoriteGames = favoriteGames.toMutableSet().apply {
                if (contains(gameId)) {
                    remove(gameId)
                } else {
                    add(gameId)
                }
            }
        }

        NavHost(navController, startDestination = GameList) {
            composable<GameList> {
                HomeScreen(
                    navController,
                    favoriteGames,
                    ::toggleFavorite
                )
            }
            composable<GameDetail> { backStackEntry ->
                val gameDetail = backStackEntry.toRoute<GameDetail>()
                DetailJeu(navController, gameDetail.gameId, favoriteGames, ::toggleFavorite)
            }
        }
    }

}