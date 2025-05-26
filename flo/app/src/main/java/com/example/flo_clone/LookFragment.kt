package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.ChartAdapter
import com.example.flo_clone.databinding.FragmentLookBinding
import com.example.flo_clone.model.ChartItem

class LookFragment : Fragment() {

    private var _binding: FragmentLookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 👉 카테고리 버튼 선택 처리
        val buttons = listOf(
            binding.categoryChart,
            binding.categoryVideo,
            binding.categoryGenre,
            binding.categorySituation,
            binding.categoryMood,
            binding.categoryAudio
        )

        buttons.forEach { it.isSelected = false }
        binding.categoryChart.isSelected = true

        buttons.forEach { button ->
            button.setOnClickListener {
                buttons.forEach { it.isSelected = false }
                button.isSelected = true
            }
        }

        // 👉 더미 차트 리스트 데이터 연결
        val dummyData = List(10) { index -> ChartItem("Item $index") }
        val chartAdapter = ChartAdapter(dummyData)
        binding.lookChartRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.lookChartRecycler.adapter = chartAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
