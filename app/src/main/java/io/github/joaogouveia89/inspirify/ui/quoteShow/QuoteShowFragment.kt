package io.github.joaogouveia89.inspirify.ui.quoteShow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.joaogouveia89.inspirify.InspirifyApplication
import io.github.joaogouveia89.inspirify.databinding.FragmentQuoteShowBinding

class QuoteShowFragment : Fragment() {

    private var _binding: FragmentQuoteShowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val inspirifyApplication: InspirifyApplication by lazy {
        (requireActivity().application as InspirifyApplication)
    }

    private val quoteShowViewModel: QuoteShowViewModel by viewModels {
        QuoteShowViewModelFactory(inspirifyApplication.inspirifyComponent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // I dont want to fetch new quote on fragment resuming
        quoteShowViewModel.inputs.requestNewData.postValue(Unit)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuoteShowBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@QuoteShowFragment
            viewModel = quoteShowViewModel
        }

        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = false
            quoteShowViewModel.inputs.requestNewData.postValue(Unit)
        }

        binding.favoriteIv.setOnClickListener {
            quoteShowViewModel.inputs.onFavoriteClick.postValue(Unit)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}