package com.example.tracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tracker.databinding.ItemRunBinding
import com.example.tracker.db.Run
import com.example.tracker.others.TrackingUtility
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter: RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    lateinit var binding: ItemRunBinding

    inner class RunViewHolder(itemView: ItemRunBinding): RecyclerView.ViewHolder(itemView.root)

        val diffCallBack = object : DiffUtil.ItemCallback<Run>() {
            override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
                return  oldItem.hashCode() == newItem.hashCode()
            }
        }
        val differ = AsyncListDiffer(this@RunAdapter,diffCallBack)
        fun submitList(list: List<Run>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
//        return RunViewHolder(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_run,parent,false
//            )
//        )
        binding = ItemRunBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RunViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.image).into(binding.ivRunImage)

            val calender = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(calender.time)

            val avgSpeed = "${run.avgSpeed}Km/hr"
            binding.tvAvgSpeed.text = avgSpeed

            val distanceInKm = "${run.distance /1000f}km"
            binding.tvDistance.text = distanceInKm

            binding.tvTime.text = TrackingUtility.getFormattedTime(run.timeInMillis)

            val caloriesBurned = "${run.caloriesBurned}kcal"
            binding.tvCalories.text = caloriesBurned
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}