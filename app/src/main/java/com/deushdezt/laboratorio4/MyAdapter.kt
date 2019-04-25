package com.deushdezt.laboratorio4

import com.deushdezt.laboratorio4.pojos.Pokemon
import java.util.*

object AppConstants{
    val dataset_saveinstance_key = "CLE"
    val MAIN_LIST_KEY = "key_list_pokemon"
}

interface MyPokemonAdapter {
    fun changeDataSet(newDataSet : List<Pokemon>)
}