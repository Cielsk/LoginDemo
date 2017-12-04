package com.cielyang.android.login.data.remote;

import com.cielyang.android.login.data.entities.Account;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserQueryResponse {

    @SerializedName("results")
    private List<Account> results;

    public List<Account> getResults() {
        return results;
    }

    public void setResults(List<Account> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "UserQueryResponse{" + "results = '" + results + '\'' + "}";
    }
}
