package ppo.converter.ui.activities;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import ppo.converter.R;
import ppo.converter.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface typeFaceFont = Typeface.
                createFromAsset(getAssets(), "fa_solid.ttf");

        String[] icons = {
                getString(R.string.fa_arrows_alt_h_solid),
                getString(R.string.fa_clock_solid),
                getString(R.string.fa_balance_scale_solid)
        };

        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), icons);
        viewPager.setAdapter(adapter);


        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeFaceFont);
                }
            }
        }
    }
}


//        int[] imageResId = {
//                R.drawable.ic_length, R.drawable.ic_money, R.drawable.ic_weight
//        };
//
//        for (int i = 0; i < imageResId.length; i++) {
//            Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(imageResId[i]);
//        }