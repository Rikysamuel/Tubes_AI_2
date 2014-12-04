<%@page import="java.util.Vector"%>
<%@page import="wekaexplorer.WekaExplorer"%>
<%@page import="weka.core.Instances"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" text="text/css" href="style.css"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tubes 2 AI | CLASSIFIER </title>        
    </head>
    <body>
        <div id="wrap">
            <div class="logo">
                 NEWS - CLASSIFIER
            </div>
            <div class="bmwrapper">
                <a class="bm" href="index.jsp?var=build">BUILD MODEL</a>
            </div>
            <hr>
            <div class="left">
                <form action="index.jsp" method="post">
                    <br/><input type = "text" name="judul" placeholder="Judul" class="underlined"/>
                   <br/><label for="konten"></label><TEXTAREA class="underlined" NAME="konten" ROWS="5" COLS="25" id="konten" placeholder="Konten"></TEXTAREA>
                   <br/><br/>
                    <input type="submit" value="Classify from text" class="submit"/>
                </form>
                <form action="index.jsp" method="post">
                  <h2>Browse File : </h2> 
                    <input type="file" id="file" name="file" class="browse"/>
                  <br/><br/>
                  <input  type="submit" value="Classify from file" class="submit"/>
               </form>
                
            </div>
            
            <% 
                
                WekaExplorer W = new WekaExplorer();
                String act = request.getParameter("var");
                if (act!=null){
                    if (act.equalsIgnoreCase("build")){
                        Instances data2 = W.getDataFromDB("SELECT FULL_TEXT,JUDUL,LABEL FROM artikel NATURAL JOIN artikel_kategori_verified NATURAL JOIN kategori");
                        W.PrintToARFF(data2, "C:\\Users\\Stephen\\Documents\\kuliah\\dataset.arff");
                        W.LoadDataset("C:\\Users\\Stephen\\Documents\\kuliah\\dataset.arff");

                        Instances dataTraining = W.getFilterNominalToString(W.getdata());
                        W.PrintToARFF(dataTraining, "C:\\Users\\Stephen\\Documents\\kuliah\\dataset.string.arff");
                        W.setClassifier(W.getClassifierFiltered(dataTraining));
                        W.PrintModel("C:\\Users\\Stephen\\Documents\\kuliah\\model.model");
                    }
                    if(act.equalsIgnoreCase("load")){
                        W.setClassifier(W.LoadModel("C:\\Users\\Stephen\\Documents\\kuliah\\model.model"));
                    }
                }
                String judul = request.getParameter("judul");
                String article = request.getParameter("konten");
                String file = request.getParameter("file");
                
                if(judul!=null){
                    
                    article = article.replaceAll("[\n\r\t]", " ");
                    
                    W.setClassifier(W.LoadModel("C:\\Users\\Stephen\\Documents\\kuliah\\model.model"));
                    //System.out.println("article: " + article);
                    //System.out.println("judul: " + judul);
                    W.readInput(judul, article, "C:\\Users\\Stephen\\Documents\\kuliah\\unlabeled.string.arff");

                    W.LoadFromFile("C:\\Users\\Stephen\\Documents\\kuliah\\unlabeled.string.arff", true);
                    Instances dataUnlabeled=W.getUnlabeled();

                    // Test data unlabeled
                    //System.out.println("classifying data............");
                    W.ClassifyInstances(W.getClassifier(),dataUnlabeled);
                    
                    Cookie cookiejudul = new Cookie("judul",judul);
                    cookiejudul.setMaxAge(60*60*2);
                    response.addCookie(cookiejudul);
                    Cookie cookiearticle = new Cookie("article",article);
                    cookiearticle.setMaxAge(60*60*2);
                    response.addCookie(cookiearticle);
                }
                else if(file!=null)
                {
                    file = application.getRealPath("/").replace("\\", "\\\\") + file;
                    W.setClassifier(W.LoadModel("C:\\Users\\Stephen\\Documents\\kuliah\\model.model"));
                    W.loadCSV(file);
                    W.CSVtoARFF(W.getdata(), application.getRealPath("/").replace("\\", "\\\\") + "unlabeledcsv.arff");
                    W.LoadUnkownLabel(application.getRealPath("/").replace("\\", "\\\\") + "unlabeledcsv.arff");
                    
                    Instances dataUnlabeled=W.getUnlabeled();
                    Instances labeled = W.ClassifyCSVInstances(W.getClassifier(),dataUnlabeled);
                    W.ARFFtoCSV(labeled, application.getRealPath("/").replace("\\", "\\\\") + "output.csv");
                }
        %>
            
            <div class="right">
                <div class="result1">
                BERITA TERMASUK KE DALAM KATEGORI : 
            </div>         
           
            <div class="result2">
                <% 
                    if (W.getPrediction()==null){
                        out.println("\"?\"");
                    }else{
                        out.println(W.getPrediction());
                    }
                %>
            </div>
            <br/><br/><br/><br/>
            <form method="get" action="#">
                <div class="change"><br>
                    <opsi>
                        KATEGORI SALAH? KATEGORI SEHARUSNYA : 
                        <select name="select-opt">
                            <option value="0">-----------------------------------</option>
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
                    </opsi>
                    <input type="submit" class="submit" value="LEARN"/>
                </div>
            </form>
            <%
                     String opt = request.getParameter("select-opt");
                     if(opt!=null){    
                            Integer getParam = Integer.parseInt(opt);
                            String val = null;
                            switch(getParam){
                               case 1 : 
                                   val = "\'Pendidikan\'"; break;
                               case 2 :
                                   val = "\'Politik\'"; break;
                               case 3:
                                   val = "\'Hukum dan Kriminal\'"; break;
                               case 4:
                                   val = "\'Sosial Budaya\'"; break;
                               case 5:
                                   val = "\'Olahraga\'"; break;
                               case 6:
                                   val = "\'Teknologi dan Sains\'"; break;
                               case 7:
                                   val = "\'Hiburan\'"; break;
                               case 8:
                                   val = "\'Bisnis dan Ekonomi\'"; break;
                               case 9:
                                   val = "\'Kesehatan\'"; break;
                               case 10:
                                   val = "\'Bencana dan Kecelakaan\'"; break;
                           }
                           if(getParam>0){
                               // Mencari cookie judul dan article yang telah disimpan
                               int i=0;
                               Cookie[] cookies = request.getCookies();
                               String judulfromcookies = "";
                               String articlefromcookies = "";
                               while(i<cookies.length) {
                                   if(cookies[i].getName().equalsIgnoreCase("judul")) {
                                       judulfromcookies = cookies[i].getValue();
                                   }
                                   else if(cookies[i].getName().equalsIgnoreCase("article")) {
                                       articlefromcookies = cookies[i].getValue();
                                   }
                                   i++;
                               }
                               judulfromcookies = judulfromcookies.replace("\"", "");
                               articlefromcookies = articlefromcookies.replace("\"", "");
                               
                               String query1= "INSERT INTO `news_aggregator`.`artikel` (`ID_ARTIKEL`, `HTML`, `FULL_TEXT`, `TGL_TERBIT`, `TGL_CRAWL`, `JUDUL`, `URL`, `INFO_WHAT`, `INFO_WHERE`, `INFO_WHY`, `INFO_WHO`, `INFO_WHEN`, `INFO_HOW`) VALUES(NULL, NULL, \""+ articlefromcookies +"\", NULL, NULL, \""+ judulfromcookies +"\", NULL, NULL, NULL, NULL, NULL, NULL, NULL);";
                               System.out.println(query1);
                               W.getDataFromDB(query1);
                               int idartikel=W.getIDdata("SELECT ID_ARTIKEL FROM artikel WHERE JUDUL=\""+ judulfromcookies +"\"");
                               int idkategori=W.getIDdata("SELECT ID_KELAS FROM kategori WHERE LABEL= " + val + "");
                               String query="INSERT INTO artikel_kategori_verified (`ID_ARTIKEL`,`ID_KELAS`) VALUES ("+idartikel+","+idkategori+")";
                               W.getDataFromDB(query);
                           }
                     }
            %>
            </div>            
        </div>
    </body>
</html>



                