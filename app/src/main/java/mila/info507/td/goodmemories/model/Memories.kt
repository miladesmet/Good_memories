package mila.info507.td.goodmemories.model

// Model d'un memorie
class Memories (
    val id: Int,
    val title: String,
    val photo: String,
    val emotion: Int,
    val date: String,
    val description: String
){
    companion object{
        const val ID= "id"
        const val TITLE= "title"
        const val PHOTO= "photo"
        const val EMOTION= "emotion"
        const val DATE= "date"
        const val DESCRIPTION= "description"
    }
}