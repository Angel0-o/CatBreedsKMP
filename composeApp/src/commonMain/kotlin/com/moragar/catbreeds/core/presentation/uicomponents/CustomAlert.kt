package com.moragar.catbreeds.core.presentation.uicomponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import catbreeds.composeapp.generated.resources.Res
import catbreeds.composeapp.generated.resources.confirm_button
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomAlert(
    title:String,
    message:String,
    openDialog:Boolean,
    onDismissDialog:()-> Unit,
    modifier: Modifier = Modifier
){
    if (openDialog){
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                onDismissDialog()
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(message)
            },
            confirmButton = {
                Button(onClick = {
                        onDismissDialog()
                    }
                ) {
                    Text(stringResource(Res.string.confirm_button))
                }
            },
            dismissButton = {}
        )
    }
}