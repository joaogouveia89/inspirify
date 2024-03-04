package io.github.joaogouveia89.inspirify.data.local

const val GET_FAVORITES_ALL = "SELECT * FROM Favorite ORDER BY id DESC"

const val FETCH_QUOTE_LOCALLY = "SELECT * FROM favorite WHERE quote = :quote AND author = :author"

const val DELETE_FAVORITE_BY_ID = "DELETE FROM favorite WHERE id = :favoriteId"

const val DATABASE_NAME = "inspirify-db"