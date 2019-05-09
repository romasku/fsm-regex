package fsmregex

// Make stack from ArrayList

fun <T> ArrayList<T>.push(element: T) {
    this.add(element)
}

fun <T> ArrayList<T>.pop(): T {
    return this.dropLast(1)[0]
}