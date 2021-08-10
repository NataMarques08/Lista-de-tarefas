package com.example.todolist.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolist.model.Task

class BancoTask(context: Context) : SQLiteOpenHelper(context, NOME_BANCO,null, VERSAO) {

    companion object{
        const val NOME_BANCO = "taskdatabase"
        const val VERSAO = 1
    }

     val TABELA = "tarefa"
     val ID = "id"
     val TITULO = "titulo"
     val HORA = "hora"
     val DATA = "data"
     val CREATE_TABLE = "CREATE TABLE $TABELA (" +
                " ${ID} INTEGER NOT NULL PRIMARY KEY, " +
                " ${TITULO} TEXT, " +
                " ${HORA} TEXT, " +
                " ${DATA} TEXT )"

    private val DROP_TABLE = "DROP TABLE IF EXISTS $TABELA"

    override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion < newVersion){
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)
    }

    fun salvarTarefa(tarefa:Task){
        val db = writableDatabase
        val values = ContentValues()
        values.put(TITULO,tarefa.title)
        values.put(HORA,tarefa.hora)
        values.put(DATA,tarefa.data)
        db.insert(TABELA,null,values)

        db.close()
    }


    fun listaTarefas():List<Task>{
        val db = readableDatabase
        val lista:MutableList<Task> = mutableListOf()
        val cursor = db.query(TABELA,null,null,null,null,null,null)

        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                val tarefa = Task(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(TITULO)),
                    cursor.getString(cursor.getColumnIndex(HORA)),
                    cursor.getString(cursor.getColumnIndex(DATA))
                )
            lista.add(tarefa)
            }
        }
        cursor.close()
        db.close()
        return lista
    }

    fun deleteTask(id:Int){
        val db = writableDatabase
        val args = arrayOf("$id")
        db.delete(TABELA,ID + " = ?",args)
        db.close()
    }

    fun selecionarTask(id: Int):Task{
        val db = readableDatabase
        val where = "$ID = $id"

        val cursor = db.query(TABELA,null,where,null,null,null,null)

        if(cursor!=null){
            cursor.moveToFirst()
        }
        db.close()
        return Task(cursor.getColumnIndex(ID),cursor.getString(cursor.getColumnIndex(TITULO)),cursor.getString(cursor.getColumnIndex(HORA)), cursor.getString(cursor.getColumnIndex(DATA)))
    }

    fun updateTask(task: Task){
        val db = writableDatabase
        val sql = "UPDATE $TABELA SET $TITULO = ?, $HORA = ?, $DATA = ? WHERE $ID = ?"
        val args = arrayOf(task.title,task.hora,task.data,task.id)

        db.execSQL(sql,args)
        db.close()
    }

}