package com.example.alcoolougasolina.u_interface

import com.example.alcoolougasolina.R
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alcoolougasolina.data.AppDatabase
import com.example.alcoolougasolina.data.PostoRepository
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.res.stringResource

@Composable
fun TelaPostos(
    navController: NavHostController,
    viewModel: PostoViewModel,
    context: Context
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        val postos by viewModel.postos.collectAsStateWithLifecycle()

        if (postos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Adicione algum posto!"
                )
            }
        } else {
            @OptIn(ExperimentalFoundationApi::class)
            LazyColumn(
                modifier = Modifier
            ) {
                stickyHeader {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(64.dp)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.postos),
                            color = MaterialTheme.colorScheme.background,
                            fontWeight = FontWeight.ExtraBold)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.seta_esquerda),
                                modifier = Modifier.size(32.dp)
                                    .clickable {
                                        navController.navigate("telainicial")
                                    },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                }

                items(postos, key = { it.id }) { posto ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp, vertical = 12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .clickable{
                                navController.navigate("detalhesposto/${posto.id}")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 21.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.Top),
                                    text = posto.nomePosto.uppercase(),
                                    color = MaterialTheme.colorScheme.background,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = stringResource(R.string.preco_Alcool),
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    modifier = Modifier,
                                    text = "R$ " + posto.valorAlcool.toString(),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = stringResource(R.string.preco_Gasolina),
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    modifier = Modifier,
                                    text = "R$ " + posto.valorGasolina.toString(),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(56.dp),
                                    shape = RoundedCornerShape(32.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    onClick = {
                                        navController.navigate("editarposto/${posto.id}")
                                    }
                                ) {
                                    Text(text = stringResource(R.string.editar))
                                }
                                Button(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(56.dp),
                                    shape = RoundedCornerShape(32.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    onClick = {
                                        viewModel.deletar(posto)
                                    }
                                ) {
                                    Text(text = stringResource(R.string.excluir))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
