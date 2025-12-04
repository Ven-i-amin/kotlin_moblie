package ru.vsu.task1.ui.composables.generic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview

data class RadioButton(
    val onClick: () -> Unit,
    val selectableComposable: @Composable (Boolean) -> Unit,
)

@Composable
fun RadioButtonRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    contents: List<RadioButton>
) {
    var selectedComposable by remember { mutableIntStateOf(0) }

    Row(
        modifier = modifier.selectableGroup(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        for (i in contents.map { it.selectableComposable }.indices) {
            val selected = i == selectedComposable

            Box(
                modifier = Modifier
                    .selectable(
                        selected = selected,
                        onClick = {
                            contents.elementAt(i).onClick()
                            selectedComposable = i
                        },
                        role = Role.RadioButton
                    ),
                contentAlignment = Alignment.Center,
            ) {
                contents.elementAt(i).selectableComposable(selected)
            }
        }
    }
}

@Preview
@Composable
fun RadioButtonPreview() {
//    val buttons = listOf("1H", "1D", "1W", "1M", "1Y", "ALL").map { text ->
//        @Composable
//        { selected:Boolean ->
//            Box(modifier = Modifier
//                .background(if (selected) defaultScheme.primary else Color.Transparent)
//            ) {
//                Text(
//                    modifier = Modifier.padding(10.dp),
//                    text = text,
//                    style = AppTypography.bodySmall
//                )
//            }
//        }
//    }
//
//    RadioButtonRow(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically, ,
//        contents = buttons
//    )
}