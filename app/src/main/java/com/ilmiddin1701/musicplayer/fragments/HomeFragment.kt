package com.ilmiddin1701.musicplayer.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ilmiddin1701.musicplayer.R
import com.ilmiddin1701.musicplayer.adapters.RvAdapter
import com.ilmiddin1701.musicplayer.databinding.FragmentHomeBinding
import com.ilmiddin1701.musicplayer.models.MusicData
import com.ilmiddin1701.musicplayer.utils.obj
import com.ilmiddin1701.musicplayer.utils.obj.musicFiles

@Suppress("DEPRECATION")
class HomeFragment : Fragment(), RvAdapter.RvAction {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var rvAdapter: RvAdapter
    private lateinit var list: MutableList<MusicData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkAndRequestPermissions()
        val navOptions = NavOptions.Builder()
        navOptions.setEnterAnim(R.anim.anim)
        navOptions.setPopExitAnim(R.anim.anim2)
        list = requireContext().musicFiles()
        binding.apply {
            musicName.isSelected = true
            singerName.isSelected = true
            if (obj.musicData != null) {
                musicName.text = obj.musicData!!.musicName
                singerName.text = obj.musicData!!.singer
            }
            playerAction.setOnClickListener {
                if (obj.musicData != null) {
                    findNavController().navigate(R.id.playerFragment, bundleOf(), navOptions.build())
                }
            }
            if (obj.mediaPlayer != null && obj.mediaPlayer!!.isPlaying) {
                btnPlayPause.setImageResource(R.drawable.ic_stop)
                obj.musicData?.isPlaying = 1
            } else {
                btnPlayPause.setImageResource(R.drawable.ic_play)
                obj.musicData?.isPlaying = 2
            }
            btnPlayPause.setOnClickListener {
                if (obj.mediaPlayer != null && obj.mediaPlayer!!.isPlaying) {
                    obj.musicData?.isPlaying = 2
                    obj.mediaPlayer!!.pause()
                    musicName.isSelected = false
                    singerName.isSelected = false
                    btnPlayPause.setImageResource(R.drawable.ic_play)
                    rvAdapter.notifyItemChanged(obj.p!!)
                } else if (obj.mediaPlayer != null && !obj.mediaPlayer!!.isPlaying) {
                    obj.musicData?.isPlaying = 1
                    obj.mediaPlayer!!.start()
                    musicName.isSelected = true
                    singerName.isSelected = true
                    btnPlayPause.setImageResource(R.drawable.ic_stop)
                    rvAdapter.notifyItemChanged(obj.p!!)
                }
            }
            btnNext.setOnClickListener {
                if (obj.mediaPlayer != null && obj.p != list.size - 1) {
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
                    rv.scrollToPosition(obj.p!! + 2)
                    rvAdapter.notifyItemRangeChanged(obj.p!!, obj.p!! + 1)
                    obj.p = obj.p!! + 1
                }
            }
        }
        return binding.root
    }

    override fun itemClick(musicData: MusicData, position: Int) {
        val navOptions = NavOptions.Builder()
        navOptions.setEnterAnim(R.anim.anim)
        navOptions.setPopExitAnim(R.anim.anim2)
        if (musicData.music != obj.musicData?.music || obj.mediaPlayer == null) {
            findNavController().navigate(R.id.playerFragment, bundleOf(), navOptions.build())
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
        } else {
            findNavController().navigate(R.id.playerFragment, bundleOf(), navOptions.build())
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_AUDIO), 1)
            } else {
                loadMusicFiles()
            }
        } else {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                loadMusicFiles()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Ruxsat berilgandan so'ng fragmentni qayta yuklash
                findNavController().navigate(R.id.homeFragment2)
            } else {
                Toast.makeText(context, "Musiqa fayllarini o'qib bo'lmaydi😔", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun loadMusicFiles() {
        list = requireContext().musicFiles()
        rvAdapter = RvAdapter(this@HomeFragment, list)
        binding.rv.adapter = rvAdapter

        // Adapterni yangilash
        rvAdapter.notifyDataSetChanged()

        obj.musicData?.let {
            binding.musicName.text = it.musicName
            binding.singerName.text = it.singer
            binding.btnPlayPause.setImageResource(if (it.isPlaying == 1) R.drawable.ic_stop else R.drawable.ic_play)
        }
    }
}