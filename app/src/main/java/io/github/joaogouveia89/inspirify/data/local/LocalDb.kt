package io.github.joaogouveia89.inspirify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.joaogouveia89.inspirify.data.local.daos.FavoriteDao
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite


@Database(
    entities = [
        Favorite::class
    ], version = 1
)
abstract class LocalDb : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}