package io.github.joaogouveia89.inspirify.data.local

const val GET_FAVORITES_ALL = "SELECT * FROM Favorite"

const val FETCH_QUOTE_LOCALLY = "SELECT * FROM favorite WHERE quote = :quote AND author = :author"

const val DATABASE_NAME = "fipe-db"

const val GET_BRANDS_GET_ALL = "SELECT * FROM Brand"