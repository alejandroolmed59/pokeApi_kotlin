package com.deushdezt.laboratorio4.adapters

import android.net.Uri
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.deushdezt.laboratorio4.MyPokemonAdapter
import com.deushdezt.laboratorio4.R
import com.deushdezt.laboratorio4.network.NetworkUtils
import com.deushdezt.laboratorio4.pojos.Pokemon
import com.deushdezt.laboratorio4.pojos.PokemonSobrecargado
import kotlinx.android.synthetic.main.cardview_pokemon.view.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class PokemonAdapter(var pokemons: List<Pokemon>, val clickListener: (PokemonSobrecargado) -> Unit) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>(), MyPokemonAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = pokemons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(pokemons[position], clickListener)

    override fun changeDataSet(newDataSet: List<Pokemon>) {
        this.pokemons = newDataSet
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var pokemonSobrecargadoBase: PokemonSobrecargado
        fun bind(item: Pokemon, clickListener: (PokemonSobrecargado) -> Unit) = with(itemView) {
            Glide.with(itemView.context)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + item.id + ".png")
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(pokemon_image_cv)
            pokemon_nombre.text = item.name
            FetchPokemonSobrecargado().execute(item.url)
            this.setOnClickListener { clickListener(pokemonSobrecargadoBase) }
        }

        private inner class FetchPokemonSobrecargado : AsyncTask<String, Void, String>() {
            override fun doInBackground(vararg params: String?): String {
                if (params.isNullOrEmpty()) return ""

                val builtUri = Uri.parse(params[0]).buildUpon().build()
                val url = URL(builtUri.toString())
                return try {
                    NetworkUtils().getResponseFromHttpUrl(url)
                } catch (e: IOException) {
                    "Error"
                }
            }

            override fun onPostExecute(pokemonInfo: String) {
                super.onPostExecute(pokemonInfo)
                if (!pokemonInfo.isEmpty()) {
                    val PokemonJson = JSONObject(pokemonInfo)
                    pokemonSobrecargadoBase = PokemonSobrecargado(PokemonJson.getString("id"), PokemonJson.getString("name"), PokemonJson.getString("weight"),
                            PokemonJson.getString("height"), PokemonJson.getString("base_experience"), PokemonJson.getJSONObject("sprites").getString("front_default"))
                }
            }
        }
    }
}