package com.manuel.fintrack.bd
import com.manuel.fintrack.model.Transaction
import androidx.room.*
import com.manuel.fintrack.model.TransactionModel
import kotlinx.coroutines.flow.Flow



@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query( "SELECT * FROM  'Transaction'" )
    // flow es un tipo de dato que se actualiza automaticamente cuando cambia la base de datos
     fun getAllTransactions(): Flow<List<TransactionModel>>

    @Query("SELECT * FROM 'Transaction' WHERE id = :id")
    suspend fun getTransactionById(id: Int): List<TransactionModel>

    @Query("DELETE FROM 'Transaction' WHERE id = :idTransaction")
    suspend fun deleteTransaction(idTransaction: Int)

    @Query("SELECT * FROM 'Transaction' WHERE type = :type")
    fun getAllTransactionsByType(type:String):Flow<List<TransactionModel>>


}