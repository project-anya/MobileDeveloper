package com.capstone.anya.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.anya.databinding.FragmentHomeBinding
import com.capstone.anya.ui.child.list.ChildListActivity
import com.capstone.anya.ui.mother.input.MotherInputActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.monChildWidget.buttonBuatDataAnak.root.setOnClickListener {
            val mIntent = Intent(requireActivity(), ChildListActivity::class.java)
            startActivity(mIntent)

        }

        binding.monMotherWidget.buttonBuatDataIbu.root.setOnClickListener {
            val mIntent = Intent(requireActivity(), MotherInputActivity::class.java)
            startActivity(mIntent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}