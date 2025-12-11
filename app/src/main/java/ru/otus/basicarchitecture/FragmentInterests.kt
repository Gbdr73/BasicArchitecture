package ru.otus.basicarchitecture

import android.os.Bundle
import android.content.res.ColorStateList
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import ru.otus.basicarchitecture.databinding.FragmentInterestsBinding
import kotlin.getValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint



class FragmentInterests : Fragment() {
    private var _binding: FragmentInterestsBinding? = null
    private val binding: FragmentInterestsBinding
        get() = _binding ?: throw RuntimeException("FragmentInterestsBinding == null")
    private val viewModel: InterestsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadListOfInterests()
        observeViewModel()
        binding.buttonNext.setOnClickListener {
            viewModel.setInterests(getSelectedInterests())
            viewModel.checkInterests()
        }
    }

    private fun observeViewModel(){
        viewModel.listOfInterests.observe(viewLifecycleOwner) {
            val selectedColor = ContextCompat.getColor(requireContext(), R.color.blue_light)
            val defaultColor = ContextCompat.getColor(requireContext(), R.color.gray_light)
            setupChips(it, selectedColor, defaultColor)
        }

        viewModel.canContinue.observe(viewLifecycleOwner) {
            if (it){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, FragmentResult.newInstance())
                    .commit()
            } else {
                Toast.makeText(requireContext(), "It is necessary to mark at least one tag", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInterestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupChips(interests: List<String>, selectedColor: Int, defaultColor: Int) {
        interests.forEach { interest ->
            val chip = Chip(requireContext()).apply {
                text = interest
                isCheckable = true
                isCheckedIconVisible = false
                chipBackgroundColor = ColorStateList.valueOf(defaultColor)

                setOnCheckedChangeListener { _, isChecked ->
                    chipBackgroundColor = ColorStateList.valueOf(
                        if (isChecked) selectedColor else defaultColor
                    )
                }
            }

            binding.chipGroupInterests.addView(chip)
        }
    }

    fun getSelectedInterests(): List<String> {
        val selected = mutableListOf<String>()

        for (i in 0 until binding.chipGroupInterests.childCount) {
            val child = binding.chipGroupInterests.getChildAt(i)

            if (child is Chip && child.isChecked) {
                selected.add(child.text.toString())
            }
        }
        return selected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentInterests()
    }
}
