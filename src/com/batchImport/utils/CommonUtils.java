package com.batchImport.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CommonUtils {

    public static Connection getConnection(String url, String user, String password) {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to MySQL successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static boolean processFile(Connection connection) {
        boolean doneProcess = false;

        try {
            int count = 1;

            String line;
            String filePath = "C:\\Users\\hytra\\IdeaProjects\\batch-import\\dataSource.txt";

            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String sql = "INSERT INTO trx (acc_no, trx_amt, descr, trx_datetime, customer_id) VALUES (?, ?, ?, ?, ?)";

            while ((line = br.readLine()) != null) {
                if (count != 1) {
                    String[] tempLineArr = line.split("\\|");

                    String trx_datetime = tempLineArr[3] + " " + tempLineArr[4];

                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, tempLineArr[0]);
                    pstmt.setDouble(2, Double.parseDouble(tempLineArr[1]));
                    pstmt.setString(3, tempLineArr[2]);
                    pstmt.setString(4, trx_datetime);
                    pstmt.setString(5, tempLineArr[5]);

                    int rowInserted = pstmt.executeUpdate();

                    String log = "Acc no: " + tempLineArr[0]
                            + " | trx amount: " + tempLineArr[1]
                            + " | desc: " + tempLineArr[2]
                            + " | trx_datetime: " + trx_datetime
                            + " | customer id: " + tempLineArr[5];

                    if (rowInserted > 0) {
                        log += " -----> inserted";
                    } else {
                        log += " -----> failed";
                    }

                    System.out.println(log);
                }

                count++;
            }

            doneProcess = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return doneProcess;
    }
}
