package com.raymedis.rxviewui.modules.study.tabmanagement;

import java.util.List;

public interface TabManager { // change the method dataype
      void addTab(String id, TabNode tab);
      void removeTab(String id);
      void markTabAsEdited(String id);
      void moveTab(String id,int index);
      TabNode getTab(String id);
      List<TabNode> getAllTabs();
}
