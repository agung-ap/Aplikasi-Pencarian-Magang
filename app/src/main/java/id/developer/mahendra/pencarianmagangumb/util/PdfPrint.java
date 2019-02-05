package id.developer.mahendra.pencarianmagangumb.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import id.developer.mahendra.pencarianmagangumb.model.UsersApply;

public class PdfPrint {
    private final String TAG;
    private File pdfFile;
    private Context context;
    private ArrayList<UsersApply> getApplyData;

    public PdfPrint(Context context,String TAG ,ArrayList<UsersApply> getApplyData ) {
        this.TAG = TAG;
        this.getApplyData = getApplyData;
        this.context = context;
    }

    public void createPdf() throws FileNotFoundException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        pdfFile = new File(docsFolder.getAbsolutePath(),"Apply_Report.pdf" + UUID.randomUUID().toString());
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        //title
        document.add(new Paragraph("Report Data Kerja Praktek\n\n"));
        //table
        PdfPTable table = new PdfPTable(2);
        for (int i = 0; i < getApplyData.size(); i++){
            //row 1 column 1
            PdfPCell cell = new PdfPCell(new Phrase(
                    "Nama\t\t : \n" +
                            "Nim\t\t : \n" +
                            "Job\t\t : \n" +
                            "Company\t\t : \n" +
                            "Date & Time\t\t : " ));
            table.addCell(cell);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //row 1 column 2
            cell = new PdfPCell(new Phrase(
                    getApplyData.get(i).getUserName()+ "\n" +
                            getApplyData.get(i).getUserNim()+ "\n" +
                            getApplyData.get(i).getTitle()+ "\n" +
                            getApplyData.get(i).getCompany()+ "\n" +
                            getApplyData.get(i).getDate()+ "\n"));
            table.addCell(cell);

        }
        //add table to page
        document.add(table);
        document.close();
        previewPdf();

    }

    private void previewPdf() {
        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".provider",
                    pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(intent);
        }else{
            Toast.makeText(context,"Silahkan Download PDF Viewer untuk melihat report",Toast.LENGTH_SHORT).show();
        }
    }
}
