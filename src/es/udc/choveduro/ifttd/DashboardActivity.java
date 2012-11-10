package es.udc.choveduro.ifttd;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.udc.choveduro.ifttd.conditions.OnTimeCondition;
import es.udc.choveduro.ifttd.consequences.ShowNotification;
import es.udc.choveduro.ifttd.types.Accion;
import es.udc.choveduro.ifttd.types.CallbackIF;
import es.udc.choveduro.ifttd.types.Condition;
import es.udc.choveduro.ifttd.types.ConditionSelectorActivity;
import es.udc.choveduro.ifttd.types.Consequence;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DashboardActivity extends EasyActivity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		List<Accion> values = new ArrayList<Accion>();

		Adapter adapter = new Accion.Adapter(this, R.layout.this_and_then_item,
				values);
		
		ListView list = (ListView) findViewById(R.id.actionListView);
		list.setAdapter((ListAdapter) adapter);
		
	
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_icon) {
            Toast.makeText(this, "Añadir pulsado", Toast.LENGTH_SHORT).show();
            final Condition cond;
            final Consequence cons;
            
            this.launchActivity(ConditionSelectorActivity.class, new CallbackIF(){

				@Override
				public void resultOK(String resultString, Bundle resultMap) {
					cond = null;
					DashboardActivity.this.addActionStep1(cond);
				}

				@Override
				public void resultCancel(String resultString, Bundle resultMap) {
					DashboardActivity.this.addCancel();
				}
            	
            });
            
            
            
            
            
        }
        
        return true;
    }

	protected void addCancel() {
		// TODO Auto-generated method stub
		
	}

	protected void addActionStep1(Condition cond) {
		// TODO Auto-generated method stub
		
	}

}
