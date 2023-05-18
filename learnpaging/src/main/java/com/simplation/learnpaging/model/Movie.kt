package com.simplation.learnpaging.model

data class Movie(val id: Int, val title: String, val year: String, val images: Images)


data class Images(val small:String)
