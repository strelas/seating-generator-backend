package login

import com.google.gson.Gson
import entity.UserData
import java.io.File

class LoginRepository {
    fun getDataFor(login: String): UserData {
        return Gson().fromJson(File("$login/data.txt").reader(), UserData::class.java)
    }

    fun saveData(data: UserData) {
        File(data.nickname).mkdir()
        val file = File("${data.nickname}/data.txt")
        file.createNewFile()
        file.writeText(Gson().toJson(data))
    }
}