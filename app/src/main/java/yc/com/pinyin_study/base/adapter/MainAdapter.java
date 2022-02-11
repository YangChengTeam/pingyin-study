package yc.com.pinyin_study.base.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class MainAdapter extends FragmentPagerAdapter {


    private List<Fragment> mFragmentList;

    public MainAdapter(FragmentManager fm, int behavior, List<Fragment> fragmentList) {
        super(fm, behavior);
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }
}
