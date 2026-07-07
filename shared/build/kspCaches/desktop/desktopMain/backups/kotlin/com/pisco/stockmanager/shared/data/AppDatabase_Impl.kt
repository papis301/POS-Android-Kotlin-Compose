package com.pisco.stockmanager.shared.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _productDao: Lazy<ProductDao> = lazy {
    ProductDao_Impl(this)
  }

  private val _saleDao: Lazy<SaleDao> = lazy {
    SaleDao_Impl(this)
  }

  private val _saleItemDao: Lazy<SaleItemDao> = lazy {
    SaleItemDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(2, "f6b5c4f8467dba564bb0d73b829e7a3c", "06add175da903cf06b469bd930cb27ed") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `products` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `purchasePrice` REAL NOT NULL, `price` REAL NOT NULL, `quantity` INTEGER NOT NULL, `category` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `active` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sales` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientId` INTEGER NOT NULL, `total` REAL NOT NULL, `createdAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sale_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `saleId` INTEGER NOT NULL, `productId` INTEGER NOT NULL, `productName` TEXT NOT NULL, `quantity` INTEGER NOT NULL, `unitPrice` REAL NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f6b5c4f8467dba564bb0d73b829e7a3c')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `products`")
        connection.execSQL("DROP TABLE IF EXISTS `sales`")
        connection.execSQL("DROP TABLE IF EXISTS `sale_items`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsProducts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsProducts.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProducts.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProducts.put("description", TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProducts.put("purchasePrice", TableInfo.Column("purchasePrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProducts.put("price", TableInfo.Column("price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProducts.put("quantity", TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProducts.put("category", TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProducts.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProducts.put("active", TableInfo.Column("active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysProducts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesProducts: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoProducts: TableInfo = TableInfo("products", _columnsProducts, _foreignKeysProducts, _indicesProducts)
        val _existingProducts: TableInfo = read(connection, "products")
        if (!_infoProducts.equals(_existingProducts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |products(com.pisco.stockmanager.shared.data.ProductEntity).
              | Expected:
              |""".trimMargin() + _infoProducts + """
              |
              | Found:
              |""".trimMargin() + _existingProducts)
        }
        val _columnsSales: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSales.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSales.put("clientId", TableInfo.Column("clientId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSales.put("total", TableInfo.Column("total", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSales.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSales: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSales: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSales: TableInfo = TableInfo("sales", _columnsSales, _foreignKeysSales, _indicesSales)
        val _existingSales: TableInfo = read(connection, "sales")
        if (!_infoSales.equals(_existingSales)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sales(com.pisco.stockmanager.shared.data.SaleEntity).
              | Expected:
              |""".trimMargin() + _infoSales + """
              |
              | Found:
              |""".trimMargin() + _existingSales)
        }
        val _columnsSaleItems: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSaleItems.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSaleItems.put("saleId", TableInfo.Column("saleId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSaleItems.put("productId", TableInfo.Column("productId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSaleItems.put("productName", TableInfo.Column("productName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSaleItems.put("quantity", TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSaleItems.put("unitPrice", TableInfo.Column("unitPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSaleItems: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSaleItems: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSaleItems: TableInfo = TableInfo("sale_items", _columnsSaleItems, _foreignKeysSaleItems, _indicesSaleItems)
        val _existingSaleItems: TableInfo = read(connection, "sale_items")
        if (!_infoSaleItems.equals(_existingSaleItems)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sale_items(com.pisco.stockmanager.shared.data.SaleItemEntity).
              | Expected:
              |""".trimMargin() + _infoSaleItems + """
              |
              | Found:
              |""".trimMargin() + _existingSaleItems)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "products", "sales", "sale_items")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ProductDao::class, ProductDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SaleDao::class, SaleDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SaleItemDao::class, SaleItemDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun productDao(): ProductDao = _productDao.value

  public override fun saleDao(): SaleDao = _saleDao.value

  public override fun saleItemDao(): SaleItemDao = _saleItemDao.value
}
