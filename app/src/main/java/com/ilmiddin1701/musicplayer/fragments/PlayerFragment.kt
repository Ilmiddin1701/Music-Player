package com.ilmiddin1701.musicplayer.fragments

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.ilmiddin1701.musicplayer.R
import com.ilmiddin1701.musicplayer.adapters.RvAdapter
import com.ilmiddin1701.musicplayer.databinding.FragmentPlayerBinding
import com.ilmiddin1701.musicplayer.models.MusicData
import com.ilmiddin1701.musicplayer.utils.obj
import com.ilmiddin1701.musicplayer.utils.obj.musicFiles

class PlayerFragment : Fragment(), RvAdapter.RvAction {
    private val binding by lazy { FragmentPlayerBinding.inflate(layoutInflater) }
    private lateinit var rvAdapter: RvAdapter
    lateinit var handler: Handler
    private var menuOrImage = false
    val liveData = MutableLiveData<Int>()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val list: MutableList<MusicData> = requireActivity().musicFiles()
        rvAdapter = RvAdapter(this@PlayerFragment, list)
        handler = Handler(Looper.getMainLooper())
        binding.apply {
            musicName.text = obj.musicData?.musicName
            singerName.text = obj.musicData?.singer
            btnMenu.setOnClickListener {
                if (!menuOrImage){
                    musicImage.visibility = View.INVISIBLE
                    cardView.visibility = View.VISIBLE
                    menuOrImage = true
                } else {
                    musicImage.visibility = View.VISIBLE
                    cardView.visibility = View.INVISIBLE
                    menuOrImage = false
                }
            }
            musicImage.setOnClickListener {
                musicImage.visibility = View.INVISIBLE
                cardView.visibility = View.VISIBLE
                menuOrImage = true
            }
            rv.adapter = rvAdapter
            if (obj.musicData?.musicImage != "") {
                musicImage1.setImageURI(Uri.parse(obj.musicData!!.musicImage))
            } else {
                musicImage1.setImageResource(R.drawable.music_icon_big)
            }
            if (obj.mediaPlayer!!.isPlaying){
                btnPause.visibility = View.VISIBLE
                btnPlay.visibility = View.INVISIBLE
            } else if (!obj.mediaPlayer!!.isPlaying){
                btnPlay.visibility = View.VISIBLE
                btnPause.visibility = View.INVISIBLE
            }
            musicName.text = obj.musicData!!.musicName
            singerName.text = obj.musicData!!.singer
            btnPopBakStake.setOnClickListener {
                findNavController().popBackStack()
            }

            btnPlay.setOnClickListener {
                obj.musicData?.isPlaying = 1
                obj.mediaPlayer!!.start()
                btnPlay.visibility = View.INVISIBLE
                btnPause.visibility = View.VISIBLE
                rvAdapter.notifyItemChanged(obj.p!!)
            }

            btnBack.setOnClickListener {
                if (obj.p != 0) {
                    obj.mediaPlayer!!.stop()
                    obj.mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(list[obj.p!! - 1].music))
                    tvDuration.text = millisToTime(obj.mediaPlayer!!.duration)
                    musicName.text = list[obj.p!! - 1].musicName
                    singerName.text = list[obj.p!! - 1].singer
                    obj.musicData?.id = list[obj.p!! - 1].id
                    obj.musicData?.music = list[obj.p!! - 1].music
                    obj.musicData?.musicName = list[obj.p!! - 1].musicName
                    obj.musicData?.singer = list[obj.p!! - 1].singer
                    obj.musicData?.isPlaying = 1
                    rv.scrollToPosition(obj.p!!-2)
                    rvAdapter.notifyItemRangeChanged(obj.p!!-1, obj.p!!)
                    btnPlay.visibility = View.INVISIBLE
                    btnPause.visibility = View.VISIBLE
                    seekBar.max = obj.mediaPlayer?.duration!!
                    handler.postDelayed(runnable, 1000)
                    obj.p = obj.p!! - 1
                    obj.mediaPlayer!!.start()
                    rvAdapter.notifyDataSetChanged()
                }
            }
            btnNext.setOnClickListener {
                if (obj.p != list.size-1) {
                    obj.mediaPlayer!!.stop()
                    obj.mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(list[obj.p!! + 1].music))
                    tvDuration.text = millisToTime(obj.mediaPlayer!!.duration)
                    musicName.text = list[obj.p!! + 1].musicName
                    singerName.text = list[obj.p!! + 1].singer
                    obj.musicData?.id = list[obj.p!! + 1].id
                    obj.musicData?.music = list[obj.p!! + 1].music
                    obj.musicData?.musicName = list[obj.p!! + 1].musicName
                    obj.musicData?.singer = list[obj.p!! + 1].singer
                    obj.musicData?.isPlaying = 1
                    rv.scrollToPosition(obj.p!!+2)
                    rvAdapter.notifyItemRangeChanged(obj.p!!, obj.p!! + 1)
                    btnPlay.visibility = View.INVISIBLE
                    btnPause.visibility = View.VISIBLE
                    seekBar.max = obj.mediaPlayer?.duration!!
                    handler.postDelayed(runnable, 1000)
                    obj.p = obj.p!! + 1
                    obj.mediaPlayer!!.start()
                }
            }

            btnPause.setOnClickListener {
                obj.musicData?.isPlaying = 2
                obj.mediaPlayer!!.pause()
                btnPause.visibility = View.INVISIBLE
                btnPlay.visibility = View.VISIBLE
                rvAdapter.notifyDataSetChanged()
            }

            seekBar.max = obj.mediaPlayer?.duration!!
            tvDuration.text = millisToTime(obj.mediaPlayer!!.duration)
            handler.postDelayed(runnable, 1000)

            liveData.observe(viewLifecycleOwner){
                if (obj.p != list.size-1) {
                    obj.mediaPlayer!!.stop()
                    obj.mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(list[it].music))
                    tvDuration.text = millisToTime(obj.mediaPlayer!!.duration)
                    musicName.text = list[it].musicName
                    singerName.text = list[it].singer
                    obj.musicData?.id = list[it].id
                    obj.musicData?.music = list[it].music
                    obj.musicData?.musicName = list[it].musicName
                    obj.musicData?.singer = list[it].singer
                    obj.musicData?.isPlaying = 1
                    rv.scrollToPosition(obj.p!!+2)
                    rvAdapter.notifyItemRangeChanged(obj.p!!, it)
                    btnPlay.visibility = View.INVISIBLE
                    btnPause.visibility = View.VISIBLE
                    seekBar.max = obj.mediaPlayer?.duration!!
                    handler.postDelayed(runnable, 1000)
                    obj.p = it
                    obj.mediaPlayer!!.start()
                }
            }

            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser){
                        obj.mediaPlayer!!.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        return binding.root
    }

    private val runnable = object : Runnable{
        override fun run() {
            binding.apply {
                tvPosition.text = millisToTime(obj.mediaPlayer!!.currentPosition)
                seekBar.progress = obj.mediaPlayer!!.currentPosition
                if (millisToTime(obj.mediaPlayer!!.currentPosition) == millisToTime(obj.mediaPlayer!!.duration)){
                    liveData.postValue(obj.p!!+1)
                }
            }
            handler.postDelayed(this, 1000)
        }
    }

    fun millisToTime(millis: Int): String {
        val time = millis / 1000
        val hour = time / 3600
        val minute = (time % 3600) / 60
        val second2 = (time % 3600) % 60
        return "$hour:$minute:$second2"
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun itemClick(musicData: MusicData, position: Int) {
        val list: MutableList<MusicData> = requireActivity().musicFiles()
        if (musicData.music != obj.musicData?.music) {
            obj.mediaPlayer!!.stop()
            musicData.isPlaying = 1
            obj.musicData = musicData
            obj.mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(musicData.music))
            obj.mediaPlayer!!.start()
            binding.musicName.text = musicData.musicName
            binding.singerName.text = musicData.singer
            binding.btnPause.visibility = View.VISIBLE
            binding.btnPlay.visibility = View.INVISIBLE
            obj.p = position
            binding.seekBar.max = obj.mediaPlayer?.duration!!
            binding.tvDuration.text = millisToTime(obj.mediaPlayer!!.duration)
            rvAdapter = RvAdapter(this@PlayerFragment, list)
            binding.rv.adapter = rvAdapter
            binding.musicImage.visibility = View.VISIBLE
            binding.cardView.visibility = View.INVISIBLE
            menuOrImage = false
        } else if (!obj.mediaPlayer!!.isPlaying){
            obj.mediaPlayer!!.start()
            obj.musicData!!.isPlaying = 1
            binding.btnPlay.visibility = View.INVISIBLE
            binding.btnPause.visibility = View.VISIBLE
            binding.seekBar.max = obj.mediaPlayer?.duration!!
            binding.tvDuration.text = millisToTime(obj.mediaPlayer!!.duration)
            rvAdapter = RvAdapter(this@PlayerFragment, list)
            binding.rv.adapter = rvAdapter
            binding.musicImage.visibility = View.VISIBLE
            binding.cardView.visibility = View.INVISIBLE
            menuOrImage = false
        } else if (obj.mediaPlayer!!.isPlaying){
            binding.musicImage.visibility = View.VISIBLE
            binding.cardView.visibility = View.INVISIBLE
            menuOrImage = false
        }
    }
}