package com.sovoro.testview;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SoVoRoTestAdapter extends FragmentStateAdapter {
    public int mCount;

    public SoVoRoTestAdapter(@NonNull FragmentActivity fragmentActivity, int mCount) {
        super(fragmentActivity);
        this.mCount = mCount;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        switch (index) {
            case 0: return SoVoRoTest1.newInstance("1","a");
            case 1: return SoVoRoTest2.newInstance("2","b");
            case 2: return SoVoRoTest3.newInstance("3","b");
            case 3: return SoVoRoTest4.newInstance("4","b");
            case 4: return SoVoRoTest5.newInstance("5","b");
            case 5: return SoVoRoTest6.newInstance("6","b");
            case 6: return SoVoRoTest7.newInstance("7","b");
            case 7: return SoVoRoTest8.newInstance("8","b");
            case 8: return SoVoRoTest9.newInstance("9","b");
            case 9: return SoVoRoTest10.newInstance("10","b");
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % mCount; }
}
