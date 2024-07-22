package frc.lib.branch

class Tree<T>(private var block: (Tree<T>) -> T?) {
    fun get(): T? {
        return block.invoke(this)
    }

    fun getOr(default: T): T {
        return block.invoke(this) ?: default
    }

    fun lock(block: (Tree<T>) -> T?) {
        this.block = block
    }
}