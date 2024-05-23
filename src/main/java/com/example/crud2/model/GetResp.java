package com.example.crud2.model;

import com.example.crud2.entity.UserEntity;
import java.util.List;

public class GetResp {
    public long total_results;
    public int page;
    public int results_per_page;
    public List<UserEntity> results;

    public GetResp(long total_results, int page, int results_per_page, List<UserEntity> userEntities) {
        this.total_results = total_results;
        this.page = page;
        this.results_per_page = results_per_page;
        this.results = userEntities;
    }
}
