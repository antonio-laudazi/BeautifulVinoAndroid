package com.marte5.beautifulvino.OnBoarding;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.marte5.beautifulvino.MainActivity;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.R;
import com.marte5.beautifulvino.Utility.Utility;

import java.util.ArrayList;

public class BoardingActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Button buttonEntra;
    private Button buttonSalta;
    private LinearLayout linearLayoutIndicators;
    private int page;
    private ArrayList<ImageView> indicators;
    private ViewPager mViewPager;
    private ImageView yellowCircle;
    private ImageView rosaCircle;
    private int fromPage;
    private int duration;
    private String TAG = BoardingActivity.class.getSimpleName();
    private Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);
        yellowCircle = findViewById(R.id.imageViewYellow);
        rosaCircle = findViewById(R.id.imageViewRosa);
        p = Utility.getScreenSize(this);

        ViewGroup.MarginLayoutParams marginParamsRosa = (ViewGroup.MarginLayoutParams) rosaCircle.getLayoutParams();
        marginParamsRosa.setMargins((int) (p.x / -7.35), (int) (p.y / 1.55), 0, 0);

        ViewGroup.MarginLayoutParams marginParamsYellow = (ViewGroup.MarginLayoutParams) yellowCircle.getLayoutParams();
        marginParamsYellow.setMargins((int) (p.x / -5.95), (int) (p.y / 1.48), 0, 0);

        rosaCircle.getLayoutParams().height = (int) (p.y / 1.85);
        rosaCircle.getLayoutParams().width = (int) (p.y / 1.85);

        yellowCircle.getLayoutParams().height = (int) (p.y / 1.09);
        yellowCircle.getLayoutParams().width = (int) (p.y / 1.09);

        duration = 500;
        fromPage = 0;
        buttonEntra = findViewById(R.id.buttonEntra);
        buttonSalta = findViewById(R.id.buttonSalta);
        buttonSalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.setFirstLaunch(BoardingActivity.this, false);
                launchMainActivity();
            }
        });
        buttonEntra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.setFirstLaunch(BoardingActivity.this, false);
                launchMainActivity();
            }
        });
        linearLayoutIndicators = findViewById(R.id.linearLayoutIndicatorsBoarding);
        indicators = new ArrayList<>();
        indicators.add((ImageView) findViewById(R.id.boarding_indicator_0));
        indicators.add((ImageView) findViewById(R.id.boarding_indicator_1));
        indicators.add((ImageView) findViewById(R.id.boarding_indicator_2));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        updateIndicators(mViewPager.getCurrentItem());

        Animation animR = new ScaleAnimation(
                1f, 1.3f, // Start and end values for the X axis scaling
                1f, 1.3f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        animR.setFillAfter(true); // Needed to keep the result of the animation
        animR.setDuration(0);
        rosaCircle.startAnimation(animR);

//The position parameter indicates where a given page is located relative to the center of the screen.
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                int pagePosition = (int) page.getTag();

                // Here you can do all kinds of stuff, like get the
                // width of the page and perform calculations based
                // on how far the user has swiped the page.
                int pageWidth = page.getWidth();
                float pageWidthTimesPosition = pageWidth * position;
                float absPosition = Math.abs(position);

                View badge1 = page.findViewById(R.id.image_gioca_b1);
                View badge2 = page.findViewById(R.id.image_gioca_b2);
                View badge3 = page.findViewById(R.id.image_gioca_b3);
                View medaglia = page.findViewById(R.id.image_gioca_medaglia);


                // Now it's time for the effects
                if (position <= -1.0f || position >= 1.0f) {

                    // The page is not visible. This is a good place to stop
                    // any potential work / animations you may have running.

                } else if (position == 0.0f) {

                    // The page is selected. This is a good time to reset Views
                    // after animations as you can't always count on the PageTransformer
                    // callbacks to match up perfectly.

                    if (pagePosition == 2 && badge1 != null && badge2 != null && badge3 != null && medaglia!=null) {
                        badge1.setAlpha(1);
                        badge2.setAlpha(1);
                        badge3.setAlpha(1);
                        final Animation myAnim = AnimationUtils.loadAnimation(BoardingActivity.this, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                        myAnim.setInterpolator(interpolator);
                        badge1.startAnimation(myAnim);
                        badge2.startAnimation(myAnim);
                        badge3.startAnimation(myAnim);
                        final Animation myAnimM = AnimationUtils.loadAnimation(BoardingActivity.this, R.anim.bounce);
                        myAnimM.setInterpolator(interpolator);
                        myAnimM.setStartOffset(1000);
                        medaglia.setAlpha(1);
                        medaglia.startAnimation(myAnimM);

                    }

                } else {

                    // The page is currently being scrolled / swiped. This is
                    // a good place to show animations that react to the user's
                    // swiping as it provides a good user experience.

                    // Let's start by animating the title.
                    // We want it to fade as it scrolls out
                    View title = page.findViewById(R.id.titleBoarding1);
                    title.setAlpha(1.0f - absPosition);
                    View description = page.findViewById(R.id.descrBoarding1);
                    description.setAlpha(1.0f - absPosition);


                    View image11 = page.findViewById(R.id.imageBoarding11);
                    if (pagePosition == 0 && image11 != null) {
                        image11.setAlpha(1.0f - absPosition);
                        image11.setTranslationX(pageWidthTimesPosition * 1f);
                    }

                    View image12 = page.findViewById(R.id.imageBoarding12);
                    if (pagePosition == 0 && image12 != null) {
                        image12.setAlpha(1.0f - absPosition);
                        image12.setTranslationX(pageWidthTimesPosition * 1.5f);
                    }

                    View image21 = page.findViewById(R.id.imageBoarding21);
                    if (pagePosition == 1 && image21 != null) {
                        image21.setAlpha(1.0f - absPosition);
                        image21.setTranslationX(pageWidthTimesPosition * 1f);
                    }

                    View image22 = page.findViewById(R.id.imageBoarding22);
                    if (pagePosition == 1 && image22 != null) {
                        image22.setAlpha(1.0f - absPosition);
                        image22.setTranslationX(pageWidthTimesPosition * 1.5f);
                    }

                    View relativeLayout = page.findViewById(R.id.relativeLayoutImages);
                    if (pagePosition == 2 && relativeLayout != null) {
                        relativeLayout.setAlpha(1.0f - absPosition);
                        badge1.setAlpha(0);
                        badge2.setAlpha(0);
                        badge3.setAlpha(0);
                        medaglia.setAlpha(0);
                    }

                    // Finally, it can be useful to know the direction
                    // of the user's swipe - if we're entering or exiting.
                    // This is quite simple:
                    if (position < 0) {
                        // Create your out animation here
                    } else {
                        // Create your in animation here
                    }
                }
            }

        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicators(page);
                int translationDelta = (p.x / 3);

                switch (position) {
                    case 0:
                        fromPage = 0;
                        Animation animG = new ScaleAnimation(
                                1.4f, 1f, // Start and end values for the X axis scaling
                                1.4f, 1f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                        animG.setFillAfter(true); // Needed to keep the result of the animation
                        animG.setDuration(duration);
                        yellowCircle.startAnimation(animG);

                        Animation animR = new ScaleAnimation(
                                1f, 1.3f, // Start and end values for the X axis scaling
                                1f, 1.3f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                        animR.setFillAfter(true); // Needed to keep the result of the animation
                        animR.setDuration(duration);
                        rosaCircle.startAnimation(animR);
                        break;
                    case 1:
                        if (fromPage == 0) {
                            Animation anim1 = new ScaleAnimation(
                                    1f, 1.4f, // Start and end values for the X axis scaling
                                    1f, 1.4f, // Start and end values for the Y axis scaling
                                    Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                            anim1.setFillAfter(true); // Needed to keep the result of the animation
                            anim1.setDuration(duration);
                            yellowCircle.startAnimation(anim1);

                            Animation animR1 = new ScaleAnimation(
                                    1.3f, 1f, // Start and end values for the X axis scaling
                                    1.3f, 1f, // Start and end values for the Y axis scaling
                                    Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                            animR1.setFillAfter(true); // Needed to keep the result of the animation
                            animR1.setDuration(duration);
                            rosaCircle.startAnimation(animR1);
                        } else if (fromPage == 2) {
                            Animation anim3 = new ScaleAnimation(
                                    2f, 1.4f, // Start and end values for the X axis scaling
                                    2f, 1.4f, // Start and end values for the Y axis scaling
                                    Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                            anim3.setFillAfter(true); // Needed to keep the result of the animation
                            anim3.setDuration(duration);
                            yellowCircle.startAnimation(anim3);

                            Animation anim2R = new TranslateAnimation(translationDelta, 0, translationDelta, 0);
                            anim2R.setFillAfter(true);
                            anim2R.setDuration(duration);
                            rosaCircle.startAnimation(anim2R);
                        }
                        break;
                    case 2:
                        fromPage = 2;
                        Animation anim2 = new ScaleAnimation(
                                1.4f, 2f, // Start and end values for the X axis scaling
                                1.4f, 2f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                        anim2.setFillAfter(true); // Needed to keep the result of the animation
                        anim2.setDuration(duration);
                        yellowCircle.startAnimation(anim2);

                        Animation anim2R = new TranslateAnimation(0, translationDelta, 0, translationDelta);
                        anim2R.setFillAfter(true);
                        anim2R.setDuration(duration);
                        rosaCircle.startAnimation(anim2R);
                        break;
                }

                buttonSalta.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                buttonEntra.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                linearLayoutIndicators.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.size(); i++) {
            indicators.get(i).setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    private void launchMainActivity() {
        Intent userActivity = new Intent(this, MainActivity.class);
        // userActivity.putExtra("name", username);
        userActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(userActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new OnboardingFragment1();
                case 1:
                    return new OnboardingFragment2();
                case 2:
                    return new OnboardingFragment3();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
