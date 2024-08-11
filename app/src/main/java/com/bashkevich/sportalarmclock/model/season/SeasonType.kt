package com.bashkevich.sportalarmclock.model.season

enum class SeasonType {
    PRE,REG,STAR,POST,OFF
}

fun SeasonType.toSeasonString(): String = when(this){
    SeasonType.PRE->"Pre-season"
    SeasonType.REG->"Regular season"
    SeasonType.STAR->"All-star"
    SeasonType.POST->"Playoffs"
    else->""
}