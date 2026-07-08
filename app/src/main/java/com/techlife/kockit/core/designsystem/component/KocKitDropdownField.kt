package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitFontFamily
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextFieldShape
import java.util.Locale

private val TurkishLocale = Locale.forLanguageTag("tr")

private fun String.matchesDropdownSearch(query: String): Boolean {
    if (query.isBlank()) return true
    return lowercase(TurkishLocale).contains(query.trim().lowercase(TurkishLocale))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KocKitDropdownField(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    searchable: Boolean = false,
    searchPlaceholder: String = "Ara...",
    emptySearchResultText: String = "Sonuç bulunamadı",
    leadingIcon: ImageVector? = null,
    leadingIconTint: Color? = null,
    leadingIconBackground: Color? = null
) {
    val colors = KocKitTheme.extraColors
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val display = selectedOption ?: ""
    val showActiveBorder = isFocused || expanded
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredOptions = remember(options, searchQuery, searchable) {
        if (!searchable || searchQuery.isBlank()) {
            options
        } else {
            options.filter { it.matchesDropdownSearch(searchQuery) }
        }
    }

    fun dismissSheet() {
        expanded = false
        searchQuery = ""
    }

    LaunchedEffect(expanded) {
        if (!expanded) searchQuery = ""
    }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = display,
            onValueChange = {},
            readOnly = true,
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { expanded = true },
            placeholder = { KocKitText(text = label, color = colors.textSecondary) },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    val tint = leadingIconTint ?: colors.textSecondary
                    if (leadingIconBackground != null) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(leadingIconBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = tint,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = tint
                        )
                    }
                }
            },
            trailingIcon = {
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            shape = TextFieldShape,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = KocKitFontFamily),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.cardBackground,
                unfocusedContainerColor = colors.cardBackground,
                focusedBorderColor = colors.pastelGreen,
                unfocusedBorderColor = if (showActiveBorder) colors.pastelGreen else colors.borderLight,
                errorBorderColor = colors.coralAccent
            )
        )

        if (expanded) {
            ModalBottomSheet(
                onDismissRequest = ::dismissSheet,
                sheetState = sheetState,
                containerColor = colors.cardBackground,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    KocKitBoldText(
                        text = label,
                        color = colors.textPrimary,
                        fontSize = KocKitTextDefaults.fontSizeTitle,
                        lineHeight = KocKitTextDefaults.lineHeightTitle
                    )

                    if (searchable) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                KocKitText(
                                    text = searchPlaceholder,
                                    color = colors.textSecondary,
                                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = colors.textSecondary
                                )
                            },
                            singleLine = true,
                            shape = TextFieldShape,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = KocKitFontFamily,
                                color = colors.textPrimary
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = colors.cardBackground,
                                unfocusedContainerColor = colors.cardBackground,
                                focusedBorderColor = colors.pastelGreen,
                                unfocusedBorderColor = colors.borderLight,
                                cursorColor = colors.pastelGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (filteredOptions.isEmpty()) {
                        KocKitText(
                            text = emptySearchResultText,
                            color = colors.textSecondary,
                            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 420.dp)
                        ) {
                            items(
                                items = filteredOptions,
                                key = { it }
                            ) { option ->
                                KocKitDropdownSheetOption(
                                    option = option,
                                    isSelected = option == selectedOption,
                                    onClick = {
                                        onOptionSelected(option)
                                        dismissSheet()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        error?.let {
            KocKitText(
                text = it,
                color = colors.errorAccent,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun KocKitDropdownSheetOption(
    option: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = KocKitTheme.extraColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) {
                    colors.pastelGreen.copy(alpha = 0.12f)
                } else {
                    Color.Transparent
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = colors.pastelGreen
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        KocKitText(
            text = option,
            color = colors.textPrimary,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
            maxLines = 2
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitDropdownFieldPreview() {
    KocKitTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            KocKitDropdownField(
                label = "Ders Seçin",
                options = listOf("Matematik", "Türkçe", "Fizik", "Kimya", "Biyoloji"),
                selectedOption = null,
                onOptionSelected = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            KocKitDropdownField(
                label = "Şehir Seçin",
                options = listOf("İstanbul", "Ankara", "İzmir", "Bursa", "Antalya"),
                selectedOption = "Ankara",
                onOptionSelected = {},
                searchable = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            KocKitDropdownField(
                label = "Hata Durumu",
                options = listOf("Opsiyon 1", "Opsiyon 2"),
                selectedOption = null,
                onOptionSelected = {},
                error = "Lütfen bir seçenek belirleyin"
            )
        }
    }
}
