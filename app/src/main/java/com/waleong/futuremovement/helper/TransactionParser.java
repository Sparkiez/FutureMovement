package com.waleong.futuremovement.helper;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.waleong.futuremovement.R;
import com.waleong.futuremovement.model.ColumnDefinition;
import com.waleong.futuremovement.util.FileParserUtil;
import com.waleong.futuremovement.util.FileWriterUtil;
import com.waleong.futuremovement.util.RecordParserUtil;
import com.waleong.futuremovement.util.RecordWriterUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * This is a helper that parse in a input file, and then output a CSV file
 * which shows the business user its daily summary report
 * Created by raymondleong on 04,July,2019
 */
public class TransactionParser {

    private List<ColumnDefinition> mColumnDefinitions;

    public String parseAndCreateReport(Context context, int columnDefResFile, int inputResFile, String outputFileName) throws IOException {
        // Read the human readable json file which contains the column definition,
        // and convert it into a list of objects.
        mColumnDefinitions = generateColumnDefinitions(context, columnDefResFile);
        List<Map<String, String>> transactions = generateTransactions(context, mColumnDefinitions, inputResFile);
        return writeToOutput(context, transactions, outputFileName);
    }

    /**
     * @param context
     * @param columnDefinitions
     * @param inputFile         The res id of the transactional input file that is to be read.
     * @return A list of transactions, where the transactions column and value is saved to a key pair.
     * @throws IOException
     */
    private List<Map<String, String>> generateTransactions(Context context, final List<ColumnDefinition> columnDefinitions, int inputFile) throws IOException {
        // Read the files 'input.txt' and save the data into a list of column mappings (column name, column value).
        final List<Map<String, String>> transactions = new ArrayList<>();

        FileParserUtil.readFile(context, inputFile, new FileParserUtil.OnLineParsedListener() {
            @Override
            public void onLineParsed(String line) {
                Map<String, String> transactionMap = RecordParserUtil.parse(columnDefinitions, line);

                if (transactionMap == null || transactionMap.isEmpty()) {
                    // Data is null, therefore its not a valid record.
                    return;
                }

                transactions.add(transactionMap);
            }
        });

        return transactions;
    }

    /**
     * @param context
     * @param columnDefFile The res id of the column definition file that is to be read.
     * @return A list of column definitions, which tells us how we parse each row of transactions.
     * @throws IOException
     */
    private List<ColumnDefinition> generateColumnDefinitions(Context context, int columnDefFile) throws IOException {
        String columnDefinition = FileParserUtil.readFile(context, columnDefFile);
        Type columnDefinitionsType = new TypeToken<ArrayList<ColumnDefinition>>() {
        }.getType();
        return GsonHelper.parse(columnDefinition, columnDefinitionsType);
    }

    /**
     * @param context
     * @param transactions   A list of transactions, where the transactions column and value is saved to a key pair.
     * @param outputFileName The name of the file to be outputted once the daily summary reported is generated.
     * @return The path to the file.
     * @throws IOException
     */
    private String writeToOutput(Context context, List<Map<String, String>> transactions, String outputFileName) throws IOException {
        // Write to CSV now.
        String csvOutput = RecordWriterUtil.generateOutput(transactions);
        Timber.tag(TransactionParser.class.toString()).v("writeToOutput(): Output is \n" + csvOutput);
        return FileWriterUtil.writeToFile(context, csvOutput, outputFileName);
    }
}
