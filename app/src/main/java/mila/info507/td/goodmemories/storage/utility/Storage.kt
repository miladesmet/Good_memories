package mila.info507.td.goodmemories.storage

import mila.info507.td.goodmemories.model.Memories

interface Storage<T> {

    fun insert(obj: T): Int

    fun size() : Int

    fun find(id: Int) : T?

    fun findAll() : List<T>

    fun findAllByEmotion(id: Int) : List<Memories>

    fun update(id: Int, obj: T)

    fun delete(id: Int)

}