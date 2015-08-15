package br.com.willfelix.ninjabluetooth.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.willfelix.ninjabluetooth.R;

/**
 * Created by willfelix on 8/5/15.
 */
public class NinjaListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<User> itens;

    public NinjaListAdapter(Context context) {
        itens = new ArrayList<User>();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;

        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (view == null) {
            //infla o layout para podermos pegar as views
            view = mInflater.inflate(R.layout.item_list, null);
            itemHolder = new ItemSuporte();
            itemHolder.name = ((TextView) view.findViewById(R.id.name));
            itemHolder.edit = ((TextView) view.findViewById(R.id.msg));
            itemHolder.image = ((ImageView) view.findViewById(R.id.profile));
            itemHolder.image2 = ((ImageView) view.findViewById(R.id.profile2));

            //define os itens na view;
            view.setTag(itemHolder);
        }
        // Se a view já existe pega os itens.
        else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        User item = itens.get(position);
        itemHolder.name.setText(item.getName());
        itemHolder.edit.setText(item.getMessage());

        if (item.getImage() == null) {
            Bitmap b = BitmapFactory.decodeResource(mInflater.getContext().getResources(), R.drawable.ninja);
            item.setBitmap(b);
        }

        if (item.isAnother()) {
            itemHolder.image2.setImageBitmap(item.getBitmap());
            itemHolder.image2.setVisibility(View.VISIBLE);
            itemHolder.image.setVisibility(View.INVISIBLE);
        } else {
            itemHolder.image.setImageBitmap(item.getBitmap());
            itemHolder.image.setVisibility(View.VISIBLE);
            itemHolder.image2.setVisibility(View.INVISIBLE);
        }

        return view;

    }

    public void addMessage(User user) {
        addMessage(user, false);
    }

    public void addMessage(User user, boolean another) {
        if (itens == null) {
            itens = new ArrayList<User>();
        }

        User item = new User(user, another);
        itens.add(item);
    }

    /**
     * Classe de suporte para os itens do layout.
     */
    private class ItemSuporte {
        TextView name;
        TextView edit;
        ImageView image;
        ImageView image2;
    }
}
