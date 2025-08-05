package com.raymedis.rxviewui.modules.study.tabmanagement;

public interface TabNode {
  TabNode  getNextTab();
  TabNode getPreviousTab();
  void setNextTab(TabNode tab);
  void setPreviousTab(TabNode tab);
}
