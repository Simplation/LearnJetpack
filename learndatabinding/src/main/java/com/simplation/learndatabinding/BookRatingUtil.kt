package com.simplation.learndatabinding

class BookRatingUtil {

    companion object {
        fun getRatingString(rating: Int): String {
            return when (rating) {
                0 -> "零星"
                1 -> "一星"
                2 -> "二星"
                3 -> "三星"
                4 -> "四星"
                5 -> "五星"
                else -> ""
            }
        }
    }
}