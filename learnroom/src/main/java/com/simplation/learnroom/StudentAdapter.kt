package com.simplation.learnroom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private val context: Context, private val studentList: MutableList<Student>) :
    RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: AppCompatTextView = itemView.findViewById(R.id.tvId)
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)
        val tvAge: AppCompatTextView = itemView.findViewById(R.id.tvAge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val student = studentList[position]

        // 进行数据绑定
        holder.tvId.text = student.name
        holder.tvName.text = student.name
        holder.tvAge.text = student.age
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}