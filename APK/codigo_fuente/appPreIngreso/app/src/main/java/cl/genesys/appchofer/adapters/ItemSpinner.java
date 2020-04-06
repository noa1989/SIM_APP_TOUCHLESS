package cl.genesys.appchofer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.entities.SpinnerHelper;

public class ItemSpinner  extends ArrayAdapter<SpinnerHelper> implements Filterable {
    private Activity activity;
    private ArrayList<SpinnerHelper> data;
    SpinnerHelper tempValues=null;
    LayoutInflater inflater;
    boolean seleccionar=true;

    /*************  CustomAdapter Constructor *****************/
    public ItemSpinner(Activity activitySpinner, int textViewResourceId, ArrayList objects)    {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        this.activity = activitySpinner;
        this.data     = objects;


        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/

        View row = inflater.inflate(R.layout.item_list_spinner, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (SpinnerHelper) data.get(position);
        TextView label        = (TextView)row.findViewById(R.id.id);
        TextView sub          = (TextView)row.findViewById(R.id.nombre);

        // Set values for spinner each row
        label.setText(tempValues.getCod());
        sub.setText(tempValues.getNombre());

        return row;
    }

}
