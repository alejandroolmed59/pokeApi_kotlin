package com.deushdezt.laboratorio4.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deushdezt.laboratorio4.AppConstants
import com.deushdezt.laboratorio4.MyPokemonAdapter
import com.deushdezt.laboratorio4.R
import com.deushdezt.laboratorio4.adapters.PokemonAdapter
import com.deushdezt.laboratorio4.adapters.PokemonSimpleListAdapter
import com.deushdezt.laboratorio4.pojos.Pokemon
import com.deushdezt.laboratorio4.pojos.PokemonSobrecargado
import kotlinx.android.synthetic.main.pokemons_list_fragment.*
import kotlinx.android.synthetic.main.pokemons_list_fragment.view.*

class MainListFragment: Fragment(){

    private lateinit var  pokemons :ArrayList<Pokemon>
    private lateinit var pokemonAdapter : MyPokemonAdapter

    var listenerTool :  SearchNewPokemonListener? = null

    companion object {
        fun newInstance(dataset : ArrayList<Pokemon>): MainListFragment{
            val newFragment = MainListFragment()
            newFragment.pokemons = dataset
            return newFragment
        }
    }

    interface SearchNewPokemonListener{
        fun buscarPorTipo(pokemonType: String)

        fun managePortraitItemClick(pokemon: PokemonSobrecargado)

        fun manageLandscapeItemClick(pokemon: PokemonSobrecargado)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.pokemons_list_fragment, container, false)

        if(savedInstanceState != null) pokemons = savedInstanceState.getParcelableArrayList<Pokemon>(AppConstants.MAIN_LIST_KEY)!!

        initRecyclerView(resources.configuration.orientation, view)
        initSearchButton(view)

        return view
    }

    fun initRecyclerView(orientation:Int, container:View){
        val linearLayoutManager = LinearLayoutManager(this.context)

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            pokemonAdapter = PokemonAdapter(pokemons, { pokemon:PokemonSobrecargado->listenerTool?.managePortraitItemClick(pokemon)})
            container.pokemon_list_rv.adapter = pokemonAdapter as PokemonAdapter
        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            pokemonAdapter = PokemonSimpleListAdapter(pokemons, { pokemon:PokemonSobrecargado->listenerTool?.manageLandscapeItemClick(pokemon)})
            container.pokemon_list_rv.adapter = pokemonAdapter as PokemonSimpleListAdapter
        }

        container.pokemon_list_rv.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }

    fun initSearchButton(container:View) = container.buscar_Por_Tipo.setOnClickListener {
        listenerTool?.buscarPorTipo(et_pokemon_tipo.text.toString())
    }

    fun updatePokemonAdapter(pokemons: ArrayList<Pokemon>){ pokemonAdapter.changeDataSet(pokemons) }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchNewPokemonListener) {
            listenerTool = context
        } else {
            throw RuntimeException("Se necesita una implementación de  la interfaz")

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.MAIN_LIST_KEY, pokemons)
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        listenerTool = null
    }
}