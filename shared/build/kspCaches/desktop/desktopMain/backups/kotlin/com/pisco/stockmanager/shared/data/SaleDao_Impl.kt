package com.pisco.stockmanager.shared.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SaleDao_Impl(
  __db: RoomDatabase,
) : SaleDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSaleEntity: EntityInsertAdapter<SaleEntity>

  private val __deleteAdapterOfSaleEntity: EntityDeleteOrUpdateAdapter<SaleEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSaleEntity = object : EntityInsertAdapter<SaleEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `sales` (`id`,`clientId`,`total`,`createdAt`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SaleEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.clientId.toLong())
        statement.bindDouble(3, entity.total)
        statement.bindLong(4, entity.createdAt)
      }
    }
    this.__deleteAdapterOfSaleEntity = object : EntityDeleteOrUpdateAdapter<SaleEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `sales` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SaleEntity) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
  }

  public override suspend fun insert(sale: SaleEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfSaleEntity.insertAndReturnId(_connection, sale)
    _result
  }

  public override suspend fun delete(sale: SaleEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfSaleEntity.handle(_connection, sale)
  }

  public override fun getAllSales(): Flow<List<SaleEntity>> {
    val _sql: String = "SELECT * FROM sales ORDER BY id DESC"
    return createFlow(__db, false, arrayOf("sales")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfClientId: Int = getColumnIndexOrThrow(_stmt, "clientId")
        val _columnIndexOfTotal: Int = getColumnIndexOrThrow(_stmt, "total")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<SaleEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SaleEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpClientId: Int
          _tmpClientId = _stmt.getLong(_columnIndexOfClientId).toInt()
          val _tmpTotal: Double
          _tmpTotal = _stmt.getDouble(_columnIndexOfTotal)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = SaleEntity(_tmpId,_tmpClientId,_tmpTotal,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
