package ru.vsu.task1.composables.generic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.task1.u1.theme.AppTypography
import kotlin.math.max

@Composable
fun RadioButtonRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    onClick: (Int) -> Unit,
    contents: List<@Composable (Boolean) -> Unit>
) {
    val selectedComposable = remember { mutableIntStateOf(0) }

    Row(
        modifier = modifier.selectableGroup(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        for (i in contents.indices) {
            val selected = i == selectedComposable.intValue

            Box(
                modifier = Modifier
                    .selectable(
                        selected = selected,
                        onClick = {
                            selectedComposable.intValue = i
                            onClick.invoke(selectedComposable.intValue)
                        },
                        role = Role.RadioButton
                    ),
                contentAlignment = Alignment.Center,
            ) {
                contents[i](selected)
            }
        }
    }
}

@Preview
@Composable
fun RadioButtonPreview() {
    val buttons = listOf("1H", "1D", "1W", "1M", "1Y", "ALL").map { text ->
        val function: @Composable (Boolean) -> Unit = { selected ->
            SelectableButton(selected = selected,
                onClick = { /*TODO*/ },
                contentPadding = PaddingValues(0.dp)) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = text,
                    style = AppTypography.bodySmall
                )
            }

        }
        function
    }

    RadioButtonRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        onClick = { /*TODO*/ },
        contents = buttons
    )
}