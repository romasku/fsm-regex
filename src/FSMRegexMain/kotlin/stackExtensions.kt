// Make stack from MutableList

fun <T> MutableList<T>.push(element: T) {
    this.add(element)
}

fun <T> MutableList<T>.pop(): T {
    val element = this[this.size - 1]
    this.removeAt(this.size -1)
    return element
}