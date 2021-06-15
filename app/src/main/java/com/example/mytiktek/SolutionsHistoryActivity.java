package com.example.mytiktek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mytiktek.Adapters.HistoryAdapter;
import com.example.mytiktek.DataObjects.Book;
import com.example.mytiktek.service.NetworkChangeListener;
import com.example.mytiktek.sqlite.HistoryDB;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SolutionsHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvSolutions;
    private HistoryDB db;
    private NetworkChangeListener networkChangeListener;
    private ImageView btnHome, btnUploadSolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_solutions_history);

        networkChangeListener = new NetworkChangeListener();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        btnUploadSolution = (ImageView)findViewById(R.id.btnUploadSolution);
        btnUploadSolution.setOnClickListener(this);
        btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);
        lvSolutions = (ListView) findViewById(R.id.lvSolutions);
        db = new HistoryDB(this);


        Cursor data = db.getHistoryOfUser(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no solutions in history!", Toast.LENGTH_LONG).show();
        } else {
            List<Book> solutionList = new ArrayList<Book>();

            while (data.moveToNext()) {
                Book book = new Book();
                book.setBookName(data.getString(1));
                String encodedSolutionImage = data.getString(2);
                byte[] b = Base64.decode(encodedSolutionImage , Base64.DEFAULT);
                Bitmap  bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                book.setBookImage(bitmap);
                solutionList.add(book);
            }
            HistoryAdapter historyAdapter = new HistoryAdapter(this, 0, 0, solutionList);
            lvSolutions.setAdapter(historyAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnHome){
            setResult(0);
            finish();
        }else if(v == btnUploadSolution){
            Toast.makeText(this, "Upload solution will be available on v2.0", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        if(networkChangeListener != null){
            unregisterReceiver(networkChangeListener);
            networkChangeListener = null;
        }
        super.onStop();
    }
}