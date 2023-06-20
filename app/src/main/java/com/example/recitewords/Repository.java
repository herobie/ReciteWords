package com.example.recitewords;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.recitewords.UserData.UserDataBase;
import com.example.recitewords.model.Word;
import com.example.recitewords.UserData.WordDao;

import java.util.List;

public class Repository {
    private WordDao wordDao;
    private Context context;
    private MainViewModel viewModel;
    public Repository(Context context, MainViewModel viewModel){
        this.context = context;
        this.viewModel = viewModel;
        UserDataBase userDataBase = UserDataBase.getInstance(context.getApplicationContext());
        wordDao = userDataBase.getWordDao();
    }

    public void queryWord(String args, String currentTags){
        new Query(args, currentTags).execute();
    }

    //获取所有tag
    public void getAllTags(){
        new GetAllTags().execute();
    }

    //获取全部数据
    public void getAllData(){
        new GetDetailedAllData().execute();
    }
    //添加单词
    public void insertWord(Word...words){new Insert().execute(words);}
    //移除单词
    public void removeWord(int id, String name){new Remove(name, id).execute();}
    //更新单词
    public void updateWord(Word word){new Update(word).execute();}
    //随机获取单词
    public void queryRandomWords(String tag, int limit){
        new QueryRandomWords(tag, limit).execute();
    }
    //根据标签获取单词
    public void queryByTag(String tag){
        new QueryByTag(tag).execute();
    }

    private class GetDetailedAllData extends AsyncTask<Void, Void, LiveData<List<Word>>>{


        @Override
        protected LiveData<List<Word>> doInBackground(Void... voids) {
            return wordDao.queryAll();
        }

        @Override
        protected void onPostExecute(LiveData<List<Word>> listLiveData) {
            super.onPostExecute(listLiveData);
            viewModel.setWordLiveData(listLiveData);
            queryRandomWords(viewModel.getConstraints()[1], Constant.reviewLimit);
            //获取全部tag信息
            getAllTags();
        }
    }

    private class Insert extends AsyncTask<Word, Void, Void>{

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWord(words);
            return null;
        }
    }

    private class Remove extends AsyncTask<Void, Void, Void>{
        private final String name;
        private final int id;

        public Remove(String name, int id) {
            this.name = name;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.RemoveWord(id, name);
            return null;
        }
    }

    private class Update extends AsyncTask<Void, Void, Void>{
        private final Word word;

        public Update(Word word) {
            this.word = word;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.updateWord(word);
            return null;
        }
    }

    private class GetAllTags extends AsyncTask<Void, Void, LiveData<List<String>>>{
        @Override
        protected LiveData<List<String>> doInBackground(Void... voids) {
            return wordDao.queryTags();
        }

        @Override
        protected void onPostExecute(LiveData<List<String>> listLiveData) {
            super.onPostExecute(listLiveData);
            //            viewModel.setTags(strings);
//
            viewModel.setTagsLiveData(listLiveData);
            viewModel.getIsTagsReady().postValue(true);
        }
    }

    private class QueryRandomWords extends AsyncTask<Void, Void, LiveData<List<Word>>>{
        private final int limit;
        private final String tag;
        public QueryRandomWords(String tag, int limit) {
            this.limit = limit;
            this.tag = tag;
        }

        @Override
        protected LiveData<List<Word>> doInBackground(Void...voids) {
            if (tag.equals("全部")){
                return wordDao.queryRandomReviewWords(limit);
            }else {
                return wordDao.queryRandomReviewWordByTag(tag, limit);
            }
        }

        @Override
        protected void onPostExecute(LiveData<List<Word>> listLiveData) {
            super.onPostExecute(listLiveData);
            viewModel.setReviewData(listLiveData);
            //发送广播通知数据已经就绪，开始注册Observer
            Intent intent = new Intent("dataReady");
            context.sendBroadcast(intent);
            Log.d(Constant.TAG, "dataReady");
        }
    }

    private class QueryByTag extends AsyncTask<Void, Void, LiveData<List<Word>>>{
        private final String tag;

        public QueryByTag(String tag) {
            this.tag = tag;
        }

        @Override
        protected LiveData<List<Word>> doInBackground(Void... voids) {
            return wordDao.queryWordByTag(tag);
        }

        @Override
        protected void onPostExecute(LiveData<List<Word>> listLiveData) {
            super.onPostExecute(listLiveData);
            viewModel.setWordLiveData(listLiveData);
            Intent intent = new Intent("dataReady");
            context.sendBroadcast(intent);
            Log.d(Constant.TAG, "dataReady");
        }
    }

    private class Query extends AsyncTask<Void, Void, LiveData<List<Word>>>{
        private final String name, tag;

        public Query(String name, String tag) {
            this.name = name;
            this.tag = tag;
        }

        @Override
        protected LiveData<List<Word>> doInBackground(Void... voids) {
            if (tag.equals("全部")){
                return wordDao.query("%"+ name +"%");
            }
            return wordDao.query("%"+ name +"%", tag);
        }

        @Override
        protected void onPostExecute(LiveData<List<Word>> listLiveData) {
            super.onPostExecute(listLiveData);
            Log.d(Constant.TAG, "isLiveDataNull" + (listLiveData == null));
            viewModel.setWordLiveData(listLiveData);
            Intent intent = new Intent("dataReady");
            context.sendBroadcast(intent);
            Log.d(Constant.TAG, "dataReady");
        }
    }
}
