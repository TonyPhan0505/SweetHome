package com.example.sweethome;

import java.util.Date;

public interface IFilterable {
    public void filterByDate(Date startDate, Date endDate);
    public void filterByKeyword(String keyword);
    public void filterByMake(String make);
    public void filterByTag(String tag);
}
