package me.milliways;

import me.milliways.map.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class Splash extends Activity {
	protected boolean active = true;
	protected int time = 2000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);
	    
	    Thread splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {
	                int waited = 0;
	                while(active && (waited < time)) {
	                    sleep(100);
	                    
	                    if(active) {
	                        waited += 100;
	                    }
	                }
	            } catch(InterruptedException e) {
	            	Log.v(getClass().getSimpleName(), "Splash interrupted.");
	            } finally {
	                startActivity(new Intent(Splash.this, Map.class));
	                finish();
	            }
	        }
	    };
	    
	    splashTread.start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        active = false;
	    }
	    
	    return true;
	}
}