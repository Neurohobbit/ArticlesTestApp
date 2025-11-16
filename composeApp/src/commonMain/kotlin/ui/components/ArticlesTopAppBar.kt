package ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import articles.composeapp.generated.resources.Res
import articles.composeapp.generated.resources.by_name
import articles.composeapp.generated.resources.from_new_to_old
import articles.composeapp.generated.resources.from_old_to_new
import articles.composeapp.generated.resources.selected
import articles.composeapp.generated.resources.sort
import org.jetbrains.compose.resources.stringResource
import ui.screen.articles.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesTopAppBar(
    title: String,
    selectedSort: SortType,
    onSortClicked: (SortType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    Icons.AutoMirrored.Filled.Sort,
                    contentDescription = stringResource(Res.string.sort)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.by_name)) },
                    trailingIcon = {
                        if (selectedSort == SortType.ByTitle) {
                            SelectedIcon()
                        }
                    },
                    onClick = {
                        expanded = false
                        onSortClicked(SortType.ByTitle)
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.from_new_to_old)) },
                    trailingIcon = {
                        if (selectedSort == SortType.ByDateAsc) {
                            SelectedIcon()
                        }
                    },
                    onClick = {
                        expanded = false
                        onSortClicked(SortType.ByDateAsc)
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.from_old_to_new)) },
                    trailingIcon = {
                        if (selectedSort == SortType.ByDateDesc) {
                            SelectedIcon()
                        }
                    },
                    onClick = {
                        expanded = false
                        onSortClicked(SortType.ByDateDesc)
                    }
                )
            }
        }
    )
}

@Composable
fun SelectedIcon() = Icon(
    imageVector = Icons.Filled.Check,
    contentDescription = stringResource(Res.string.selected)
)