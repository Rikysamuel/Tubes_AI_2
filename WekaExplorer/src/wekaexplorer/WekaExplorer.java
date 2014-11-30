package wekaexplorer;

import java.io.*;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.bayes.ComplementNaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.*;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ConverterUtils;
import weka.core.tokenizers.WordTokenizer;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaExplorer {

    private Instances data; //traning data
    private Instances unlabeled; //testing data
    private Classifier classifier;
    NominalToString NTS = new NominalToString();
	
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
    
    // Method untuk Supplied Test Set
    public FilteredClassifier getClassifierFiltered(Instances train, Instances test) throws Exception{
        NaiveBayes NB = new NaiveBayes();
        FilteredClassifier FC = new FilteredClassifier();
        
        // Set train dan set menjadi word vector
        train = getFilterToWordVector(train);
        test = getFilterToWordVector(test);

        // Membangun model dan melakukan test
        FC.setClassifier(NB);
        FC.buildClassifier(train);
        for (int i = 0; i < test.numInstances(); i++) {
           double pred = FC.classifyInstance(test.instance(i));
           System.out.print("ID: " + test.instance(i).value(0));
           System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
           System.out.println(", predicted: " + test.classAttribute().value((int) pred));
        }
        return FC;
    }
    
    // Method untuk mmebuat instance menjadi terfilter yang akan digunakan untuk kategorisasi
    public Instances getFilterToWordVector(Instances train) throws Exception
    {
        StringToWordVector filter = new StringToWordVector();
//        filter.setDoNotOperateOnPerClassBasis(true);
//        filter.setLowerCaseTokens(true);
        
        // Mengset tokenizer untuk memisahkan kata - kata
        WordTokenizer wt = new WordTokenizer();
        
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = "1-2";
        filter.setOptions(options);
        filter.setInputFormat(train);
        
        String delimiters = " \r\n\t.,;:\"\'()?!-¿¡+*&#$%\\/=<>[]`@~0123456789";
        wt.setDelimiters(delimiters);
        filter.setTokenizer(wt);
        filter.setStopwords(new File("stopwords.txt"));
        filter.setWordsToKeep(100000);
        
        train = Filter.useFilter(train, filter);
        return train;
    }
    
    // Method untuk membuat data train input terfilter menjadi string
    public Instances getFilterNominalToString(Instances dataTrain) throws Exception{
        String[] options = new String[2];
        options[0] = "-C";
        options[1] = "1-2";
        NTS.setOptions(options);
        NTS.setInputFormat(dataTrain);
        
        dataTrain = Filter.useFilter(dataTrain,NTS);
        return dataTrain;
    }
    
    // Method untuk membuat data test input terfilter menjadi string
    public Instances getFilterNominalToStringTest(Instances test) throws Exception{
        String[] options = new String[2];
        options[0] = "-C";
        options[1] = "1-2";
        NTS.setOptions(options);
        
        test = Filter.useFilter(test, NTS);
        return test;
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
    
    public void complementNaiveBayes(String filepath) throws Exception{
         //data set
        Instances train =  readDataFile(filepath);
        
        //set class attribute
        train.setClassIndex(0);
        
        //create model
//        ComplementNaiveBayes cls = new ComplementNaiveBayes();
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
    
    public Instances getDataFromDB(String _query) throws Exception{
        InstanceQuery query = new InstanceQuery();
        query.setDatabaseURL("jdbc:mysql://localhost:3306/news_aggregator");
        query.setUsername("root");
        query.setPassword("");
        
        query.setQuery(_query);
        Instances returnData = query.retrieveInstances();
        return returnData;
    }
    
    // Program Utama
    public static void main(String[] args) throws Exception {
        
//        WekaExplorer W = new WekaExplorer();
//        
//        // Mengambil data dari DB dan menulis ke file
//        Instances data2 = W.getDataFromDB("SELECT FULL_TEXT,JUDUL,LABEL FROM artikel NATURAL JOIN artikel_kategori_verified NATURAL JOIN kategori");
//        W.PrintToARFF(data2, "dataset.arff");
//        
//        // Meload data set dari file eksternal
//        W.LoadDataset("dataset.arff");
//
//        
//        // Membuat filter untuk merubah format data training
//        Instances dataTraining = W.getFilterNominalToString(W.getdata());
//        W.PrintToARFF(dataTraining, "dataset.string.arff");
//
//        // Meload data yang ingin diklasifikasi dari file eksternal
//        W.LoadUnkownLabel("unlabeled.arff");
//        
//        // Membuat filter untuk merubah format data unlabeled
//        Instances dataUnlabeled = W.getFilterNominalToStringTest(W.getUnlabeled());
//        
//        W.PrintToARFF(dataUnlabeled, "unlabeled.string.arff");
//
//        // Membuat model dan Mengklasifikasikan data yang belum berlabel
//        W.getClassifierFiltered(dataTraining, dataUnlabeled);
    }
}
