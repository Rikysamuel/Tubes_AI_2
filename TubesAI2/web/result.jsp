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
//        
//                    // Membuat model dan Mengklasifikasikan data yang belum berlabel
//                    W.getClassifierFiltered(dataTraining, dataUnlabeled);   
                %>
           
            <div class="result2">
                <% // out.println(W.prediction); %>
            </div>
            <br/><br/><br/><br/><br/><br/>
            <opsi>
                Kategori Salah? Kategori seharusnya : 
                <select name="select-opt">
                    <option value="pendidikan">PENDIDIKAN</option>
                    <option value="politik">POLITIK</option>
                    <option value="hukumdankriminal">HUKUM DAN KRIMINAL</option>
                    <option value="sosialbudaya">SOSIAL BUDAYA</option>
                    <option value="olahraga">OLAHRAGA</option>
                    <option value="teknologidansains">TEKNOLOGI DAN SAINS</option>
                    <option value="hiburan">HIBURAN</option>
                    <option value="bisnisdanekonomi">BISNIS DAN EKONOMI</option>
                    <option value="kesehatan">KESEHATAN</option>
                    <option value="bencanadankecelakaan">BENCANA DAN KECELAKAAN</option>
                  </select>
            </opsi>
             <div class="change">
                 <button type="button" onclick="<%
//                    String query1= "INSERT INTO `news_aggregator`.`artikel` (`ID_ARTIKEL`, `HTML`, `FULL_TEXT`, `TGL_TERBIT`, `TGL_CRAWL`, `JUDUL`, `URL`, `INFO_WHAT`, `INFO_WHERE`, `INFO_WHY`, `INFO_WHO`, `INFO_WHEN`, `INFO_HOW`) VALUES(NULL, NULL, "+ article +", NULL, NULL, "+ judul +", NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
//                    String idjudul="";
                        out.println(request.getParameter("select-opt"));
//                    String kategori="SELECT ID_KELAS WHERE LABEL="+  +";
//                    String query="INSERT INTO artikel_kategori_verified ()";
                 %>">re-build model</button>
            </div>
        </div>
        <% 
       
        %>
        
    </body>
</html>
