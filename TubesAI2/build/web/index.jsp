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
                <form action="" method="post">
                  <h2>Browse File : </h2> 
                    <input type="file" value="Browse Dir" class="browse"/>
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
                        W.PrintToARFF(data2, "D:\\dataset.arff");
                        W.LoadDataset("D:\\dataset.arff");

                        Instances dataTraining = W.getFilterNominalToString(W.getdata());
                        W.PrintToARFF(dataTraining, "D:\\dataset.string.arff");
                        W.setClassifier(W.getClassifierFiltered(dataTraining));
                        W.PrintModel("D:\\model.model");
                    }
                    if(act.equalsIgnoreCase("load")){
                        W.setClassifier(W.LoadModel("D:\\model.model"));
                    }
                }
                String judul = request.getParameter("judul");
                String article = request.getParameter("konten");
//                if(article.indexOf("\n")>=0){
//                    article = article.replace("\n", " ");
//                }
//                out.println(judul);
//                out.println(article);
                if(judul!=null){
                    System.out.println("masuk");
                        W.setClassifier(W.LoadModel("D:\\model.model"));
                        System.out.println("article: " + article);
                        System.out.println("judul: " + judul);
                        W.readInput(judul, article, "D:\\unlabeled.string.arff");

                        W.LoadFromFile("D:\\unlabeled.string.arff", true);
                        Instances dataUnlabeled=W.getUnlabeled();

                        // Test data unlabeled
                        System.out.println("classifying data............");
                        W.ClassifyInstances(W.getClassifier(),dataUnlabeled);
                }
        %>
            
            <div class="right">
                <div class="result1">
                BERITA TERMASUK KE DALAM KATEGORI : 
            </div>         
           
            <div class="result2">
                <% out.println(W.getPrediction());%>
            </div>
            <br/><br/><br/><br/><br/><br/>
            <form method="get" action="index.jsp">
                <opsi>
                    KATEGORI SALAH? KATEGORI SEHARUSNYA : 
                    <select name="select-opt">
                        <option value="0"></option>
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
                <!--<br/><input type="submit" value="rebuild"/>-->
            </form>
             <div class="change">
                <BR>
                <button type="button">TAMBAHKAN KE DALAM BASIS DATA</button>
                <BR><button type="button">BUAT ULANG MODEL</button>
            </div>
            </div>            
        </div>
        <% 
       //     WekaExplorer w = new WekaExplorer();
        %>
        
    </body>
</html>



                