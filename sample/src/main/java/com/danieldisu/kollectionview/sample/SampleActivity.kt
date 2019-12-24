package com.danieldisu.kollectionview.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danieldisu.kollectionview.KollectionView

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sample)

        val kollectionView: KollectionView<SampleViewModel, SampleItemView> =
            findViewById(R.id.kollectionView)


        kollectionView.configure(
            viewFactory = ::viewFactory,
            viewModelComparisonFunction = ::viewModelComparisonFunction,
            diffInMainThread = UITestUtils.isEspresso()
        )

        kollectionView.update(
            listOf(
                SampleViewModel(
                    id = "id1",
                    name = "name 1",
                    points = "55"
                ),
                SampleViewModel(
                    id = "id2",
                    name = "name 2",
                    points = "35"
                ),
                SampleViewModel(
                    id = "id3",
                    name = "name 3",
                    points = "25"
                ),
                SampleViewModel(
                    id = "id4",
                    name = "name 4",
                    points = "20"
                ),
                SampleViewModel(
                    id = "id5",
                    name = "name 5",
                    points = "18"
                )
            )
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun viewFactory(viewType: Int): SampleItemView = SampleItemView(this)

    private fun viewModelComparisonFunction(left: SampleViewModel, right: SampleViewModel): Boolean =
        left.id == right.id
}
