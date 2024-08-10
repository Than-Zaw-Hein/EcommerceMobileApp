package com.tzh.sneakerland.screen.detail.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun AnimatedContainerDropdownBox(
    title: String,
    content: @Composable () -> Unit,
) {

    var expendedDesc by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable {
                expendedDesc = !expendedDesc
            }
    ) {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
                IconButton(
                    onClick = {
                        expendedDesc = !expendedDesc
                    },
                ) {
                    ExposedDropdownMenuDefaults.TrailingIcon(expendedDesc)
                }
            }

            if (expendedDesc) {
                content()
            }
            Spacer(Modifier.height(8.dp))
            Divider()
        }
    }
}
