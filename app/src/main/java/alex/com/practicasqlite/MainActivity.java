package alex.com.practicasqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnGuardar, btnBuscar, btnBorrar, btnActualizar;
    EditText etId, etNombres, etTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnBorrar = (Button)findViewById(R.id.btnBorrar);
        btnActualizar = (Button)findViewById(R.id.btnActualizar);

        etId = (EditText)findViewById(R.id.etId);
        etNombres = (EditText)findViewById(R.id.etNombres);
        etTelefono = (EditText)findViewById(R.id.etTelefono);

        final AyudaBD ayudabd = new AyudaBD(getApplicationContext());

        btnGuardar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //permite escribir en la base de datos
                SQLiteDatabase db = ayudabd.getWritableDatabase();
                //obtiene el contenido de los valores a ingresar
                ContentValues valores = new ContentValues();

                //estos valores seran enviados a la tabla correspondiente asociados a sus Textos corrspondientes
                valores.put(AyudaBD.DatosTabla.COLUMNA_ID,etId.getText().toString());
                valores.put(AyudaBD.DatosTabla.COLUMNA_NOMBRES,etNombres.getText().toString());
                valores.put(AyudaBD.DatosTabla.COLUMNA_TELEFONO,etTelefono.getText().toString());

                Long IdGuardado = db.insert(AyudaBD.DatosTabla.NOMBRE_TABLA, AyudaBD.DatosTabla.COLUMNA_ID, valores);
                Toast.makeText(getApplicationContext(), "Se guardo el dato: "+IdGuardado, Toast.LENGTH_LONG).show();

                etId.setText("");
                etNombres.setText("");
                etTelefono.setText("");



            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                SQLiteDatabase db = ayudabd.getReadableDatabase();
                String [] argsel = {etId.getText().toString()};
                String[] projection = {
                        AyudaBD.DatosTabla.COLUMNA_NOMBRES,
                        AyudaBD.DatosTabla.COLUMNA_TELEFONO,
                };
                Cursor c = db.query(AyudaBD.DatosTabla.NOMBRE_TABLA, projection, AyudaBD.DatosTabla.COLUMNA_ID+"=?",argsel, null, null, null);
                //
                try{
                    c.moveToFirst();
                    etNombres.setText(c.getString(0));
                    etTelefono.setText(c.getString(1));
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Ha ocurrido un ERROR"+ " "+e,Toast.LENGTH_LONG).show();
                }


            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                SQLiteDatabase db = ayudabd.getWritableDatabase();
                String Selection = AyudaBD.DatosTabla.COLUMNA_ID+"=?";
                String [] argsel = {etId.getText().toString()};

                int IdBorrado = db.delete(AyudaBD.DatosTabla.NOMBRE_TABLA, Selection, argsel);
                Toast.makeText(getApplicationContext(), "Se ha borrado el dato: "+ IdBorrado, Toast.LENGTH_LONG).show();


            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SQLiteDatabase db = ayudabd.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(AyudaBD.DatosTabla.COLUMNA_NOMBRES, etNombres.getText().toString());
                valores.put(AyudaBD.DatosTabla.COLUMNA_TELEFONO, etTelefono.getText().toString());
                String [] argsel = {etId.getText().toString()};
                String Selection = AyudaBD.DatosTabla.COLUMNA_ID+"=?";

                int count = db.update(AyudaBD.DatosTabla.NOMBRE_TABLA, valores, Selection,argsel);
                Toast.makeText(getApplicationContext(), "Se actualizó el dato: "+ count, Toast.LENGTH_LONG).show();




            }
        });
    }
}
