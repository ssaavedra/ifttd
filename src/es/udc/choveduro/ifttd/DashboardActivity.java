package es.udc.choveduro.ifttd;

import java.util.ArrayList;
import java.util.List;

import es.udc.choveduro.ifttd.types.Accion;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

public class DashboardActivity extends Activity {

	private class AccionAdapter extends ArrayAdapter<Accion> {
		public AccionAdapter(Context ctx, int textViewResID) {
			super(ctx, textViewResID);
		}
		public AccionAdapter(Context ctx, int textViewResID, List<Accion> items) {
			super(ctx, textViewResID, items);
		}
		public AccionAdapter(Context ctx, int textViewResID, Accion items[]) {
			super(ctx, textViewResID, items);
		}
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
        List<Accion> values = new ArrayList<Accion>();
        
        Adapter adapter = new AccionAdapter(this, R.layout.this_and_then_item, values);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }
}
