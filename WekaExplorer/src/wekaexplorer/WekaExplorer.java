package wekaexplorer;

import java.io.*;
<<<<<<< HEAD
import java.util.Enumeration;
=======
>>>>>>> bae1a7aec34e4e68a6d72528d06dc4430a7d4f44
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
<<<<<<< HEAD
import weka.core.*;
=======
import weka.core.Instance;
import weka.core.Instances;
>>>>>>> bae1a7aec34e4e68a6d72528d06dc4430a7d4f44
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.tokenizers.Tokenizer;
import weka.core.tokenizers.WordTokenizer;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaExplorer {

    private Instances data;
    private Instances unlabeled;
<<<<<<< HEAD
    private Classifier classifier;
	
    // Method untuk mengset data training
    public void setDataset(Instances _data)
    {
        data = _data;
    }
    
    // Method untuk mengset data yang tidak berlabel
    public void setUnlabeled(Instances _unlabeled)
    {
        unlabeled = _unlabeled;
    }
    
=======
	
>>>>>>> bae1a7aec34e4e68a6d72528d06dc4430a7d4f44
    // Method untuk mengload instance yg ingin diklasifikasi dari file eksternal
    public void LoadUnkownLabel(String file) 
    {
        LoadFromFile(file,true);
    }
	
    // Method untuk mengload data set dari file eksternal
    public void LoadDataset(String file)
    {
        LoadFromFile(file,false);
    }
    
    // Mengload file .arff (baik data set maupun instance yang ingin diklasifikasi)
    private void LoadFromFile(String file, boolean unknown)
    {
       try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            ArffReader arff = new ArffReader(reader, 1000);
            Instances temp;
            temp = arff.getStructure();
            temp.setClassIndex(temp.numAttributes() - 1);
            Instance inst;
            while ((inst = arff.readInstance(temp)) != null) {
                temp.add(inst);
            }
            
            if (unknown) {
                this.unlabeled = temp;
            } else {
                this.data = temp;
            }
        }catch(Exception e) {}
    }
    
    // Method untuk menampilkan hasil statistik pembelajaran dengan 10-fold cross validation
<<<<<<< HEAD
    public void FoldSchema()
=======
    public void FoldSchema(Classifier classifier)
>>>>>>> bae1a7aec34e4e68a6d72528d06dc4430a7d4f44
    {
        try{
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(classifier, data, 10, new Random(1));
            System.out.println(eval.toSummaryString("\nResults 10 folds cross-validation\n\n", false));
        }catch(Exception e) {}
    }
    
    // Method untuk menampilkan hasil statistik pembelajaran dengan full-training
<<<<<<< HEAD
    public void FullSchema()
    {
        try{
=======
    public void FullSchema(Classifier classifier)
    {
        try{
            
>>>>>>> bae1a7aec34e4e68a6d72528d06dc4430a7d4f44
            Evaluation eval = new Evaluation(data);
            eval.evaluateModel(classifier,data);
            System.out.println(eval.toSummaryString("\nResults Full-Training\n\n", false));
        }catch(Exception e) {}
    }
    
    // Method untuk menuliskan model hipotesis ke file eksternal
<<<<<<< HEAD
    public void PrintModel(String file)
=======
    public void PrintModel(Classifier classifier, String file)
>>>>>>> bae1a7aec34e4e68a6d72528d06dc4430a7d4f44
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(classifier);
            oos.flush();
        }catch(Exception e) {}
    }
    
    // Method untuk mengload model hipotesis dari file eksternal
    public Classifier LoadModel(String file)
    {
        Classifier classifier = null;
        try{
            FileInputStream fis = new FileInputStream(file);
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                classifier = (Classifier) ois.readObject();
            }
        }catch(IOException | ClassNotFoundException e) {}
        return classifier;
    }
    
    // Method untuk mengklasifikasikan sebuah instance
<<<<<<< HEAD
    public Instances Classify()
=======
    public Instances Classify(Classifier classifier)
>>>>>>> bae1a7aec34e4e68a6d72528d06dc4430a7d4f44
    {
        Instances labeled = new Instances(this.unlabeled);
        for (int i = 0; i < unlabeled.numInstances(); ++i) {
            try {
                double clsLabel = classifier.classifyInstance(unlabeled.instance(i));
                labeled.instance(i).setClassValue(clsLabel);
            } catch (Exception ex) {}
        }
        return labeled;
    }
    
    // Method untuk menuliskan instance ke "dataset.arff"
    public void PrintToARFF(Instances Data, String filename)
    {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(Data.toString());
            writer.flush();
            writer.close();
        }
        catch(Exception e){}
    }
    
    // Method untuk mengambil atribut data
    public Instances getdata()
    {
        return data;
    }
    
    // Method untuk mendapatkan yang unlabeled
    public Instances getUnlabeled() {
            return this.unlabeled;
    }
    
<<<<<<< HEAD
    // Method untuk mengambil filter yang akan digunakan untuk kategorisasi
    public StringToWordVector getFilter()
    {
=======
    // Method untuk m
    
    // Program Utama
    public static void main(String[] args) throws Exception {
        
        // Meload data set dari file eksternal
        String file = "dataset.arff";
        WekaExplorer W = new WekaExplorer();
        W.LoadDataset(file);
>>>>>>> bae1a7aec34e4e68a6d72528d06dc4430a7d4f44
        StringToWordVector filter = new StringToWordVector();
        filter.setDoNotOperateOnPerClassBasis(true);
        filter.setLowerCaseTokens(true);
        
        // Mengset tokenizer untuk memisahkan kata - kata
        WordTokenizer wt = new WordTokenizer();
        String delimiters = " \r\n\t.,;:\"\'()?!-¿¡+*&#$%\\/=<>[]`@~0123456789";
        wt.setDelimiters(delimiters);
        filter.setTokenizer(wt);
        filter.setStopwords(new File("stopwords.txt"));
        filter.setWordsToKeep(100000);
<<<<<<< HEAD
        
        return filter;
    }
    
    // Mengset classifier sebagai atribut
    public void setClassifier(Classifier _classifier)
    {
        classifier = _classifier;
    }
    
    // Membangun classifier yang dipilih
    public void buildClassifier()
    {
        try{
            classifier.buildClassifier(data);
        }catch(Exception e) {}
    }
    
    
    
    // Program Utama
    public static void main(String[] args) throws Exception {
        
        WekaExplorer W = new WekaExplorer();
        
        // Meload data set dari file eksternal
        W.LoadDataset("dataset.arff");
        
        // Membuat filter untuk merubah format data training
        StringToWordVector filter = W.getFilter();
        filter.setInputFormat(W.getdata());
        Instances dataTraining = Filter.useFilter(W.getdata(),filter);
        W.PrintToARFF(dataTraining, "dataset.vector.arff");
        W.setDataset(dataTraining);
        
        // Meload data yang ingin diklasifikasi dari file eksternal
        W.LoadUnkownLabel("unlabeled.arff");
        
        // Membuat filter untuk merubah format data unlabeled
        Instances dataUnlabeled = Filter.useFilter(W.getUnlabeled(), filter);
        W.PrintToARFF(dataUnlabeled, "unlabeled.vector.arff");
        W.setUnlabeled(dataUnlabeled);
        
        // Membuat Classifier baru untuk kategorisasi dan di build
        Classifier bayes = new NaiveBayes();
        W.setClassifier(bayes);
        W.buildClassifier();
        
        // Mengklasifikasikan data yang belum berlabel
        Instances result = W.Classify();
        System.out.println(result);
=======

        filter.setInputFormat(W.getdata());
        Instances dataTraining = Filter.useFilter(W.getdata(),filter);
        W.PrintToARFF(dataTraining, "dataset.vector.arff");     
>>>>>>> bae1a7aec34e4e68a6d72528d06dc4430a7d4f44
    }
}

