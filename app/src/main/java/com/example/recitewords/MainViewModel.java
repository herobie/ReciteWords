package com.example.recitewords;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recitewords.model.Word;
import com.example.recitewords.ui.MainFragment;
import com.example.recitewords.ui.adapters.TagsAdapter;
import com.example.recitewords.ui.detail.DetailBaseFragment;
import com.example.recitewords.ui.dialog.InsertDialog;
import com.example.recitewords.ui.dialog.UpdateDialog;
import com.example.recitewords.ui.review.ReviewFragment;
import com.example.recitewords.ui.adapters.WordsAdapter;
import com.example.recitewords.ui.tags.TagsDialog;
import com.example.recitewords.ui.words.WordsFragment;
import com.example.recitewords.adapters.FragmentsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private WordsFragment wordsFragment;
    private ReviewFragment reviewFragment;
    private MainFragment mainFragment;
    private DetailBaseFragment detailBaseFragment;
    private FragmentsAdapter fragmentsAdapter;
    private Repository repository;
    private InsertDialog insertDialog;
    private UpdateDialog updateDialog;
    private TagsDialog tagsDialog;
    private TagsAdapter tagsAdapter;
    private WordsAdapter wordsAdapter;
    private MutableLiveData<String> currentDisplayedTags;
    private MutableLiveData<Boolean> isDataReady, isInDetailedPage, isTagsReady;
    private LiveData<List<Word>> wordLiveData;//数据库
    private LiveData<List<Word>> reviewData;
    private List<Word> words;//通过上面livedata获取的集合
    private List<Word> reviewWords;//同上，不过是复习的
    private LiveData<List<String>> tagsLiveData;//标签集合
    private List<String> tags;
    private String[] constraints = {null, "全部"};
    private final int[] colors = {R.drawable.blue_and_white_card, R.drawable.crystal_card, R.drawable.cyan_card,
            R.drawable.sharp_card, R.drawable.orange_card, R.drawable.yellow_card, R.drawable.sky_card,
            R.drawable.sky2_card, R.drawable.scientific_card, R.drawable.colorful_card};

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application, this);
        isDataReady = new MutableLiveData<>();
        currentDisplayedTags = new MutableLiveData<>();
        tags = new ArrayList<>();
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setWordLiveData(LiveData<List<Word>> wordLiveData) {
        this.wordLiveData = wordLiveData;
    }

    public void setTagsLiveData(LiveData<List<String>> tagsLiveData) {
        this.tagsLiveData = tagsLiveData;
    }

    public void setReviewData(LiveData<List<Word>> reviewData) {
        this.reviewData = reviewData;
    }

    public String[] getConstraints() {
        return constraints;
    }

    public MutableLiveData<String> getCurrentDisplayedTags() {
        return currentDisplayedTags;
    }

    public LiveData<List<Word>> getWordLiveData() {
        return wordLiveData;
    }

    public LiveData<List<Word>> getReviewData() {
        return reviewData;
    }

    public LiveData<List<String>> getTagsLiveData() {
        return tagsLiveData;
    }

    public MutableLiveData<Boolean> getIsInDetailedPage() {
        if (isInDetailedPage == null){
            isInDetailedPage = new MutableLiveData<>();
        }
        return isInDetailedPage;
    }

    public List<Word> getWords() {
        return words;
    }

    public List<Word> getReviewWords() {
        return reviewWords;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setReviewWords(List<Word> reviewWords) {
        this.reviewWords = reviewWords;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public MutableLiveData<Boolean> getIsDataReady() {
        return isDataReady;
    }

    public MutableLiveData<Boolean> getIsTagsReady() {
        if (isTagsReady == null){
            isTagsReady = new MutableLiveData<>();
        }
        return isTagsReady;
    }

    public InsertDialog getInsertDialog(Context context) {
        if (insertDialog == null){
            insertDialog = new InsertDialog(context, this);
        }
        return insertDialog;
    }

    public UpdateDialog getUpdateDialog(Context context) {
        if (updateDialog == null){
            updateDialog = new UpdateDialog(context, this);
        }
        return updateDialog;
    }

    public TagsDialog getTagsDialog(Context context) {
        if (tagsDialog == null){
            tagsDialog = new TagsDialog(context, this);
        }
        return tagsDialog;
    }

    public void setFragmentsAdapter(FragmentsAdapter fragmentsAdapter) {
        this.fragmentsAdapter = fragmentsAdapter;
    }

    public TagsAdapter getTagsAdapter(Context context) {
        if (tagsAdapter == null){
            tagsAdapter = new TagsAdapter(this, context);
        }
        return tagsAdapter;
    }

    public WordsAdapter getWordsAdapter(Context context) {
        if (wordsAdapter == null){
            wordsAdapter = new WordsAdapter(context, this);
        }
        return wordsAdapter;
    }

    public FragmentsAdapter getFragmentsAdapter() {
        return fragmentsAdapter;
    }

    public Repository getRepository() {
        return repository;
    }

    public ReviewFragment getReviewFragment() {
        if (reviewFragment == null){
            reviewFragment = ReviewFragment.getInstance();
        }
        return reviewFragment;
    }

    public WordsFragment getWordsFragment() {
        if (wordsFragment == null){
            wordsFragment = WordsFragment.getInstance();
        }
        return wordsFragment;
    }

    public MainFragment getMainFragment() {
        if (mainFragment == null){
            mainFragment = new MainFragment();
        }
        return mainFragment;
    }

    public DetailBaseFragment getDetailBaseFragment() {
        if (detailBaseFragment == null){
            detailBaseFragment = new DetailBaseFragment();
        }
        return detailBaseFragment;
    }

    public int[] getColors() {
        return colors;
    }
}
