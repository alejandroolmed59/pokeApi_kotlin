package com.naldana.ejemplo08

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import com.naldana.ejemplo08.models.Pokemon
import com.naldana.ejemplo08.utilities.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var pokemonMutableList: MutableList<Pokemon> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FetchPokemonTask().execute()
    }


    fun initRecycler() {


        viewManager = LinearLayoutManager(this)
        viewAdapter = PokemonAdapter(pokemonMutableList)

        rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {

            val pokeAPI = NetworkUtils.buildUrl()

            return try {
                NetworkUtils.getResponseFromHttpUrl(pokeAPI)!!
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }


        override fun onPostExecute(pokemonInfo: String?) {

            if (pokemonInfo != null || pokemonInfo != "") {

                var jsonPokeInfo= JSONObject(pokemonInfo)
                var arrayPokemons= jsonPokeInfo.getJSONArray("results")
                for(i in 0 until arrayPokemons.length()) {

                    var nuevoPok= Pokemon(i, arrayPokemons.getJSONObject(i).getString("name"), arrayPokemons.getJSONObject(i).getString("url"))
                    pokemonMutableList.add(nuevoPok)
                    Log.v("Print", pokemonMutableList[i].toString())
                }
                initRecycler()
            }
        }
    }
}
