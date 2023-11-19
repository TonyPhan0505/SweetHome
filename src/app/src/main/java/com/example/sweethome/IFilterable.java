package com.example.sweethome;

import com.google.firebase.Timestamp;

public interface IFilterable {
    public void filterByDate(Timestamp startDate, Timestamp endDate);
    public void filterByKeyword(String keyword);
    public void filterByMake(String make);
    public void filterByTag();
}
