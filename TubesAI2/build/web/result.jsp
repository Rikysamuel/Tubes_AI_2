<%-- 
    Document   : result
    Created on : Dec 1, 2014, 12:05:30 PM
    Author     : Chrestella Stephanie
--%>

<%@page import="weka.core.Instances"%>
<%@page import="wekaexplorer.WekaExplorer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" text="text/css" href="style.css"/>
<!DOCTYPE html>
<html>
    <head>
        <% 
//            WekaExplorer W = new WekaExplorer();
//            Instances data2 = W.getDataFromDB("SELECT FULL_TEXT,JUDUL,LABEL FROM artikel NATURAL JOIN artikel_kategori_verified NATURAL JOIN kategori");
//            W.PrintToARFF(data2, "D:\\dataset.arff");
//            W.LoadDataset("D:\\dataset.arff");
//
//            Instances dataTraining = W.getFilterNominalToString(W.getdata());
//            W.PrintToARFF(dataTraining, "D:\\dataset.string.arff");
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tubes 2 AI | CLASSIFIER </title>
    </head>
    <body>
        <div id="wrap">
            <div class="result1">
                berita termasuk ke dalam kategori : 
            </div>
                <%
//                    String article = request.getParameter("konten");
//                    String judul = request.getParameter("judul");
//                    W.readInput(judul, article, "D:\\unlabeled.arff");
//
//                    W.LoadUnkownLabel("D:\\unlabeled.arff");
//                    Instances dataUnlabeled = W.getFilterNominalToStringTest(W.getUnlabeled()); 
//                    W.PrintToARFF(dataUnlabeled, "D:\\unlabeled.string.arff");
        
                    // Membuat model dan Mengklasifikasikan data yang belum berlabel
//                    W.getClassifierFiltered(dataTraining, dataUnlabeled);   
//                    W.prediction = "Hiburan";
//                %>
           
            <div class="result2">
                <% // out.println(W.prediction); System.out.println("lewat");%>
            </div>
            <br/><br/><br/><br/><br/><br/>
            <form>
                <opsi>
                    Kategori Salah? Kategori seharusnya : 
                    <select name="select-opt">
                        <option value="1">PENDIDIKAN</option>
                        <option value="2">POLITIK</option>
                        <option value="3">HUKUM DAN KRIMINAL</option>
                        <option value="4">SOSIAL BUDAYA</option>
                        <option value="5">OLAHRAGA</option>
                        <option value="6">TEKNOLOGI DAN SAINS</option>
                        <option value="7">HIBURAN</option>
                        <option value="8">BISNIS DAN EKONOMI</option>
                        <option value="9">KESEHATAN</option>
                        <option value="10">BENCANA DAN KECELAKAAN</option>
                    </select>
                    <!--<button type="button" value="a"/>re-build model-->
                </opsi>
                <br/><input type="submit" value="rebuild"/>
            </form>
             <div class="change">
                 <%
                     String val = null;
                     Integer getParam = Integer.parseInt(request.getParameter("select-opt"));
                     switch(getParam){
                        case 1 : 
                            val = "Pendidikan"; break;
                        case 2 :
                            val = "Politik"; break;
                        case 3:
                            val = "\'Hukum dan Kriminal\'"; break;
                        case 4:
                            val = "\'Sosial Budaya\'"; break;
                        case 5:
                            val = "Olahraga"; break;
                        case 6:
                            val = "\'Teknologi dan Sains\'"; break;
                        case 7:
                            val = "Hiburan"; break;
                        case 8:
                            val = "\'Bisnis dan Ekonomi\'"; break;
                        case 9:
                            val = "Kesehatan"; break;
                        case 10:
                            val = "\'Bencana dan Kecelakaan\'"; break;
                     }
//                    String query1= "INSERT INTO `news_aggregator`.`artikel` (`ID_ARTIKEL`, `HTML`, `FULL_TEXT`, `TGL_TERBIT`, `TGL_CRAWL`, `JUDUL`, `URL`, `INFO_WHAT`, `INFO_WHERE`, `INFO_WHY`, `INFO_WHO`, `INFO_WHEN`, `INFO_HOW`) VALUES(NULL, NULL, "+ article +", NULL, NULL, "+ judul +", NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
//                    System.out.println(query1);
//                    
//                    String idartikel="SELECT ID_ARTIKEL FROM artikel WHERE JUDUL="+judul+"";
////                        out.println(val);
//                    String idkategori="SELECT ID_KELAS FROM kategori WHERE LABEL="+ getParam +"";
//                    String query="INSERT INTO artikel_kategori_verified (`ID_ARTIKEL`,`ID_KELAS`) VALUES ("+idartikel+","+idkategori+")";
//                    System.out.println(query);
                 %>
            </div>
        </div>
        <% 
       
        %>
        
    </body>
</html>
