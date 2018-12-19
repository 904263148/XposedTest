package com.dktlh.ktl.xposedtest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Tag
{
  private ArrayList<Tag> mChildren = new ArrayList();
  private String mContent;
  private String mName;
  private String mPath;
  
  Tag(String paramString1, String paramString2)
  {
    this.mPath = paramString1;
    this.mName = paramString2;
  }
  
  void addChild(Tag paramTag)
  {
    this.mChildren.add(paramTag);
  }
  
  Tag getChild(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < this.mChildren.size())) {
      return (Tag)this.mChildren.get(paramInt);
    }
    return null;
  }
  
  ArrayList<Tag> getChildren()
  {
    return this.mChildren;
  }
  
  int getChildrenCount()
  {
    return this.mChildren.size();
  }
  
  String getContent()
  {
    return this.mContent;
  }
  
  HashMap<String, ArrayList<Tag>> getGroupedElements()
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator = this.mChildren.iterator();
    while (localIterator.hasNext())
    {
      Tag localTag = (Tag)localIterator.next();
      String str = localTag.getName();
      ArrayList localArrayList2 = (ArrayList)localHashMap.get(str);
      ArrayList localArrayList1 = localArrayList2;
      if (localArrayList2 == null)
      {
        localArrayList1 = new ArrayList();
        localHashMap.put(str, localArrayList1);
      }
      localArrayList1.add(localTag);
    }
    return localHashMap;
  }
  
  String getName()
  {
    return this.mName;
  }
  
  String getPath()
  {
    return this.mPath;
  }
  
  boolean hasChildren()
  {
    return this.mChildren.size() > 0;
  }
  
  void setContent(String paramString)
  {
    int k = 0;
    int j = k;
    if (paramString != null)
    {
      int i = 0;
      for (;;)
      {
        j = k;
        if (i >= paramString.length()) {
          break;
        }
        j = paramString.charAt(i);
        if ((j != 32) && (j != 10))
        {
          j = 1;
          break;
        }
        i += 1;
      }
    }
    if (j != 0) {
      this.mContent = paramString;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Tag: ");
    localStringBuilder.append(this.mName);
    localStringBuilder.append(", ");
    localStringBuilder.append(this.mChildren.size());
    localStringBuilder.append(" children, Content: ");
    localStringBuilder.append(this.mContent);
    return localStringBuilder.toString();
  }
}

