package com.pisco.stockmanager.presentation.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.pisco.stockmanager.domain.model.BackupData
import com.pisco.stockmanager.presentation.viewmodel.BackupViewModel
import kotlinx.coroutines.launch

@Composable
fun RestoreScreen(

    viewModel: BackupViewModel =
        hiltViewModel()

) {

    val context = LocalContext.current

    val scope =
        rememberCoroutineScope()

    val openFileLauncher =

        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts.OpenDocument()

        ) { uri ->

            if (uri == null)
                return@rememberLauncherForActivityResult

            scope.launch {

                try {

                    val json =

                        context
                            .contentResolver
                            .openInputStream(uri)
                            ?.bufferedReader()
                            ?.use {

                                it.readText()
                            } ?: ""

                    val backup =

                        Gson().fromJson(

                            json,

                            BackupData::class.java
                        )

                    viewModel.restoreBackup(
                        backup
                    )

                    Toast.makeText(

                        context,

                        "Sauvegarde restaurée",

                        Toast.LENGTH_LONG

                    ).show()

                } catch (
                    e: Exception
                ) {

                    Toast.makeText(

                        context,

                        "Erreur : ${e.message}",

                        Toast.LENGTH_LONG

                    ).show()
                }
            }
        }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.Center
    ) {

        Button(

            modifier =
                Modifier.fillMaxWidth(),

            onClick = {

                openFileLauncher.launch(

                    arrayOf(
                        "application/json"
                    )
                )
            }
        ) {

            Text(
                "Choisir une sauvegarde"
            )
        }
    }
}