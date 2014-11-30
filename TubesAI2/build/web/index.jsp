<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import ="wekaexplorer.WekaExplorer" %>
<link rel="stylesheet" text="text/css" href="style.css"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tubes 2 AI </title>
        <% 
            WekaExplorer W = new WekaExplorer();
        %>
    </head>
    <body>
        <div id="wrap">
          <form action="soal" method="post">
             <br/><input type = "text" name="judul" placeholder="Judul" class="underlined"/>
             <br/><label for="konten"></label><TEXTAREA class="underlined" NAME="SpecialRequest" ROWS="5" COLS="25" id="konten" placeholder="Konten"></TEXTAREA>
             <br/>
             <br/><input type="submit" value="Classify" class="submit"/>
         </form>
            <form>
            <h2>Browse File : </h2> 
            <input type="file" value="Browse Dir"/></form>
        </div>
        
    </body>
</html>
