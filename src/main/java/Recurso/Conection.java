/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recurso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author User
 */
public class Conection {

    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36";
    String loginUrl = "https://blackdesert.playredfox.com/black_desert/sign_in";
    String marketUrlBefore = "https://blackdesert-tradeweb.playredfox.com/";
    String marketUrlAfter = "https://blackdesert-tradeweb.playredfox.com/Home/ConfirmSecondPwd";
    String marketUrlAfterW= "https://blackdesert.playredfox.com/black_desert/wmarket";
    String listglobal = "https://blackdesert-tradeweb.playredfox.com/Home/GetWorldMarketHotList";
    
    String username = "";
    String password = "";
    String secondPass = "";

    HashMap<String, String> cookies = new HashMap<>();
    HashMap<String, String> formDataLogin = new HashMap<>();
    HashMap<String, String> formDataSecondPass = new HashMap<>();
    HashMap<String, String> SearchListGlocal = new HashMap<>();
    

    public static void main(String[] args) {
        Conection ck = new Conection();
        ck.connect();
    }

    public void connect() {

        try {

            Connection.Response loginForm = Jsoup.connect(loginUrl).method(Connection.Method.GET).execute();
            Document loginDoc = loginForm.parse();

            cookies.putAll(loginForm.cookies());

            String authToken = loginDoc.select("form >input[name='authenticity_token']").attr("value");
            System.out.println(authToken);
            formDataLogin.put("utf8", "e2 9c 93");
            formDataLogin.put("authenticity_token", authToken);
            formDataLogin.put("user[email]", username);
            formDataLogin.put("user[password]", password);
            formDataLogin.put("commit", "ENTRAR");

            //System.out.println(formDataLogin);

            Connection.Response afterLoginPage = Jsoup.connect(loginUrl)
                    .cookies(cookies)
                    .data(formDataLogin)
                    .method(Connection.Method.POST)
                    .execute();

            Connection.Response marketPageBeforePass = Jsoup.connect(marketUrlBefore)
                    .cookies(afterLoginPage.cookies())
                    .execute();
            
        //    System.out.println(marketPageBeforePass.parse().toString());
            
//            SearchListGlocal.put("__RequestVerificationToken", authToken);
//            SearchListGlocal.put("searchText", "Frenesi");
//            
//            Document List = Jsoup.connect(listglobal)
//                    .cookies(afterLoginPage.cookies())
//                    .data(SearchListGlocal)
//                    .method(Connection.Method.POST)
//                    .userAgent(USER_AGENT)
//                    .get();
//            
       //      System.out.println(marketPageBeforePass.select("form[id='frmConfirmSecondPass']>input[name='__RequestVerificationToken']").attr("value"));
            
            
//             Document list = Jsoup.connect(listglobal)
//                    .cookies(cookies)
//                    .method(Connection.Method.POST)
//                    .userAgent(USER_AGENT)
//                    .get();
             
       //      System.out.println(list.toString());
       
       

            formDataSecondPass.put("secondPwd", secondPass);;
            formDataSecondPass.put("__RequestVerificationToken", marketPageBeforePass.parse().
                    select("form[id='frmConfirmSecondPass']>input[name='__RequestVerificationToken']").attr("value"));
            
//            formDataSecondPass.put("searchText", "Frenesi");
//            formDataSecondPass.put("__RequestVerificationToken", marketPageBeforePass.parse().
//                    select("form[id='frmConfirmSecondPass']>input[name='__RequestVerificationToken']").attr("value"));
            
            

            Connection.Response  marketPageAfterPass = Jsoup.connect(marketUrlAfter)
                    .cookies(marketPageBeforePass.cookies())
                    .data(formDataSecondPass)
                    .method(Connection.Method.POST)
                    .execute();
            
            System.out.println(marketPageAfterPass.statusCode() + " : " + marketPageAfterPass.url());
            System.out.println(marketPageAfterPass.parse());
                    

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
