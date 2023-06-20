package com.example.recitewords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.recitewords.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        acquirePermissions();
        initView();
    }

    private void acquirePermissions(){
        if(ContextCompat.checkSelfPermission(this , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 1);
        }
        if(ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} , 1);
        }
    }

    private void initView(){
        setSupportActionBar(binding.toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frag_container, viewModel.getMainFragment());
        transaction.commit();

        //观察是否处于详情页面，用于切换Fragment
        viewModel.getIsInDetailedPage().observe(this, aBoolean -> {
            if (aBoolean){
                replaceFragment(viewModel.getMainFragment(),viewModel.getDetailBaseFragment(), true);
            }else {
                replaceFragment(viewModel.getDetailBaseFragment(), viewModel.getMainFragment(),false);
            }
        });
    }

    private void replaceFragment(Fragment from, Fragment to, boolean isAddToBackStack){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!to.isAdded()){
            transaction.hide(from).add(R.id.main_frag_container, to).commit();
        }else {
            transaction.hide(from).show(to).commit();
        }
//        transaction.replace(R.id.main_frag_container, from);
        if (isAddToBackStack){//判断是否加入返回栈
            transaction.addToBackStack(null);
        }
//        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Boolean.TRUE.equals(viewModel.getIsInDetailedPage().getValue())){
            viewModel.getIsInDetailedPage().setValue(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main , menu);
        Log.d(Constant.TAG, "createOptionMenu");
        initSearch(menu);
        return true;
    }

    private void initSearch(Menu menu) {
        SearchView searchView;
        MenuItem searchItem = menu.findItem(R.id.menu_search_view);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        Log.d(Constant.TAG, "SearchView" + (searchView.isActivated()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(Constant.TAG, "TextSubmit");
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.isEmpty()){
                    viewModel.getConstraints()[0] = s;
                    viewModel.getRepository().queryWord(s, viewModel.getConstraints()[1]);
                }else {
                    viewModel.getConstraints()[0] = null;
                    if (viewModel.getConstraints()[1].equals("全部")){
                        viewModel.getRepository().getAllData();
                    }else {
                        viewModel.getRepository().queryWord(s, viewModel.getConstraints()[1]);
                    }
                }
                return true;
            }
        });
    }
}