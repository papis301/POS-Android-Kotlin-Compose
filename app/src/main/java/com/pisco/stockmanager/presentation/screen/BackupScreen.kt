package com.pisco.stockmanager.presentation.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pisco.stockmanager.presentation.viewmodel.BackupViewModel
import kotlinx.coroutines.launch

@Composable
fun BackupScreen(

    viewModel: BackupViewModel =
        hiltViewModel()

) {

    val context = LocalContext.current

    val scope =
        rememberCoroutineScope()

    val createFileLauncher =

        rememberLauncherForActivityResult(

            ActivityResultContracts
                .CreateDocument(
                    "application/json"
                )

        ) { uri ->

            if (uri == null)
                return@rememberLauncherForActivityResult

            scope.launch {

                val json =
                    viewModel.createBackup()

                context
                    .contentResolver
                    .openOutputStream(uri)
                    ?.use {

                        it.write(
                            json.toByteArray()
                        )
                    }

                Toast.makeText(

                    context,

                    "Sauvegarde créée",

                    Toast.LENGTH_LONG

                ).show()
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(

            modifier =
                Modifier.fillMaxWidth(),

            onClick = {

                createFileLauncher.launch(

                    "stockmanager_backup.json"
                )
            }
        ) {

            Text(
                "Sauvegarder"
            )
        }
    }
}