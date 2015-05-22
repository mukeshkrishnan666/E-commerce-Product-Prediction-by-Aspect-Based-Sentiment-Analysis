/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.project;


/**
 *
 * @author Jatan
 */
public class Review 
{  
    String productID;
    String title;
    String rating;
    String summary;
    String text;
    
    public Review(String[] details)
    {  
          productID = details[0];
          title = details[1];
          rating = details[2];
          summary = details[3];
          text = details[4];
    }  
    //public print
 }  