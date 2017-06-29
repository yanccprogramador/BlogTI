package br.com.yanccprogramador.blogti.BD

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import org.w3c.dom.Text

/**
 * Created by allanromanato on 5/27/15.
 */
class CriaBanco(context: Context) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO) {

    override fun onCreate(db: SQLiteDatabase) {

        val sql2 = "CREATE TABLE usuario(login text primary key, senha text,nome text)"
        db.execSQL(sql2)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private val NOME_BANCO = "banco.db"
        private val TABELA = "artigos"
        private val ID = "id"
        private val TITULO = "titulo"
        private val AUTOR = "autor"
        private val artigo = "artigo"
        private val VERSAO = 1
    }
}