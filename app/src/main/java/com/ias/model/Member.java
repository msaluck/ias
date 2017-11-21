package com.ias.model;

import android.media.Image;

/**
 * Created by user on 9/25/2017.
 */

public class Member {

    String userName;
    String fullName;
    String stnkNumber;
    String chapterName;
    String imageProfile;
    String branchName;
    String brandName;
    String noSim;
    String carTypeName;
    String year;
    String communityAdminName;
    String iasAdminName;
    String regionName;
    String hp;

    public Member(String imageProfile, String userName, String fullName, String stnkNumber,String chapterName){
        this.imageProfile=imageProfile;
        this.userName=userName;
        this.fullName=fullName;
        this.stnkNumber=stnkNumber;
        this.chapterName=chapterName;
    }

    public Member(String imageProfile, String userName, String fullName, String stnkNumber,String chapterName,String noSim, String branchName,String brandName, String carTypeName, String year, String communityAdminName, String iasAdminName, String regionName, String hp){
        this.imageProfile=imageProfile;
        this.userName=userName;
        this.fullName=fullName;
        this.stnkNumber=stnkNumber;
        this.chapterName=chapterName;
        this.noSim=noSim;
        this.branchName = branchName;
        this.brandName=brandName;
        this.carTypeName=carTypeName;
        this.year=year;
        this.communityAdminName=communityAdminName;
        this.iasAdminName=iasAdminName;
        this.regionName=regionName;
        this.hp=hp;

    }

    public Member(){

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getStnkNumber() {
        return stnkNumber;
    }

    public void setStnkNumber(String stnkNumber) {
        this.stnkNumber = stnkNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getNoSim() {
        return noSim;
    }

    public void setNoSim(String noSim) {
        this.noSim = noSim;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCommunityAdminName() {
        return communityAdminName;
    }

    public void setCommunityAdminName(String communityAdminName) {
        this.communityAdminName = communityAdminName;
    }

    public String getIasAdminName() {
        return iasAdminName;
    }

    public void setIasAdminName(String iasAdminName) {
        this.iasAdminName = iasAdminName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }
}
