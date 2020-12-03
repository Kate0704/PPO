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
import ppo.converter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        String[] icons = {getString(R.string.fa_arrows_alt_h_solid), getString(R.string.fa_clock_solid), getString(R.string.fa_balance_scale_solid)};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), icons);
        binding.viewPager.setAdapter(adapter);
        binding.slidingTabs.setupWithViewPager(binding.viewPager);
        setTabIcons();

        setContentView(binding.getRoot());
    }

    void setTabIcons(){
        Typeface typeFaceFont = Typeface.createFromAsset(getAssets(), "fa_solid.ttf");
        ViewGroup vg = (ViewGroup) binding.slidingTabs.getChildAt(0);
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
