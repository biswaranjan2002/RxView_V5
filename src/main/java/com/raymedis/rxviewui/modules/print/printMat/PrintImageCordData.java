package com.raymedis.rxviewui.modules.print.printMat;


import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class PrintImageCordData {
   private Mat mat;
   private Rect layoutRect;

   public PrintImageCordData(Mat mat, Rect layoutRect) {
      this.mat = mat;
      this.layoutRect = layoutRect;
   }

   public PrintImageCordData(){

   }

   public Mat getMat() {
      return mat;
   }

   public void setMat(Mat mat) {
      this.mat = mat;
   }

   public Rect getLayoutRect() {
      return layoutRect;
   }

   public void setLayoutRect(Rect layoutRect) {
      this.layoutRect = layoutRect;
   }

   @Override
   public String toString() {
      return "PrintImageCordData{" +
              "mat=" + mat +
              ", layoutRect=" + layoutRect +
              '}';
   }
}
