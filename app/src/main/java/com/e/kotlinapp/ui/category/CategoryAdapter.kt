package com.e.kotlinapp.ui.category;

import android.content.Context;
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.e.kotlinapp.BR
import com.e.kotlinapp.databinding.ItemCategoryBinding
import com.e.kotlinapp.databinding.ItemCategoryBindingImpl
import com.e.kotlinapp.local.IkoponCategoryDatabase
import com.e.kotlinapp.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Constructor

class CategoryAdapter(
    var context: Context,
    private var layout: Int,
    private var properties: MutableList<Category>,
    val  selectItemCallBack: (position: Int, view: View) -> Unit) : RecyclerView.Adapter<CategoryAdapter.PropertiesViewHolder>() {

    private var index: Int = 0;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertiesViewHolder {
        return PropertiesViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), layout, parent, false));
    }


    override fun onBindViewHolder(holder: PropertiesViewHolder, position: Int) {
//        holder.binding.setCategory(properties.get(position));
        holder.binding.category = properties.get(position)
    }


    override fun getItemCount(): Int {
        return properties.size;
    }

    inner class PropertiesViewHolder(var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this);
        }


        override fun onClick(v: View) {
            var position: Int = adapterPosition;

            CoroutineScope(Dispatchers.IO).launch {
               IkoponCategoryDatabase.getInstance(context).getPostsDao().insertPosts(Category(catSlug="ddddddaeqwesdf", name="fffewrsadf"+position, icon="asqwerdff" ,logo="sdafewda"))
           }
            for (i in properties.listIterator()) {
                i.selected = false
            }
            properties.get(position).selected = true
//            notifyDataSetChanged()
            selectItemCallBack(position, v)
        }
    }

}

//class PropertiesViewHolder(var binding: ItemCategoryBinding, private var properties: MutableList<Category>) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
//
//    init {
//        itemView.setOnClickListener(this);
//    }
//
//
//    override fun onClick(v: View) {
//        var position: Int = adapterPosition;
//        for (i in properties.listIterator()) {
//            i.selected = false
//        }
//        properties[position].selected = true
//        notifyDataSetChanged()
//        selectItemCallBack(position, v)
//    }
//}

