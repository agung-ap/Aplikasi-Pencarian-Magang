package id.developer.mahendra.pencarianmagangumb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ViewUserCv extends AppCompatActivity {
    private static final String TAG = ViewUserCv.class.getSimpleName();
    private String cvUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_cv);

        getSupportActionBar().setTitle("User CV");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cvUrl = getIntent().getStringExtra("cv");
        Log.i(TAG, "cv url = " + cvUrl);
        readPdf(cvUrl);
    }
    //fungsi untuk membaca file pdf
    private void readPdf(String filePath){
        try {
            InputStream stream = new URL(filePath).openStream();
            PDFView pdfView = (PDFView)findViewById(R.id.pdf_view); //hubungkan layout dengan java code
            //mulai baca file pdf dari folder Assets
            pdfView.fromStream(stream)
                    //tampilkan toast bila pembacaan pdf Eror
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(ViewUserCv.this, "terjadi eror", Toast.LENGTH_SHORT).show();
                            Log.e(TAG , "Error " + t.getLocalizedMessage());
                        }
                    })
                    //tampilkan pdf pada device
                    .load();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG , "Error " + e.getLocalizedMessage());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
