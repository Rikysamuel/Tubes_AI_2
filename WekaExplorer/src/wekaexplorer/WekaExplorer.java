package wekaexplorer;

import java.io.*;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.ComplementNaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.*;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ConverterUtils;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaExplorer {

    private Instances data; //traning data
    private Instances unlabeled; //testing data
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
    public void FoldSchema()
    {
        try{
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(classifier, data, 10, new Random(1));
            System.out.println(eval.toSummaryString("\nResults 10 folds cross-validation\n\n", false));
        }catch(Exception e) {}
    }
    
    // Method untuk menampilkan hasil statistik pembelajaran dengan full-training
    public void FullSchema()
    {
        try{
            Evaluation eval = new Evaluation(data);
            eval.evaluateModel(classifier,data);
            System.out.println(eval.toSummaryString("\nResults Full-Training\n\n", false));
        }catch(Exception e) {}
    }
    
    // Method untuk menuliskan model hipotesis ke file eksternal
    public void PrintModel(String file)
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(classifier);
            oos.flush();
        }catch(Exception e) {}
    }
    
    // Method untuk mengload model hipotesis dari file eksternal
    public Classifier LoadModel(String file)
    {
        Classifier classifier2 = null;
        try{
            FileInputStream fis = new FileInputStream(file);
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                classifier2 = (Classifier) ois.readObject();
            }
        }catch(IOException | ClassNotFoundException e) {}
        return classifier2;
    }
    
    // Method untuk mengklasifikasikan sebuah instance
    public Instances Classify()
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
    public void PrintToARFF(Instances Data, String filename) throws IOException
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(Data.toString());
            writer.flush();
        }
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
    
    public FilteredClassifier getClassifierFiltered(){
    FilteredClassifier FC = new FilteredClassifier();
        return FC;
    }
    
    // Method untuk mengambil filter yang akan digunakan untuk kategorisasi
    public StringToWordVector getFilter()
    {
        StringToWordVector filter = new StringToWordVector();
//        filter.setDoNotOperateOnPerClassBasis(true);
//        filter.setLowerCaseTokens(true);
        
        // Mengset tokenizer untuk memisahkan kata - kata
        WordTokenizer wt = new WordTokenizer();
        String delimiters = " \r\n\t.,;:\"\'()?!-¿¡+*&#$%\\/=<>[]`@~0123456789";
        wt.setDelimiters(delimiters);
        filter.setTokenizer(wt);
        filter.setStopwords(new File("stopwords.txt"));
        filter.setWordsToKeep(100000);
        
        return filter;
    }
    
    public NominalToString getFilter1(){
        NominalToString filter1 = new NominalToString();
        return filter1;
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
    
    public Instances readDataFile(String filename){
        Instances datainstances = null;                
        try{
           datainstances = ConverterUtils.DataSource.read(filename);
        } catch (Exception e){
            System.err.println(e);
        }
        return datainstances;
    }
     
    public void fullTrainingSet(String filepath, String testfilepath) throws Exception{
        //data set
        Instances train =  readDataFile(filepath);
        Instances test = readDataFile(testfilepath);
        
        //set class attribute
        train.setClassIndex(train.numAttributes()-1);
        test.setClassIndex(test.numAttributes()-1);
        
        //train classifier
        Classifier cls = (Classifier)new NaiveBayes();   
        cls.buildClassifier(train);
        
        //copy to Model variable
        classifier = cls;
        
        //evaluate classifier and print some statistics
        Evaluation eval = new Evaluation(test);
        eval.evaluateModel(classifier, test);
        
        //print some information
        System.out.println(cls.toString());
        System.out.println(eval.toSummaryString("\nResults\n======\n",false));
        System.out.println(eval.toClassDetailsString("\n=== Detailed Accuracy By Class ===\n")); //print class accuracy
        System.out.println(eval.toMatrixString());  //print confused matrix
    } 
    
     public void crossValidation(String filepath) throws Exception{
         //data set
        Instances train =  readDataFile(filepath);
        
        //set class attribute
        train.setClassIndex(0);
        
        //create model
        NaiveBayes cls = new NaiveBayes();
        cls.buildClassifier(train);
        
        //copy tree to Model variable
        classifier = cls;
        
        //evalute classifier and print some statistics
        Evaluation eval = new Evaluation(train);
        eval.crossValidateModel(classifier, train, 10, new Random(1));
        
        //print some information
        System.out.println(cls.toString());    //print unprunned tree
        System.out.println(eval.toSummaryString("=== Summary ===", false)); //print summary
        System.out.println(eval.toClassDetailsString("\n=== Detailed Accuracy By Class ===\n")); //print class accuracy
        System.out.println(eval.toMatrixString());  //print confused matrix
    }
    
    public void complementNaiveBayes(String filepath) throws Exception{
         //data set
        Instances train =  readDataFile(filepath);
        
        //set class attribute
        train.setClassIndex(0);
        
        //create model
        ComplementNaiveBayes cls = new ComplementNaiveBayes();
        cls.buildClassifier(train);
        
        //copy tree to Model variable
        classifier = cls;
        
        //evalute classifier and print some statistics
        Evaluation eval = new Evaluation(train);
        eval.crossValidateModel(classifier, train, 10, new Random(1));
        
        //print some information
        System.out.println(cls.toString());    //print unprunned tree
        System.out.println(eval.toSummaryString("=== Summary ===", false)); //print summary
        System.out.println(eval.toClassDetailsString("\n=== Detailed Accuracy By Class ===\n")); //print class accuracy
        System.out.println(eval.toMatrixString());  //print confused matrix
    }
     
    // Program Utama
    public static void main(String[] args) throws Exception {
        
        WekaExplorer W = new WekaExplorer();
        
        // Meload data set dari file eksternal
        W.LoadDataset("D:\\Dropbox\\sem5\\IF3170 Intelegensi Buatan\\tugas\\tubes 2\\github\\WekaExplorer\\dataset.arff");
        
        // Membuat filter untuk merubah format data training
        StringToWordVector filter = W.getFilter();
        NominalToString filter1 = W.getFilter1();
        
        String[] options = new String[2];
        options[0] = "-C";
        options[1] = "1-2";
        
        filter1.setOptions(options);
        filter1.setInputFormat(W.getdata());
        Instances dataTraining = Filter.useFilter(W.getdata(),filter1);
        
        String[] options2 = new String[2];
        options2[0] = "-R";
        options2[1] = "1-2";
        
        filter.setOptions(options2);
        filter.setInputFormat(dataTraining);
        Instances dataTraining2 = Filter.useFilter(W.getdata(), filter);
        
        W.PrintToARFF(dataTraining2, "dataset.vector.arff");
//        W.setDataset(dataTraining2);
        //crossvalidation
//        W.crossValidation("dataset.vector.arff");
        
        //complementnaive bayes
       // W.complementNaiveBayes("dataset.vector.arff");
        
        //multinomial naive bayes
//        W.naiveBayesMultinomial("dataset.vector.arff"); >>belom ada methodnya
        
//        // Meload data yang ingin diklasifikasi dari file eksternal
//        W.LoadUnkownLabel("unlabeled.arff");
////        
//        // Membuat filter untuk merubah format data unlabeled
//        Instances dataUnlabeled = Filter.useFilter(W.getUnlabeled(), filter1);
//        Instances dataUnlabeled2 = Filter.useFilter(dataUnlabeled, filter);
//        
//        W.PrintToARFF(dataUnlabeled2, "unlabeled.vector.arff");
//        W.setUnlabeled(dataUnlabeled);
        
        // Membuat Classifier baru untuk kategorisasi dan di build
//        Classifier bayes = new NaiveBayes();
//        W.setClassifier(bayes);
//        W.buildClassifier();

//        W.Classify();
        
        // Mengklasifikasikan data yang belum berlabel
//        Instances result = W.Classify();
//        System.out.println(result);
    }
}
