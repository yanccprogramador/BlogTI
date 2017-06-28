package br.com.yanccprogramador.blogti

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * Created by yanccprogramador on 15/06/2017.
 */

class BancoController(context: Context) {

    private var db: SQLiteDatabase? = null
    private val banco: CriaBanco

    init {

        banco = CriaBanco(context)
    }


    fun insereUser(nome: String, login: String, senha: String): String {
        val valores: ContentValues
        val resultado: Long

        db = banco.writableDatabase
        valores = ContentValues()
        valores.put("login", login)
        valores.put("senha", senha)
        valores.put("nome", nome)

        resultado = db!!.insert("usuario", null, valores)
        db!!.close()

        if (resultado.compareTo(-1)==0)
            return "Erro ao inserir registro"
        else
            return "Registro Inserido com sucesso"

    }

    fun carregaUser(): Cursor {
        val cursor: Cursor?
        val campos = arrayOf("nome", "login", "senha")
        db = banco.readableDatabase
        cursor = db!!.query("usuario", campos, null, null, null, null, null, null)

        cursor?.moveToLast()
        db!!.close()
        return cursor
    }
}
