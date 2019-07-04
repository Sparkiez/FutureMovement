package com.waleong.futuremovement;

import com.google.gson.reflect.TypeToken;
import com.waleong.futuremovement.data.TransactionData;
import com.waleong.futuremovement.helper.GsonHelper;
import com.waleong.futuremovement.model.ColumnDefinition;
import com.waleong.futuremovement.util.FileWriterUtil;
import com.waleong.futuremovement.util.RecordParserUtil;
import com.waleong.futuremovement.util.RecordWriterUtil;
import com.waleong.futuremovement.utils.FileReader;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by raymondleong on 03,July,2019
 */
public class RecordWriterUtilTest {

    private List<ColumnDefinition> mColumnDefinitions;
    private List<Map<String, String>> mTransactions;

    private static final int TRANSACTIONS_SIZE = 717;

    @Before
    public void setup() {
        String text = FileReader.readFile("col_definition.json");
        Type columnDefinitionsType = new TypeToken<ArrayList<ColumnDefinition>>(){}.getType();
        mColumnDefinitions = GsonHelper.parse(text, columnDefinitionsType);

        mTransactions = new ArrayList<>();
        FileReader.readFile("input.txt", new FileReader.OnLineParsedListener() {
            @Override
            public void onLineParsed(String line) {
                Map<String, String> transactionMap = RecordParserUtil.parse(mColumnDefinitions, line);

                if (transactionMap == null || transactionMap.isEmpty()) {
                    // Data is null, therefore its not a valid record.
                    return;
                }

                mTransactions.add(transactionMap);
            }
        });

    }


    @Test
    public void testNumLineSizeForCorrectlyParsed() {
        assert(mTransactions.size() == TRANSACTIONS_SIZE);
    }

    @Test
    public void testCsvOutputLineSize() {
        String csvOutput = RecordWriterUtil.generateOutput(mTransactions);
        String[] csvOutputArray = csvOutput.split("\n");
        // Adding one due to header.
        assert(csvOutputArray.length == TRANSACTIONS_SIZE + 1);
    }

    @Test
    public void testCsvOuputHeaderIsCorrect() {
        String csvOutput = RecordWriterUtil.generateOutput(mTransactions);

        String[] csvOutputArray = csvOutput.split("\n");
        assert("Client_Information,Product_Information,Total_Transaction_Amount".equals(csvOutputArray[0]));
    }

    @Test
    public void testFirstTransactionOfCsvIsCorrect() {
        String csvOutput = RecordWriterUtil.generateOutput(mTransactions);
        String[] csvOutputArray = csvOutput.split("\n");
        assert("CL  432100020001,SGX FUNK    20100910,1".equals(csvOutputArray[1]));
    }

    @Test
    public void testLastTransactionOfCsvIsCorrect() {
        String csvOutput = RecordWriterUtil.generateOutput(mTransactions);
        String[] csvOutputArray = csvOutput.split("\n");
        assert("CL  432100030001,CME FUN1    20100910,-6".equals(csvOutputArray[csvOutputArray.length - 1]));
    }

    @Test
    public void testTransactionWithBadTotalTransactionAmountNumbers() {
        Map<String, String> transactionMap = RecordParserUtil.parse(mColumnDefinitions, TransactionData.TEST_TRANSACTION_1_WITH_BAD_TTAMOUNT);
        List<Map<String, String>> transactions = new ArrayList<>();
        transactions.add(transactionMap);

        String csvOutput = RecordWriterUtil.generateOutput(transactions);
        String[] csvOutputArray = csvOutput.split("\n");
        assert("CL  432100020001,SGX FUNK    20100910,null".equals(csvOutputArray[csvOutputArray.length - 1]));
    }
}
