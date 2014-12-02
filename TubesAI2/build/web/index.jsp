<%@page import="weka.core.Instances"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import ="wekaexplorer.WekaExplorer" %>
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
                 News - Classifier
            </div>
                <form action="result.jsp" method="post">
                    <br/><input type = "text" name="judul" placeholder="Judul" id="judul" class="underlined"/>
                    <br/><input type = "text" name="konten" placeholder="Konten" id="artikel" class="underlined"/>
                   <br/><br/>
                  <h2>Browse File : </h2> 
                  <input type="file" value="Browse Dir" class="browse"/>
                      <br/><br/><br/><input  type="submit" value="Classify" class="submit" />
               </form>
        </div>
    </body>
</html>
