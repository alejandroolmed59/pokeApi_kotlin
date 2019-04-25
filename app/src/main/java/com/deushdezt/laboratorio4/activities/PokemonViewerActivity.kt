package com.deushdezt.laboratorio4.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.deushdezt.laboratorio4.R
import com.deushdezt.laboratorio4.pojos.PokemonSobrecargado
import kotlinx.android.synthetic.main.viewer_pokemon.*

class PokemonViewerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewer_pokemon)

        setSupportActionBar(toolbarviewer)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setDisplayShowHomeEnabled(true)
        pokemon_name_activity2.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        pokemon_name_activity2.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        val reciever: PokemonSobrecargado = intent?.extras?.getParcelable("POKEMON") ?: PokemonSobrecargado()
        init(reciever)
    }

    fun init(pokemonSobrecargado: PokemonSobrecargado){
        Glide.with(this)
                .load(pokemonSobrecargado.imagen)
                .placeholder(R.drawable.ic_launcher_background)
                .into(app_bar_image_viewer)
        pokemon_name_activity2.title = pokemonSobrecargado.name
        app_bar_id_viewer.text = pokemonSobrecargado.id
        peso_viewer.text = pokemonSobrecargado.peso
        altura_viewer.text = pokemonSobrecargado.altura
        experiencia_viewer.text = pokemonSobrecargado.experienciaNecesaria

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {onBackPressed();true}
            else -> super.onOptionsItemSelected(item)
        }
    }
}