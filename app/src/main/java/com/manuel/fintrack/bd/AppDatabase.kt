package com.manuel.fintrack.bd

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.manuel.fintrack.model.Transaction


@Database(
    entities = [Transaction::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // para formatear la fecha en la base de datos
abstract class AppDatabase : RoomDatabase() { // Clase abstracta que representa la base de datos
    abstract fun transactionDao(): TransactionDao // Función abstracta que devuelve el DAO para la entidad Transaction
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fintrack_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
