package br.com.appdreams.estreladocabula.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.utils.BaseActivityUtils;

/**
 * Created by Paulo on 16/03/2017.
 */

public class BaseActivity extends BaseActivityUtils
{

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
