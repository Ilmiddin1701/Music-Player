package com.ilmiddin1701.musicplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ilmiddin1701.musicplayer.R
import com.ilmiddin1701.musicplayer.databinding.ItemRvBinding
import com.ilmiddin1701.musicplayer.models.MusicData
import com.ilmiddin1701.musicplayer.utils.obj

class RvAdapter(var rvAction: RvAction, var list: MutableList<MusicData>) : Adapter<RvAdapter.Vh>() {

    inner class Vh(var itemRvBinding: ItemRvBinding) : ViewHolder(itemRvBinding.root) {
        fun onBind(musicData: MusicData, position: Int) {
            itemRvBinding.root.setOnClickListener {
                rvAction.itemClick(musicData, position)
            }
            if (musicData.id == obj.musicData?.id){
                if (obj.musicData?.isPlaying == 1) {
                        itemRvBinding.itemBackground.setBackgroundResource(R.drawable.item_style)
                        itemRvBinding.btnPlay.visibility = View.GONE
                        itemRvBinding.btnPause.visibility = View.VISIBLE
                    }else{
                        itemRvBinding.itemBackground.setBackgroundResource(R.drawable.item_style)
                        itemRvBinding.btnPlay.visibility = View.VISIBLE
                        itemRvBinding.btnPause.visibility = View.GONE
                    }
            }else{
                itemRvBinding.itemBackground.setBackgroundResource(R.drawable.item_style1)
                itemRvBinding.btnPlay.visibility = View.VISIBLE
                itemRvBinding.btnPause.visibility = View.GONE
            }
            itemRvBinding.tvMusicName.text = musicData.musicName
            itemRvBinding.tvSingerName.text = musicData.singer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    interface RvAction {
        fun itemClick(musicData: MusicData, position: Int)
    }
}