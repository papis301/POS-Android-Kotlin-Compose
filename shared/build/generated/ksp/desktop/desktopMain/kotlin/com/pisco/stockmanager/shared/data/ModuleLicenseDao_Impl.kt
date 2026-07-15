package com.pisco.stockmanager.shared.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ModuleLicenseDao_Impl(
  __db: RoomDatabase,
) : ModuleLicenseDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfModuleLicenseEntity: EntityInsertAdapter<ModuleLicenseEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfModuleLicenseEntity = object : EntityInsertAdapter<ModuleLicenseEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `module_licenses` (`moduleId`,`isActivated`,`activationDate`,`expirationDate`,`lastCheckedTimestamp`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ModuleLicenseEntity) {
        statement.bindText(1, entity.moduleId)
        val _tmp: Int = if (entity.isActivated) 1 else 0
        statement.bindLong(2, _tmp.toLong())
        statement.bindLong(3, entity.activationDate)
        statement.bindLong(4, entity.expirationDate)
        statement.bindLong(5, entity.lastCheckedTimestamp)
      }
    }
  }

  public override suspend fun insertOrUpdate(license: ModuleLicenseEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfModuleLicenseEntity.insert(_connection, license)
  }

  public override suspend fun getLicense(moduleId: String): ModuleLicenseEntity? {
    val _sql: String = "SELECT * FROM module_licenses WHERE moduleId = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, moduleId)
        val _columnIndexOfModuleId: Int = getColumnIndexOrThrow(_stmt, "moduleId")
        val _columnIndexOfIsActivated: Int = getColumnIndexOrThrow(_stmt, "isActivated")
        val _columnIndexOfActivationDate: Int = getColumnIndexOrThrow(_stmt, "activationDate")
        val _columnIndexOfExpirationDate: Int = getColumnIndexOrThrow(_stmt, "expirationDate")
        val _columnIndexOfLastCheckedTimestamp: Int = getColumnIndexOrThrow(_stmt, "lastCheckedTimestamp")
        val _result: ModuleLicenseEntity?
        if (_stmt.step()) {
          val _tmpModuleId: String
          _tmpModuleId = _stmt.getText(_columnIndexOfModuleId)
          val _tmpIsActivated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsActivated).toInt()
          _tmpIsActivated = _tmp != 0
          val _tmpActivationDate: Long
          _tmpActivationDate = _stmt.getLong(_columnIndexOfActivationDate)
          val _tmpExpirationDate: Long
          _tmpExpirationDate = _stmt.getLong(_columnIndexOfExpirationDate)
          val _tmpLastCheckedTimestamp: Long
          _tmpLastCheckedTimestamp = _stmt.getLong(_columnIndexOfLastCheckedTimestamp)
          _result = ModuleLicenseEntity(_tmpModuleId,_tmpIsActivated,_tmpActivationDate,_tmpExpirationDate,_tmpLastCheckedTimestamp)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllLicenses(): List<ModuleLicenseEntity> {
    val _sql: String = "SELECT * FROM module_licenses"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfModuleId: Int = getColumnIndexOrThrow(_stmt, "moduleId")
        val _columnIndexOfIsActivated: Int = getColumnIndexOrThrow(_stmt, "isActivated")
        val _columnIndexOfActivationDate: Int = getColumnIndexOrThrow(_stmt, "activationDate")
        val _columnIndexOfExpirationDate: Int = getColumnIndexOrThrow(_stmt, "expirationDate")
        val _columnIndexOfLastCheckedTimestamp: Int = getColumnIndexOrThrow(_stmt, "lastCheckedTimestamp")
        val _result: MutableList<ModuleLicenseEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ModuleLicenseEntity
          val _tmpModuleId: String
          _tmpModuleId = _stmt.getText(_columnIndexOfModuleId)
          val _tmpIsActivated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsActivated).toInt()
          _tmpIsActivated = _tmp != 0
          val _tmpActivationDate: Long
          _tmpActivationDate = _stmt.getLong(_columnIndexOfActivationDate)
          val _tmpExpirationDate: Long
          _tmpExpirationDate = _stmt.getLong(_columnIndexOfExpirationDate)
          val _tmpLastCheckedTimestamp: Long
          _tmpLastCheckedTimestamp = _stmt.getLong(_columnIndexOfLastCheckedTimestamp)
          _item = ModuleLicenseEntity(_tmpModuleId,_tmpIsActivated,_tmpActivationDate,_tmpExpirationDate,_tmpLastCheckedTimestamp)
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
