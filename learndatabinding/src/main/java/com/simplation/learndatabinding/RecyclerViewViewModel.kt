package com.simplation.learndatabinding

class RecyclerViewViewModel {
    companion object {
        fun getBooks(): MutableList<Book> {
            val books = mutableListOf<Book>()
            for (i in 0..100) {
                val book = Book("Android 进阶之光 $i", "刘望舒$i", "$i+1", i)
                books.add(book)
            }
            return books
        }
    }
}