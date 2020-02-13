package yc.com.pinyin_study.study.adapter;

import android.view.ViewGroup;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by wanglin  on 2018/10/25 17:12.
 */
public class StudyMainAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private FragmentManager fm;

    public StudyMainAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fm = fm;
        this.fragments = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }


    /**
     * FragmentPagerAdapter 中重写instantiateItem和destroyItem 这两个方法主要用于加载大量数据的时候，其他
     * 情况一般不需要重写
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.show(fragment);
        /**
         * 使用的 commit方法是在Activity的onSaveInstanceState()之后调用的，这样会出错，因为
         * onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后
         * 再给它添加Fragment就会出错。解决办法就是把commit（）方法替换成 commitAllowingStateLoss()就行
         * 了，其效果是一样的。
         */
//        fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragments.get(position);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}
