package com.sovoro.wordview;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SoVoRoWordAdapter extends FragmentStateAdapter {
    public int mCount;
    public SoVoRoWordAdapter(@NonNull FragmentActivity fragmentActivity, int _mCount) {
        super(fragmentActivity);
        mCount=_mCount;
    }
//    public SoVoRoWordAdapter(@NonNull Fragment fragment) {
//        super(fragment);
//    }
//
//    public SoVoRoWordAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
//        super(fragmentManager, lifecycle);
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        switch (index) {
            case 0: return SoVoRoWord1.newInstance("1","a");
            case 1: return SoVoRoWord2.newInstance("2","b");
            case 2: return SoVoRoWord3.newInstance("3","b");
            case 3: return SoVoRoWord4.newInstance("4","b");
            case 4: return SoVoRoWord5.newInstance("5","b");
            case 5: return SoVoRoWord6.newInstance("6","b");
            case 6: return SoVoRoWord7.newInstance("7","b");
            case 7: return SoVoRoWord8.newInstance("8","b");
            case 8: return SoVoRoWord9.newInstance("9","b");
            case 9: return SoVoRoWord10.newInstance("10","b");
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % mCount; }
}
