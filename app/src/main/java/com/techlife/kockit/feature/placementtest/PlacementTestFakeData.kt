package com.techlife.kockit.feature.placementtest

import androidx.annotation.DrawableRes
import com.techlife.kockit.R
import com.techlife.kockit.feature.placementtest.PlacementTestFakeData.QUESTION_IMAGE_RES

data class PlacementQuestion(
    @DrawableRes val imageResId: Int = QUESTION_IMAGE_RES,
    val prompt: String = "",
    val subject: String = "Matematik"
)

object PlacementTestFakeData {
    @DrawableRes
    val QUESTION_IMAGE_RES: Int = R.drawable.soru

    fun questionsFor(section: PlacementTestSection): List<PlacementQuestion> {
        if (section == PlacementTestSection.GENERAL_CULTURE) {
            return listOf(
                PlacementQuestion(
                    prompt = "Soru 1",
                    subject = "Tarih"
                ),
                PlacementQuestion(
                    prompt = "Soru 2",
                    subject = "Coğrafya"
                ),
                PlacementQuestion(
                    prompt = "Soru 3",
                    subject = "Fen Bilimleri"
                )
            )
        }
        return listOf(
            PlacementQuestion(
                prompt = "Soru 1",
                subject = "Matematik"
            ),
            PlacementQuestion(
                prompt = "Soru 2",
                subject = "Sayısal Yetenek"
            ),
            PlacementQuestion(
                prompt = "Soru 3",
                subject = "Sözel Yetenek"
            )
        )
    }
}
