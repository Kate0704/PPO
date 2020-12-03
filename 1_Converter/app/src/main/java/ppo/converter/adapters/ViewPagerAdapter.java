package ppo.converter.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import ppo.converter.R;
import ppo.converter.ui.fragments.LengthFragment;
import ppo.converter.ui.fragments.MoneyFragment;
import ppo.converter.ui.fragments.WeightFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String[] icons;

    private Fragment[] fragments = {
            new LengthFragment(),
            new MoneyFragment(),
            new WeightFragment()
    };

    public ViewPagerAdapter(@NonNull FragmentManager fm, String[] icons) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.icons = icons;
    }

    @Override public int getCount() {
        return fragments.length;
    }

    @NotNull
    @Override public Fragment getItem(int position) {
        return fragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return icons[position];
    }
}
