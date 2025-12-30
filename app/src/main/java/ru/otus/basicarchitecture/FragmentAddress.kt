package ru.otus.basicarchitecture

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import kotlin.getValue
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import dagger.hilt.android.AndroidEntryPoint
import android.widget.ArrayAdapter

@AndroidEntryPoint
class FragmentAddress : Fragment() {
    private var _binding: FragmentAddressBinding? = null
    private val binding: FragmentAddressBinding
        get() = _binding ?: throw RuntimeException("FragmentAddressBinding == null")

    private val viewModel: AddressViewModel by viewModels()
    private val adapter by lazy {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf<String>()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addressInput.setAdapter(adapter)
        observeViewModel()
        addTextChangedListeners()
        binding.buttonNext.setOnClickListener {
            viewModel.validateData()
        }

        binding.addressInput.setOnItemClickListener {  _, _, position, _ ->
            val selectedItem = binding.addressInput.adapter.getItem(position) as? UserAddress
                ?: return@setOnItemClickListener
            selectedItem.let {
                val address = listOf(
                    it.country,
                    it.city,
                    it.street,
                    it.house,
                    it.block
                ).filter { !it.isBlank() }
                    .joinToString(", ")
                binding.addressInput.setText(address)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun observeViewModel(){
        viewModel.errorNetwork.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.errorEmptyAddress.observe(viewLifecycleOwner) {
            with(binding) {
                if (it){
                    addressInput.error = String.format(resources.getString(R.string.empty_field),
                        addressInput.hint.toString()
                    )
                } else {
                    addressInput.error = null
                }
            }
        }

        viewModel.listUserAddress.observe(viewLifecycleOwner) { listUserAddress ->
            adapter.clear()
            adapter.addAll(listUserAddress.map {
                listOf(
                    it.country,
                    it.city,
                    it.street,
                    it.house,
                    it.block
                ).filter { !it.isBlank() }
                    .joinToString(", ")
            })
        }

        viewModel.canContinue.observe(viewLifecycleOwner) {
            if (it){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, FragmentInterests.newInstance())
                    .commit()
            }
        }
    }

    private fun addTextChangedListeners(){
        with(binding){
            addressInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    val address = addressInput.text.toString()
                    viewModel.setAddress(address)
                    viewModel.searchAddress(address)
                }
               override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val EXTRA_USER_NAME = "user_name"

        fun newInstance() = FragmentAddress()
    }
}
