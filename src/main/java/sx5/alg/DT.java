package sx5.alg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DT {

    static double[] std = new double[200];
    static int[] id = new int[200];
    public static void qsort(int l, int r)
    {
        double mid = std[(l + r) / 2];
        int i = l,j = r;
        do{
            while(std[i] < mid) i++;
            while(std[j] > mid) j--;
            if(i <= j){
                double tmp = std[i];
                std[i] = std[j];
                std[j] = tmp;
                int tmp2 = id[i];
                id[i] = id[j];
                id[j] = tmp2;
                i++;
                j--;
            }
        }while(i <= j);
        if(l < j) qsort(l, j);
        if(i < r) qsort(i, r);
    }

    static int dimension_num;

    // preprocess part

    static double[] mean = new double[200];

    static int train_sample_num;
    static double[][] train_feature = new double[5000][200];
    static int[] label = new int[5000];
    static int[] category = new int[10];
    static int[] pos_tr = new int[1000000 << 2];
    static int[] neg_tr = new int[1000000 << 2];

    public static void train_tree(int p, int d, int i)
    {
        if(d == 20) {
            if(label[i] == 1) pos_tr[p]++;
            else neg_tr[p]++;
            return;
        }
        if(train_feature[i][id[d]] >= mean[id[d]]) train_tree(p << 1, d + 1, i);
        else train_tree(p << 1 | 1, d + 1, i);
    }

    static int test_sample_num;
    static double[][] test_feature = new double[5000][200];
    static int[] true_label = new int[5000];

    public static int test_tree(int p, int d, int i)
    {
        if(d == 20) {
            if(pos_tr[p] > neg_tr[p]) return 1;
            else return 2;
        }
        int tmp = 0;
        if(train_feature[i][id[d]] >= mean[id[d]]) tmp = test_tree(p << 1, d + 1, i);
        else tmp = test_tree(p << 1 | 1, d + 1, i);
        return tmp;
    }

    public static String trainDataSetPath = "";
    public static String testDataSetPath = "";

    public static Map<String, ArrayList<ArrayList<Double>>> makePredict(String trainPath, String testPath) { // System.out.println(a);

        LinkedHashMap<String, ArrayList<ArrayList<Double>>> resultMap = new LinkedHashMap<>();

        trainDataSetPath = trainPath;
        testDataSetPath = testPath;

        // readIn part

        List<List<String>> record1 = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(trainDataSetPath))) { // JDT PDE Lucene
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                record1.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<List<String>> record2 = new ArrayList<>();
        record2.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(testDataSetPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                record2.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int dimension_num = record1.get(0).size() - 1;
        int train_sample_num = record1.size() - 1;

        for(int i = 1; i <= train_sample_num; ++i) {
            for(int j = 0; j < dimension_num; ++j) {
                train_feature[i][j] = Double.parseDouble(record1.get(i).get(j));
                mean[j] += Double.parseDouble(record1.get(i).get(j));
            }
            String str = record1.get(i).get(dimension_num);
            if(str.equals("clean")) label[i] = 1;
            else if(str.equals("buggy")) label[i] = 2;
            category[label[i]]++;
        }

        test_sample_num = record2.size() - 1;

        for(int i = 1; i <= test_sample_num; ++i) {
            for(int j = 0; j < dimension_num; ++j) {
                test_feature[i][j] = Double.parseDouble(record2.get(i).get(j));
                mean[j] += Double.parseDouble(record2.get(i).get(j));
            }
            String str = record2.get(i).get(dimension_num);
            if(str.equals("clean")) true_label[i] = 1;
            else if(str.equals("buggy")) true_label[i] = 2;
        }

        // calculate mean and std

        for(int j = 0; j < dimension_num; ++j) {
            mean[j] = mean[j] / (train_sample_num + test_sample_num);
        }
        for(int i = 1; i <= train_sample_num; ++i) {
            for(int j = 0; j < dimension_num; ++j) {
                std[j] += (train_feature[i][j] - mean[j]) * (train_feature[i][j] - mean[j]);
            }
        }
        for(int i = 1; i <= test_sample_num; ++i) {
            for(int j = 0; j < dimension_num; ++j) {
                std[j] += (test_feature[i][j] - mean[j]) * (test_feature[i][j] - mean[j]);
            }
        }

        // pre calculate

        for(int j = 0; j < dimension_num; ++j) {
            std[j] *= (-1);
        }
        for(int j = 0; j < dimension_num; ++j) {
            id[j] = j;
        }
        qsort(0, dimension_num - 1);

        int predict_label[] = new int[5000];

        // train part

        for(int i = 1; i <= train_sample_num; ++i) {
            train_tree(1, 1, i);
        }

        // test part

        for(int i = 1; i <= train_sample_num; ++i) {
            predict_label[i] = test_tree(1, 1, i);
        }

        // calculate accuracy

        double true_classify = 0;
        double false_classify = 0;
        for(int i = 1; i <= test_sample_num; ++i) {
            if(predict_label[i] == true_label[i]) true_classify++;
            else false_classify++;
        }
        double accuracy = true_classify / (true_classify + false_classify);

        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(accuracy);
        ArrayList<ArrayList<Double>> listArrayList = new ArrayList<>();
        listArrayList.add(arrayList);
        resultMap.put("bxy", listArrayList);

        return resultMap;
    }
}
