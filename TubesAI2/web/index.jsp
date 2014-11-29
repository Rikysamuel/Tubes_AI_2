<%@page import="java.io.FileWriter"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.text.AttributedCharacterIterator.Attribute"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.print.attribute.standard.MediaSize.Other"%>
<%@page import="weka.classifiers.bayes.NaiveBayes"%>
<%@page import="weka.classifiers.Classifier"%>
<%@page import="weka.core.Instances"%>
<%@page import="weka.filters.unsupervised.attribute.StringToWordVector"%>
<%@page import="weka.filters.Filter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="wekaexplorer.WekaExplorer" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
            WekaExplorer W = new WekaExplorer();
            
            // Meload data set dari file eksternal
            W.LoadDataset("C:\\Users\\Stephen\\Documents\\NetBeansProjects\\TubesAI2\\dataset.arff");
            
            // Membuat filter untuk merubah format data training
            StringToWordVector filter = W.getFilter();
            filter.setInputFormat(W.getdata());
            Instances dataTraining = Filter.useFilter(W.getdata(),filter);
            W.PrintToARFF(dataTraining, "C:\\Users\\Stephen\\Documents\\NetBeansProjects\\TubesAI2\\dataset.vector.arff");
            W.setDataset(dataTraining);

            // Meload data yang ingin diklasifikasi dari file eksternal
            W.LoadUnkownLabel("C:\\Users\\Stephen\\Documents\\NetBeansProjects\\TubesAI2\\unlabeled.arff");

            // Membuat filter untuk merubah format data unlabeled
            Instances dataUnlabeled = Filter.useFilter(W.getUnlabeled(), filter);
            W.PrintToARFF(dataUnlabeled, "C:\\Users\\Stephen\\Documents\\NetBeansProjects\\TubesAI2\\unlabeled.vector.arff");
            W.setUnlabeled(dataUnlabeled);

            // Membuat Classifier baru untuk kategorisasi dan di build
            Classifier bayes = new NaiveBayes();
            W.setClassifier(bayes);
            W.buildClassifier();

            // Mengklasifikasikan data yang belum berlabel
            Instances result = W.Classify();
            out.println(result);
        %>
    </body>
</html>
