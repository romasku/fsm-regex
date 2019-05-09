package ndfsm

const val emptyMoveCharacter = 0.toChar()

class NDFSMState {
    private val _transitions: MutableMap<Char, MutableList<NDFSMState>> = HashMap()
    val transitions: Map<Char, List<NDFSMState>> get() = _transitions

    fun addTransition(char: Char, state: NDFSMState) {
        _transitions[char]?.add(state) ?: run {
            _transitions[char] = arrayListOf(state)
        }
    }
}