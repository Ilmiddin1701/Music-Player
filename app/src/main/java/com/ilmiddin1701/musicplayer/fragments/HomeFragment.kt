package com.ilmiddin1701.musicplayer.fragments

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ilmiddin1701.musicplayer.R
import com.ilmiddin1701.musicplayer.adapters.RvAdapter
import com.ilmiddin1701.musicplayer.databinding.FragmentHomeBinding
import com.ilmiddin1701.musicplayer.models.MusicData
import com.ilmiddin1701.musicplayer.utils.obj
import com.ilmiddin1701.musicplayer.utils.obj.musicFiles

class HomeFragment : Fragment(), RvAdapter.RvAction {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var rvAdapter: RvAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navOptions = NavOptions.Builder()
        navOptions.setEnterAnim(R.anim.anim)
        navOptions.setPopExitAnim(R.anim.anim2)
        val list: MutableList<MusicData> = requireActivity().musicFiles()
        binding.apply {
            if (obj.musicData != null){
                musicName.text = obj.musicData!!.musicName
                singerName.text = obj.musicData!!.singer
            }
            playerAction.setOnClickListener {
                if (obj.musicData != null){
                    findNavController().navigate(R.id.playerFragment, bundleOf(), navOptions.build())
                }
            }
            if (obj.mediaPlayer != null && obj.mediaPlayer!!.isPlaying){
                btnPlayPause.setImageResource(R.drawable.ic_stop)
                obj.musicData?.isPlaying = 1
            } else {
                btnPlayPause.setImageResource(R.drawable.ic_play)
                obj.musicData?.isPlaying = 2
            }
            btnPlayPause.setOnClickListener {
                if (obj.mediaPlayer != null && obj.mediaPlayer!!.isPlaying ){
                    obj.musicData?.isPlaying = 2
                    obj.mediaPlayer!!.pause()
                    btnPlayPause.setImageResource(R.drawable.ic_play)
                    rvAdapter.notifyItemChanged(obj.p!!)
                } else if (obj.mediaPlayer != null && !obj.mediaPlayer!!.isPlaying){
                    obj.musicData?.isPlaying = 1
                    obj.mediaPlayer!!.start()
                    btnPlayPause.setImageResource(R.drawable.ic_stop)
                    rvAdapter.notifyItemChanged(obj.p!!)
                }
            }
            btnNext.setOnClickListener {
                if (obj.p != list.size-1) {
                    obj.mediaPlayer!!.stop()
                    obj.mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(list[obj.p!! + 1].music))
                    obj.musicData?.id = list[obj.p!! + 1].id
                    obj.musicData?.music = list[obj.p!! + 1].music
                    obj.musicData?.musicName = list[obj.p!! + 1].musicName
                    obj.musicData?.singer = list[obj.p!! + 1].singer
                    obj.musicData?.isPlaying = 1
                    btnPlayPause.setImageResource(R.drawable.ic_stop)
                    musicName.text = list[obj.p!! + 1].musicName
                    singerName.text = list[obj.p!! + 1].singer
                    obj.mediaPlayer!!.start()
                    rv.scrollToPosition(obj.p!!+2)
                    rvAdapter.notifyItemRangeChanged(obj.p!!, obj.p!!+1)
                    obj.p = obj.p!! + 1
                }
            }
            rvAdapter = RvAdapter(this@HomeFragment, list)
            rv.adapter = rvAdapter
        }
        return binding.root
    }

    override fun itemClick(musicData: MusicData, position: Int) {
        val navOptions = NavOptions.Builder()
        navOptions.setEnterAnim(R.anim.anim)
        navOptions.setPopExitAnim(R.anim.anim2)
        if (musicData.music != obj.musicData?.music || obj.mediaPlayer == null) {
            if (obj.mediaPlayer == null) {
                obj.mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(musicData.music))
                obj.mediaPlayer!!.start()
            } else {
                obj.mediaPlayer!!.stop()
                obj.mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(musicData.music))
                obj.musicData = musicData
                obj.mediaPlayer!!.start()
            }
            binding.musicName.text = musicData.musicName
            binding.singerName.text = musicData.singer
            musicData.isPlaying = 1
            obj.musicData = musicData
            obj.p = position
            binding.btnPlayPause.setImageResource(R.drawable.ic_stop)
            findNavController().navigate(R.id.playerFragment, bundleOf(), navOptions.build())
        } else {
            findNavController().navigate(R.id.playerFragment, bundleOf(), navOptions.build())
        }
    }
}