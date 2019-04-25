package com.deushdezt.laboratorio4.adapters

import android.net.Uri
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deushdezt.laboratorio4.MyPokemonAdapter
import com.deushdezt.laboratorio4.R
import com.deushdezt.laboratorio4.network.NetworkUtils
import com.deushdezt.laboratorio4.pojos.Pokemon
import com.deushdezt.laboratorio4.pojos.PokemonSobrecargado
import kotlinx.android.synthetic.main.list_item_pokemon.view.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class PokemonSimpleListAdapter(var pokemons: List<Pokemon>, val clickListener: (PokemonSobrecargado) -> Unit) : RecyclerView.Adapter<PokemonSimpleListAdapter.ViewHolder>(), MyPokemonAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = pokemons.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) = holder.bind(pokemons[pos], clickListener)

    override fun changeDataSet(newDataSet: List<Pokemon>) {
        this.pokemons = newDataSet
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var pokemonSobrecargadoBase: PokemonSobrecargado
        fun bind(pokemon: Pokemon, clickListener: (PokemonSobrecargado) -> Unit) = with(itemView) {
            tittle_name_pokemon.text = pokemon.name
            FetchPokemonSobrecargado().execute(pokemon.url)
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