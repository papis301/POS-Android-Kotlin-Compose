package com.pisco.stockmanager.shared.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SaleItemDao_Impl(
  __db: RoomDatabase,
) : SaleItemDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSaleItemEntity: EntityInsertAdapter<SaleItemEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSaleItemEntity = object : EntityInsertAdapter<SaleItemEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `sale_items` (`id`,`saleId`,`productId`,`productName`,`quantity`,`unitPrice`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SaleItemEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.saleId.toLong())
        statement.bindLong(3, entity.productId.toLong())
        statement.bindText(4, entity.productName)
        statement.bindLong(5, entity.quantity.toLong())
        statement.bindDouble(6, entity.unitPrice)
      }
    }
  }

  public override suspend fun insert(saleItem: SaleItemEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSaleItemEntity.insert(_connection, saleItem)
  }

  public override suspend fun getItemsForSale(saleId: Int): List<SaleItemEntity> {
    val _sql: String = "SELECT * FROM sale_items WHERE saleId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, saleId.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSaleId: Int = getColumnIndexOrThrow(_stmt, "saleId")
        val _columnIndexOfProductId: Int = getColumnIndexOrThrow(_stmt, "productId")
        val _columnIndexOfProductName: Int = getColumnIndexOrThrow(_stmt, "productName")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _columnIndexOfUnitPrice: Int = getColumnIndexOrThrow(_stmt, "unitPrice")
        val _result: MutableList<SaleItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SaleItemEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpSaleId: Int
          _tmpSaleId = _stmt.getLong(_columnIndexOfSaleId).toInt()
          val _tmpProductId: Int
          _tmpProductId = _stmt.getLong(_columnIndexOfProductId).toInt()
          val _tmpProductName: String
          _tmpProductName = _stmt.getText(_columnIndexOfProductName)
          val _tmpQuantity: Int
          _tmpQuantity = _stmt.getLong(_columnIndexOfQuantity).toInt()
          val _tmpUnitPrice: Double
          _tmpUnitPrice = _stmt.getDouble(_columnIndexOfUnitPrice)
          _item = SaleItemEntity(_tmpId,_tmpSaleId,_tmpProductId,_tmpProductName,_tmpQuantity,_tmpUnitPrice)
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
