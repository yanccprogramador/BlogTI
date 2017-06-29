package br.com.yanccprogramador.blogti.BD

/**
 * Created by yanccprogramador on 15/06/2017.
 */

class BancoController(context: android.content.Context) {

    private var db: android.database.sqlite.SQLiteDatabase? = null
    private val banco: br.com.yanccprogramador.blogti.BD.CriaBanco

    init {

        banco = br.com.yanccprogramador.blogti.BD.CriaBanco(context)
    }


    fun insereUser(nome: String, login: String, senha: String): String {
        val valores: android.content.ContentValues
        val resultado: Long

        db = banco.writableDatabase
        valores = android.content.ContentValues()
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

    fun carregaUser(): android.database.Cursor {
        val cursor: android.database.Cursor?
        val campos = arrayOf("nome", "login", "senha")
        db = banco.readableDatabase
        cursor = db!!.query("usuario", campos, null, null, null, null, null, null)

        cursor?.moveToLast()
        db!!.close()
        return cursor
    }
    fun deleteUser(): Void? {
        db = banco.writableDatabase
        val sql2 = "delete from usuario;"
        db?.execSQL(sql2)
        return null;
    }
}
