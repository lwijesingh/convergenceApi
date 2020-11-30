package com.wiley.common;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.assertion.DefaultFailureHandler;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.dataset.*;

import java.util.List;

public class CompareData {


    public static String assertEquals(ITable expectedTable, ITable actualTable, String[] columnsToSort) throws DatabaseUnitException {


        DefaultFailureHandler failureHandler = new DefaultFailureHandler();
        ITableMetaData expectedMetaData = expectedTable.getTableMetaData();
        ITableMetaData actualMetaData = actualTable.getTableMetaData();
        String msg = "";
        boolean flag = true;
        // Put the columns into the same order
        Column[] expectedColumns = Columns.getSortedColumns(expectedMetaData);
        Column[] actualColumns = Columns.getSortedColumns(actualMetaData);

        // Verify columns
        Columns.ColumnDiff columnDiff = Columns.getColumnDiff(expectedMetaData, actualMetaData);
        if (columnDiff.hasDifference()) {
            String message = columnDiff.getMessage();
            msg = message + " Expected Columns: " + Columns.getColumnNamesAsString(expectedColumns) + " Actual Cloumns: " + Columns.getColumnNamesAsString(actualColumns) + "\n";
            flag = false;
            //   logger.error(error.toString());
        }

        // Verify row count
        int expectedRowsCount = expectedTable.getRowCount();
        int actualRowsCount = actualTable.getRowCount();

        if (flag==true && expectedRowsCount != actualRowsCount) {
             msg = "Row count mismatch. Expected row  count: " + expectedRowsCount +", Actual row count: " + actualRowsCount + "\n";
            flag = false;
        }

        DiffCollectingFailureHandler diffHandler = new DiffCollectingFailureHandler();



        if (flag==true && columnsToSort.length != 0) {
            ITable sortedExpectedTable = new SortedTable(expectedTable, columnsToSort);
            ITable sortedActualTable = new SortedTable(actualTable, columnsToSort);
            Assertion.assertEquals(sortedExpectedTable, sortedActualTable, diffHandler);

            if ( diffHandler.getDiffList().size() !=0) {

                List diffList = diffHandler.getDiffList();

                for (Object obj : diffList) {
                    Difference diff = (Difference) obj;
                    for (int j =0;j<columnsToSort.length;j++){
                        msg =  msg + columnsToSort[j] +  ": " + sortedExpectedTable.getValue(diff.getRowIndex(),columnsToSort[j]) + " -";
                    }
                    msg = msg + "Column Name : " + diff.getColumnName() + ", Expected : " + diff.getExpectedValue() + ", Actual : " + diff.getActualValue() + "\n";
                }
            }

        }

        return msg;
    }

    public static String getRowData(ITable iTable, int row){

        String msg = "";

        try {
            Column[] columns = Columns.getSortedColumns(iTable.getTableMetaData());
            for (int i=0;i<columns.length;i++){
                msg = msg + columns[i].getColumnName() + ": " + iTable.getValue(row, columns[i].getColumnName()) + "  ";
            }


        } catch (DataSetException e) {
            e.printStackTrace();
        }

        return msg;

    }

}
