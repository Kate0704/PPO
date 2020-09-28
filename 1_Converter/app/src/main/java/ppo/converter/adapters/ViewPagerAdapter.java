package ppo.converter.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import ppo.converter.fragments.LengthFragment;
import ppo.converter.fragments.MoneyFragment;
import ppo.converter.fragments.WeightFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments = {
            new LengthFragment(),
            new MoneyFragment(),
            new WeightFragment()
    };

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
        return null;
    }
}
