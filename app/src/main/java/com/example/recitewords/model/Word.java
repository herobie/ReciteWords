package com.example.recitewords.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.recitewords.util.StringUtil;

@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String translate;
    @ColumnInfo
    private String tags;
    @ColumnInfo
    private String sentence;
    @ColumnInfo
    private String sentenceTranslate;

    public String split(String s){
        return StringUtil.splitString(s);
    }

    public String splitWithNumber(String s){
        return StringUtil.splitStringWithNumber(s);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentenceTranslate() {
        return sentenceTranslate;
    }

    public void setSentenceTranslate(String sentenceTranslate) {
        this.sentenceTranslate = sentenceTranslate;
    }
}
