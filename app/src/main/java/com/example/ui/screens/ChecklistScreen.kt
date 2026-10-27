package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "PRIORITÉS DU MOMENT",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MutedText,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Liste de contrôle d’exécution immédiate",
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                color = InkText
            )
            Text(
                text = "Cochez les tâches au fur et à mesure.",
                fontSize = 12.sp,
                color = MutedText
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✓ Vos prochaines actions",
                        fontSize = 12.5.sp,
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
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF543722)
                        )
                    }
                }

                Divider(color = BorderLine, thickness = 0.5.dp)

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
            .background(
                if (item.isCompleted) Color(0xFFFAF6F0) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(checkedColor = GoldPrimary)
        )
        Text(text = item.symbol, fontSize = 20.sp, modifier = Modifier.padding(top = 2.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.dateText.uppercase(),
                    fontSize = 10.sp,
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
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeFg
                    )
                }
            }
            Text(
                text = item.title,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (item.isCompleted) MutedText else InkText,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
            Text(
                text = item.description,
                fontSize = 11.5.sp,
                color = MutedText,
                lineHeight = 16.sp
            )
        }
    }
}
