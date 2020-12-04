package com.company;

import java.util.ArrayList;
import java.util.Scanner;

class Path{
    String p="";
    int n;
}

public class Main {

    //значения количества вершн, дуг, начальная вершина, конечная соответственно
    static int N, N_res, start, end;

    //матрица смежности
    //хранит в себе вес дуг
    static int[][] matrix;

    //массив посещенных вершин
    static boolean[] visited;

    //массив минимальных путей
    static int D[];

    //массив всех путей
    static ArrayList<Path> paths = new ArrayList<>();

    public static void main(String[] args) {
        //метод ввода данных
        enterData();

        //нахождение всех путей
        go(start, end);

        //вывод путей
        print();

        //нахождение середины орграфа
        middle();
    }

    //метод ввода данных
    private static void enterData(){
        Scanner scan = new Scanner(System.in);

        System.out.println("Введите количество вершин.");
        while (!scan.hasNextInt()) {
            System.out.println("Необходимо ввести целое число. Повторите ввод.");
            scan.next();
        }
        N = scan.nextInt();

        matrix = new int[N][N];

        visited = new boolean[N];

        D = new int[N];

        System.out.println("Ввод дуг:\nВведите вершину начала дуги, конца и вес дуги.\nВведите символ, отличный от числа, чтобы завершить ввод.");
        do {
            System.out.println("-------");
            if (!scan.hasNextInt()) break;
            int i = scan.nextInt();
            int j = scan.nextInt();
            if (!scan.hasNextDouble()) break;
            matrix[i-1][j-1] = scan.nextInt();
        } while (true);

        System.out.println("Укажите начальную вершину.");
        while (!scan.hasNextInt()) {
            System.out.println("Необходимо ввести целое число. Повторите ввод.");
            scan.next();
        }
        start = scan.nextInt()-1;
        System.out.println("Укажите конечную вершину.");
        while (!scan.hasNextInt()) {
            System.out.println("Необходимо ввести целое число. Повторите ввод.");
            scan.next();
        }
        end = scan.nextInt()-1;

        System.out.println("Матрица смежности:");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(matrix[i][j]==0) matrix[i][j]=Integer.MAX_VALUE;
            }
        }
    }

    //метод поиска всех путей
    private static void go(int curr, int b){
        int i;
        //если дошли до самой точки
        if(curr==b) {
            //создаем новый объект для массива
            Path path = new Path();
            //сумма путей
            int sum=0;
            if(N_res!=0) path.p+=(D[0]+1)+" -> ";
            for(i=1; i<N_res; i++) {
                sum+=matrix[D[i-1]][D[i]];
                path.p+=(D[i] + 1) + " -> ";
            }
            sum+=matrix[D[N_res-1]][end];
            path.p+=(b+1);
            path.n = sum;
            paths.add(path);
            return;
        }
        //помечаем, что прошли эту точку
        visited[curr]=true;
        //переходим к следующей
        D[N_res++]=curr;
        for(i=0; i<5; i++)
            if(matrix[curr][i]!=Integer.MAX_VALUE && !visited[i]) go(i, b);
        visited[curr]=false;
        N_res--;
    }

    private static void print(){
        //сортируем все пути по мере возрастания
        for(int i=0;i<paths.size();i++){
            for(int j=i+1;j<paths.size();j++){
                if(paths.get(j).n<paths.get(i).n){
                    Path temp = paths.get(i);
                    paths.set(i, paths.get(j));
                    paths.set(j, temp);
                }
            }
        }
        System.out.println("Кратчайший путь из точки "+(start+1)+" в точку "+(end+1)+"\n"+paths.get(0).p+" = "+paths.get(0).n);
        System.out.println("Самый длинный путь из точки "+(start+1)+" в точку "+(end+1)+"\n"+paths.get(paths.size()-1).p+" = "+paths.get(paths.size()-1).n);
        System.out.println("Все пути из точки "+(start+1)+" в точку "+(end+1));
        for(int i=0;i<paths.size();i++) System.out.println(paths.get(i).p+" = "+paths.get(i).n);
    }

    private static void middle(){
        //для того, чтобы реализовать алгоритм Флойда, необходимо складывать значения ячеек матрицы
        //если значение равно самому большому числу типа int, то при сложении его с другим числом получится большое отрицательное число
        //поэтому заменим значение Integer.MAX_VALUE на просто большое число, которое будем считать бесконечностью
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(matrix[i][j]==Integer.MAX_VALUE) matrix[i][j] = 1000;
            }
        }

        //алгоритм Флойда
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (matrix[i][k] + matrix[k][j] < matrix[i][j]) {
                        matrix[i][j] = matrix[i][k] + matrix[k][j];
                    }
                }
            }
        }

        
        int minEccentricity = 1000;
        int ind = -1;

        for (int j = 0; j < N; j++) {
            int eccentricity = -1;
            for (int i = 0; i < N; i++)
                if (matrix[i][j] != 1000 && matrix[i][j] > eccentricity)
                    eccentricity = matrix[i][j];
            if (eccentricity < minEccentricity && eccentricity != -1) {
                minEccentricity = eccentricity;
                ind = j;
            }

        }

        System.out.println("Центр орграфа - вершина "+(ind+1)+"\nЕе эксцентриситет равен "+minEccentricity);
    }
}