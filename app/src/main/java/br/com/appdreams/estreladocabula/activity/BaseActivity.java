package br.com.appdreams.estreladocabula.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.fragments.HomeFragment;
import br.com.appdreams.estreladocabula.utils.BaseActivityUtils;

/**
 * Created by Paulo on 16/03/2017.
 */

public class BaseActivity extends BaseActivityUtils
{

    protected DrawerLayout drawerLayout;
    protected static Context context;
    protected NavigationView navigationView;

    //Configura a Toolbar
    protected void setUpToolbar(String titulo)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
        {
            toolbar.setTitle(titulo);
            setSupportActionBar(toolbar);
        }
    }

    //Botão Voltar
    protected void setHomeAsUp()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        if (upArrow != null)
        {
            upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    // Configura o Nav Drawer
    protected void setUpNavDrawer() {
        // Drawer Layout
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            // Ícone do menu do nav drawer
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.menu_drawer);
            if (navigationView != null && drawerLayout != null) {
                // Atualiza os dados do header do Navigation View
                //setNavHeaderValues(navigationView, "Paulo Vinicius", "pvinicius@gmail.com", "https://s-media-cache-ak0.pinimg.com/564x/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", R.drawable.nav_drawer_header2);

                // Trata o evento de clique no menu.
                navigationView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener()
                        {
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem)
                            {
                                // Seleciona a linha
                                menuItem.setChecked(true);
                                // Fecha o menu
                                drawerLayout.closeDrawers();
                                // Trata o evento do menu
                                onNavDrawerItemSelected(menuItem);
                                return true;
                            }
                        });
            }
        }
    }

    // Atualiza os dados do header do Navigation View
    public void setNavHeaderValues(NavigationView navView, String nome, String email, String foto, int backgroung)
    {
        View headerView         = navView.getHeaderView(0);
        TextView txtNome        = (TextView) headerView.findViewById(R.id.txtNome);
        TextView txtEmail       = (TextView) headerView.findViewById(R.id.txtEmail);
        ImageView imgView       = (ImageView) headerView.findViewById(R.id.imgFoto);
        ImageView imgBackground = (ImageView) headerView.findViewById(R.id.imgBackground);

        txtNome.setText(nome);
        txtEmail.setText(email);
        //imgView.setImageResource(img);
        //Picasso.with(BaseActivity.context).load(backgroung).into(imgBackground);
        Picasso.with(BaseActivity.this).load(foto).placeholder(R.drawable.sem_foto).into(imgView);
    }

    // Atualiza o menu do Navigation View
    public static void setNavMenuView(NavigationView navView, int menu)
    {
        navView.getMenu().clear(); //clear old inflated items.
        navView.inflateMenu(menu); //inflate new items.
    }

    // Trata o evento do menu lateral
    public void onNavDrawerItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.nav_menu_home:
                //navigationView = (NavigationView) findViewById(R.id.menu_drawer);
                setNavHeaderValues(navigationView, "Paulo Vinicius", "pvinicius@gmail.com", "http://pixel.nymag.com/imgs/daily/vulture/2015/12/18/18-will-smith-chatroom-silo.w1200.h630.png", R.drawable.bg_teste1);
                setNavMenuView(navigationView, R.menu.nav_drawer_menu);
                break;
            case R.id.nav_item_carros_todos:
                // Nada aqui pois somente a MainActivity possui menu lateral
                //navigationView = (NavigationView) findViewById(R.id.menu_drawer);
                setNavHeaderValues(navigationView, "Maria Joaquina", "maria@bol.com.br", "http://portal.tododia.uol.com.br/_midias/jpg/2014/10/06/larissa_manoela-268490.jpg", R.drawable.nav_drawer_header1);
                setNavMenuView(navigationView, R.menu.nav_drawer_menu);
                break;
            case R.id.nav_item_carros_classicos:
                /*Intent intent = new Intent(getContext(), CarrosActivity.class);
                intent.putExtra("tipo", R.string.classicos);
                startActivity(intent);*/
                break;
            case R.id.nav_item_carros_esportivos:
                /*intent = new Intent(getContext(), CarrosActivity.class);
                intent.putExtra("tipo", R.string.esportivos);
                startActivity(intent);*/
                break;
            case R.id.nav_item_carros_luxo:
                /*intent = new Intent(getContext(), CarrosActivity.class);
                intent.putExtra("tipo", R.string.luxo);
                startActivity(intent);*/
                break;
            case R.id.nav_item_site_livro:
                /*startActivity(new Intent(getContext(), SiteLivroActivity.class));*/
                break;
            case R.id.nav_menu_sair:

                break;
        }
    }

    // Adiciona o fragment no centro da tela
    protected void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // Trata o clique no botão que abre o menu.
                if (drawerLayout != null)
                {
                    openDrawer();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    // Abre o menu lateral
    protected void openDrawer()
    {
        if (drawerLayout != null)
        {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // Fecha o menu lateral
    protected void closeDrawer()
    {
        if (drawerLayout != null)
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
