package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChecklistItem
import com.example.ui.theme.*

@Composable
fun ChecklistScreen(
    items: List<ChecklistItem>,
    onToggleCheck: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "PRIORITÉS DU MOMENT",
                fontSize = 9.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MutedText,
                letterSpacing = 1.1.sp
            )
            Text(
                text = "Liste de contrôle d’exécution immédiate",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = InkText,
                lineHeight = 19.sp
            )
            Text(
                text = "Cochez les tâches au fur et à mesure pour synchroniser votre avancement.",
                fontSize = 10.5.sp,
                color = MutedText,
                lineHeight = 14.sp
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✓ Vos prochaines actions",
                        fontSize = 11.5.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5C3B24),
                        maxLines = 1
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFEBDCCF)
                    ) {
                        Text(
                            text = "OCTOBRE 2026",
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp),
                            fontSize = 7.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF543722)
                        )
                    }
                }

                HorizontalDivider(color = BorderLine, thickness = 0.5.dp)

                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    items.forEach { item ->
                        ChecklistRow(item = item, onToggle = { onToggleCheck(item.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun ChecklistRow(item: ChecklistItem, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .background(
                if (item.isCompleted) Color(0xFFFAF6F0) else Color.Transparent,
                RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 4.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = SuccessGreen,
                uncheckedColor = GoldPrimary
            ),
            modifier = Modifier
                .size(20.dp)
                .padding(top = 1.dp)
        )
        Text(
            text = item.symbol,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 1.dp)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.dateText.uppercase(),
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5C3B24)
                )
                // Badge
                val (badgeBg, badgeFg) = when (item.badgeType) {
                    "past" -> Color(0xFFE9F3EC) to Color(0xFF1F644B)
                    "today" -> Color(0xFFF6ECdf) to Color(0xFF5C3A21)
                    "imminent" -> Color(0xFFFCE8EB) to Color(0xFF94253A)
                    "future" -> Color(0xFFEAF1F7) to Color(0xFF1C5286)
                    else -> Color(0xFFF5EDE4) to Color(0xFF614631)
                }
                Surface(
                    shape = RoundedCornerShape(99.dp),
                    color = badgeBg
                ) {
                    Text(
                        text = item.badgeText,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeFg
                    )
                }
            }
            Text(
                text = item.title,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (item.isCompleted) MutedText else InkText,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                lineHeight = 15.sp
            )
            Text(
                text = item.description,
                fontSize = 10.sp,
                color = MutedText,
                lineHeight = 13.5.sp
            )
        }
    }
}
