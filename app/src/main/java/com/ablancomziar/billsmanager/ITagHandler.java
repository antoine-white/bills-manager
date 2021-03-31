package com.ablancomziar.billsmanager;

import java.util.List;

public interface ITagHandler {
    List<ITag> getTags();
    ITag getTagById(int id);
    ITag addTag(String name);
}
