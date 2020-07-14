package com.orionedwards.azsmediabrowser

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(
            viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
            item: Any) {
        val movie = item as Movie

        viewHolder.title.text = movie.showName
        viewHolder.body.text = movie.title
    }
}
