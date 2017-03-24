package br.com.appdreams.estreladocabula.utils;

/**
 * Created by Paulo on 11/10/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


/**
 * Created by Ricardo Lecheta on 22/09/2014.
 */
public class BaseActivityUtils extends DebugActivityUtils
{

    private static final String TAG = BaseActivityUtils.class.getSimpleName();

    protected Context getContext()
    {
        return this;
    }

    protected Activity getActivity()
    {
        return this;
    }

    protected void log(String paulo, String msg)
    {
        Log.d(TAG, msg);
    }

    private ProgressDialog loading;

    protected void toast(String msg)
    {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void toast(int msg)
    {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void alert(String msg)
    {
        AlertUtils.alert(this, msg);
    }

    protected void alert(String title, String msg)
    {
        AlertUtils.alert(this, title, msg);
    }

    protected void alert(int msg)
    {
        AlertUtils.alert(this, getString(msg));
    }

    protected void alert(int title, int msg)
    {
        AlertUtils.alert(this,getString(title), getString(msg));
    }

    protected void snack(View view, int msg, Runnable runnable)
    {
        this.snack(view, this.getString(msg), runnable);
    }

    protected void snack(View view, int msg)
    {
        this.snack(view, this.getString(msg), (Runnable) null);
    }

    protected void snack(View view, String msg)
    {
        this.snack(view, msg, (Runnable) null);
    }

    protected void snack(View view, String msg, final Runnable runnable)
    {
        Snackbar.make(view, msg, 0).setAction("Ok", new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (runnable != null)
                {
                    runnable.run();
                }

            }
        }).show();
    }

    public boolean getBoolean(int res)
    {
        return getResources().getBoolean(res);
    }

    public void loadingShow()
    {
        loading = ProgressDialog.show(getContext(), "Salvando...", "Aguarde...", false, false);
    }

    public void loadingShow(String titulo)
    {
        loading = ProgressDialog.show(getContext(), titulo, "Aguarde...", false, false);
    }

    public void loadingShow(String titulo, String mensagem)
    {
        loading = ProgressDialog.show(getContext(), titulo, mensagem, false, false);
    }

    public void loadingHide()
    {
        loading.dismiss();
    }
}