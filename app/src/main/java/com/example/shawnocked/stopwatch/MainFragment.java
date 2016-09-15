
package com.example.shawnocked.stopwatch;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    public static final String COUNT = "COUNT";
    public static final String BUTTON_ENABLED = "BUTTON_ENABLED";
    TextView count = null;
    Button start = null;
    Timer t = null;
    CounterTask task = null;

    Handler h = null;

    SoundPool sp = null;
    int bloopSound = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    // only construct once when the fragment is initially created
    // but only when setRetainInstance(true)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        this.t = new Timer();
        this.task = new CounterTask(0); // not 0 always
        //soundPool we should use factory method though
        this.sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        this.bloopSound = this.sp.load(getContext(), R.raw.bloop, 1);


        this.h = new Handler();
    }

    @Override
    // This is similar to onCreate in MainActivity, but this return View
    // Inflater convert xml objects to Java objects
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        this.count = (TextView)root.findViewById(R.id.count);
        this.start = (Button)root.findViewById(R.id.start);


        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setEnabled(false);
                t.scheduleAtFixedRate(MainFragment.this.task, 0, 1000);
            }
        });

        // set a listener on the count view
        this.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animator anim = AnimatorInflater.loadAnimator(getContext(),
                        R.animator.animate_count);
                anim.setTarget(MainFragment.this.count);
                anim.start();

                sp.play(bloopSound,1f, 1f, 1, 0, 1);
            }
        });
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.t.cancel();
        this.h.removeCallbacksAndMessages(null); //remove all runnable and messages in the UI thread
    }

    /* ---------------------------------------*/
    class CounterTask extends TimerTask {

        long count = 0;
        public CounterTask(long i){this.count = i;}

        @Override
        public void run() {
            h.post(new Runnable() {
                @Override
                public void run() {
                    MainFragment.this.count.setText(Long.toString(count++));

                    if(count % 5 == 0)
                        Toast.makeText(getContext(), "Running" + CounterTask.this.count, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
