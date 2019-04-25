package com.deushdezt.laboratorio4.activities

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.deushdezt.laboratorio4.AppConstants
import com.deushdezt.laboratorio4.R
import com.deushdezt.laboratorio4.fragments.MainContentFragment
import com.deushdezt.laboratorio4.fragments.MainListFragment
import com.deushdezt.laboratorio4.network.NetworkUtils
import com.deushdezt.laboratorio4.pojos.Pokemon
import com.deushdezt.laboratorio4.pojos.PokemonSobrecargado
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity(), MainListFragment.SearchNewPokemonListener {
    private lateinit var mainFragment: MainListFragment
    private lateinit var mainContentFragment: MainContentFragment


    private var pokemonList = ArrayList<Pokemon>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pokemonList = savedInstanceState?.getParcelableArrayList(AppConstants.dataset_saveinstance_key) ?: ArrayList()
        FetchPokemons().execute()
        initMainFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.dataset_saveinstance_key, pokemonList)
        super.onSaveInstanceState(outState)
    }

    fun initMainFragment() {
        mainFragment = MainListFragment.newInstance(pokemonList)

        val resource = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            R.id.main_fragment
        else {
            mainContentFragment = MainContentFragment.newInstance(PokemonSobrecargado())
            changeFragment(R.id.land_main_cont_fragment, mainContentFragment)
            R.id.land_main_fragment
        }

        changeFragment(resource, mainFragment)
    }

    fun addPokemonToList(pokemon: Pokemon) {
        pokemonList.add(pokemon)
        mainFragment.updatePokemonAdapter(pokemonList)
        Log.d("Number", pokemonList.size.toString())
    }

    override fun buscarPorTipo(pokemonType: String) {
        FetchPokemons().execute(pokemonType)
        mainFragment.updatePokemonAdapter(pokemonList)
    }

    override fun managePortraitItemClick(pokemon: PokemonSobrecargado) {

        val pokemonBundle = Bundle()
        pokemonBundle.putParcelable("POKEMON", pokemon)
        startActivity(Intent(this, PokemonViewerActivity::class.java).putExtras(pokemonBundle))
    }

    override fun manageLandscapeItemClick(pokemon: PokemonSobrecargado) {
        mainContentFragment = MainContentFragment.newInstance(pokemon)
        changeFragment(R.id.land_main_cont_fragment, mainContentFragment)
    }

    private fun changeFragment(id: Int, frag: Fragment) {
        supportFragmentManager.beginTransaction().replace(id, frag).commit()
    }


    private inner class FetchPokemons : AsyncTask<String, Void, String>() {
        var flag = false
        override fun doInBackground(vararg params: String): String {
            var pokemonTypo: String = ""
            if (!params.isNullOrEmpty()) {
                flag = true
                pokemonTypo = params[0]
            }

            // val pokemonName = params[0]
            val pokemonUrl = if (!flag) {
                NetworkUtils().buildtSearchUrl(pokemonTypo)
            } else {
                URL((Uri.parse("https://pokeapi.co/api/v2/type").buildUpon().appendPath(pokemonTypo)).toString())
            }
            Log.v("http a", pokemonUrl.toString())
            return try {
                NetworkUtils().getResponseFromHttpUrl(pokemonUrl)
            } catch (e: IOException) {
                ""
            }
        }

        override fun onPostExecute(pokemonInfo: String) {
            super.onPostExecute(pokemonInfo)

            if (!pokemonInfo.isEmpty()) {
                val pokemonJSON: JSONArray
                if (!flag) {
                    pokemonJSON= JSONObject(pokemonInfo).getJSONArray("results")
                    for (i in 0 until pokemonJSON.length()) {

                        val obj = pokemonJSON.getJSONObject(i)
                        var id = obj.getString("url").substring(34, (obj.getString("url").length - 1))
                        val pokemon = Pokemon(id, obj.getString("name"), obj.getString("url"))
                        pokemonList.add(pokemon)
                    }
                }else{
                    pokemonList.clear()
                    pokemonJSON=JSONObject(pokemonInfo).getJSONArray("pokemon")
                    for (i in 0 until pokemonJSON.length()) {

                        val obj = pokemonJSON.getJSONObject(i)
                        var id = obj.getJSONObject("pokemon").getString("url").substring(34, (obj.getJSONObject("pokemon").getString("url").length - 1))
                        val pokemon = Pokemon(id, obj.getJSONObject("pokemon").getString("name"), obj.getJSONObject("pokemon").getString("url"))
                        pokemonList.add(pokemon)
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "A ocurrido un error,", Toast.LENGTH_LONG).show()
            }
        }
    }


}

