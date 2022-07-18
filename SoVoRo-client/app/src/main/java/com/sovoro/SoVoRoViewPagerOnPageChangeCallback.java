package com.sovoro;

import androidx.viewpager2.widget.ViewPager2;

public class SoVoRoViewPagerOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
    private ViewPager2 mPager;
    public SoVoRoViewPagerOnPageChangeCallback(ViewPager2 mPager) {
        this.mPager=mPager;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        if (positionOffsetPixels == 0) {
            mPager.setCurrentItem(position);
        }
    }
    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        //mIndicator.animatePageSelected(position%num_page);
    }
}
