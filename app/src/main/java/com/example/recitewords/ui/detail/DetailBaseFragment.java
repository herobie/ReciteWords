package com.example.recitewords.ui.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recitewords.Constant;
import com.example.recitewords.MainViewModel;
import com.example.recitewords.R;

public class DetailBaseFragment extends Fragment {
    private MainViewModel viewModel;
    private ChangePageReceiver changePageReceiver;
    private ViewPager2 detail_vp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_base, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && Constant.isDataChanged){
            detail_vp.setAdapter(new ViewPagerAdapter(this, viewModel.getWords().size()));
            Constant.isDataChanged = false;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constant.TAG, "ViewCreated");
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        initBroadcast();
        detail_vp = view.findViewById(R.id.detail_vp);
        detail_vp.setAdapter(new ViewPagerAdapter(this, viewModel.getWords().size()));
        detail_vp.setSaveEnabled(false);
    }

    private void initBroadcast(){
        changePageReceiver = new ChangePageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("changePage");
        getContext().registerReceiver(changePageReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class ChangePageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int page = intent.getIntExtra("page", 0);
            detail_vp.setCurrentItem(page);
        }
    }

    public class ViewPagerAdapter extends FragmentStateAdapter{
        private int size;
        public ViewPagerAdapter(@NonNull Fragment fragment, int size) {
            super(fragment);
            this.size = size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new DetailFragment(position);
        }

        @Override
        public int getItemCount() {
            return size;
        }
    }
}