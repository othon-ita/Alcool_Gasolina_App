package com.example.alcoolougasolina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.alcoolougasolina.ui.theme.AlcoolOuGasolinaTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alcoolougasolina.u_interface.TelaPostos
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.alcoolougasolina.data.AppDatabase
import com.example.alcoolougasolina.data.PostoRepository
import com.example.alcoolougasolina.u_interface.PostoViewModel
import com.example.alcoolougasolina.u_interface.PostoViewModelFactory
import com.example.alcoolougasolina.u_interface.TelaInicial
import com.example.alcoolougasolina.u_interface.TelaAdicionarPosto
import androidx.navigation.NavType
import android.content.Intent
import android.net.Uri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcoolOuGasolinaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController: NavHostController = rememberNavController()
                    val context = LocalContext.current
                    val viewModel: PostoViewModel = viewModel(
                        factory = PostoViewModelFactory(
                            PostoRepository(
                                AppDatabase.getDatabase(context).postoDao()
                            )
                        )
                    )

                    NavHost(navController = navController, startDestination = "telainicial") {
                        composable("telainicial") {
                            TelaInicial(navController, viewModel = viewModel, context = context)
                        }
                        composable(
                            "telapostos/{novoTextoAlcool}/{novoTextoGasolina}" +
                                    "/{novoTextoPosto}/{dataFormatada}/{textoEndereco}" +
                                    "/{localizacao}",
                            arguments = listOf(
                                navArgument("novoTextoAlcool") {type = NavType.StringType},
                                navArgument("novoTextoGasolina") {type = NavType.StringType},
                                navArgument("novoTextoPosto") {type = NavType.StringType},
                                navArgument("dataFormatada") {type = NavType.StringType},
                                navArgument("textoEndereco") {type = NavType.StringType},
                                navArgument("localizacao") {type = NavType.StringType}
                            )
                        ) { backStackEntry ->
                            val novoTextoAlcool = backStackEntry
                                .arguments?.getString("novoTextoAlcool") ?: ""
                            val novoTextoGasolina = backStackEntry
                                .arguments?.getString("novoTextoGasolina") ?: ""
                            val novoTextoPosto = backStackEntry
                                .arguments?.getString("novoTextoPosto") ?: ""
                            val dataFormatada = backStackEntry
                                .arguments?.getString("dataFormatada") ?: ""
                            val textoEndereco = backStackEntry
                                .arguments?.getString("textoEndereco") ?: ""
                            val localizacao = backStackEntry
                                .arguments?.getString("localizacao") ?: ""

                            TelaPostos(
                                navController,
                                viewModel = viewModel,
                                context = context,
                                novoTextoAlcool = novoTextoAlcool,
                                novoTextoGasolina = novoTextoGasolina,
                                novoTextoPosto = novoTextoPosto,
                                dataFormatada = dataFormatada,
                                textoEndereco = textoEndereco,
                                localizacao = localizacao
                            )

                        }
                        composable (
                            "telaadicionarposto/{textoAlcool}/{textoGasolina}/{textoPosto}",
                            arguments = listOf(
                                navArgument("textoAlcool") {type = NavType.StringType},
                                navArgument("textoGasolina") {type = NavType.StringType},
                                navArgument("textoPosto") {type = NavType.StringType}
                            )
                        ) { backStackEntry ->
                            val textoAlcool = backStackEntry
                                .arguments?.getString("textoAlcool") ?: ""
                            val textoGasolina = backStackEntry
                                .arguments?.getString("textoGasolina") ?: ""
                            val textoPosto = backStackEntry
                                .arguments?.getString("textoPosto") ?: ""

                            TelaAdicionarPosto(
                                navController,
                                viewModel = viewModel,
                                context = context,
                                textoAlcool = textoAlcool,
                                textoGasolina = textoGasolina,
                                textoPosto = textoPosto
                            )
                        }
                    }
                }
            }
        }
    }
}