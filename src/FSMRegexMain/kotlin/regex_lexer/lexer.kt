package regex_lexer

import ndfsm.NDFSMState

class InputStream(private val string: String) {
    private var position: Int = 0
    val nextChar get() = this.string[position]
    fun proceed(): Char {
        val currentChar = nextChar
        position += 1
        return currentChar
    }
}

/*
fun regex(stream: InputStream): PartialNDFSM {

}

fun regexT(stream: InputStream) {

}

fun variable(stream: InputStream) {

}

fun variableT(stream: InputStream) {
    if (stream.nextChar != '|' && stream.nextChar != ')') {

    }
}

fun single(stream: InputStream): PartialNDFSM {
    return fSingle(stream)
}

fun modif(stream: InputStream) {

}

fun fSingle(stream: InputStream): PartialNDFSM  {
    return when (stream.nextChar) {
        '(' -> {
            stream.proceed()
            val result = regex(stream)
            if (stream.nextChar != ')') {
                throw Exception("Expected )")
            }
            stream.proceed()
            result
        }
        '\\' -> {
            stream.proceed()
            return escapeSeq(stream)
        }
        '.' -> {
            stream.proceed()
            return PartialNDFSM.any
        }
        else -> character(stream)
    }
}

fun character(stream: InputStream): PartialNDFSM {
    return PartialNDFSM.forCharacter(stream.proceed())
}

fun escapeSeq(stream: InputStream): PartialNDFSM {
    return when (stream.proceed()) {
        'd' -> PartialNDFSM.digits
        '\\' -> PartialNDFSM.forCharacter('\\')
        else -> throw Exception("bad escape seq")
    }
}
*/
