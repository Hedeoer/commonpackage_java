package pojo;

import java.util.Optional;

public class Book {
   String title;
   Optional<String> subTitle;
   
   // getters and setters omitted

   public Book() {
   }

   public Book(String title, Optional<String> subTitle) {
      this.title = title;
      this.subTitle = subTitle;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public Optional<String> getSubTitle() {
      return subTitle;
   }

   public void setSubTitle(Optional<String> subTitle) {
      this.subTitle = subTitle;
   }

   @Override
   public String toString() {
      return "Book{" +
              "title='" + title + '\'' +
              ", subTitle=" + subTitle +
              '}';
   }
}