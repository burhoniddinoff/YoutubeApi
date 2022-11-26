package com.example.youtubeapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeapi.adapter.YoutubeAdapter
import com.example.youtubeapi.databinding.FragmentFirstBinding
import com.example.youtubeapi.model.YoutubeModel
import com.example.youtubeapi.network.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val youtubeAdapter by lazy { YoutubeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = youtubeAdapter
        }
        callApi()
        youtubeAdapter.onClick = {
            val bundle = bundleOf("item" to it)
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtras(bundle)
            requireContext().startActivity(intent)
        }
    }

    private fun callApi() {
        RetroInstance.retroInstance().getAllData().enqueue(object : Callback<YoutubeModel> {
            override fun onResponse(call: Call<YoutubeModel>, response: Response<YoutubeModel>) {
                if (response.isSuccessful) {
                    binding.prb.isVisible = false
                    youtubeAdapter.submitList(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<YoutubeModel>, t: Throwable) {
                Log.d("@@@", "onFailure: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}