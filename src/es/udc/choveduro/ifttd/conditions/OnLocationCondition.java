/**
 * 
 */
package es.udc.choveduro.ifttd.conditions;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import es.udc.choveduro.ifttd.EasyActivity;
import es.udc.choveduro.ifttd.R;
import es.udc.choveduro.ifttd.types.CallbackIF;
import es.udc.choveduro.ifttd.types.Condition;

/**
 * @author ch01
 * 
 */
public class OnLocationCondition extends Condition {

	/**
	 */
	@Override
	public String getName() {
		return "Localización";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.udc.choveduro.ifttd.types.Condition#getImageResource()
	 */
	@Override
	public int getImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.udc.choveduro.ifttd.types.Condition#getShortDesc()
	 */
	@Override
	public String getShortDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.udc.choveduro.ifttd.types.Condition#configure(es.udc.choveduro.ifttd
	 * .EasyActivity, es.udc.choveduro.ifttd.types.CallbackIF)
	 */
	@Override
	public void configure(EasyActivity ctx, CallbackIF callback) {
		// TODO Auto-generated method stub

	}

	public final class SetLocationMapActivity extends MapActivity {

		GeoPoint p;

		class PuntoMapOverlay extends com.google.android.maps.Overlay {
			@Override
			public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
					long when) {
				super.draw(canvas, mapView, shadow);

				// ---translate the GeoPoint to screen pixels---
				Point screenPts = new Point();
				mapView.getProjection().toPixels(p, screenPts);

				// ---add the marker---
				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.pushpin);
				canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
				return true;
			}
		}
		
		class TouchMapOverlay extends com.google.android.maps.Overlay {
			@Override
			public boolean onTouchEvent(MotionEvent event, MapView mapView) {
				// ---when user lifts his finger---
				if (event.getAction() == 1) {
					p = mapView.getProjection().fromPixels((int) event.getX(),
							(int) event.getY());
					MapView map = (MapView) findViewById(R.id.mapviewselect);
					if(map.getOverlays().size() == 1){
						map.getOverlays().add(new PuntoMapOverlay());
					}
				}
				return false;
			}

		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.select_point_map);

			MapView map = (MapView) findViewById(R.id.mapviewselect);
			map.setBuiltInZoomControls(true);
			map.getOverlays().add(new TouchMapOverlay());
			
			
			Button b_accept = (Button) findViewById(R.id.configure_accept_button);
			b_accept.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (p != null) {
						SharedPreferences sp = null; // TODO
						sp.edit().putInt("longitud", p.getLongitudeE6());
						sp.edit().putInt("latitud", p.getLatitudeE6());

						SetLocationMapActivity.this.finish();

					} else {
						// TODO toast!!
					}

				}

			});

			Button b_cancel = (Button) findViewById(R.id.configure_accept_button);
			b_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SetLocationMapActivity.this.finish();
				}

			});
		}

		@Override
		protected boolean isRouteDisplayed() {
			return false;
		}

	}

}
