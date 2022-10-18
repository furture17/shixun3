package sx5.alg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BYS {

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

        // preprocess part

        double[] mean = new double[200];

        int train_sample_num = record1.size() - 1;
        double[][] train_feature = new double[5000][200];
        int[] label = new int[5000];
        int[] category = new int[10];

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

        int test_sample_num = record2.size() - 1;
        double[][] test_feature = new double[5000][200];
        int[] true_label = new int[5000];

        for(int i = 1; i <= test_sample_num; ++i) {
            for(int j = 0; j < dimension_num; ++j) {
                test_feature[i][j] = Double.parseDouble(record2.get(i).get(j));
                mean[j] += Double.parseDouble(record2.get(i).get(j));
            }
            String str = record2.get(i).get(dimension_num);
            if(str.equals("clean")) true_label[i] = 1;
            else if(str.equals("buggy")) true_label[i] = 2;
        }

        // calculate mean

        for(int j = 0; j < dimension_num; ++j) {
            mean[j] = mean[j] / (train_sample_num + test_sample_num);
        }

        // pre calculate

        double p[][][] = new double[200][10][10];
        for(int j = 0; j < dimension_num; ++j) {
            int positive[] = new int[10];
            for(int i = 1; i <= train_sample_num; ++i) {
                if(train_feature[i][j] <= mean[j]) {
                    p[j][1][label[i]]++;
                } else {
                    p[j][2][label[i]]++;
                }
            }
            p[j][1][1] /= train_sample_num;
            p[j][2][1] /= train_sample_num;
            p[j][1][2] /= train_sample_num;
            p[j][2][2] /= train_sample_num;
        }

        // test part

        int[] predict_label = new int[5000];
        for(int i = 1; i <= test_sample_num; ++i) {
            double pre_positive = 1;
            double pre_negative = 1;
            for(int j = 0; j < dimension_num; ++j) {
                if(test_feature[i][j] <= mean[j]) {
                    pre_positive *= p[j][1][1];
                    pre_negative *= p[j][1][2];
                } else {
                    pre_positive *= p[j][2][1];
                    pre_negative *= p[j][2][2];
                }
            }
            if(pre_positive >= pre_negative) {
                predict_label[i] = 1;
            } else {
                predict_label[i] = 2;
            }
        }

        // calculate accuracy

        double true_classify = 0;
        double false_classify = 0;
        for(int i = 1; i <= test_sample_num; ++i) {
            if(predict_label[i] == true_label[i]) true_classify++;
            else false_classify++;
        }
        double accuracy = true_classify / (true_classify + false_classify);

        // output

        System.out.print("TRUE_LABEL    ");
        for(int i = 1; i <= test_sample_num; ++i) {
            System.out.print(true_label[i] + " ");
        }
        System.out.println();
        System.out.print("PREDICT_LABEL ");
        for(int i = 1; i <= test_sample_num; ++i) {
            System.out.print(predict_label[i] + " ");
        }
        System.out.println();
        System.out.print(accuracy * 100 + " %");

        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(accuracy);
        ArrayList<ArrayList<Double>> listArrayList = new ArrayList<>();
        listArrayList.add(arrayList);
        resultMap.put("bxy", listArrayList);

        return resultMap;
    }
}
