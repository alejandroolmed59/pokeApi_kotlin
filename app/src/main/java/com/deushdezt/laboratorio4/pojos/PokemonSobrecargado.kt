package com.deushdezt.laboratorio4.pojos

import android.os.Parcel
import android.os.Parcelable

data class PokemonSobrecargado(
        val id:String="N/A",
        val name: String="N/A",
        val peso: String="N/A",
        val altura: String="N/A",
        val experienciaNecesaria: String="N/A",
        val imagen:String="N/A"
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(peso)
        parcel.writeString(altura)
        parcel.writeString(experienciaNecesaria)
        parcel.writeString(imagen)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonSobrecargado> {
        override fun createFromParcel(parcel: Parcel): PokemonSobrecargado {
            return PokemonSobrecargado(parcel)
        }

        override fun newArray(size: Int): Array<PokemonSobrecargado?> {
            return arrayOfNulls(size)
        }
    }
}