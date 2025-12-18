package com.example.englishlearningapp.ui.tests

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController


class TestsFragment : Fragment(R.layout.fragment_tests) {

    private lateinit var viewModel: TestsViewModel
    private lateinit var adapter: TestsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1️⃣ ИНИЦИАЛИЗИРУЕМ ViewModel СРАЗУ
        viewModel = ViewModelProvider(this)[TestsViewModel::class.java]

        // 2️⃣ RecyclerView
        val recycler = view.findViewById<RecyclerView>(R.id.testsRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = TestsAdapter { test ->
            // Пока заглушка — позже тут будет переход на экран теста
            findNavController().navigate(R.id.testRunFragment)

        }

        recycler.adapter = adapter

        // 3️⃣ Подписка на данные
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tests.collect { list ->
                adapter.submitList(list)
            }
        }
    }
}
