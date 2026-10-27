package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.ChecklistItem
import java.io.File
import java.io.FileOutputStream

object PdfHelper {
    fun generateAndSharePdf(
        context: Context,
        checklistItems: List<ChecklistItem>
    ) {
        try {
            val pdfDoc = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 at 72dpi
            val page = pdfDoc.startPage(pageInfo)
            val canvas = page.canvas

            val paint = Paint().apply { isAntiAlias = true }

            // Header Banner
            paint.color = Color.rgb(92, 59, 36)
            canvas.drawRect(0f, 0f, 595f, 95f, paint)

            paint.color = Color.rgb(244, 235, 216)
            paint.strokeWidth = 3f
            canvas.drawLine(0f, 95f, 595f, 95f, paint)

            paint.color = Color.WHITE
            paint.textSize = 21f
            paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("MON PARCOURS · FATOU", 36f, 42f, paint)

            paint.color = Color.rgb(240, 222, 201)
            paint.textSize = 11.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("RMA of New York (East Side) · Protocole FIV & En route vers la Grossesse", 36f, 68f, paint)

            var y = 130f

            // Section 1: Étapes vers la Grossesse
            paint.color = Color.rgb(92, 59, 36)
            paint.textSize = 14f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("♡ ÉTAPES CLÉS DU PARCOURS", 36f, y, paint)
            y += 18f

            paint.color = Color.rgb(226, 211, 196)
            paint.strokeWidth = 1f
            canvas.drawLine(36f, y - 5f, 559f, y - 5f, paint)
            y += 12f

            val milestones = listOf(
                "27 oct. 2026" to "Résultats PGT-A (8 blastocystes vitrifiés)",
                "28 oct. 08:00" to "Hystérosonographie SIS (RMA East Side - Écho 3D)",
                "30 oct. 2026" to "Hystéroscopie opératoire (Gramercy Surgery Center)",
                "31 oct. 2026" to "Transfert d'Embryon Congelé FET (Jour de l'Implantation)",
                "08 nov. 2026" to "Prise de sang Bêta-hCG n°1 (Test Officiel Grossesse)",
                "10 nov. 2026" to "Prise de sang Bêta-hCG n°2 (Évolution & Doublage)",
                "22 nov. 2026" to "1ère Échographie de Viabilité (Battement de cœur)"
            )

            paint.textSize = 10.5f
            milestones.forEachIndexed { idx, (date, title) ->
                paint.color = Color.rgb(141, 102, 72)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("${idx + 1}. $date", 40f, y, paint)

                paint.color = Color.rgb(43, 35, 32)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText(title, 140f, y, paint)
                y += 20f
            }

            y += 15f

            // Section 2: Checklist & Préparatifs
            paint.color = Color.rgb(92, 59, 36)
            paint.textSize = 14f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("☑ CHECKLIST & PRÉPARATIFS", 36f, y, paint)
            y += 18f

            paint.color = Color.rgb(226, 211, 196)
            canvas.drawLine(36f, y - 5f, 559f, y - 5f, paint)
            y += 12f

            paint.textSize = 10.5f
            checklistItems.forEach { item ->
                val checkSymbol = if (item.isCompleted) "[✓]" else "[  ]"
                paint.color = if (item.isCompleted) Color.rgb(43, 120, 83) else Color.rgb(110, 96, 88)
                paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
                canvas.drawText(checkSymbol, 40f, y, paint)

                paint.color = Color.rgb(43, 35, 32)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText("${item.title}  (${item.dateText})", 70f, y, paint)
                y += 20f
            }

            y += 15f

            // Section 3: Contacts RMA
            paint.color = Color.rgb(92, 59, 36)
            paint.textSize = 14f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("🏥 CONTACTS MÉDICAUX RMA", 36f, y, paint)
            y += 18f

            paint.color = Color.rgb(226, 211, 196)
            canvas.drawLine(36f, y - 5f, 559f, y - 5f, paint)
            y += 14f

            paint.color = Color.rgb(43, 35, 32)
            paint.textSize = 10.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("• RMA of New York (East Side) : 635 Madison Ave 10th Fl · Tél: 212-756-5777", 40f, y, paint)
            y += 18f
            canvas.drawText("• Gramercy Surgery Center : 380 2nd Ave 10th Fl · Tél: 212-475-8058", 40f, y, paint)
            y += 18f
            canvas.drawText("• Équipe Médicale : Dr. Lucky Sekhon, MD · Infirmière Coordinatrice: Alix", 40f, y, paint)
            y += 24f

            // Footer
            paint.color = Color.rgb(150, 140, 130)
            paint.textSize = 9.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            canvas.drawText("Mon Parcours · Fatou · Document généré pour suivi personnel et médical RMA · Strictement confidentiel", 36f, 800f, paint)

            pdfDoc.finishPage(page)

            // Save PDF into cacheDir
            val file = File(context.cacheDir, "Mon_Parcours_Fatou.pdf")
            val outputStream = FileOutputStream(file)
            pdfDoc.writeTo(outputStream)
            outputStream.flush()
            outputStream.close()
            pdfDoc.close()

            // Share / Save via FileProvider
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Mon Parcours - Dossier Médical Fatou")
                putExtra(Intent.EXTRA_TEXT, "Dossier récapitulatif du protocole FIV de Fatou (RMA of New York).")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Enregistrer / Partager le PDF"))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erreur PDF : ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
