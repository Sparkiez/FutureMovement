package com.waleong.futuremovement.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.waleong.futuremovement.R;
import com.waleong.futuremovement.helper.TransactionParser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.loading_text)
    protected TextView mLoadingTextView;
    @BindView(R.id.error_text)
    protected TextView mErrorTextView;
    @BindView(R.id.result_text)
    protected TextView mResultTextView;
    @BindView(R.id.generate_button)
    protected Button mGenerateButton;

    private TransactionParser mTransactionParser;
    private boolean mIsGeneratingReport;
    private String mOutputFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.tag(this.getClass().getSimpleName());

        mTransactionParser = new TransactionParser();
        setupClickListeners();
    }

    private void setupClickListeners() {
        mGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToGenerateReport();
            }
        });

        mResultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(mOutputFilePath);
            }
        });
    }

    private void tryToGenerateReport() {
        if (mIsGeneratingReport) {
            // We don't want to generate the report if it is
            // doing it now.
            return;
        }

        hideAllMessages();
        showTextViewWithMessage(mLoadingTextView, "Loading, please wait");

        mIsGeneratingReport = true;

        // Using threading operation to ensure conccurency on the UI
        // as we handle file reading and writing operation.
        Observable.fromCallable(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return mTransactionParser.parseAndCreateReport(MainActivity.this, R.raw.col_definition,
                                R.raw.input, "Output.csv");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mIsGeneratingReport = false;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String fileOutputPath) throws Exception {
                        mOutputFilePath = fileOutputPath;
                        showTextViewWithMessage(mResultTextView, "Report has been successfully generated. Click here to access your file at " + fileOutputPath);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showTextViewWithMessage(mErrorTextView, "There has been an error. " + throwable.getMessage());
                    }
                });
    }

    private void hideAllMessages() {
        mLoadingTextView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        mResultTextView.setVisibility(View.GONE);
    }

    private void showTextViewWithMessage(TextView textView, String message) {
        hideAllMessages();
        textView.setVisibility(View.VISIBLE);
        textView.setText(message);
    }

    private void openFile(String filePath) {
        if (filePath == null) {
            return;
        }

        try {
            Uri uri = Uri.fromFile(new File(filePath));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "text/plain");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
        catch (Exception e) {
            showTextViewWithMessage(mErrorTextView, "There has been an error with opening the file. " + e.getMessage());
        }
    }
}
