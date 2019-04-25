package com.deushdezt.laboratorio4.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.deushdezt.laboratorio4.R
import com.deushdezt.laboratorio4.pojos.PokemonSobrecargado
import kotlinx.android.synthetic.main.main_content_fragment_layout.view.*

class MainContentFragment: Fragment() {

    var pokemon = PokemonSobrecargado()

    companion object {
        fun newInstance(pokemon: PokemonSobrecargado): MainContentFragment{
            val newFragment = MainContentFragment()
            newFragment.pokemon = pokemon
            return newFragment
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.main_content_fragment_layout, container, false)

        bindData(view)

        return view
    }

    fun bindData(view: View){
        view.pokemon_name_main_content_fragment.text = pokemon.name
        view.pokemon_id_main_content_fragment.text = pokemon.id
        view.peso_content_fragment.text = pokemon.peso
        view.altura_content_fragment.text = pokemon.altura
        view.xp_main_content_fragment.text = pokemon.experienciaNecesaria
        Glide.with(view).load(pokemon.imagen)
            .placeholder(R.drawable.ic_launcher_background)
            .into(view.image_main_content_fragment)
    }

}