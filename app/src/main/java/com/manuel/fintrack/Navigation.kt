package com.manuel.fintrack

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

class Screen(val route: String, val arguments: List<NamedNavArgument>) {
   companion object{
       val Login = Screen("login", emptyList())
       val Main  = Screen("main", emptyList())
       val Balance  = Screen("balance", emptyList())
       val income  = Screen("income", emptyList())
      // val expence  = Screen("expence", emptyList())
   }

    fun withArgs(arg: String): String {
        return "$route/$arg"
    }
}
