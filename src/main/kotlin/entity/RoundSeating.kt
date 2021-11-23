package entity

data class RoundSeating(val tables: MutableList<TableSeating>) {
    val isLegalRound: Boolean
        get() {
            return tables.all { it.isLegalTable }
        }

    override fun toString(): String {
        return tables.mapIndexed { index, tableSeating ->  "Стол ${index+1}: $tableSeating"}.joinToString("\n")
    }
}