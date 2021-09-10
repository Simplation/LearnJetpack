package com.simplation.learnnavigation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.simplation.learnnavigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var mViewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        // TODO: 2021/09/10 IllegalStateException: Activity does not have a NavController
        /**
         * 两种修改方式：
         *      1.修改获取 NavController 的方式
         *          val navHomeFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
         *           navController = navHomeFragment.navController
         *      2.修改 xml 文件，把 androidx.fragment.app.FragmentContainerView 改为 fragment
         */

        val navHomeFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHomeFragment.navController

        // 原来的写法
        // navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            // 收到的切换事件
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        ) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }
}