package sx5.alg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KNN {

    public static String trainDataSetPath = "";
    public static String testDataSetPath = "";

    public static int elect_num;
    public static int neighbor;

    static double[][] dis = new double[5000][5000];
    static int[] id = new int[5000];

    static int nb[][] = new int[5000][30];
    public static void qsort(int now, int l, int r)
    {
        double mid = dis[now][(l + r) / 2];
        int i = l,j = r;
        do{
            while(dis[now][i] < mid) i++;
            while(dis[now][j] > mid) j--;
            if(i <= j){
                double tmp = dis[now][i];
                dis[now][i] = dis[now][j];
                dis[now][j] = tmp;
                int tmp2 = id[i];
                id[i] = id[j];
                id[j] = tmp2;
                i++;
                j--;
            }
        }while(i <= j);
        if(l < j) qsort(now, l, j);
        if(i < r) qsort(now, i, r);
    }

//    public static void main(String[] args) { // System.out.println(a);
    public static Map<String, ArrayList<ArrayList<Double>>> makePredict(String trainPath, String testPath, int electNum, int neighbour) { // System.out.println(a);

        LinkedHashMap<String, ArrayList<ArrayList<Double>>> resultMap = new LinkedHashMap<>();
        ArrayList<ArrayList<Double>> arrayList1 = new ArrayList<>();
        ArrayList<ArrayList<Double>> arrayList2 = new ArrayList<>();

        trainDataSetPath = trainPath;
        testDataSetPath = testPath;

        // standard optimizer

        boolean standard0 = true; // remove 0 standard
        boolean standard1 = true; // max-min standard
        boolean standard2 = false; // mean-std standard
        boolean standard3 = false; // sigmoid standard
        elect_num = electNum; //elect std_max
        neighbor = neighbour; //select neighbor

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

        double[] maxx = new double[200];
        double[] minn = new double[200];
        double[] max_min = new double[200];
        double[] mean = new double[200];
        double[] std = new double[200];
        int[] zero_num = new int[200];

        int train_sample_num = record1.size() - 1;
        double[][] train_feature = new double[5000][200];
        int[] label = new int[5000];
        int[] category = new int[10];

        for(int i = 1; i <= train_sample_num; ++i) {
            for(int j = 0; j < dimension_num; ++j) {
                train_feature[i][j] = Double.parseDouble(record1.get(i).get(j));
                maxx[j] = Math.max(maxx[j], Double.parseDouble(record1.get(i).get(j)));
                minn[j] = Math.min(minn[j], Double.parseDouble(record1.get(i).get(j)));
                mean[j] += Double.parseDouble(record1.get(i).get(j));
                if(train_feature[i][j] == 0) zero_num[j]++;
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
                maxx[j] = Math.max(maxx[j], Double.parseDouble(record2.get(i).get(j)));
                minn[j] = Math.min(minn[j], Double.parseDouble(record2.get(i).get(j)));
                mean[j] += Double.parseDouble(record2.get(i).get(j));
                if(test_feature[i][j] == 0) zero_num[j]++;
            }
            String str = record2.get(i).get(dimension_num);
            if(str.equals("clean")) true_label[i] = 1;
            else if(str.equals("buggy")) true_label[i] = 2;
        }

        for(int j = 0; j < dimension_num; ++j) {
            max_min[j] = maxx[j] - minn[j];
            if(max_min[j] == 0) max_min[j] = 1;
        }

        // remove zero

        if(standard0 == true) {
            for(int j = 0; j < dimension_num; ++j) {
                if(zero_num[j] > 0.6 * (train_sample_num + test_sample_num)) {
                    mean[j] = 0;
                    std[j] = 1;
                    for(int i = 1; i <= train_sample_num; ++i) {
                        train_feature[i][j] = 0;
                    }
                    for(int i = 1; i <= test_sample_num; ++i) {
                        test_feature[i][j] = 0;
                    }
                }
            }
        }

        // max-min standard

        if(standard1 == true) {
            for(int i = 1; i <= train_sample_num; ++i) {
                for(int j = 0; j < dimension_num; ++j) {
                    train_feature[i][j] = (train_feature[i][j] - minn[j]) / max_min[j];
                }
            }
            for(int i = 1; i <= test_sample_num; ++i) {
                for(int j = 0; j < dimension_num; ++j) {
                    test_feature[i][j] = (test_feature[i][j] - minn[j]) / max_min[j];
                }
            }
        }

        // mean-std standard

        if(standard2 == true) {
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
            for(int j = 0; j < dimension_num; ++j) {
                std[j] = std[j] / (train_sample_num + test_sample_num);
                std[j] = Math.sqrt(std[j]);
                if(mean[j] == 0) std[j] = 1;
            }
            for(int i = 1; i <= train_sample_num; ++i) {
                for(int j = 0; j < dimension_num; ++j) {
                    train_feature[i][j] = (train_feature[i][j] - mean[j]) / std[j];
                }
            }
            for(int i = 1; i <= test_sample_num; ++i) {
                for(int j = 0; j < dimension_num; ++j) {
                    test_feature[i][j] = (test_feature[i][j] - mean[j]) / std[j];
                }
            }
            System.out.print("MEAN    ");
            for(int j = 0; j < dimension_num; ++j) {
                System.out.print(mean[j] + " ");
            }
            System.out.println();
            System.out.print("STD    ");
            for(int j = 0; j < dimension_num; ++j) {
                System.out.print(std[j] + " ");
            }
            System.out.println();
        }

        // sigmoid

        if(standard3 == true) {
            for(int i = 1; i <= train_sample_num; ++i) {
                for(int j = 0; j < dimension_num; ++j) {
                    train_feature[i][j] = 1 / (1 + Math.exp((-1) * train_feature[i][j]));
                }
            }
            for(int i = 1; i <= test_sample_num; ++i) {
                for(int j = 0; j < dimension_num; ++j) {
                    test_feature[i][j] = 1 / (1 + Math.exp((-1) * test_feature[i][j]));
                }
            }
        }

        // train part

        double center[][] = new double[10][5000];
        for(int i = 1; i <= train_sample_num; ++i) {
            for(int j = 1; j <= dimension_num ; ++j) {
                center[label[i]][j] += train_feature[i][j];
            }
        }
        for(int j = 1; j <= dimension_num ; ++j) {
            if(category[1] > 0) center[1][j] /= category[1];
            if(category[2] > 0) center[2][j] /= category[2];
        }

        // test part

        int[] predict_label = new int[5000];
        for(int i = 1; i <= test_sample_num; ++i) {
            double dis1 = 0;
            double dis2 = 0;
            for(int j = 0; j < dimension_num; ++j) {
                dis1 += (center[1][j] - test_feature[i][j]) * (center[1][j] - test_feature[i][j]);
                dis2 += (center[2][j] - test_feature[i][j]) * (center[2][j] - test_feature[i][j]);
            }
            if(dis1 <= dis2) predict_label[i] = 1;
            else predict_label[i] = 2;
            String str = record2.get(i).get(dimension_num);
            if(str.equals("clean")) true_label[i] = 1;
            else if(str.equals("buggy")) true_label[i] = 2;
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

        //elect std_max
        double re_std[] = new double[200];
        double re_mean[] = new double[200];
        if(elect_num > 0) {
            for(int i = 1; i <= test_sample_num; ++i) {
                for(int j = 0; j < dimension_num; ++j) {
                    re_mean[j] += test_feature[i][j];
                }
            }
            for(int j = 0; j < dimension_num; ++j) {
                re_mean[j] = re_mean[j] / test_sample_num;
            }
            for(int i = 1; i <= test_sample_num; ++i) {
                for(int j = 0; j < dimension_num; ++j) {
                    re_std[j] += (test_feature[i][j] - re_mean[j]) * (test_feature[i][j] - re_mean[j]);
                }
            }
            for(int j = 0; j < dimension_num; ++j) {
                re_std[j] = re_std[j] / test_sample_num;
                re_std[j] = Math.sqrt(re_std[j]);
                if(re_mean[j] == 0) re_std[j] = 1;
            }
            System.out.println();
            System.out.print("RE_MEAN    ");
            for(int j = 0; j < dimension_num; ++j) {
                System.out.print(re_mean[j] + " ");
            }
            System.out.println();
            System.out.print("RE_STD    ");
            for(int j = 0; j < dimension_num; ++j) {
                System.out.print(re_std[j] + " ");
            }
            int std_max_feature[] = new int[200];
            boolean elect_feature[] = new boolean[200];
            for(int p = 1; p <= elect_num; ++p) {
                double max_now = 0;
                int selecting = 0;
                for(int j = 1; j <= dimension_num; ++j) {
                    if(re_std[j] > max_now && elect_feature[j] == false) {
                        max_now = re_std[j];
                        selecting = j;
                    }
                }
                elect_feature[selecting] = true;
                std_max_feature[p] = selecting;
            }

            if(neighbor > 0) {
                double point[][] = new double[5000][2];
                for(int i = 1; i <= test_sample_num; ++i) {
                    point[i][0] = (Math.random() - 0.5) * 2;
                    point[i][1] = (Math.random() - 0.5) * 2;
                }

                for(int i = 1; i <= test_sample_num; ++i) {
                    for(int j = 1; j <= test_sample_num; ++j) {
                        for(int p = 1; p <= elect_num; ++p) {
                            dis[i][j] += (test_feature[i][std_max_feature[p]] - test_feature[j][std_max_feature[p]])
                                    * (test_feature[i][std_max_feature[p]] - test_feature[j][std_max_feature[p]]);
                        }
                    }

                    for(int l = 1; l <= test_sample_num; ++l) {
                        id[l] = l;
                    }
                    qsort(i, 1, test_sample_num);
                    for(int k = 1; k <= neighbor; ++k) {
                        nb[i][k] = id[k + 1];
                    }
                }

                double alpha = 0.005;
                for(int epoch = 1; epoch <= 1000; ++epoch) {
                    for(int i = 1; i <= test_sample_num; ++i) {
                        double x = 0;
                        double y = 0;
                        for(int k = 1; k <= neighbor; ++k) {
                            x += point[nb[i][k]][0];
                            y += point[nb[i][k]][1];
                        }
                        x = 0;
                        y = 0;
                        point[i][0] += (x - point[i][0]) * alpha;
                        point[i][1] += (y - point[i][1]) * alpha;
                        for(int k = 1; k <= neighbor; ++k) {
                            int random = (int) (test_sample_num * Math.random());
                            x += point[random][0];
                            y += point[random][1];
                        }
                        point[i][0] -= (x - point[i][0]) * alpha;
                        point[i][1] -= (y - point[i][1]) * alpha;
                    }
                }

//                ArrayList<Map<String, Double>> resultList = new ArrayList<>();
//                for(int i = 1; i <= test_sample_num; ++i) {
//                    Map<String, Double> mapElement= new LinkedHashMap<>();
//                    mapElement.put("x", point[i][0]);
//                    mapElement.put("y", point[i][0]);
//                    mapElement.put("l", (double) true_label[i]);
//                    resultList.add(mapElement);
//                }

                for(int i = 1; i <= test_sample_num; ++i) {
//                for(int i = 1; i <= 100; ++i) {
                    ArrayList<Double> arrayList = new ArrayList<>();
                    if (true_label[i] == 1) {
                        arrayList.add(point[i][0]);
                        arrayList.add(point[i][1]);
                        arrayList1.add(arrayList);
                    } else if (true_label[i] == 2) {
                        arrayList.add(point[i][0]);
                        arrayList.add(point[i][1]);
                        arrayList2.add(arrayList);
                    }
                }
            }
        }


        resultMap.put("data1", arrayList1);
        resultMap.put("data2", arrayList2);

        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(accuracy);
        ArrayList<ArrayList<Double>> listArrayList = new ArrayList<>();
        listArrayList.add(arrayList);
        resultMap.put("bxy", listArrayList);

        return resultMap;
    }
}
