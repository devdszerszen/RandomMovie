package pl.dszerszen.randommovie.CustomViews;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import pl.dszerszen.randommovie.R;

public class LoadingView extends LinearLayout {

    final String TAG = "RandomMovie_log";

    ImageView rewind;
    ImageView pause;
    ImageView play;
    ImageView forward;

    final int ANIMATION_LENGTH = 300;


    ArrayList<ImageView> viewsList = new ArrayList<>();

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.loading_view, this);

        rewind = findViewById(R.id.loader_rewind);
        pause = findViewById(R.id.loader_pause);
        play = findViewById(R.id.loader_play);
        forward = findViewById(R.id.loader_forward);

        viewsList.add(rewind);
        viewsList.add(pause);
        viewsList.add(play);
        viewsList.add(forward);

    }

    public void showLoader() {
        this.setVisibility(VISIBLE);
        startAnimation();
    }

    public void hideLoader() {
        this.setVisibility(GONE);
        for (ImageView view: viewsList) {
            view.clearAnimation();
        }
    }

    private void startAnimation() {
        int i = 0;
        for (ImageView view : viewsList) {
            final Animation loadingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loader_anim);
            loadingAnimation.setStartOffset(i * ANIMATION_LENGTH);
            loadingAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (view.equals(viewsList.get(viewsList.size()-1))) {
                        startReverseAnimation();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(loadingAnimation);
            i++;
        }
    }

    private void startReverseAnimation() {
        int i = 0;
        for (ImageView view : viewsList) {
            final Animation loadingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loader_reverse_anim);
            loadingAnimation.setStartOffset(i * ANIMATION_LENGTH);
            loadingAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (view.equals(viewsList.get(viewsList.size()-1))) {
                        startAnimation();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(loadingAnimation);
            i++;
        }
    }
}
