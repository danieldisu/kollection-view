package com.danieldisu.kollectionview.sample

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.danieldisu.kollectionview.internal.KollectionItemView

class SampleItemView(context: Context) :
    KollectionItemView<SampleViewModel>,
    LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_view_sample, this, true)
    }

    override fun bind(viewModel: SampleViewModel) {
        val nameView = findViewById<TextView>(R.id.nameView)
        val scoreView = findViewById<TextView>(R.id.scoreView)

        nameView.text = viewModel.name
        scoreView.text = viewModel.points
    }

}
