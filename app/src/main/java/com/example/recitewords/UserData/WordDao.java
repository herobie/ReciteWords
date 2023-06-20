package com.example.recitewords.UserData;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.recitewords.model.Word;

import java.util.List;
@Dao
public interface WordDao {
    //插入词语
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWord(Word ... words);
    //获取全部词语
    @Query("SELECT * FROM word ORDER BY id DESC")
    LiveData<List<Word>> queryAll();
    //删除指定词语
    @Query("DELETE FROM Word WHERE name=:name AND id=:id")
    void RemoveWord(int id, String name);
    //更新单词数据
    @Update
    void updateWord(Word...word);
    //随机获取指定数量的单词
    @Query("SELECT * FROM Word ORDER BY Random() LIMIT :limit")
    LiveData<List<Word>> queryRandomReviewWords(int limit);
    //随机获取指定tag的单词
    @Query("SELECT * FROM Word WHERE tags =:tag ORDER BY Random() LIMIT :limit")
    LiveData<List<Word>> queryRandomReviewWordByTag(String tag, int limit);
    //根据标签获取数据
    @Query("SELECT * FROM Word WHERE tags =:tag ORDER BY id DESC")
    LiveData<List<Word>> queryWordByTag(String tag);
    //获取所有标题
    @Query("SELECT DISTINCT tags FROM Word ")
    LiveData<List<String>> queryTags();
    //搜索栏搜索返回
    @Query("SELECT * FROM Word WHERE name LIKE:name AND tags=:tag ORDER BY id DESC")
    LiveData<List<Word>> query(String name, String tag);
    //搜索栏搜索返回(全部tag)
    @Query("SELECT * FROM Word WHERE name LIKE:name ORDER BY id DESC")
    LiveData<List<Word>> query(String name);
}
